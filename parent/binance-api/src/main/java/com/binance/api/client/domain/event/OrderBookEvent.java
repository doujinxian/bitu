package com.binance.api.client.domain.event;

import com.binance.api.client.constant.BinanceApiConstants;
import com.binance.api.client.domain.market.OrderBookEntry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Order book of a symbol in Binance.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBookEvent {

  /**
   * Last update id of this order book.
   */
  @JsonProperty("lastUpdateId")
  private long lastUpdateId;

  /**
   * List of bids (price/qty).
   */
  @JsonProperty("bids")
  private List<OrderBookEntry> bids;

  /**
   * List of asks (price/qty).
   */
  @JsonProperty("asks")
  private List<OrderBookEntry> asks;

  public long getLastUpdateId() {
    return lastUpdateId;
  }

  public void setLastUpdateId(long lastUpdateId) {
    this.lastUpdateId = lastUpdateId;
  }

  public List<OrderBookEntry> getBids() {
    return bids;
  }

  public void setBids(List<OrderBookEntry> bids) {
    this.bids = bids;
  }

  public List<OrderBookEntry> getAsks() {
    return asks;
  }

  public void setAsks(List<OrderBookEntry> asks) {
    this.asks = asks;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
        .append("lastUpdateId", lastUpdateId)
        .append("bids", bids)
        .append("asks", asks)
        .toString();
  }
}
