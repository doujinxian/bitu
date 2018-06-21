package com.bitm.service.orderBook;

import com.binance.api.client.domain.event.OrderBookEvent;
import com.bitm.model.entity.OrderBook;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/24 19:27
 */
public interface OrderBookService {
    int insertOrderBook(OrderBook orderBook);

    void insertOrderBook(String symbol, OrderBookEvent response);
}
