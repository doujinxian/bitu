package com.bitm.service.trade.impl;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.bitm.model.constants.CommonConstants;
import com.bitm.model.entity.OrderBook;
import com.bitm.service.trade.TriangleTradeService;
import com.bitm.service.utils.BinanceApiClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/30 13:39
 */
@Service
public class TriangleTradeServiceImpl implements TriangleTradeService {

    private Logger logger = LoggerFactory.getLogger(TriangleTradeServiceImpl.class);

    BinanceApiRestClient client = BinanceApiClientUtil.newRestClient();

    @Override
    public void triangleTrade(OrderBook ob1, OrderBook ob2, OrderBook ob3) {
        if (ob1 == null || ob2 == null || ob3 == null) {
            return;
        }
        logger.info("-------------------------------------------------------------------------------------------------------------------");
        logger.info("| [{}]:买一价格：[{}]，买一数量:[{}];卖一价格：[{}]，卖一数量:[{}]", ob1.getSymbol(), ob1.getBid1Price(), ob1.getBid1Qty(), ob1.getAsk1Price(), ob1.getAsk1Qty());
        logger.info("| [{}]:买一价格：[{}]，买一数量:[{}];卖一价格：[{}]，卖一数量:[{}]", ob2.getSymbol(), ob2.getBid1Price(), ob2.getBid1Qty(), ob2.getAsk1Price(), ob2.getAsk1Qty());
        logger.info("| [{}]:买一价格：[{}]，买一数量:[{}];卖一价格：[{}]，卖一数量:[{}]", ob3.getSymbol(), ob3.getBid1Price(), ob3.getBid1Qty(), ob3.getAsk1Price(), ob3.getAsk1Qty());
        BigDecimal mut = new BigDecimal(ob3.getAsk1Price()).multiply(new BigDecimal(ob1.getAsk1Price()));
        BigDecimal sub = mut.subtract(new BigDecimal(ob2.getBid1Price()));
        BigDecimal ratio = BigDecimal.ZERO;
        if (sub.doubleValue() < 0) {
            /**
             * 当P32*P12<P21
             * Qmin3221 = min(Q32,Q21)
             * Q = min(P32*Qmin3221,Q12)
             * 计算交易数量 minQty
             * */
            BigDecimal minQty = new BigDecimal(ob3.getAsk1Qty());
            BigDecimal ob2Bid1Qty = new BigDecimal(ob2.getBid1Qty());
            if (minQty.compareTo(ob2Bid1Qty) > 0) {
                minQty = ob2Bid1Qty;
            }
            BigDecimal ob1Ask1Qty = new BigDecimal(ob1.getAsk1Qty());
            BigDecimal ob3Ask1Price = new BigDecimal(ob3.getAsk1Price());
            if (ob1Ask1Qty.compareTo(minQty.multiply(ob3Ask1Price)) < 0) {
                minQty = ob1Ask1Qty.divide(ob3Ask1Price, 8, RoundingMode.DOWN);
            } else {
                ob1Ask1Qty = minQty.multiply(ob3Ask1Price).setScale(8, RoundingMode.DOWN);
            }
            BigDecimal step1Cost = minQty.multiply(new BigDecimal(ob3.getAsk1Price()));
            BigDecimal step2Get = minQty.multiply(new BigDecimal(ob2.getBid1Price()));
            BigDecimal step3Cost = ob1Ask1Qty.multiply(new BigDecimal(ob1.getAsk1Price()));
            /*
            初始币 usdt
            1 再把usdt买成btc
            2 先用btc买eth
            3 再把eth卖成usdt
             */
            ratio = (step2Get.subtract(step1Cost).subtract(step3Cost)).divide(step1Cost.add(step2Get).add(step3Cost), 8, RoundingMode.DOWN);
            if(ratio.doubleValue()>0.5 && CommonConstants.realTrade){
                logger.info("| 1) 买入{}，买入价：{},数量为：{},花费：{}", ob1.getSymbol(), ob1.getAsk1Price(), ob1Ask1Qty, step3Cost);
                NewOrderResponse newOrderResponse1 = client.newOrder(NewOrder.limitBuy(ob1.getSymbol(), TimeInForce.GTC, ob1Ask1Qty.toString(), ob1.getAsk1Price()).newOrderRespType(NewOrderResponseType.FULL));
                logger.info(newOrderResponse1.toString());
                logger.info("| 2) 买入{}，买入价：{} 数量为：{},花费：{}", ob3.getSymbol(), ob3.getAsk1Price(), minQty, step1Cost);
                NewOrderResponse newOrderResponse2 = client.newOrder(NewOrder.limitBuy(ob3.getSymbol(), TimeInForce.GTC, minQty.toString(), ob3.getAsk1Price()).newOrderRespType(NewOrderResponseType.FULL));
                logger.info(newOrderResponse2.toString());
                logger.info("| 3) 卖出{}，卖出价：{},数量为：{},得到：{}", ob2.getSymbol(), ob2.getBid1Price(), minQty, step2Get);
                NewOrderResponse newOrderResponse3 = client.newOrder(NewOrder.limitSell(ob2.getSymbol(), TimeInForce.GTC, minQty.toString(), ob2.getBid1Price()).newOrderRespType(NewOrderResponseType.FULL));
                logger.info(newOrderResponse3.toString());
            }else{
                logger.info("| 1) 买入{}，买入价：{},数量为：{},花费：{}", ob1.getSymbol(), ob1.getAsk1Price(), ob1Ask1Qty, step3Cost);
                logger.info("| 2) 买入{}，买入价：{} 数量为：{},花费：{}", ob3.getSymbol(), ob3.getAsk1Price(), minQty, step1Cost);
                logger.info("| 3) 卖出{}，卖出价：{},数量为：{},得到：{}", ob2.getSymbol(), ob2.getBid1Price(), minQty, step2Get);
            }
        } else if (sub.doubleValue() > 0) {
            /**
             * 当P31*P11>P22
             * Qmin3122 = min(Q31,Q22)
             * Q = min(P31*Qmin3122,Q11)
             * 计算交易数量 minQty
             */

            BigDecimal minQty = new BigDecimal(ob3.getBid1Qty());
            BigDecimal ob2Ask1Qty = new BigDecimal(ob2.getAsk1Qty());
            if (minQty.compareTo(ob2Ask1Qty) > 0) {
                minQty = ob2Ask1Qty;
            }
            BigDecimal ob1Bid1Qty = new BigDecimal(ob1.getBid1Qty());
            BigDecimal ob3Bid1Price = new BigDecimal(ob3.getBid1Price());
            if (ob1Bid1Qty.compareTo(minQty.multiply(ob3Bid1Price)) < 0) {
                minQty = ob1Bid1Qty.divide(ob3Bid1Price, 8, RoundingMode.DOWN);
            } else {
                ob1Bid1Qty = minQty.multiply(ob3Bid1Price).setScale(8, RoundingMode.DOWN);
            }
            BigDecimal step1Get = minQty.multiply(new BigDecimal(ob3.getBid1Price()));
            BigDecimal step2Cost = minQty.multiply(new BigDecimal(ob2.getAsk1Price()));
            BigDecimal step3Get = ob1Bid1Qty.multiply(new BigDecimal(ob1.getBid1Price()));

            /*
            1  再把usdt买成btc
            2  再把eth卖成usdt
            3  先用btc买eth
            */
            ratio = (step1Get.add(step3Get).subtract(step2Cost)).divide(step1Get.add(step2Cost).add(step3Get), 8, RoundingMode.DOWN);
            if(ratio.doubleValue()>0.5 && CommonConstants.realTrade){
                logger.info("| 3) 买入{}，买入价：{},数量为：{},花费：{}", ob2.getSymbol(), ob2.getAsk1Price(), minQty, step2Cost);
                NewOrderResponse newOrderResponse1 = client.newOrder(NewOrder.limitBuy(ob2.getSymbol(), TimeInForce.GTC, minQty.toString(), ob2.getAsk1Price()).newOrderRespType(NewOrderResponseType.FULL));
                logger.info(newOrderResponse1.toString());
                logger.info("| 2) 卖出{}，卖出价：{} 数量为：{},得到：{}", ob3.getSymbol(), ob3.getBid1Price(), minQty, step1Get);
                NewOrderResponse newOrderResponse2 = client.newOrder(NewOrder.limitSell(ob3.getSymbol(), TimeInForce.GTC, minQty.toString(), ob3.getBid1Price()).newOrderRespType(NewOrderResponseType.FULL));
                logger.info(newOrderResponse2.toString());
                logger.info("| 1) 卖出{}，卖出价：{},数量为：{},得到：{}", ob1.getSymbol(), ob1.getBid1Price(), ob1Bid1Qty, step3Get);
                NewOrderResponse newOrderResponse3 = client.newOrder(NewOrder.limitSell(ob1.getSymbol(), TimeInForce.GTC, ob1Bid1Qty.toString(), ob1.getBid1Price()).newOrderRespType(NewOrderResponseType.FULL));
                logger.info(newOrderResponse3.toString());
            }else{
                logger.info("| 3) 买入{}，买入价：{},数量为：{},花费：{}", ob2.getSymbol(), ob2.getAsk1Price(), minQty, step2Cost);
                logger.info("| 2) 卖出{}，卖出价：{} 数量为：{},得到：{}", ob3.getSymbol(), ob3.getBid1Price(), minQty, step1Get);
                logger.info("| 1) 卖出{}，卖出价：{},数量为：{},得到：{}", ob1.getSymbol(), ob1.getBid1Price(), ob1Bid1Qty, step3Get);
            }
        }
        logger.info("------------------------------------------------------------------------------------------------------------------- {}", ratio);

    }
}
