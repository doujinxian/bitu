package com.bitm.model.entity;

import java.util.Date;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/23 21:09
 */
public class OrderBook {
    private Long id;
    private Integer platform;
    private String symbol;
    private Long lastUpdateId;
    private String bid1Price;
    private String bid1Qty;
    private String bid2Price;
    private String bid2Qty;
    private String bid3Price;
    private String bid3Qty;
    private String bid4Price;
    private String bid4Qty;
    private String bid5Price;
    private String bid5Qty;
    private String ask1Price;
    private String ask1Qty;
    private String ask2Price;
    private String ask2Qty;
    private String ask3Price;
    private String ask3Qty;
    private String ask4Price;
    private String ask4Qty;
    private String ask5Price;
    private String ask5Qty;
    private Date createTime;
    private Date updateTime;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(Long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public String getBid1Price() {
        return bid1Price;
    }

    public void setBid1Price(String bid1Price) {
        this.bid1Price = bid1Price;
    }

    public String getBid1Qty() {
        return bid1Qty;
    }

    public void setBid1Qty(String bid1Qty) {
        this.bid1Qty = bid1Qty;
    }

    public String getAsk1Price() {
        return ask1Price;
    }

    public void setAsk1Price(String ask1Price) {
        this.ask1Price = ask1Price;
    }

    public String getAsk1Qty() {
        return ask1Qty;
    }

    public void setAsk1Qty(String ask1Qty) {
        this.ask1Qty = ask1Qty;
    }

    public String getBid2Price() {
        return bid2Price;
    }

    public void setBid2Price(String bid2Price) {
        this.bid2Price = bid2Price;
    }

    public String getBid2Qty() {
        return bid2Qty;
    }

    public void setBid2Qty(String bid2Qty) {
        this.bid2Qty = bid2Qty;
    }

    public String getBid3Price() {
        return bid3Price;
    }

    public void setBid3Price(String bid3Price) {
        this.bid3Price = bid3Price;
    }

    public String getBid3Qty() {
        return bid3Qty;
    }

    public void setBid3Qty(String bid3Qty) {
        this.bid3Qty = bid3Qty;
    }

    public String getBid4Price() {
        return bid4Price;
    }

    public void setBid4Price(String bid4Price) {
        this.bid4Price = bid4Price;
    }

    public String getBid4Qty() {
        return bid4Qty;
    }

    public void setBid4Qty(String bid4Qty) {
        this.bid4Qty = bid4Qty;
    }

    public String getBid5Price() {
        return bid5Price;
    }

    public void setBid5Price(String bid5Price) {
        this.bid5Price = bid5Price;
    }

    public String getBid5Qty() {
        return bid5Qty;
    }

    public void setBid5Qty(String bid5Qty) {
        this.bid5Qty = bid5Qty;
    }

    public String getAsk2Price() {
        return ask2Price;
    }

    public void setAsk2Price(String ask2Price) {
        this.ask2Price = ask2Price;
    }

    public String getAsk2Qty() {
        return ask2Qty;
    }

    public void setAsk2Qty(String ask2Qty) {
        this.ask2Qty = ask2Qty;
    }

    public String getAsk3Price() {
        return ask3Price;
    }

    public void setAsk3Price(String ask3Price) {
        this.ask3Price = ask3Price;
    }

    public String getAsk3Qty() {
        return ask3Qty;
    }

    public void setAsk3Qty(String ask3Qty) {
        this.ask3Qty = ask3Qty;
    }

    public String getAsk4Price() {
        return ask4Price;
    }

    public void setAsk4Price(String ask4Price) {
        this.ask4Price = ask4Price;
    }

    public String getAsk4Qty() {
        return ask4Qty;
    }

    public void setAsk4Qty(String ask4Qty) {
        this.ask4Qty = ask4Qty;
    }

    public String getAsk5Price() {
        return ask5Price;
    }

    public void setAsk5Price(String ask5Price) {
        this.ask5Price = ask5Price;
    }

    public String getAsk5Qty() {
        return ask5Qty;
    }

    public void setAsk5Qty(String ask5Qty) {
        this.ask5Qty = ask5Qty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }
}
