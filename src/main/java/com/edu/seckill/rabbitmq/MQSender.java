package com.edu.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/23 23:25
 * 消息发送者/生产者
 * 学习MQ
 */
@Service
@Slf4j
public class MQSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg) {
        log.info("发送消息{}", msg);
        //向queue 队列发送消息
        rabbitTemplate.convertAndSend("queue", msg);
    }

    public void sendFanout(Object msg) {
        log.info("发送消息" + msg);
        //消息发送到交换机
        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    }

    public void sendDirect1(Object msg) {
        log.info("发送消息" + msg);
        //将消息发送到directExchange 交换机, 同时指定路由queue.red
        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
    }
    public void sendDirect2(Object msg) {
        log.info("发送消息" + msg);
        //将消息发送到directExchange 交换机, 同时指定路由queue.green
        rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
    }



}
