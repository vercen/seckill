package com.edu.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.seckill.pojo.TGoods;
import com.edu.seckill.vo.GoodsVo;

import java.util.List;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 20:11
 */
public interface GoodsService extends IService<TGoods> {
    //秒杀商品表
    List<GoodsVo> findGoodsvo();


    GoodsVo findgoodbyid(Long goodid);

}
