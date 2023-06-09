package com.edu.seckill.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edu.seckill.config.AccessLimit;
import com.edu.seckill.pojo.SeckillMessage;
import com.edu.seckill.pojo.TOrder;
import com.edu.seckill.pojo.TSeckillOrder;
import com.edu.seckill.pojo.User;
import com.edu.seckill.rabbitmq.MQReceiver;
import com.edu.seckill.rabbitmq.MQReceiverConsumer;
import com.edu.seckill.rabbitmq.MQSenderMessage;
import com.edu.seckill.service.GoodsService;
import com.edu.seckill.service.OrderService;
import com.edu.seckill.service.SeckillOrderService;
import com.edu.seckill.vo.GoodsVo;
import com.edu.seckill.vo.RespBean;
import com.edu.seckill.vo.RespBeanEnum;
import com.ramostear.captcha.HappyCaptcha;
import com.ramostear.captcha.common.Fonts;
import com.ramostear.captcha.support.CaptchaStyle;
import com.ramostear.captcha.support.CaptchaType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 22:59
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    private OrderService orderService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MQSenderMessage mqSenderMessage;
    @Resource
    private RedisScript<Long> script;

    //当有库存为false，当无库存为true。防止库存没有了，
    //还会到Redis 进行判断操作
    private HashMap<Long, Boolean> entryStockMap = new HashMap<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsvo();

        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            entryStockMap.put(goodsVo.getId(), false);
        });


    }

    @RequestMapping(value = "/{path}/doSeckill")
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, Model model, User user, Long goodsId) {

        //System.out.println("-----秒杀V1.0--------");
        //===================秒杀v1.0 start =========================
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
//秒杀路径验证
        boolean b = orderService.checkPath(user, goodsId, path);
        if (!b) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }


        GoodsVo goodsVo = goodsService.findgoodbyid(goodsId);

        //判断库存
        if (goodsVo.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
        }


        //解决重复抢购
//        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
//        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
//            return "secKillFail";
//        }
        TSeckillOrder o = (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsVo.getId());
        if (o != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        //如果库存为空，避免总是到reids 去查询库存，给redis 增加负担（内存标记）
        if (entryStockMap.get(goodsId)) {//如果当前这个秒杀商品已经是空库存，则直接返回.
            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
        }
        //预减库存的代码, 能否放在防止复购代码之前? 试分析可能出现什么情况?  可能出现少卖
        //抢购，先尝试在redis减1  decrement原子性
//        Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsVo.getId());
//        if (decrement<0){
//            entryStockMap.put(goodsId, true);
//            redisTemplate.opsForValue().increment("seckillGoods:" + goodsVo.getId());
//            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
//        }


        //1 获取锁，setnx
        //得到一个uuid 值，作为锁的值
        String uuid = UUID.randomUUID().toString();

//        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        // 使用redis 执行lua 执行
//        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//        redisScript.setScriptText(script);
//        redisScript.setResultType(Long.class);

        Boolean lock =
                redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
        if (lock) {

            //拿到锁
            //定义lua 脚本
            Long decrement = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsVo.getId());
            if (decrement < 0) {
                entryStockMap.put(goodsId, true);
                redisTemplate.opsForValue().increment("seckillGoods:" + goodsVo.getId());
                //释放锁
                redisTemplate.execute(script, Arrays.asList("lock"), uuid);
                return RespBean.error(RespBeanEnum.ENTRY_STOCK);
            }
            //释放锁
            redisTemplate.execute(script, Arrays.asList("lock"), uuid);

        } else {
            return RespBean.error(RespBeanEnum.ENTRY_STOCK);
        }


        //将秒杀放入RQ
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSenderMessage.senderMessage(JSONUtil.toJsonStr(seckillMessage));

        return RespBean.error(RespBeanEnum.SEC_KILL_WAIT);


        //
//        TOrder order = orderService.seckill(user, goodsVo);
//        if (order == null) {
//            model.addAttribute("errmsg", RespBeanEnum.ENTRY_STOCK.getMessage());
//            return "secKillFail";
//        }
//
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//        return "orderDetail";
    }

    /**
     * 增加方法, 获取到秒杀路径, 随机生成的
     * user 对象是哪里来的?, 已经登录过,存放到了Redis 中
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if (user == null || goodsId < 0 || !StringUtils.hasText(captcha)) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

//        //限流防刷
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String uri = request.getRequestURI();
//        String key=uri + ":" + user.getId();
//        Integer count =(Integer) valueOperations.get(uri + ":" + user.getId());
//        if (count==null){
//            valueOperations.set(key,1,10,TimeUnit.SECONDS);//设置10秒
//        }else if(count<5){
//            valueOperations.increment(key);
//        }else {
//            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACH);
//        }

        //验证验证码
        boolean b = orderService.checkCaptcha(user, goodsId, captcha);
        if (!b) {
            return RespBean.error(RespBeanEnum.CAPTCHA_ERROR);
        }

        //创建真正的地址
        String url = orderService.createPath(user, goodsId);
        return RespBean.success(url);
    }

    //验证码
    @GetMapping("/captcha")
    public void happyCaptcha(User user, Long goodsId, HttpServletRequest request, HttpServletResponse response) {

        //System.out.println("请求来了... goodsId=" + goodsId);
        HappyCaptcha.require(request, response)
                .style(CaptchaStyle.IMG) //设置展现样式为动画
                .type(CaptchaType.ARITHMETIC) //设置验证码内容为数字
                .length(6) //设置字符长度为6
//                .width(220) //设置动画宽度为220
//                .height(100) //设置动画高度为80
                .font(Fonts.getInstance().zhFont()) //设置汉字的字体
                .build().finish(); //生成并输出验证码
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, (String) request.getSession().getAttribute("happy-captcha"), 100, TimeUnit.SECONDS);
    }

}
