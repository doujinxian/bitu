package com.binance.api.examples;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.TradeHistoryItem;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.market.BookTicker;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.exception.BinanceApiException;

import java.util.List;

import static com.binance.api.client.domain.account.NewOrder.limitBuy;
import static com.binance.api.client.domain.account.NewOrder.marketBuy;

/**
 * Examples on how to place orders, cancel them, and query account information.
 */
public class OrdersExample {

  public static void main(String[] args) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("RxKTP14SHxVRXpRZW84LeLdfgFUuKk8Kpzz8NH1MhFQzkt3EEUGUmavmptHHFaY3", "cuKiNipR5Rzk3YHRKJFZq8YHAEl7VQ5czVzMBqRKLA3b1f7VAgZg1ELgBL1kv1UX");
    BinanceApiRestClient client = factory.newRestClient();

//   List<TradeHistoryItem> items = client.getHistoricalTrades("LINKETH", 10,28457L);
//   System.out.println(items);

   TickerPrice price = client.getPrice("LTCBTC");
   System.out.println(price);

      List<BookTicker> bookTickers = client.getBookTickers();
      StringBuilder sb = new StringBuilder();
      for(BookTicker b:bookTickers){
          sb.append("\"").append(b.getSymbol()).append("\",");
      }
      System.out.println(sb.toString());
      System.out.println(bookTickers);

      BookTicker bookTicker = client.getBookTicker("LTCBTC");
    System.out.println(bookTicker);

    // Getting list of open orders
    List<Order> openOrders = client.getOpenOrders(new OrderRequest("LINKETH"));
    System.out.println(openOrders);

    // Getting list of all orders with a limit of 10
    List<Order> allOrders = client.getAllOrders(new AllOrdersRequest("LINKETH").limit(10));
    System.out.println(allOrders);

    // Get status of a particular order
    Order order = client.getOrderStatus(new OrderStatusRequest("LINKETH", 751698L));
    System.out.println(order);

    // Canceling an order
    try {
      client.cancelOrder(new CancelOrderRequest("LINKETH", 756762l));
    } catch (BinanceApiException e) {
      System.out.println(e.getError().getMsg());
    }

    // Placing a test LIMIT order
    client.newOrderTest(limitBuy("LINKETH", TimeInForce.GTC, "1000", "0.0001"));

    // Placing a test MARKET order
    client.newOrderTest(marketBuy("LINKETH", "1000"));

    // Placing a real LIMIT order
    NewOrderResponse newOrderResponse = client.newOrder(limitBuy("LINKETH", TimeInForce.GTC, "1000", "0.0001").newOrderRespType(NewOrderResponseType.FULL));
    System.out.println(newOrderResponse);
  }

}
