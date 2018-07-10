package com.binance.api.client.domain.event;

import com.binance.api.client.constant.BinanceApiConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An aggregated trade event for a symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FcoinTradeEvent {
    /*{
        "type":"trade.ethbtc",
            "id":76000,
            "amount":1.000000000,
            "ts":1523419946174,
            "side":"sell",
            "price":4.000000000
    }*/
    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private long tradeId;

    @JsonProperty("amount")
    private String quantity;

    @JsonProperty("ts")
    private long tradeTime;

    @JsonProperty("side")
    private String side;

    @JsonProperty("price")
    private String price;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
                .append("type", type)
                .append("tradeId", tradeId)
                .append("quantity", quantity)
                .append("tradeTime", tradeTime)
                .append("quantity", quantity)
                .append("side", side)
                .toString();
    }
}
