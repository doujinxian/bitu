package com.bitm.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018\6\9 0009
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BiTradeQtyVo implements Serializable {
    private Long tradeTime;
    private BigDecimal qty;
    private BigDecimal buyerMakerQty;
    private BigDecimal sellerMakerQty;

    public BigDecimal getQty() {
        if (qty == null) {
            return getBuyerMakerQty().subtract(getSellerMakerQty());
        }
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getBuyerMakerQty() {
        if (buyerMakerQty == null) {
            return BigDecimal.ZERO;
        }
        return buyerMakerQty;
    }

    public void setBuyerMakerQty(BigDecimal buyerMakerQty) {
        this.buyerMakerQty = buyerMakerQty;
    }

    public BigDecimal getSellerMakerQty() {
        if (sellerMakerQty == null) {
            return BigDecimal.ZERO;
        }
        return sellerMakerQty;
    }

    public void setSellerMakerQty(BigDecimal sellerMakerQty) {
        this.sellerMakerQty = sellerMakerQty;
    }

    public Long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Long tradeTime) {
        this.tradeTime = tradeTime;
    }
}
