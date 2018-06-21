package com.bitm.service.utils;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/24 15:42
 */
public class BinanceApiClientUtil {

    public static BinanceApiRestClient newRestClient(){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("", "");
        BinanceApiRestClient client = factory.newRestClient();
        return client;
    }
}
