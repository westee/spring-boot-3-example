package com.westee.quartz.controller;

import com.westee.quartz.service.OrderDeliveryScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@RestController
public class OrderController {
    OrderDeliveryScheduler orderDelivery;

    @Autowired
    public OrderController(OrderDeliveryScheduler orderDelivery) {
        this.orderDelivery = orderDelivery;
    }
    @RequestMapping("/order")
    public void scheduleOrderDelivery() {
        String orderId = "1";
        String name = "spicy";
        orderDelivery.scheduleOrderDelivery(getDate(), name, orderId);
    }

    @RequestMapping("/order/cancel")
    public void cancelOrderDelivery(String orderId) {
        orderDelivery.cancelOrderDelivery(orderId);
    }

    @RequestMapping("/order/reschedule")
    public void rescheduleOrderDelivery() {
        String orderId = "1";
        orderDelivery.rescheduleOrderDelivery(orderId, getDate());
    }

    public Date getDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.plusMinutes(1);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
