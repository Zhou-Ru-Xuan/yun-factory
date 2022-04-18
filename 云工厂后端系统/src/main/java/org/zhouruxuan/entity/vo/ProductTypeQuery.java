package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-14-19:37
 */


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * zhouruxuan
 * 2021/7/14 19:37
 * 1.0
 **/
@Data
public class ProductTypeQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String typeName;

    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    private String end;
}
