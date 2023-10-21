package com.example.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RabbitMQConfig {
    /**
     * orderQueue是正常的订单队列，用于接收实时订单
     * @return Queue
     */
    @Bean
    public Queue orderQueue() {
        return new Queue("orderQueue");
    }

    /**
     * orderDelayQueue是延迟队列，用于接收延迟订单
     * @return Queue
     */
    @Bean
    public Queue orderDelayQueue() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");                     // 设置死信交换机
        args.put("x-dead-letter-routing-key", "orderQueue");        // 设置死信路由键
        args.put("x-message-ttl", 60000);                           // 设置消息过期时间，单位毫秒
        return new Queue("orderDelayQueue", true, false, false, args);
    }

    @Bean
    public Queue orderCancelQueue() {
        return new Queue("orderCancelQueue", true, false, false);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("orderExchange");
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("orderRoutingKey");
    }

    @Bean
    public Binding orderDelayBinding() {
        return BindingBuilder.bind(orderDelayQueue()).to(orderExchange()).with("orderDelayRoutingKey");
    }

    @Bean
    public Binding orderCancelBinding() {
        return BindingBuilder.bind(orderCancelQueue()).to(orderExchange()).with("orderCancelRoutingKey");
    }
}
