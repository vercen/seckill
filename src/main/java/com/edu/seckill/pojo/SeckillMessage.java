package com.edu.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/24 19:13
 * SeckillMessage 表示秒杀消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodsId;
}
