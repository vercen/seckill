package com.edu.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.seckill.mapper.TGoodsMapper;

import com.edu.seckill.pojo.TGoods;

import com.edu.seckill.service.GoodsService;
import com.edu.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 20:12
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements GoodsService {
    @Resource
    private TGoodsMapper tGoodsMapper;

    @Override
    public List<GoodsVo> findGoodsvo() {
        List<GoodsVo> goodVo = tGoodsMapper.findGoodVo();
        return goodVo;
    }

    @Override
    public GoodsVo findgoodbyid(Long goodid) {
        GoodsVo goodsVoByGoodsId = tGoodsMapper.findGoodsVoByGoodsId(goodid);
        return goodsVoByGoodsId;
    }
}
