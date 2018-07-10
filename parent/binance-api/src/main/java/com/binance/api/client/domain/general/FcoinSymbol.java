package com.binance.api.client.domain.general;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/7/10
 */
public class FcoinSymbol implements Serializable {
    private String name;
    private String base_currency;
    private String quote_currency;
    private int price_decimal;
    private int amount_decimal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase_currency() {
        return base_currency;
    }

    public void setBase_currency(String base_currency) {
        this.base_currency = base_currency;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }

    public int getPrice_decimal() {
        return price_decimal;
    }

    public void setPrice_decimal(int price_decimal) {
        this.price_decimal = price_decimal;
    }

    public int getAmount_decimal() {
        return amount_decimal;
    }

    public void setAmount_decimal(int amount_decimal) {
        this.amount_decimal = amount_decimal;
    }

    @Override
    public String toString() {
        return "FcoinSymbol{" +
                "name='" + name + '\'' +
                ", base_currency='" + base_currency + '\'' +
                ", quote_currency='" + quote_currency + '\'' +
                ", price_decimal=" + price_decimal +
                ", amount_decimal=" + amount_decimal +
                '}';
    }
}
