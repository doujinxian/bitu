package com.bitm.center.utils;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.TradeEvent;
import com.bitm.model.constants.CommonConstants;
import com.bitm.model.entity.Trade;
import com.bitm.service.trade.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


public class BinanceTradeHelper {

    private final String symbol;
    private final BinanceApiWebSocketClient wsClient;
    private final WsCallback wsCallback = new WsCallback();
    private volatile Closeable webSocket;
    private TradeService tradeService;

    private Logger logger = LoggerFactory.getLogger(BinanceTradeHelper.class);

    public BinanceTradeHelper(String symbol, BinanceApiClientFactory factory, TradeService tradeService) {
        this.symbol = symbol;
        this.tradeService = tradeService;
        if (factory == null) {
            factory = BinanceApiClientFactory.newInstance();
        }
        this.wsClient = factory.newWebSocketClient();
        initialize();
    }

    private void initialize() {
        // 1. Subscribe to depth events and cache any events that are received.
        final List<TradeEvent> pendingDeltas = startDepthEventStreaming();

        // 3. & 4. handled in here.
        applyPendingDeltas(pendingDeltas);
    }

    /**
     * Begins streaming of depth events.
     * <p>
     * Any events received are cached until the rest API is polled for an initial snapshot.
     */
    private List<TradeEvent> startDepthEventStreaming() {
        final List<TradeEvent> pendingDeltas = new CopyOnWriteArrayList<>();
        wsCallback.setHandler(pendingDeltas::add);

        this.webSocket = wsClient.onTradeEvent(symbol.toLowerCase(), wsCallback);

        return pendingDeltas;
    }


    /**
     * Deal with any cached updates and switch to normal running.
     */
    private void applyPendingDeltas(final List<TradeEvent> pendingDeltas) {
        final Consumer<TradeEvent> updateOrderBook = newEvent -> {
            try {
                Trade tradeInfo = new Trade();
                tradeInfo.setSymbol(newEvent.getSymbol());
                tradeInfo.setPlatform(CommonConstants.PLATFORM_BINANCE);
                tradeInfo.setTradeId(newEvent.getTradeId());
                tradeInfo.setIsBuyerMaker(newEvent.isBuyerMaker() ? 1 : 0);
                tradeInfo.setPrice(newEvent.getPrice());
                tradeInfo.setQty(newEvent.getQuantity());
                tradeInfo.setTradeTime(newEvent.getTradeTime());
                long now = System.currentTimeMillis();
                tradeInfo.setCreateTime(now);
                tradeInfo.setUpdateTime(now);
                tradeService.insertTrade(tradeInfo);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("--------binance symbol:[{}] -----" + newEvent.toString(), symbol);
            }
        };

        wsCallback.setHandler(updateOrderBook);
    }

    public void close() throws IOException {
        webSocket.close();
    }


    private final class WsCallback implements BinanceApiCallback<TradeEvent> {

        private final AtomicReference<Consumer<TradeEvent>> handler = new AtomicReference<>();

        @Override
        public void onResponse(TradeEvent TradeEvent) {
            try {
                handler.get().accept(TradeEvent);
            } catch (final Exception e) {
                logger.error("binance trade symbol:[{}] ----- Exception caught processing depth event", symbol);
                e.printStackTrace(System.err);
            }
        }

        @Override
        public void onFailure(Throwable cause) {
            logger.info("binance trade symbol:[{}] ----- WS connection failed. Reconnecting. cause:" + cause.getMessage(), symbol);

            initialize();
        }

        private void setHandler(final Consumer<TradeEvent> handler) {
            this.handler.set(handler);
        }
    }
}
