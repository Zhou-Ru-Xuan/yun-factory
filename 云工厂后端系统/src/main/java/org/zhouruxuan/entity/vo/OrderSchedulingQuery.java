package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-16-13:59
 */


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * zhouruxuan
 * 2021/7/16 13:59
 * 1.0
 **/
@Data
public class OrderSchedulingQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceName;

    private String orderId;

}
