package com.edu.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_seckill_order")
public class TSeckillOrder {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  private long userId;
  private long orderId;
  private long goodsId;



}
