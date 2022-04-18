package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-15-15:22
 */


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * zhouruxuan
 * 2021/7/15 15:22
 * 1.0
 **/
@Data
public class OrderItemQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;

    private String orderNo;

    private String receiverName;

    private String belong;

    private String winner;

    private String bidEndTime;

    private Integer orderState;

    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
    private String end;

}
