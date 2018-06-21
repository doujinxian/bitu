package com.bitm.service.trade;

import com.bitm.model.entity.OrderBook;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/30 13:39
 */
public interface TriangleTradeService {
    /**
     * 三角套利
     */
    void triangleTrade(OrderBook ob1, OrderBook ob2, OrderBook ob3);
}
