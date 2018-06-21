package com.bitm.center.controller;

import com.bitm.service.orderBook.OrderBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: java类作用描述
 * @Author: jinxian.dou
 * @CreateDate: 2018/5/23 19:41
 */
@RestController
@RequestMapping(value = "/orderBook")
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    private Logger logger = LoggerFactory.getLogger(OrderBookController.class);

    @RequestMapping("")
    public String getOrderBook() {
        return "ok";
    }
}
