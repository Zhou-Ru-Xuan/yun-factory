package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-14-21:46
 */


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * zhouruxuan
 * 2021/7/14 21:46
 * 1.0
 **/
@Data
public class DeviceQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceNo;

    private String deviceName;

    private String typeName;

    private Integer deviceState;

    private Integer hireState;

    private Integer deviceSource;

    private String belong;

    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    private String end;
}
