package com.edu.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("seckill_user")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 用户ID,手机号码
   */
  @TableId(value = "id", type = IdType.ASSIGN_ID)
  private Long id;

  private String nickname;
  private String password;
  private String slat;
  /**
   * 头像
   */
  private String head;
  /**
   * 注册时间
   */
  private Date registerDate;
  /*最后登录时间*/
  private Date lastLoginDate;
  /*登录次数*/
  private long loginCount;

}
