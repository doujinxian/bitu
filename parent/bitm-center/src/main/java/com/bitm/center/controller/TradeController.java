package com.bitm.center.controller;

import com.bitm.service.trade.TradeService;
import com.bitm.model.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/23 19:41
 */
@RestController
@RequestMapping(value = "/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;


    private Logger logger = LoggerFactory.getLogger(TradeController.class);


    @RequestMapping("/openTrade")
    public String getTrade(Boolean openTrade, Boolean realTrade, String symbols) {
        //symbols = "BTCUSDT,ETHUSDT,ETHBTC"
        if (openTrade != null) {
            CommonConstants.openTrade = openTrade;
        }
        if (realTrade != null) {
            CommonConstants.realTrade = realTrade;
        }
        if (StringUtils.isNotBlank(symbols)) {
            CommonConstants.setTriangleSymbols(Arrays.asList(symbols.toUpperCase().split(",")));
        }
        return "ok";
    }

    @RequestMapping("/getTradeId")
    public String getTradeId() {
        Long tradeId = tradeService.findMaxTradeIdBySymbol("BTCUSDT");
        return "ok:" + tradeId;
    }

}
