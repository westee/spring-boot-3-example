package com.example.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(String  params) {
        var id = "1";
        rabbitTemplate.convertAndSend("orderExchange", "orderRoutingKey", params, message -> {
            message.getMessageProperties().setExpiration("120000");                              // 设置消息过期时间，单位毫秒
            message.getMessageProperties().setMessageId(id);
            return message;
        });
    }

    public void sendDelayedOrder(Date pickUpTime, String param) {
        var id = "2";
        rabbitTemplate.convertAndSend("orderExchange", "orderDelayRoutingKey", param, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(getMilSecond(pickUpTime)));// 设置消息过期时间，单位毫秒
            message.getMessageProperties().setMessageId(id);
            return message;
        });
    }

    public long getMilSecond(Date pickupTime) {
        long deliveryTime = pickupTime.getTime();
        // 计算过期时间的毫秒数
        return deliveryTime - System.currentTimeMillis();
    }
}
