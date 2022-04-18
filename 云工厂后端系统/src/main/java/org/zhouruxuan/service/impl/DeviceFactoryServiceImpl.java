package org.zhouruxuan.service.impl;/**
 * @author zhouruxuan
 * @create 2021-07-14-21:57
 */


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zhouruxuan.entity.Device;
import org.zhouruxuan.mapper.DeviceMapper;
import org.zhouruxuan.service.DeviceFactoryService;

/**
 * TODO
 * zhouruxuan
 * 2021/7/14 21:57
 * 1.0
 **/
@Service
public class DeviceFactoryServiceImpl  extends ServiceImpl<DeviceMapper, Device> implements DeviceFactoryService {
}
