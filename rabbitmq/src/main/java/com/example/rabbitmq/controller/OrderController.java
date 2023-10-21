package com.example.rabbitmq.controller;

import com.example.rabbitmq.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order_expiration")
    public void order() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusMinutes(1);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        orderService.sendDelayedOrder(date, "延迟订单");
    }

    @GetMapping("/order_delay")
    public void orderDelay() {
        orderService.sendOrder("设置message delay");
    }
}
