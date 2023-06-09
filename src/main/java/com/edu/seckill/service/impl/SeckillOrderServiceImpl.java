package com.edu.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seckill.mapper.SeckillGoodsMapper;
import com.edu.seckill.mapper.SeckillOrderMapper;
import com.edu.seckill.pojo.TSeckillGoods;
import com.edu.seckill.pojo.TSeckillOrder;
import com.edu.seckill.service.SeckillGoodsService;
import com.edu.seckill.service.SeckillOrderService;
import org.springframework.stereotype.Service;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 23:24
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, TSeckillOrder> implements SeckillOrderService {
}
