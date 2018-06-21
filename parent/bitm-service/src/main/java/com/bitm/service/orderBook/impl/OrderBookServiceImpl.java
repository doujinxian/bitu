package com.bitm.service.orderBook.impl;

import com.binance.api.client.domain.event.OrderBookEvent;
import com.binance.api.client.domain.market.OrderBookEntry;
import com.bitm.service.orderBook.OrderBookService;
import com.bitm.model.constants.CommonConstants;
import com.bitm.model.entity.OrderBook;
import com.bitm.model.mapper.OrderBookMapper;
import com.bitm.service.trade.TriangleTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/24 19:27
 */
@Service
public class OrderBookServiceImpl implements OrderBookService {
    @Autowired
    private OrderBookMapper orderBookMapper;
    @Autowired
    private TriangleTradeService triangleTradeService;

    private Map<String, OrderBook> orderBookCache = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(OrderBookServiceImpl.class);

    @Override
    public int insertOrderBook(OrderBook orderBook) {
        return orderBookMapper.insertOrderBook(orderBook);
    }

    @Override
    public void insertOrderBook(String symbol, OrderBookEvent book) {
        OrderBook bookVo = new OrderBook();
        bookVo.setSymbol(symbol);
        bookVo.setPlatform(CommonConstants.PLATFORM_BINANCE);
        bookVo.setLastUpdateId(book.getLastUpdateId());
        List<OrderBookEntry> bids = book.getBids();
        if (bids != null) {
            if (bids.size() > 0) {
                bookVo.setBid1Price(bids.get(0).getPrice());
                bookVo.setBid1Qty(bids.get(0).getQty());
            }
            if (bids.size() > 1) {
                bookVo.setBid2Price(bids.get(1).getPrice());
                bookVo.setBid2Qty(bids.get(1).getQty());
            }
            if (bids.size() > 2) {
                bookVo.setBid3Price(bids.get(2).getPrice());
                bookVo.setBid3Qty(bids.get(2).getQty());
            }
            if (bids.size() > 3) {
                bookVo.setBid4Price(bids.get(3).getPrice());
                bookVo.setBid4Qty(bids.get(3).getQty());
            }
            if (bids.size() > 4) {
                bookVo.setBid5Price(bids.get(4).getPrice());
                bookVo.setBid5Qty(bids.get(4).getQty());
            }
        }
        List<OrderBookEntry> asks = book.getAsks();
        if (bids != null) {
            if (asks.size() > 0) {
                bookVo.setAsk1Price(asks.get(0).getPrice());
                bookVo.setAsk1Qty(asks.get(0).getQty());
            }
            if (asks.size() > 1) {
                bookVo.setAsk2Price(asks.get(1).getPrice());
                bookVo.setAsk2Qty(asks.get(1).getQty());
            }
            if (asks.size() > 2) {
                bookVo.setAsk3Price(asks.get(2).getPrice());
                bookVo.setAsk3Qty(asks.get(2).getQty());
            }
            if (asks.size() > 3) {
                bookVo.setAsk4Price(asks.get(3).getPrice());
                bookVo.setAsk4Qty(asks.get(3).getQty());
            }
            if (asks.size() > 4) {
                bookVo.setAsk5Price(asks.get(4).getPrice());
                bookVo.setAsk5Qty(asks.get(4).getQty());
            }
        }
        try {
            // TODO 1: 交易逻辑后期走消息队列 2: orderBookCache可考虑做分布式缓存
            if (CommonConstants.openTrade && CommonConstants.triangleSymbols.contains(bookVo.getSymbol())) {
                orderBookCache.put(bookVo.getSymbol(), bookVo);
                OrderBook ob1 = orderBookCache.get(CommonConstants.triangleSymbols.get(0));
                OrderBook ob2 = orderBookCache.get(CommonConstants.triangleSymbols.get(1));
                OrderBook ob3 = orderBookCache.get(CommonConstants.triangleSymbols.get(2));
                triangleTradeService.triangleTrade(ob1, ob2, ob3);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        insertOrderBook(bookVo);
    }
}
