package com.binance.api.client.impl;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.FcoinApiClient;
import com.binance.api.client.constant.BinanceApiConstants;
import com.binance.api.client.constant.FcoinApiConstants;
import com.binance.api.client.domain.event.FcoinTradeEvent;
import com.binance.api.client.domain.general.FcoinSymbol;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.io.Closeable;
import java.util.List;

import static com.binance.api.client.impl.BinanceApiServiceGenerator.createService;
import static com.binance.api.client.impl.BinanceApiServiceGenerator.executeSync;

/**
 * Fcoin API WebSocket client implementation using OkHttp.
 */
public class FcoinApiClientImpl implements FcoinApiClient, Closeable {

    private OkHttpClient client;
    private FcoinApiService fcoinApiService;

    public FcoinApiClientImpl() {
        Dispatcher d = new Dispatcher();
        d.setMaxRequestsPerHost(1000);
        this.client = new OkHttpClient.Builder().dispatcher(d).build();
        fcoinApiService = createService(FcoinApiService.class);
    }


    public Closeable onTradeEvent(String symbol, BinanceApiCallback<FcoinTradeEvent> callback) {
        return createNewWebSocket(symbol, new ApiWebSocketListener<>(callback, FcoinTradeEvent.class));
    }

    @Override
    public List getSymbols() {
        return executeSync(fcoinApiService.getSymbols(FcoinApiConstants.API_PUBLIC_SYMBOLS)).getData();
    }

    @Override
    public void close() {
        client.dispatcher().executorService().shutdown();
    }

    private Closeable createNewWebSocket(String symbol, ApiWebSocketListener<?> listener) {
        Request request = new Request.Builder().url(FcoinApiConstants.WS_API_BASE_URL).build();
        final WebSocket webSocket = client.newWebSocket(request, listener);
        webSocket.send("{\"cmd\":\"sub\",\"args\":" + symbol + ",\"id\":\"1\"}");
        return () -> {
            final int code = 1000;
            listener.onClosing(webSocket, code, null);
            webSocket.close(code, null);
            listener.onClosed(webSocket, code, null);
        };
    }
}
