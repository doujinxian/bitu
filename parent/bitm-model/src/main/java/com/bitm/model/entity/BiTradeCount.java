package com.bitm.model.entity;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/6/8 13:20
 */
public class BiTradeCount {
    private Long id;
    private Integer platform;
    private String symbol;
    private Long count;
    private String qtyFrom;
    private String qtyTo;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getQtyFrom() {
        return qtyFrom;
    }

    public void setQtyFrom(String qtyFrom) {
        this.qtyFrom = qtyFrom;
    }

    public String getQtyTo() {
        return qtyTo;
    }

    public void setQtyTo(String qtyTo) {
        this.qtyTo = qtyTo;
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
