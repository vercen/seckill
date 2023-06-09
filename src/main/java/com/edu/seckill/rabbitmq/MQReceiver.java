package com.edu.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/23 23:34
 * 消息接受者/消费者
 * 学习MQ
 */
@Service
@Slf4j
public class MQReceiver {
    /**
     * 监听队列queue
     * @param msg
     */
    @RabbitListener(queues = "queue")
    public void receive(Object msg) {
        log.info("接收到消息--" + msg);
    }

    /**
     * 监听队列queue_fanout01
     * @param msg
     */
    @RabbitListener(queues = "queue_fanout01")
    public void receive1(Object msg) {
        log.info("从queue_fanout01 接收消息-" + msg);
    }

    /**
     * 监听队列queue_fanout01
     * @param msg
     */
    @RabbitListener(queues = "queue_fanout02")
    public void receive2(Object msg) {
        log.info("从queue_fanout02 接收消息-" + msg);
    }

    //direct
    @RabbitListener(queues = "queue_direct01")
    public void queue_direct1(Object msg) {
        log.info("从queue_direct01 接收消息-" + msg);
    }

    @RabbitListener(queues = "queue_direct02")
    public void queue_direct2(Object msg) {
        log.info("从queue_direct02 接收消息-" + msg);
    }
}
