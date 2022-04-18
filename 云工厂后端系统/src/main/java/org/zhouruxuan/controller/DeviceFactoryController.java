package org.zhouruxuan.controller;/**
 * @author zhouruxuan
 * @create 2021-07-14-21:45
 */


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Device;
import org.zhouruxuan.entity.vo.DeviceQuery;
import org.zhouruxuan.service.DeviceFactoryService;
import org.zhouruxuan.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * zhouruxuan
 * 2021/7/14 21:45
 * 1.0
 **/
@RestController
@RequestMapping("/deviceFactory")
@CrossOrigin
public class DeviceFactoryController {
    @Autowired
    @Qualifier("deviceFactoryServiceImpl")
    DeviceFactoryService deviceFactoryService;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @GetMapping("findAll")
    public R findAllDeviceFactorys() {
        return R.ok().data("items", deviceFactoryService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeDeviceFactory(@PathVariable String id) {
        boolean flag = deviceFactoryService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageDeviceFactory/{current}/{limit}")
    public R pageListDeviceFactory(@PathVariable long current,
                            @PathVariable long limit) {
        //创建page对象
        Page<Device> pageDeviceFactory = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageDeviceFactory对象里面
        deviceFactoryService.page(pageDeviceFactory, null);

        long total = pageDeviceFactory.getTotal();//总记录数
        List<Device> records = pageDeviceFactory.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("deviceFactorys", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageDeviceFactoryCondition/{current}/{limit}")
    public R pageDeviceFactoryCondition(@PathVariable long current, @PathVariable long limit,
                                        @RequestBody(required = false) DeviceQuery deviceFactoryQuery) {


        //创建page对象
        Page<Device> pageDeviceFactory = new Page<>(current, limit);

        //构建条件
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String deviceNo = deviceFactoryQuery.getDeviceNo();
        String deviceName = deviceFactoryQuery.getDeviceName();
        String typeName = deviceFactoryQuery.getTypeName();
        Integer deviceState = deviceFactoryQuery.getDeviceState();
        Integer deviceSource = deviceFactoryQuery.getDeviceSource();
        String begin = deviceFactoryQuery.getBegin();
        String end = deviceFactoryQuery.getEnd();

        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(deviceState)) {
            //构建条件
            wrapper.eq("device_state", deviceState);
        }
        if (!StringUtils.isEmpty(deviceSource)) {
            //构建条件
            wrapper.eq("device_source", deviceSource);
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


        //调用方法实现条件查询分页
        deviceFactoryService.page(pageDeviceFactory, wrapper);

        long total = pageDeviceFactory.getTotal();//总记录数
        List<Device> records = pageDeviceFactory.getRecords(); //数据list集合
        return R.ok().data("total", total).data("deviceFactorys", records);
    }

    //添加产品信息接口的方法
    @PostMapping("addDeviceFactory")
    public R addDeviceFactory(@RequestBody Device deviceFactory) {
        boolean save = deviceFactoryService.save(deviceFactory);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据产品信息id进行查询
    @GetMapping("getDeviceFactory/{id}")
    public R getDeviceFactory(@PathVariable String id) {
        Device deviceFactory = deviceFactoryService.getById(id);
        return R.ok().data("deviceFactory", deviceFactory);
    }

    //产品信息修改功能
    @PostMapping("updateDeviceFactory")
    public R updateDeviceFactory(@RequestBody Device deviceFactory) {
        boolean flag = deviceFactoryService.updateById(deviceFactory);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}
