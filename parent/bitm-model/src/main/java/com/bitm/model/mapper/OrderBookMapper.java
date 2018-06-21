package com.bitm.model.mapper;

import com.bitm.model.entity.OrderBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderBookMapper {

    @Insert("INSERT INTO order_book_3(symbol, last_update_id, bid1_price, bid1_qty,bid2_price, bid2_qty,bid3_price, bid3_qty,bid4_price, bid4_qty,bid5_price, bid5_qty" +
            ", ask1_price,ask1_qty, ask2_price,ask2_qty, ask3_price,ask3_qty, ask4_price,ask4_qty, ask5_price,ask5_qty) " +
            "VALUES(#{symbol}, #{lastUpdateId}, #{bid1Price}, #{bid1Qty},#{bid2Price}, #{bid2Qty},#{bid3Price}, #{bid3Qty},#{bid4Price}, #{bid4Qty},#{bid5Price}, #{bid5Qty}" +
            ", #{ask1Price}, #{ask1Qty}, #{ask2Price}, #{ask2Qty}, #{ask3Price}, #{ask3Qty}, #{ask4Price}, #{ask4Qty}, #{ask5Price}, #{ask5Qty})")
    int insertOrderBook(OrderBook orderBook);
}