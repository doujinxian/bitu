package com.bitm.service.trade;

import com.bitm.model.entity.BiTradeQty;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/7 18:33
 */
public interface BiTradeQtyService {
    int insertBiTradeQty(BiTradeQty biTradeQty);

    Long findMaxTradeStartTime();

    void initBiTradeQty();

    List<BiTradeQty> findBiTradeQty(String symbol, long tradeStartTime, long tradeEndTime);
}
