package com.bitm.service.trade.impl;

import com.bitm.model.constants.BiConstants;
import com.bitm.model.constants.CommonConstants;
import com.bitm.model.entity.BiTradeQty;
import com.bitm.model.mapper.BiTradeQtyMapper;
import com.bitm.service.trade.BiTradeQtyService;
import com.bitm.service.trade.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/7 18:37
 */
@Service
public class BiTradeQtyServiceImpl implements BiTradeQtyService {
    private Logger logger = LoggerFactory.getLogger(BiTradeQtyServiceImpl.class);
    @Autowired
    private TradeService tradeService;
    @Autowired
    private BiTradeQtyMapper biTradeQtyMapper;

    @Override
    public int insertBiTradeQty(BiTradeQty biTradeQty) {
        return biTradeQtyMapper.insertBiTradeQty(biTradeQty);
    }

    @Override
    public Long findMaxTradeStartTime() {
        return biTradeQtyMapper.findMaxTradeStartTime();
    }

    @Override
    public void initBiTradeQty() {
        //防止信息不全，把上一阶段信息删除，重新生成
        Long tradeStartTime = findMaxTradeStartTime();
        if (tradeStartTime == null) {
            tradeStartTime = BiConstants.BI_START_TRADE_TIME;
        } else {
            tradeStartTime += BiConstants.BI_TRADE_QTY_TIME;
            //biTradeQtyMapper.deleteByTradeStartTime(tradeStartTime);
        }
        try {
            for (; ; ) {
                Long tradeEndTime = tradeStartTime + BiConstants.BI_TRADE_QTY_TIME;
                if (tradeEndTime > System.currentTimeMillis()) {
                    logger.info("---------initBiTradeQty wait one hour-----");
                    Thread.sleep(tradeEndTime - System.currentTimeMillis() + 10000);//整点后延迟10秒统计
                }
                logger.info("---------query TradeQty data from [{}] to [{}] -----", new Date(tradeStartTime), new Date(tradeEndTime));
                List<BiTradeQty> biTradeQtyList = tradeService.findBiTradeQtyList(tradeStartTime, tradeEndTime);
                if (biTradeQtyList != null || biTradeQtyList.size() > 0) {
                    for (BiTradeQty bi : biTradeQtyList) {
                        bi.setTradeStartTime(tradeStartTime);
                        bi.setTradeEndTime(tradeEndTime);
                        bi.setCreateTime(System.currentTimeMillis());
                        insertBiTradeQty(bi);
                    }
                }
                tradeStartTime = tradeEndTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BiTradeQty> findBiTradeQty(String symbol, long tradeStartTime, long tradeEndTime) {
        return biTradeQtyMapper.findBiTradeQty(symbol, tradeStartTime, tradeEndTime);
    }
}
