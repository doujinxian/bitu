package com.bitm.center.controller;

import com.alibaba.fastjson.JSONArray;
import com.bitm.model.entity.BiTradeCount;
import com.bitm.model.entity.BiTradeQty;
import com.bitm.model.vo.BiTradeCountVo;
import com.bitm.model.vo.BiTradeQtyVo;
import com.bitm.model.vo.ResultVo;
import com.bitm.service.trade.BiTradeCountService;
import com.bitm.service.trade.BiTradeQtyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 净流入 和 单净比接口
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/8
 */
@RestController
@RequestMapping(value = "/biTrade")
public class BiTradeController {

    @Autowired
    private BiTradeCountService biTradeCountService;

    @Autowired
    private BiTradeQtyService biTradeQtyService;


    private Logger logger = LoggerFactory.getLogger(BiTradeController.class);

    @RequestMapping("/qty")
    public ResultVo getTradeQty(String symbol, Integer timeUnit, Long tradeStartTime) {
        JSONArray array = new JSONArray();
        if (timeUnit == null) {
            timeUnit = 60;//某一天的24小时数据
        }
        if (StringUtils.isBlank(symbol)) {
            symbol = "ETHBTC";
            //return ResultVo.failure("请选择币种");
        }
        long tradeEndTime = 0;
        Date now = new Date();

        switch (timeUnit) {
            case 360://6小时
                break;
            default://默认查某一天24小时的数据
                if (tradeStartTime == null) {
                    tradeStartTime = DateUtils.truncate(now, Calendar.DAY_OF_MONTH).getTime();
                    tradeEndTime = DateUtils.truncate(now, Calendar.HOUR).getTime();
                } else {
                    tradeEndTime = tradeStartTime + DateUtils.MILLIS_PER_DAY;
                }
                break;
        }

        Map<Long, BiTradeQtyVo> resultData = new TreeMap<>();
        List<BiTradeQty> trades = biTradeQtyService.findBiTradeQty(symbol.toUpperCase(), tradeStartTime, tradeEndTime);
        //TODO  如果查的今天的数据tradeEndTime 到当前时间的数据
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH");
        if (trades != null) {
            Collections.sort(trades, (trade1, trade2) -> trade1.getTradeStartTime().compareTo(trade2.getTradeStartTime()));
            for (BiTradeQty trade : trades) {
                Long startTime = trade.getTradeStartTime();
                BiTradeQtyVo vo = resultData.get(startTime);
                if (vo == null) {
                    vo = new BiTradeQtyVo();
                    vo.setTradeTime(startTime);
                    resultData.put(startTime, vo);
                }
                if (trade.getIsBuyerMaker() == 1) {
                    vo.setBuyerMakerQty(new BigDecimal(trade.getQty()));
                } else {
                    vo.setSellerMakerQty(new BigDecimal(trade.getQty()));
                }
            }
        }
        return ResultVo.success(resultData.values());
    }

    @RequestMapping("/count")
    public ResultVo getTradeCount(String symbol, Integer timeUnit, Long tradeStartTime) {
        JSONArray array = new JSONArray();
        if (timeUnit == null) {
            timeUnit = 1440;//某一天的24小时数据
        }
        if (StringUtils.isBlank(symbol)) {
            symbol = "ETHBTC";
            //return ResultVo.failure("请选择币种");
        }
        long tradeEndTime = 0;
        Date now = new Date();

        switch (timeUnit) {
            case 60://1小时
                if (tradeStartTime == null) {
                    tradeStartTime = DateUtils.truncate(now, Calendar.DAY_OF_MONTH).getTime();
                }
                tradeEndTime = tradeStartTime + DateUtils.MILLIS_PER_HOUR;
                break;
            case 120://2小时
                if (tradeStartTime == null) {
                    tradeStartTime = DateUtils.truncate(now, Calendar.DAY_OF_MONTH).getTime();
                }
                tradeEndTime = tradeStartTime + (DateUtils.MILLIS_PER_HOUR * 2);
                break;
            case 360://6小时
                if (tradeStartTime == null) {
                    tradeStartTime = DateUtils.truncate(now, Calendar.DAY_OF_MONTH).getTime();
                }
                tradeEndTime = tradeStartTime + (DateUtils.MILLIS_PER_HOUR * 6);
                break;
            default://默认查某一天24小时的数据
                if (tradeStartTime == null) {
                    tradeStartTime = DateUtils.truncate(now, Calendar.DAY_OF_MONTH).getTime();
                    tradeEndTime = DateUtils.truncate(now, Calendar.HOUR).getTime();
                } else {
                    tradeEndTime = tradeStartTime + DateUtils.MILLIS_PER_DAY;
                }
                break;
        }

        Map<BigDecimal, BiTradeCountVo> resultData = new TreeMap<>();
        List<BiTradeCount> trades = biTradeCountService.findBiTradeCount(symbol.toUpperCase(), tradeStartTime, tradeEndTime);
        //TODO  如果查的今天的数据tradeEndTime 到当前时间的数据
        Collections.sort(trades, (trade1, trade2) -> new BigDecimal(trade1.getQtyFrom()).compareTo(new BigDecimal(trade2.getQtyFrom())));
        if (trades != null) {
            for (BiTradeCount trade : trades) {
                if (trade.getCount() == 0) {
                    continue;
                }
                BigDecimal qtyFrom = new BigDecimal(trade.getQtyFrom());
                BiTradeCountVo vo = resultData.get(qtyFrom);
                if (vo == null) {
                    vo = new BiTradeCountVo();
                    vo.setQtyFrom(qtyFrom);
                    vo.setCount(trade.getCount());
                    resultData.put(qtyFrom, vo);
                } else {
                    vo.setCount(vo.getCount() + trade.getCount());
                }
            }
        }
        return ResultVo.success(resultData.values());
    }

    /*public static void main(String[] args) {
        Date now = new Date();
        System.out.println(DateUtils.truncate(now, Calendar.DAY_OF_MONTH).getTime());
        System.out.println(DateUtils.truncate(now, Calendar.HOUR).getTime());
    }*/

}
