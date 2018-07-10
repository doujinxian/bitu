package com.bitm.center.utils;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.FcoinApiClient;
import com.binance.api.client.domain.event.FcoinTradeEvent;
import com.bitm.model.constants.CommonConstants;
import com.bitm.model.entity.Trade;
import com.bitm.service.trade.TradeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


public class FcoinTradeHelper {

    private final String symbol;
    private final FcoinApiClient client;
    private final WsCallback wsCallback = new WsCallback();
    private volatile Closeable webSocket;
    private TradeService tradeService;

    private Logger logger = LoggerFactory.getLogger(FcoinTradeHelper.class);

    public FcoinTradeHelper(String symbol, FcoinApiClient client, TradeService tradeService) {
        this.symbol = symbol;
        this.tradeService = tradeService;
        this.client = client;
        initialize();
    }

    private void initialize() {
        // 1. Subscribe to depth events and cache any events that are received.
        final List<FcoinTradeEvent> pendingDeltas = startDepthEventStreaming();

        // 3. & 4. handled in here.
        applyPendingDeltas(pendingDeltas);
    }

    /**
     * Begins streaming of depth events.
     * <p>
     * Any events received are cached until the rest API is polled for an initial snapshot.
     */
    private List<FcoinTradeEvent> startDepthEventStreaming() {
        final List<FcoinTradeEvent> pendingDeltas = new CopyOnWriteArrayList<>();
        wsCallback.setHandler(pendingDeltas::add);

        this.webSocket = client.onTradeEvent(symbol.toLowerCase(), wsCallback);

        return pendingDeltas;
    }


    /**
     * Deal with any cached updates and switch to normal running.
     */
    private void applyPendingDeltas(final List<FcoinTradeEvent> pendingDeltas) {
        final Consumer<FcoinTradeEvent> updateOrderBook = newEvent -> {
            try {
                if (StringUtils.isNotBlank(newEvent.getPrice()) && StringUtils.isNotBlank(newEvent.getQuantity())) {
                    Trade tradeInfo = new Trade();
                    tradeInfo.setSymbol(newEvent.getType().replace("trade.", ""));
                    tradeInfo.setPlatform(CommonConstants.PLATFORM_FCOIN);
                    tradeInfo.setTradeId(newEvent.getTradeId());
                    tradeInfo.setIsBuyerMaker(newEvent.getSide().equalsIgnoreCase("buy") ? 1 : 0);
                    tradeInfo.setPrice(newEvent.getPrice());
                    tradeInfo.setQty(newEvent.getQuantity());
                    tradeInfo.setTradeTime(newEvent.getTradeTime());
                    long now = System.currentTimeMillis();
                    tradeInfo.setCreateTime(now);
                    tradeInfo.setUpdateTime(now);
                    tradeService.insertTrade(tradeInfo);
                } else {
                    logger.info("--------fcoin trade -----" + newEvent.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("--------fcoin trade -----" + newEvent.toString());
            }
        };

        wsCallback.setHandler(updateOrderBook);
    }

    public void close() throws IOException {
        webSocket.close();
    }


    private final class WsCallback implements BinanceApiCallback<FcoinTradeEvent> {

        private final AtomicReference<Consumer<FcoinTradeEvent>> handler = new AtomicReference<>();

        @Override
        public void onResponse(FcoinTradeEvent TradeEvent) {
            try {
                handler.get().accept(TradeEvent);
            } catch (final Exception e) {
                logger.error("fcoin trade ----- Exception caught processing depth event");
                e.printStackTrace(System.err);
            }
        }

        @Override
        public void onFailure(Throwable cause) {
            logger.info("fcoin trade ----- WS connection failed. Reconnecting. cause:" + cause.getMessage());

            initialize();
        }

        private void setHandler(final Consumer<FcoinTradeEvent> handler) {
            this.handler.set(handler);
        }
    }
}
