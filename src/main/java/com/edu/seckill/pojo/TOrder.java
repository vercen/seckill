package com.edu.seckill.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_order")
public class TOrder {

  @TableId(value = "id",type = IdType.AUTO)
  private Long id;
  private long userId;
  private long goodsId;
  private long deliveryAddrId;
  private String goodsName;
  private long goodsCount;
  private double goodsPrice;
  private long orderChannel;
  private long status;
  private Date createDate;
  private Date payDate;

}
