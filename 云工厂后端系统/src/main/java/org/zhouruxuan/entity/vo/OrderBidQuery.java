package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-16-9:40
 */


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * zhouruxuan
 * 2021/7/16 9:40
 * 1.0
 **/
@Data
public class OrderBidQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer orderBidState;

    private String factoryName;

    private String orderId;

    private String username;

    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
    private String end;

}
