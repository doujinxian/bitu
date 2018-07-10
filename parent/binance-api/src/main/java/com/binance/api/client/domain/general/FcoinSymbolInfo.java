package com.binance.api.client.domain.general;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/7/10
 */
public class FcoinSymbolInfo implements Serializable {
    private Integer status;
    private List<FcoinSymbol> data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<FcoinSymbol> getData() {
        return data;
    }

    public void setData(List<FcoinSymbol> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FcoinSymbol{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
