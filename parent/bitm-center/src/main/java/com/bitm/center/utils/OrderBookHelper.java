package com.bitm.center.utils;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.OrderBookEvent;
import com.bitm.service.orderBook.OrderBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


public class OrderBookHelper {

    private final String symbol;
    private final BinanceApiWebSocketClient wsClient;
    private final WsCallback wsCallback = new WsCallback();
    private volatile Closeable webSocket;
    private OrderBookService orderBookService;

    private Logger logger = LoggerFactory.getLogger(OrderBookHelper.class);

    public OrderBookHelper(String symbol, BinanceApiClientFactory factory, OrderBookService orderBookService) {
        this.symbol = symbol;
        this.orderBookService = orderBookService;
        if (factory == null) {
            factory = BinanceApiClientFactory.newInstance();
        }
        this.wsClient = factory.newWebSocketClient();
        initialize();
    }

    private void initialize() {
        // 1. Subscribe to depth events and cache any events that are received.
        final List<OrderBookEvent> pendingDeltas = startDepthEventStreaming();

        // 3. & 4. handled in here.
        applyPendingDeltas(pendingDeltas);
    }

    /**
     * Begins streaming of depth events.
     * <p>
     * Any events received are cached until the rest API is polled for an initial snapshot.
     */
    private List<OrderBookEvent> startDepthEventStreaming() {
        final List<OrderBookEvent> pendingDeltas = new CopyOnWriteArrayList<>();
        wsCallback.setHandler(pendingDeltas::add);

        this.webSocket = wsClient.onDepthEvent(symbol.toLowerCase(), 5, wsCallback);

        return pendingDeltas;
    }


    /**
     * Deal with any cached updates and switch to normal running.
     */
    private void applyPendingDeltas(final List<OrderBookEvent> pendingDeltas) {
        final Consumer<OrderBookEvent> updateOrderBook = newEvent -> {
            orderBookService.insertOrderBook(symbol, newEvent);
            //logger.info("--------symbol:[{}] -----" + newEvent.toString(), symbol);
        };

        wsCallback.setHandler(updateOrderBook);
    }


    public void close() throws IOException {
        webSocket.close();
    }


    private final class WsCallback implements BinanceApiCallback<OrderBookEvent> {

        private final AtomicReference<Consumer<OrderBookEvent>> handler = new AtomicReference<>();

        @Override
        public void onResponse(OrderBookEvent OrderBookEvent) {
            try {
                handler.get().accept(OrderBookEvent);
            } catch (final Exception e) {
                logger.error("binance order book symbol:[{}] ----- Exception caught processing depth event", symbol);
                e.printStackTrace(System.err);
            }
        }

        @Override
        public void onFailure(Throwable cause) {
            logger.info("binance order book symbol:[{}] ----- WS connection failed. Reconnecting. cause:" + cause.getMessage(), symbol);

            initialize();
        }

        private void setHandler(final Consumer<OrderBookEvent> handler) {
            this.handler.set(handler);
        }
    }
}
