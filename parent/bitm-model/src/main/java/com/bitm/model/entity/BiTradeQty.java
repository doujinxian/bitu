package com.bitm.model.entity;

import java.io.Serializable;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/7 18:34
 */
public class BiTradeQty implements Serializable {
    private Long id;
    private Integer platform;
    private String symbol;
    private String qty;
    private Integer isBuyerMaker;
    private Long tradeStartTime;
    private Long tradeEndTime;
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Integer getIsBuyerMaker() {
        return isBuyerMaker;
    }

    public void setIsBuyerMaker(Integer isBuyerMaker) {
        this.isBuyerMaker = isBuyerMaker;
    }

    public Long getTradeStartTime() {
        return tradeStartTime;
    }

    public void setTradeStartTime(Long tradeStartTime) {
        this.tradeStartTime = tradeStartTime;
    }

    public Long getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(Long tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
