package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-13-15:50
 */


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * zhouruxuan
 * 2021/7/13 15:50
 * 1.0
 **/
@Data
public class FactoryQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;

    private String factoryName;

    private String factoryState;

    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    private String end;
}
