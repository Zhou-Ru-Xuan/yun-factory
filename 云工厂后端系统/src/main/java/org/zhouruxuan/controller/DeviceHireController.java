package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Device;
import org.zhouruxuan.entity.DeviceHire;
import org.zhouruxuan.entity.vo.DeviceHireQuery;
import org.zhouruxuan.service.DeviceHireService;
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
@RequestMapping("/deviceHire")
@CrossOrigin
public class DeviceHireController {
    @Autowired
    @Qualifier("deviceHireServiceImpl")
    DeviceHireService deviceHireService;
    @Autowired
    @Qualifier("deviceServiceImpl")
    DeviceService deviceService;


    @GetMapping("findAll")
    public R findAllDeviceHires() {
        return R.ok().data("items", deviceHireService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeDeviceHire(@PathVariable String id) {
        boolean flag = deviceHireService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageDeviceHire/{current}/{limit}")
    public R pageListDeviceHire(@PathVariable long current,
                                @PathVariable long limit) {
        //创建page对象
        Page<DeviceHire> pageDeviceHire = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageDeviceHire对象里面
        deviceHireService.page(pageDeviceHire, null);

        long total = pageDeviceHire.getTotal();//总记录数
        List<DeviceHire> records = pageDeviceHire.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("deviceHires", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageDeviceHireCondition/{current}/{limit}")
    public R pageDeviceHireCondition(@PathVariable long current, @PathVariable long limit,
                                     @RequestBody(required = false) DeviceHireQuery deviceHireQuery) {
        //创建page对象
        Page<DeviceHire> pageDeviceHire = new Page<>(current, limit);

        //构建条件
        QueryWrapper<DeviceHire> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql

        String deviceName = deviceHireQuery.getDeviceName();
        String username = deviceHireQuery.getUsername();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(deviceName)) {
            //构建条件
            wrapper.like("device_name", deviceName);
        }
        if (!StringUtils.isEmpty(username)) {
            //构建条件
            wrapper.like("username", username);
        }
        //调用方法实现条件查询分页
        deviceHireService.page(pageDeviceHire, wrapper);

        long total = pageDeviceHire.getTotal();//总记录数
        List<DeviceHire> records = pageDeviceHire.getRecords(); //数据list集合
        return R.ok().data("total", total).data("deviceHires", records);
    }

    //添加设备租用信息接口的方法
    @PostMapping("addDeviceHire")
    public R addDeviceHire(@RequestBody DeviceHire deviceHire) {
        //1.添加设备租用信息
        boolean save = deviceHireService.save(deviceHire);
        //2.获取设备并更新设备的belong、hireState
        Device device = deviceService.getById(deviceHire.getDeviceId());
        device.setBelong(deviceHire.getUsername());
        device.setHireState(1);
        deviceService.updateById(device);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据设备租用信息id进行查询
    @GetMapping("getDeviceHire/{id}")
    public R getDeviceHire(@PathVariable String id) {
        DeviceHire deviceHire = deviceHireService.getById(id);
        return R.ok().data("deviceHire", deviceHire);
    }

    //设备租用信息修改功能
    @PostMapping("updateDeviceHire")
    public R updateDeviceHire(@RequestBody DeviceHire deviceHire) {
        boolean flag = deviceHireService.updateById(deviceHire);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

