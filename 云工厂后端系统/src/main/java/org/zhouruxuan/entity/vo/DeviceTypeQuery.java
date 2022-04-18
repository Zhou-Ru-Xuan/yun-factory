package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-14-21:53
 */


import lombok.Data;

/**
 * TODO
 * zhouruxuan
 * 2021/7/14 21:53
 * 1.0
 **/
@Data
public class DeviceTypeQuery {
    private static final long serialVersionUID = 1L;

    private String deviceName;
    private String deviceNo;
    private String typeName;
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
    private String end;
}
