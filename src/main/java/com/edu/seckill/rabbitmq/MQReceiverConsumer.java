package com.edu.seckill.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.edu.seckill.pojo.SeckillMessage;
import com.edu.seckill.pojo.TOrder;
import com.edu.seckill.pojo.User;
import com.edu.seckill.service.GoodsService;
import com.edu.seckill.service.OrderService;
import com.edu.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/24 19:28
 * 消费者
 */
@Service
@Slf4j
public class MQReceiverConsumer {
    @Resource
    private GoodsService goodsService;
    @Resource
    private OrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void queue(String message) {
        log.info("接收到的消息：" + message);
        //TOrder order = orderService.seckill(user, goodsVo);
        //反序列化
        SeckillMessage seckillMessage = JSONUtil.toBean(message, SeckillMessage.class);
        Long goodId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findgoodbyid(goodId);
        orderService.seckill(user, goodsVo);
    }

}
