package com.bitm.center.component;

import com.alibaba.fastjson.JSONArray;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.FcoinApiClient;
import com.binance.api.client.FcoinApiClientFactory;
import com.binance.api.client.domain.general.FcoinSymbol;
import com.bitm.center.utils.FcoinTradeHelper;
import com.bitm.center.utils.OrderBookHelper;
import com.bitm.center.utils.BinanceTradeHelper;
import com.bitm.model.constants.CommonConstants;
import com.bitm.service.orderBook.OrderBookService;
import com.bitm.service.trade.TradeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/29 13:32
 */
@Component
public class PullDataStarter implements CommandLineRunner {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private OrderBookService orderBookService;
    private Logger logger = LoggerFactory.getLogger(PullDataStarter.class);

    @Value("${bitm.start.pullData}")
    private Boolean startPullData;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("startPullData:" + startPullData);
        if (startPullData != null && startPullData == true) {
            ExecutorService executorService = Executors.newFixedThreadPool(2);
           /* executorService.submit(new Runnable() {
                @Override
                public void run() {
                    logger.info("---------- init binance data start -----------");
                    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
                    for (String symbol : CommonConstants.ALL_SYMBOLS.split(",")) {
                        //获取币安交易记录
                        new BinanceTradeHelper(symbol, factory, tradeService);
                        //获取币安报价信息
                        new OrderBookHelper(symbol, factory, orderBookService);
                    }
                    logger.info("---------- init binance data finish -----------");
                }
            });*/
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("---------- init fcoin data start -----------");
                        FcoinApiClientFactory factory = FcoinApiClientFactory.newInstance();
                        FcoinApiClient client = factory.newClient();
                        List<FcoinSymbol> symbols = client.getSymbols();
                        if (symbols == null || symbols.size() == 0) {
                            logger.error("---------- fcoin public symbols is null -----------");
                            return;
                        }
                        JSONArray topics = new JSONArray();
                        for (FcoinSymbol symbol : symbols) {
                            topics.add("trade." + symbol.getName());
                        }
                        //获取币安交易记录
                        new FcoinTradeHelper(URLEncoder.encode(topics.toString(),"UTF-8"), client, tradeService);
                        logger.info("---------- init fcoin data finish -----------");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
