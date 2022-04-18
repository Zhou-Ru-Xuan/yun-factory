package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Device;
import org.zhouruxuan.entity.OrderItem;
import org.zhouruxuan.entity.OrderScheduling;
import org.zhouruxuan.entity.vo.OrderSchedulingQuery;
import org.zhouruxuan.service.DeviceService;
import org.zhouruxuan.service.OrderItemService;
import org.zhouruxuan.service.OrderSchedulingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 *  前端控制器

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
@RestController
@RequestMapping("/orderScheduling")
@CrossOrigin
public class OrderSchedulingController {
    @Autowired
    @Qualifier("orderSchedulingServiceImpl")
    OrderSchedulingService orderSchedulingService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    OrderItemService orderItemService;

    @GetMapping("findAll")
    public R findAllOrderSchedulings() {
        return R.ok().data("orderSchedulings", orderSchedulingService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeOrderScheduling(@PathVariable String id) {
        //
        boolean flag = orderSchedulingService.removeById(id);

        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageOrderScheduling/{current}/{limit}")
    public R pageListOrderScheduling(@PathVariable long current,
                               @PathVariable long limit) {
        //创建page对象
        Page<OrderScheduling> pageOrderScheduling = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageOrderScheduling对象里面
        orderSchedulingService.page(pageOrderScheduling, null);

        long total = pageOrderScheduling.getTotal();//总记录数
        List<OrderScheduling> records = pageOrderScheduling.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("orderSchedulings", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageOrderSchedulingCondition/{current}/{limit}")
    public R pageOrderSchedulingCondition(@PathVariable long current, @PathVariable long limit,
                                    @RequestBody(required = false) OrderSchedulingQuery orderSchedulingQuery) {
        //创建page对象
        Page<OrderScheduling> pageOrderScheduling = new Page<>(current, limit);

        //构建条件
        QueryWrapper<OrderScheduling> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String orderId = orderSchedulingQuery.getOrderId();
        String deviceName = orderSchedulingQuery.getDeviceName();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(deviceName)) {
            //构建条件
            wrapper.like("device_name", deviceName);
        }
        if (!StringUtils.isEmpty(orderId)) {
            //构建条件
            wrapper.eq("order_id", orderId);
        }


        //调用方法实现条件查询分页
        orderSchedulingService.page(pageOrderScheduling, wrapper);

        long total = pageOrderScheduling.getTotal();//总记录数
        List<OrderScheduling> records = pageOrderScheduling.getRecords(); //数据list集合
        return R.ok().data("total", total).data("orderSchedulings", records);
    }

    //添加订单排产信息接口的方法
    @PostMapping("addOrderScheduling")
    public R addOrderScheduling(@RequestBody OrderScheduling orderScheduling) {
        //1.添加排产信息
        boolean save = orderSchedulingService.save(orderScheduling);
        //2.根据deviceName获取设备，更新deviceState
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        wrapper.eq("device_name",orderScheduling.getDeviceName());
        Device one = deviceService.getOne(wrapper);
        one.setDeviceState(1);
        deviceService.updateById(one);
        //3.更新订单状态为已排产
        OrderItem orderItem = orderItemService.getById(orderScheduling.getOrderId());
        orderItem.setOrderState(4);
        orderItemService.updateById(orderItem);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据订单排产信息id进行查询
    @GetMapping("getOrderScheduling/{id}")
    public R getOrderScheduling(@PathVariable String id) {
        OrderScheduling orderScheduling = orderSchedulingService.getById(id);
        return R.ok().data("orderScheduling", orderScheduling);
    }

    //订单排产信息修改功能
    @PostMapping("updateOrderScheduling")
    public R updateOrderScheduling(@RequestBody OrderScheduling orderScheduling) {
        boolean flag = orderSchedulingService.updateById(orderScheduling);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}

