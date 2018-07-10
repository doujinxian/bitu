package com.bitm.model.mapper;

import com.bitm.model.entity.BiTradeQty;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/7 18:40
 */
@Mapper
public interface BiTradeQtyMapper {

    @Insert("INSERT INTO bi_trade_qty(symbol, qty, is_buyer_maker, trade_start_time, trade_end_time, create_time ) " +
            "VALUES(#{symbol}, #{qty}, #{isBuyerMaker}, #{tradeStartTime},#{tradeEndTime}, #{createTime})")
    int insertBiTradeQty(BiTradeQty biTradeQty);

    @Select("SELECT MAX(trade_start_time) FROM bi_trade_qty")
    Long findMaxTradeStartTime();

    @Delete("delete FROM bi_trade_qty where trade_start_time = #{tradeStartTime}")
    int deleteByTradeStartTime(@Param("tradeStartTime") Long tradeStartTime);

    @Select("SELECT t.`is_buyer_maker` as isBuyerMaker, t.`qty`,t.`trade_start_time` as tradeStartTime FROM `bi_trade_qty` t WHERE t.`symbol` = #{symbol} AND t.`trade_start_time` >= #{tradeStartTime} AND t.`trade_end_time` <= #{tradeEndTime} and platform =1")
    List<BiTradeQty> findBiTradeQty(@Param("symbol") String symbol, @Param("tradeStartTime") Long tradeStartTime, @Param("tradeEndTime") Long tradeEndTime);
}
