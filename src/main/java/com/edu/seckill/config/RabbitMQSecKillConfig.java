package com.edu.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/24 19:20
 * 定义消息队列
 * 交换机
 */
@Configuration
public class RabbitMQSecKillConfig {

    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue queueSeckill() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchangeSeckill() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingSeckill() {
        return BindingBuilder.bind(queueSeckill()).to(topicExchangeSeckill()).with("seckill.#");
    }



}
