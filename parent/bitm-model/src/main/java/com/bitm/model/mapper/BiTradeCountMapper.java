package com.bitm.model.mapper;

import com.bitm.model.entity.BiTradeCount;
import com.bitm.model.entity.BiTradeQty;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/8
 */
@Mapper
public interface BiTradeCountMapper {

    @Insert("INSERT INTO bi_trade_count(symbol, count, qty_from, qty_to, trade_start_time, trade_end_time, create_time ) " +
            "VALUES(#{symbol}, #{count}, #{qtyFrom}, #{qtyTo}, #{tradeStartTime},#{tradeEndTime}, #{createTime})")
    int insertBiTradeCount(BiTradeCount biTradeCount);

    @Delete("delete FROM bi_trade_count where trade_start_time = #{tradeStartTime}")
    int deleteByTradeStartTime(@Param("tradeStartTime") Long tradeStartTime);

    @Select("SELECT MAX(trade_start_time) FROM bi_trade_count")
    Long findMaxTradeStartTime();

    @Select("SELECT t.`count`, t.`qty_from` AS qtyFrom FROM `bi_trade_count` t WHERE t.`symbol` = #{symbol} AND t.`trade_start_time` >= #{tradeStartTime} AND t.`trade_end_time` <= #{tradeEndTime} AND platform=1")
    List<BiTradeCount> findBiTradeCount(@Param("symbol") String symbol, @Param("tradeStartTime") Long tradeStartTime, @Param("tradeEndTime") Long tradeEndTime);
}
