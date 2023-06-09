package com.edu.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seckill.mapper.OrderMapper;
import com.edu.seckill.pojo.TOrder;
import com.edu.seckill.pojo.TSeckillGoods;
import com.edu.seckill.pojo.TSeckillOrder;
import com.edu.seckill.pojo.User;
import com.edu.seckill.service.OrderService;
import com.edu.seckill.service.SeckillGoodsService;
import com.edu.seckill.service.SeckillOrderService;
import com.edu.seckill.utils.MD5Utils;
import com.edu.seckill.utils.UUIDUtil;
import com.edu.seckill.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 22:37
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,TOrder> implements OrderService {

    @Resource
    private SeckillGoodsService seckillGoodsService;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public TOrder seckill(User user, GoodsVo goodsVo) {
        //查询商品
        TSeckillGoods tSeckillGoods = seckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goodsVo.getId()));
        //下面这句和判断库存不是原子性操作，会出现问题,后面再优化
        // tSeckillGoods.setStockCount(tSeckillGoods.getStockCount()-1);
        // seckillGoodsService.updateById(tSeckillGoods);


        boolean update = seckillGoodsService.update(new UpdateWrapper<TSeckillGoods>().setSql("stock_count = stock_count-1").eq("goods_id", goodsVo.getId()).gt("stock_count", 0));
        if (!update) {//如果更新失败,说明已经没有库存了
            return null;
        }

        //生成普通订单
        TOrder order = new TOrder();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(tSeckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //生成秒杀订单
        TSeckillOrder seckillOrder = new TSeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrderService.save(seckillOrder);

        //抢购之后放入redis，防止复购
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder);

        return order;
    }

    //生成秒杀路径
    @Override
    public String createPath(User user, Long goodsId) {
        String path = MD5Utils.md5(UUIDUtil.uuid());
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, path,60, TimeUnit.SECONDS);
        return path;
    }

    //秒杀路径校验
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0) {
            return false;
        }
        String str = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(str);
    }

    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || !StringUtils.hasText(captcha)) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
