package com.binance.api.client.impl;

import com.binance.api.client.domain.general.FcoinSymbolInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Binance's REST API URL mappings and endpoint security configuration.
 */
public interface FcoinApiService {

    @GET
    Call<FcoinSymbolInfo> getSymbols(@Url String url);
}
