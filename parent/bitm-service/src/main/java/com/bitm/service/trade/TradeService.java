package com.bitm.service.trade;

import com.binance.api.client.domain.event.TradeEvent;
import com.bitm.model.entity.BiTradeQty;
import com.bitm.model.entity.Trade;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/24 19:27
 */
public interface TradeService {
    int insertTrade(Trade trade);

    Long findMaxTradeIdBySymbol(String symbol);

    void saveBinanceTrade(TradeEvent newEvent);

    List<BiTradeQty> findBiTradeQtyList(Long tradeStartTime, Long tradeEndTime);

    List<BigDecimal> findQtyList(String symbol, Integer limit);

    List<BigDecimal> findQtyListInTradeTime(String symbol, Long tradeStartTime, Long tradeEndTime);
}
