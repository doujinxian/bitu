package com.bitm.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018\6\10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BiTradeCountVo implements Serializable {
    private Long count;
    private BigDecimal qtyFrom;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getQtyFrom() {
        return qtyFrom;
    }

    public void setQtyFrom(BigDecimal qtyFrom) {
        this.qtyFrom = qtyFrom;
    }
}
