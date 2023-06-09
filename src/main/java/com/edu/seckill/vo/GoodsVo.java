package com.edu.seckill.vo;

import com.edu.seckill.pojo.TGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 19:46
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class GoodsVo extends TGoods {

    private double seckillPrice;
    private long stockCount;
    private Date startDate;
    private Date endDate;

}
