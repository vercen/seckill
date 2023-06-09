package com.edu.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seckill.pojo.TOrder;
import com.edu.seckill.pojo.User;
import com.edu.seckill.vo.GoodsVo;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 22:33
 */
public interface OrderService extends IService<TOrder> {
    //秒杀
    TOrder seckill(User user, GoodsVo goodsVo);
    //生成秒杀路径
    String createPath(User user, Long goodsId);
    //秒杀路径校验
    boolean checkPath(User user, Long goodsId, String path);
    //验证验证码
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
