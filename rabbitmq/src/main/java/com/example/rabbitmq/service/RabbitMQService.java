package com.example.rabbitmq.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RabbitMQService {
    @RabbitListener(queues = "orderQueue")
    public void deal(String params, Channel channel, Message message) {
        System.out.println("orderQueue" + params);
    }

    @RabbitListener(queues = "orderDelayQueue")
    public void dealDelay(String params, Channel channel, Message message) {
        System.out.println("orderDelayQueue" + params);
    }
}
