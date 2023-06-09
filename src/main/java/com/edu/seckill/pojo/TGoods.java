package com.edu.seckill.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_goods")
@Data
public class TGoods {

  @TableId(value = "id",type = IdType.AUTO)
  private Long id;
  private String goodsName;
  private String goodsTitle;
  private String goodsImg;
  private String goodsDetail;
  private double goodsPrice;
  private Long goodsStock;



}
