//package com.edu.seckill.controller;
//
//import com.edu.seckill.rabbitmq.MQSender;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.annotation.Resource;
//
///**
// * @author vercen
// * @version 1.0
// * @date 2023/4/23 23:36
// * 学习MQ
// */
//@Controller
//public class RabbitMQHandler {
//    @Resource
//    private MQSender mqSender;
//
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq() {
//        mqSender.send("hello");
//    }
//
//    @GetMapping("/mq/fanout")
//    @ResponseBody
//    public void fanout() {
//        mqSender.sendFanout("hello~");
//    }
//
//    //direct 模式
//    @GetMapping("/mq/direct01")
//    @ResponseBody
//    public void direct01() {
//        mqSender.sendDirect1("hello:red");
//    }
//
//    @GetMapping("/mq/direct02")
//    @ResponseBody
//    public void direct02() {
//        mqSender.sendDirect2("hello:green");
//    }
//
//}
