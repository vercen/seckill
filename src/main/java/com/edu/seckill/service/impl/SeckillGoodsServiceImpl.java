package com.edu.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seckill.mapper.SeckillGoodsMapper;
import com.edu.seckill.pojo.TSeckillGoods;
import com.edu.seckill.service.SeckillGoodsService;
import org.springframework.stereotype.Service;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 20:30
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, TSeckillGoods> implements SeckillGoodsService {
}
