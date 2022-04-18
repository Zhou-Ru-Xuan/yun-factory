package org.zhouruxuan.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一异常类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyExcption extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String msg;
    
}