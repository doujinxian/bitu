package com.bitm.test;

import com.bitm.model.entity.OrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/29 16:52
 */
public class TriangleTest {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TriangleTest.class);

        Map<String, OrderBook> symbolMap = new HashMap<>();
        OrderBook btcusdtVo = new OrderBook();
        btcusdtVo.setSymbol("BTCUSDT");
        btcusdtVo.setBid1Price("7143.85000000");
        btcusdtVo.setBid1Qty("0.04451300");
        btcusdtVo.setAsk1Price("7143.98000000");
        btcusdtVo.setAsk1Qty("0.00364700");
        symbolMap.put(btcusdtVo.getSymbol(), btcusdtVo);

        OrderBook ethusdtVo = new OrderBook();
        ethusdtVo.setSymbol("ETHUSDT");
        ethusdtVo.setBid1Price("522.46000000");
        ethusdtVo.setBid1Qty("0.38236000");
        ethusdtVo.setAsk1Price("523.06000000");
        ethusdtVo.setAsk1Qty("0.07000000");
        symbolMap.put(ethusdtVo.getSymbol(), ethusdtVo);

        OrderBook ethBtcVo = new OrderBook();
        ethBtcVo.setSymbol("ETHBTC");
        ethBtcVo.setBid1Price("0.07304700");
        ethBtcVo.setBid1Qty("0.61900000");
        ethBtcVo.setAsk1Price("0.07309500");
        ethBtcVo.setAsk1Qty("6.97800000");
        symbolMap.put(ethBtcVo.getSymbol(), ethBtcVo);

        String[] triangle = "BTCUSDT,ETHUSDT,ETHBTC".split(",");
        OrderBook ob1 = symbolMap.get(triangle[0]);
        OrderBook ob2 = symbolMap.get(triangle[1]);
        OrderBook ob3 = symbolMap.get(triangle[2]);
        logger.info("----------------------------------------------------------------------------------");
        logger.info("| [{}]:买一价格：[{}]，买一数量:[{}];卖一价格：[{}]，卖一数量:[{}]", ob1.getSymbol(), ob1.getBid1Price(), ob1.getBid1Qty(), ob1.getAsk1Price(), ob1.getAsk1Qty());
        logger.info("| [{}]:买一价格：[{}]，买一数量:[{}];卖一价格：[{}]，卖一数量:[{}]", ob2.getSymbol(), ob2.getBid1Price(), ob2.getBid1Qty(), ob2.getAsk1Price(), ob2.getAsk1Qty());
        logger.info("| [{}]:买一价格：[{}]，买一数量:[{}];卖一价格：[{}]，卖一数量:[{}]", ob3.getSymbol(), ob3.getBid1Price(), ob3.getBid1Qty(), ob3.getAsk1Price(), ob3.getAsk1Qty());
        BigDecimal mut = new BigDecimal(ob3.getAsk1Price()).multiply(new BigDecimal(ob1.getAsk1Price()));
        BigDecimal sub = mut.subtract(new BigDecimal(ob2.getBid1Price()));
        if (sub.doubleValue() < 0) {
            /**
             * 当P32*P12<P21
             * Qmin3221 = min(Q32,Q21)
             * Q = min(P32*Qmin3221,Q12)
             * 计算交易数量 qty
             * */
            BigDecimal minQty = new BigDecimal(ob3.getAsk1Qty());
            BigDecimal ob2Bid1Qty = new BigDecimal(ob2.getBid1Qty());
            if (minQty.compareTo(ob2Bid1Qty) > 0) {
                minQty = ob2Bid1Qty;
            }
            BigDecimal ob1Ask1Qty = new BigDecimal(ob1.getAsk1Qty());
            BigDecimal ob3Ask1Price = new BigDecimal(ob3.getAsk1Price());
            if (ob1Ask1Qty.compareTo(minQty.multiply(ob3Ask1Price)) < 0) {
                //qty = minQty.multiply(new BigDecimal(ob3.getAsk1Qty()));
                minQty = ob1Ask1Qty.divide(ob3Ask1Price, 8, RoundingMode.DOWN);
            } else {
                ob1Ask1Qty = minQty.multiply(ob3Ask1Price).setScale(8, RoundingMode.DOWN);
            }
            BigDecimal step1Cost = minQty.multiply(new BigDecimal(ob3.getAsk1Price()));
            BigDecimal step2Get = minQty.multiply(new BigDecimal(ob2.getBid1Price()));
            BigDecimal step3Cost = ob1Ask1Qty.multiply(new BigDecimal(ob1.getAsk1Price()));
            logger.info("| 1) 买入{}，买入价：{} 数量为：{},花费：{}", ob3.getSymbol(), ob3.getAsk1Price(), minQty, step1Cost);
            logger.info("| 2) 卖出{}，卖出价：{},数量为：{},得到：{}", ob2.getSymbol(), ob2.getBid1Price(), minQty, step2Get);
            logger.info("| 3) 买入{}，买入价：{},数量为：{},花费：{}", ob1.getSymbol(), ob1.getAsk1Price(), ob1Ask1Qty, step3Cost);

        } else if (sub.longValue() > 0) {
            /**
             * 当P31*P11>P22
             * Qmin3122 = min(Q31,Q22)
             * Q = min(P31*Qmin3122,Q11)
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
            logger.info("| 1) 买入{}，买入价：{},数量为：{},花费：{}", ob2.getSymbol(), ob2.getAsk1Price(), minQty, step2Cost);
            logger.info("| 2) 卖出{}，卖出价：{} 数量为：{},得到：{}", ob3.getSymbol(), ob3.getBid1Price(), minQty, step1Get);
            logger.info("| 3) 卖出{}，卖出价：{},数量为：{},得到：{}", ob1.getSymbol(), ob1.getBid1Price(), ob1Bid1Qty, step3Get);

        }
        logger.info("----------------------------------------------------------------------------------");
/*
 *

当P32*P12<P21
Qmin3221 = min(Q32,Q21)
Q = min(P32*Qmin3221,Q12)
盈利：  P21*Q-P12*P32*Q (Q实际交易数量)  "BTCUSDT,ETHUSDT,ETHBTC"
1.假如实际的BTC/ETH市场价格P3低于公允价格（P3 < P2/P1），就在ETH/BTC(3)市场买入一定数量（记为Q3）的ETH（花费相应数量P3*Q3的BTC），
同时在ETH/USDT(2)市场卖出数量为Q3的ETH（得到相应数量P2*Q3的USDT），在BTC/USDT(1)市场买入数量为P3*Q3的BTC(花费相应数量P1*P3*Q3的USDT)。
整个过程中，BTC和ETH的数量不变，而USDT的数量增多(P2*Q3-P1*P3*Q3= P1*Q3*(P2/P1 – P3) > 0 ),从而实现稳定盈利。

当P31*P11>P22
Qmin3122 = min(Q31,Q22)
Q = min(P31*Qmin3122,Q11)
盈利：  P11*P31*Q-P22*Q(Q实际交易数量)
2.假如实际的BTC/ETH价格P3高于公允价格（P3>P2/P1），就在ETH/BTC市场卖出一定数量（记为Q3）的ETH（得到相应数量P3*Q3的BTC），
同时在ETH/USDT市场买入数量为Q3的ETH（花费相应数量P2*Q3的USDT），在BTC/USDT市场卖出数量为P3*Q3的BTC(获得相应数量P1*P3*Q3的USDT。
整个过程中，BTC和ETH的数量不变，而USDT的数量增多(P1*P3*Q3-P2*Q3= P1*Q3*(P3-P2/P1) > 0),从而实现稳定盈利。


*/


    }
}
