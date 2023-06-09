package com.edu.seckill.exception;

import com.edu.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vercen
 * @version 1.0
 * @date 2023/4/22 13:41
 * 全局异常处理类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;

}
