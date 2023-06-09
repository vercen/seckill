package com.edu.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.seckill.pojo.TGoods;
import com.edu.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 19:50
 */
public interface TGoodsMapper extends BaseMapper<TGoods> {

    //获取秒杀商品类列表
    List<GoodsVo> findGoodVo();

    //获取某个商品详情
    //获取商品详细
    GoodsVo findGoodsVoByGoodsId(Long goodsId);

}
