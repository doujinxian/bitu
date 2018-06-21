package com.bitm.center.component;

import com.bitm.service.trade.BiTradeCountService;
import com.bitm.service.trade.BiTradeQtyService;
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
public class BiTradeStarter implements CommandLineRunner {
    @Autowired
    private BiTradeQtyService biTradeQtyService;
    @Autowired
    private BiTradeCountService biTradeCountService;

    private Logger logger = LoggerFactory.getLogger(BiTradeStarter.class);

    @Value("${bitm.start.biTrade}")
    private Boolean startBiTrade;

    @Override
    public void run(String... strings) throws Exception {

        System.out.println("startBiTrade:" + startBiTrade);
        if (startBiTrade != null && startBiTrade == true) {
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    logger.info("---------- init bi_trade_qty start -----------");
                    biTradeQtyService.initBiTradeQty();
                }
            });

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    logger.info("---------- init bi_trade_count start -----------");
                    //System.out.println(biTradeCountService.buildQtyRegion());
                    biTradeCountService.initBiTradeCount();
                }
            });
        }
    }
}
