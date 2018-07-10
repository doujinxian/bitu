package com.binance.api.client;

import com.binance.api.client.domain.event.FcoinTradeEvent;
import com.binance.api.client.domain.general.FcoinSymbol;

import java.io.Closeable;
import java.util.List;

/**
 * Fcoin API data streaming façade, supporting streaming of events through web sockets.
 */
public interface FcoinApiClient extends Closeable {

    Closeable onTradeEvent(String symbol, BinanceApiCallback<FcoinTradeEvent> callback);

    List<FcoinSymbol> getSymbols();

    void close();
}
