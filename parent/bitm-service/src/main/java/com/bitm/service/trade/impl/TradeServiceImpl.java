package com.bitm.service.trade.impl;

import com.binance.api.client.domain.event.TradeEvent;
import com.bitm.model.entity.BiTradeQty;
import com.bitm.service.trade.TradeService;
import com.bitm.model.constants.CommonConstants;
import com.bitm.model.entity.Trade;
import com.bitm.model.mapper.TradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/24 19:27
 */
@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private TradeMapper tradeMapper;

    @Override
    public int insertTrade(Trade trade) {
        return tradeMapper.insertTrade(trade);
    }

    @Override
    public Long findMaxTradeIdBySymbol(String symbol) {
        return tradeMapper.findMaxTradeIdBySymbol(symbol);
    }

    @Override
    public List<BigDecimal> findQtyList(String symbol, Integer limit) {
        return tradeMapper.findQtyList(symbol, limit);
    }

    @Override
    public void saveBinanceTrade(TradeEvent trade) {
        Trade tradeInfo = new Trade();
        tradeInfo.setSymbol(trade.getSymbol());
        tradeInfo.setPlatform(CommonConstants.PLATFORM_BINANCE);
        tradeInfo.setTradeId(trade.getTradeId());
        tradeInfo.setIsBuyerMaker(trade.isBuyerMaker() ? 1 : 0);
        tradeInfo.setPrice(trade.getPrice());
        tradeInfo.setQty(trade.getQuantity());
        tradeInfo.setTradeTime(trade.getTradeTime());
        long now = System.currentTimeMillis();
        tradeInfo.setCreateTime(now);
        tradeInfo.setUpdateTime(now);
        insertTrade(tradeInfo);
    }

    @Override
    public List<BiTradeQty> findBiTradeQtyList(Long tradeStartTime, Long tradeEndTime) {
        return tradeMapper.findBiTradeQtyList(tradeStartTime, tradeEndTime);
    }

    @Override
    public List<BigDecimal> findQtyListInTradeTime(String symbol, Long tradeStartTime, Long tradeEndTime) {
        return tradeMapper.findQtyListInTradeTime(symbol, tradeStartTime, tradeEndTime);
    }
}
