package com.bitm.service.trade;

import com.bitm.model.entity.BiTradeCount;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/8
 */
public interface BiTradeCountService {
    int insertBiTradeCount(BiTradeCount biTradeCount);

    Long findMaxTradeStartTime();

    String buildQtyRegion();

    void initBiTradeCount();

    List<BiTradeCount> findBiTradeCount(String symbol, Long tradeStartTime, Long tradeEndTime);
}
