package com.bitm.service.trade.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bitm.model.constants.BiConstants;
import com.bitm.model.constants.CommonConstants;
import com.bitm.model.entity.BiTradeCount;
import com.bitm.model.entity.BiTradeQty;
import com.bitm.model.mapper.BiTradeCountMapper;
import com.bitm.model.mapper.BiTradeQtyMapper;
import com.bitm.service.trade.BiTradeCountService;
import com.bitm.service.trade.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/8
 */
@Service
public class BiTradeCountServiceImpl implements BiTradeCountService {
    private Logger logger = LoggerFactory.getLogger(BiTradeCountServiceImpl.class);
    @Autowired
    private TradeService tradeService;
    @Autowired
    private BiTradeCountMapper biTradeCountMapper;

    @Override
    public int insertBiTradeCount(BiTradeCount biTradeCount) {
        return biTradeCountMapper.insertBiTradeCount(biTradeCount);
    }

    @Override
    public Long findMaxTradeStartTime() {
        return biTradeCountMapper.findMaxTradeStartTime();
    }

    @Override
    public String buildQtyRegion() {
        JSONObject symbols = new JSONObject();
        for (String symbol : CommonConstants.ALL_SYMBOLS.split(",")) {
            logger.info("------------get sysmbol:[{}] trade size start-------", symbol);
            List<BigDecimal> biTradeQtyList = tradeService.findQtyList(symbol, 10000);
            if (biTradeQtyList != null && biTradeQtyList.size() > 0) {
                Collections.sort(biTradeQtyList);
                JSONArray array = new JSONArray();
                array.add("0");
                int page = 20;
                BigDecimal lastPoint = BigDecimal.ZERO;
                for (int i = 1; i < page; i++) {
                    BigDecimal point = biTradeQtyList.get(biTradeQtyList.size() / page * i);
                    if (lastPoint.compareTo(point) != 0) {
                        array.add(point.toString());
                    } else {
                        logger.info("-------same point------");
                    }
                    lastPoint = point;
                }
                array.add("99999999");
                symbols.put(symbol, array);
            } else {
                logger.info("------------sysmbol:[{}] trade size is 0-------", symbol);
            }
        }
        return symbols.toString();
    }

    @Override
    public void initBiTradeCount() {
        //防止信息不全，把上一阶段信息删除，重新生成
        Long tradeStartTime = findMaxTradeStartTime();
        if (tradeStartTime == null) {
            tradeStartTime = BiConstants.BI_START_TRADE_TIME;
        } else {
            tradeStartTime += BiConstants.BI_TRADE_COUNT_TIME;
            //biTradeCountMapper.deleteByTradeStartTime(tradeStartTime);
        }

        for (; ; ) {
            Long tradeEndTime = tradeStartTime + BiConstants.BI_TRADE_COUNT_TIME;
            if (tradeEndTime > System.currentTimeMillis()) {
                long waitTimeMillis = tradeEndTime - System.currentTimeMillis() + 10000;//整点后延迟10秒统计
                logger.info("---------initBiTradeQty wait {} minutes -----", waitTimeMillis / 60000);
                try {
                    Thread.sleep(waitTimeMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info("---------query TradeCount data from [{}] to [{}] -----", new Date(tradeStartTime), new Date(tradeEndTime));
            try {
                for (String symbol : CommonConstants.ALL_SYMBOLS.split(",")) {

                    JSONObject symbolRegions = JSONObject.parseObject(BiConstants.SYMBOLS_REGION);
                    JSONArray regions = (JSONArray) symbolRegions.get(symbol);
                    //logger.info("------------get sysmbol:[{}] trade size start-------", symbol);
                    List<BigDecimal> biTradeQtyList = tradeService.findQtyListInTradeTime(symbol, tradeStartTime, tradeEndTime);
                    if (biTradeQtyList != null && biTradeQtyList.size() > 0) {
                        if (biTradeQtyList.size() > 50000) {
                            logger.warn("---------query TradeCount data from [{}] to [{}], trade size is [{}] -----", new Date(tradeStartTime), new Date(tradeEndTime), biTradeQtyList.size());
                        }
                        BiTradeCount bi = null;
                        for (int i = 0; i < regions.size() - 1; i++) {
                            String from = (String) regions.get(i);
                            String to = (String) regions.get(i + 1);
                            bi = new BiTradeCount();
                            bi.setSymbol(symbol);
                            bi.setCount(getCountFromList(biTradeQtyList, new BigDecimal(from), new BigDecimal(to)));
                            bi.setQtyFrom(from);
                            bi.setQtyTo(to);
                            bi.setTradeStartTime(tradeStartTime);
                            bi.setTradeEndTime(tradeEndTime);
                            bi.setCreateTime(System.currentTimeMillis());
                            insertBiTradeCount(bi);
                        }
                    } else {
                        logger.info("------------sysmbol:[{}] trade size is 0-------", symbol);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            tradeStartTime = tradeEndTime;
        }

    }

    private Long getCountFromList(List<BigDecimal> biTradeQtyList, BigDecimal from, BigDecimal to) {
        if (from.compareTo(to) >= 0) {
            logger.error("-------- get CountFromList from is equals to-------");
            return 0L;
        }
        long count = 0;
        for (BigDecimal qty : biTradeQtyList) {
            if (qty.compareTo(from) >= 0 && qty.compareTo(to) < 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<BiTradeCount> findBiTradeCount(String symbol, Long tradeStartTime, Long tradeEndTime) {
        return biTradeCountMapper.findBiTradeCount(symbol, tradeStartTime, tradeEndTime);
    }
}
