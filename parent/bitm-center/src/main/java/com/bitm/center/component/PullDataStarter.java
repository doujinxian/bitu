package com.bitm.center.component;

import com.binance.api.client.BinanceApiClientFactory;
import com.bitm.center.utils.OrderBookHelper;
import com.bitm.center.utils.TradeHelper;
import com.bitm.model.constants.CommonConstants;
import com.bitm.service.orderBook.OrderBookService;
import com.bitm.service.trade.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    logger.info("---------- init binance data start -----------");
                    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
                    for (String symbol : CommonConstants.ALL_SYMBOLS.split(",")) {
                        //获取币安交易记录
                        new TradeHelper(symbol, factory, tradeService);
                        //获取币安报价信息
                        new OrderBookHelper(symbol, factory, orderBookService);
                    }
                    logger.info("---------- init binance data finish -----------");
                }
            });
        }
    }
}
