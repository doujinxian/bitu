package com.binance.api.client.domain.event;

import com.binance.api.client.constant.BinanceApiConstants;
import com.binance.api.client.domain.market.AggTrade;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An aggregated trade event for a symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeEvent {
    @JsonProperty("e")
    private String eventType;

    @JsonProperty("E")
    private long eventTime;

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("t")
    private long tradeId;

    @JsonProperty("p")
    private String price;

    @JsonProperty("q")
    private String quantity;

    @JsonProperty("b")
    private long buyerOrderId;

    @JsonProperty("a")
    private long sellerOrderId;

    @JsonProperty("T")
    private long tradeTime;

    @JsonProperty("m")
    private boolean isBuyerMaker;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getBuyerOrderId() {
        return buyerOrderId;
    }

    public void setBuyerOrderId(long buyerOrderId) {
        this.buyerOrderId = buyerOrderId;
    }

    public long getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(long sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public boolean isBuyerMaker() {
        return isBuyerMaker;
    }

    public void setBuyerMaker(boolean buyerMaker) {
        isBuyerMaker = buyerMaker;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
                .append("eventType", eventType)
                .append("eventTime", eventTime)
                .append("symbol", symbol)
                .append("price", price)
                .append("quantity", quantity)
                .append("tradeTime", tradeTime)
                .toString();
    }
}
