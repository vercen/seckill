package com.edu.seckill.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_seckill_goods")
public class TSeckillGoods {

  @TableId(value = "id",type = IdType.AUTO)
  private Long id;
  private long goodsId;
  private double seckillPrice;
  private long stockCount;
  private Date startDate;
  private Date endDate;

}
