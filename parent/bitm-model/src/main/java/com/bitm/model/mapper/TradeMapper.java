package com.bitm.model.mapper;

import com.bitm.model.entity.BiTradeQty;
import com.bitm.model.entity.Trade;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TradeMapper {

    @Insert("INSERT INTO trade_5(symbol, platform, trade_id, price, qty, is_buyer_maker, TRADE_TIME, CREATE_TIME, UPDATE_TIME ) " +
            "VALUES(#{symbol}, #{platform}, #{tradeId}, #{price}, #{qty}, #{isBuyerMaker}, #{tradeTime}, #{createTime}, #{updateTime})")
    int insertTrade(Trade trade);

    @Select("select max(t.`trade_id`) from trade_5 t where t.`symbol` = #{symbol}  and platform=1 ")
    Long findMaxTradeIdBySymbol(@Param("symbol") String symbol);

    @Select("SELECT ROUND(SUM(b.`qty`),8) as qty,b.`is_buyer_maker` as isBuyerMaker ,b.`symbol` FROM  trade_5 b WHERE b.`TRADE_TIME`>= #{tradeStartTime} AND b.`TRADE_TIME` < #{tradeEndTime} and platform=1 GROUP BY  b.`symbol`,b.`is_buyer_maker`")
    List<BiTradeQty> findBiTradeQtyList(@Param("tradeStartTime") Long tradeStartTime, @Param("tradeEndTime") Long tradeEndTime);

    @Select("SELECT b.`qty` FROM  `trade_5` b WHERE b.`symbol` = #{symbol}  and platform=1 limit #{limit}")
    List<BigDecimal> findQtyList(@Param("symbol") String symbol, @Param("limit") Integer limit);

    @Select("SELECT b.`qty` FROM  `trade_5` b WHERE b.`symbol` = #{symbol} AND b.`TRADE_TIME`>= #{tradeStartTime} AND b.`TRADE_TIME`< #{tradeEndTime} and platform=1 limit 100000")
    List<BigDecimal> findQtyListInTradeTime(@Param("symbol") String symbol, @Param("tradeStartTime") Long tradeStartTime, @Param("tradeEndTime") Long tradeEndTime);
}