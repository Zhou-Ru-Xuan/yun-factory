package org.zhouruxuan.entity.vo;/**
 * @author zhouruxuan
 * @create 2021-07-16-21:44
 */


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 * zhouruxuan
 * 2021/7/16 21:44
 * 1.0
 **/
@Data
public class DeviceHireQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String deviceName;

}
