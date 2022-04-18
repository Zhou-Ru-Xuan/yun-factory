package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Device;
import org.zhouruxuan.entity.vo.DeviceQuery;
import org.zhouruxuan.service.DeviceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 * 前端控制器

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
@RestController
@RequestMapping("/device")
@CrossOrigin
public class DeviceController {
    @Autowired
    @Qualifier("deviceServiceImpl")
    DeviceService deviceService;


    @GetMapping("findAll")
    public R findAllDevices() {
        return R.ok().data("items", deviceService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeDevice(@PathVariable String id) {
        boolean flag = deviceService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageDevice/{current}/{limit}")
    public R pageListDevice(@PathVariable long current,
                            @PathVariable long limit) {
        //创建page对象
        Page<Device> pageDevice = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageDevice对象里面
        deviceService.page(pageDevice, null);

        long total = pageDevice.getTotal();//总记录数
        List<Device> records = pageDevice.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("devices", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageDeviceCondition/{current}/{limit}")
    public R pageDeviceCondition(@PathVariable long current, @PathVariable long limit,
                                 @RequestBody(required = false) DeviceQuery deviceQuery) {
        //创建page对象
        Page<Device> pageDevice = new Page<>(current, limit);

        //调用方法实现条件查询分页
        deviceService.page(pageDevice, getWrapper(deviceQuery));

        long total = pageDevice.getTotal();//总记录数
        List<Device> records = pageDevice.getRecords(); //数据list集合
        return R.ok().data("total", total).data("devices", records);
    }

    //添加产品信息接口的方法
    @PostMapping("addDevice")
    public R addDevice(@RequestBody Device device) {
        boolean save = deviceService.save(device);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据产品信息id进行查询
    @GetMapping("getDevice/{id}")
    public R getDevice(@PathVariable String id) {
        Device device = deviceService.getById(id);
        return R.ok().data("device", device);
    }

//    @PostMapping("getDeviceByCondition")
//    public R getDeviceByCondition(DeviceQuery deviceQuery) {
//        List<Device> list = deviceService.list(getWrapper(deviceQuery));
//        return R.ok().data("total", list.size()).data("devices", list);
//    }

    /**
     * 动态SQL条件拼接
     *
     * @param deviceQuery
     * @return
     */
    private QueryWrapper<Device> getWrapper(DeviceQuery deviceQuery) {
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        String deviceNo = deviceQuery.getDeviceNo();
        String deviceName = deviceQuery.getDeviceName();
        String typeName = deviceQuery.getTypeName();
        Integer deviceState = deviceQuery.getDeviceState();
        Integer deviceSource = deviceQuery.getDeviceSource();
        Integer hireState = deviceQuery.getHireState();
        String belong = deviceQuery.getBelong();
        String begin = deviceQuery.getBegin();
        String end = deviceQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(deviceState)) {
            //构建条件
            wrapper.eq("device_state", deviceState);
        }
        if (!StringUtils.isEmpty(hireState)) {
            //构建条件
            wrapper.eq("hire_state", hireState);
        }
        if (!StringUtils.isEmpty(deviceSource)) {
            //构建条件
            wrapper.eq("device_source", deviceSource);
        }
        if (!StringUtils.isEmpty(belong)) {
            //构建条件
            wrapper.like("belong", belong);
        }
        if (!StringUtils.isEmpty(deviceNo)) {
            //构建条件
            wrapper.like("device_no", deviceNo);
        }
        if (!StringUtils.isEmpty(deviceName)) {
            wrapper.like("device_name", deviceName);
        }
        if (!StringUtils.isEmpty(typeName)) {
            wrapper.like("type_name", typeName);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        return wrapper;
    }

    /**
     * 获取属于belong工厂，但未参与orderId订单排产的设备
     *
     * @param belong
     * @param orderId
     * @return
     */
    @PostMapping("getDeviceByBelongAndOrderId/{belong}/{orderId}")
    public R getDeviceByBelongAndOrderId(@PathVariable String belong, @PathVariable String orderId) {
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        //获取设备，条件为属于该用户名belong的设备，同时没参与该订单orderId的排产
        wrapper.eq("device.belong", belong).notInSql("device.device_name", "select device.device_name from device,order_scheduling where order_scheduling.is_deleted = 0 and  device.device_name = order_scheduling.device_name  and order_id = '" + orderId + "'");
        List<Device> list = deviceService.list(wrapper);

        return R.ok().data("devices", list);

    }

    //产品信息修改功能
    @PostMapping("updateDevice")
    public R updateDevice(@RequestBody Device device) {
        boolean flag = deviceService.updateById(device);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

