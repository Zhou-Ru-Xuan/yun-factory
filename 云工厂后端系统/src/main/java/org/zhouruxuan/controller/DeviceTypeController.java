package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Device;
import org.zhouruxuan.entity.DeviceType;
import org.zhouruxuan.entity.vo.DeviceTypeQuery;
import org.zhouruxuan.service.DeviceService;
import org.zhouruxuan.service.DeviceTypeService;

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
@RequestMapping("/deviceType")
@CrossOrigin
public class DeviceTypeController {
    @Autowired
    @Qualifier("deviceTypeServiceImpl")
    DeviceTypeService deviceTypeService;
    @Autowired
    DeviceService deviceService;

    @GetMapping("findAll")
    public R findAllDeviceTypes() {
        return R.ok().data("deviceTypes", deviceTypeService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeDeviceType(@PathVariable String id) {
        boolean flag = deviceTypeService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageDeviceType/{current}/{limit}")
    public R pageListDeviceType(@PathVariable long current,
                                @PathVariable long limit) {
        //创建page对象
        Page<DeviceType> pageDeviceType = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageDeviceType对象里面
        deviceTypeService.page(pageDeviceType, null);

        long total = pageDeviceType.getTotal();//总记录数
        List<DeviceType> records = pageDeviceType.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("deviceTypes", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageDeviceTypeCondition/{current}/{limit}")
    public R pageDeviceTypeCondition(@PathVariable long current, @PathVariable long limit,
                                     @RequestBody(required = false) DeviceTypeQuery deviceTypeQuery) {
        //创建page对象
        Page<DeviceType> pageDeviceType = new Page<>(current, limit);

        //构建条件
        QueryWrapper<DeviceType> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String typeName = deviceTypeQuery.getTypeName();
        String begin = deviceTypeQuery.getBegin();
        String end = deviceTypeQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
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
        deviceTypeService.page(pageDeviceType, wrapper);

        long total = pageDeviceType.getTotal();//总记录数
        List<DeviceType> records = pageDeviceType.getRecords(); //数据list集合
        return R.ok().data("total", total).data("deviceTypes", records);
    }

    //添加产品类型接口的方法
    @PostMapping("addDeviceType")
    public R addDeviceType(@RequestBody DeviceType deviceType) {
        boolean save = deviceTypeService.save(deviceType);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据产品类型id进行查询
    @GetMapping("getDeviceType/{id}")
    public R getDeviceType(@PathVariable String id) {
        DeviceType deviceType = deviceTypeService.getById(id);
        return R.ok().data("deviceType", deviceType);
    }

    //产品类型修改功能
    @PostMapping("updateDeviceType")
    public R updateDeviceType(@RequestBody DeviceType deviceType) {
        //1.获取原产品类型的信息
        DeviceType byId = deviceTypeService.getById(deviceType.getId());
        //2.更新产品类型
        boolean flag = deviceTypeService.updateById(deviceType);
        //3.判断产品名称是否改变，如果改变，则更新相应产品的信息
        if (!byId.getTypeName().equals(deviceType.getTypeName())) {
            QueryWrapper<Device> wrapper = new QueryWrapper<>();
            wrapper.eq("type_name", byId.getTypeName());
            List<Device> list = deviceService.list(wrapper);
            for (Device device : list) {
                device.setTypeName(deviceType.getTypeName());
            }
            if (list.size() == 1)
                deviceService.updateById(list.get(0));
            else if (list.size() > 1) {
                deviceService.saveOrUpdateBatch(list, list.size());
            }
        }
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

