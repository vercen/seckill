package com.edu.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/23 23:18
 */
@Configuration
public class RabbitMQConfig {

    private static final String QUEUE = "queue";
    private static final String QUEUE01 = "queue_fanout01";
    private static final String QUEUE02 = "queue_fanout02";
    private static final String exchange = "fanoutExchange";

    private static final String QUEUE_Direct1 = "queue_direct01";
    private static final String QUEUE_Direct2 = "queue_direct02";
    private static final String exchange_Direct = "directExchange";
    private static final String rountingkey01 = "queue.red";
    private static final String rountingkey02 = "queue.green";
    /**
     * 1. 配置队列
     * 2. 队列名为queue
     * 3. true 表示: 持久化
     * durable： 队列是否持久化。队列默认是存放到内存中的，rabbitmq 重启则丢失，
     * 若想重启之后还存在则队列要持久化，
     * 保存到Erlang 自带的Mnesia 数据库中，当rabbitmq 重启之后会读取该数据库
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue queue1() {
        return new Queue(QUEUE01, true);
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE02, true);
    }

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(exchange);
    }

    /**
     * 将queue_fanout01 队列绑定到交换机fanoutExchange
     * @return
     */
    @Bean
    public Binding binding01() {
        return BindingBuilder.bind(queue1()).to(exchange());
    }

    /**
     * 将queue_fanout02 队列绑定到交换机fanoutExchange
     * @return
     */
    @Bean
    public Binding binding02() {
        return BindingBuilder.bind(queue2()).to(exchange());
    }

    //direct 模式
    /**
     * 创建QUEUE_Direct1 队列
     * @return
     */
    @Bean
    public Queue queue_direct1() {
        return new Queue(QUEUE_Direct1);
    }
    @Bean
    public Queue queue_direct2() {
        return new Queue(QUEUE_Direct2);
    }
    /**
     * 创建Direct 交换机
     * @return
     */
    @Bean
    public DirectExchange exchange_direct() {
        return new DirectExchange(exchange_Direct);
    }
    /**
     * 将队列绑定到指定交换机，并指定路由
     * queue_direct1(): 队列
     * exchange_direct(): 交换机
     * rountingkey01: 路由
     * @return
     */
    @Bean
    public Binding binding_direct1() {
        return BindingBuilder.bind(queue_direct1()).to(exchange_direct()).with(rountingkey01);
    }
    @Bean
    public Binding binding_direct2() {
        return BindingBuilder.bind(queue_direct2()).to(exchange_direct()).with(rountingkey02);
    }
}
