package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.OrderItem;
import org.zhouruxuan.entity.vo.OrderItemQuery;
import org.zhouruxuan.service.OrderItemService;

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
@RequestMapping("/orderItem")
@CrossOrigin
public class OrderItemController {
    @Autowired
    @Qualifier("orderItemServiceImpl")
    OrderItemService orderItemService;


    @GetMapping("findAll")
    public R findAllOrderItems() {
        return R.ok().data("items", orderItemService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeOrderItem(@PathVariable String id) {
        boolean flag = orderItemService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }


    }

    @GetMapping("pageOrderItem/{current}/{limit}")
    public R pageListOrderItem(@PathVariable long current,
                            @PathVariable long limit) {
        //创建page对象
        Page<OrderItem> pageOrderItem = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageOrderItem对象里面
        orderItemService.page(pageOrderItem, null);

        long total = pageOrderItem.getTotal();//总记录数
        List<OrderItem> records = pageOrderItem.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("orderItems", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageOrderItemCondition/{current}/{limit}")
    public R pageOrderItemCondition(@PathVariable long current, @PathVariable long limit,
                                 @RequestBody(required = false) OrderItemQuery orderItemQuery) {
        //创建page对象
        Page<OrderItem> pageOrderItem = new Page<>(current, limit);

        //构建条件
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String orderNo = orderItemQuery.getOrderNo();
        String bidEndTime = orderItemQuery.getBidEndTime();
        String productName = orderItemQuery.getProductName();
        String receiverName = orderItemQuery.getReceiverName();
        Integer orderState = orderItemQuery.getOrderState();
        String winner = orderItemQuery.getWinner();
        String belong = orderItemQuery.getBelong();
        String begin = orderItemQuery.getBegin();
        String end = orderItemQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
//        if (!StringUtils.isEmpty(orderState)) {
//            //构建条件
//            wrapper.eq("order_state", orderState);
//        }
        //对于云工厂用户，获取排产订单时，既能获得未排产的，也能查看已排产的
        if(!StringUtils.isEmpty(orderState)){
            if(orderState==3){
                wrapper.ge("order_state",3);
//                wrapper.le("order_state",8);
            }else{
                wrapper.eq("order_state", orderState);
            }

        }
        if (!StringUtils.isEmpty(orderNo)) {
            //构建条件
            wrapper.like("order_no", orderNo);
        }
        if (!StringUtils.isEmpty(bidEndTime)) {
            //构建条件
            wrapper.le("bid_end_time", bidEndTime);
        }
        if (!StringUtils.isEmpty(belong)) {
            //构建条件
            wrapper.like("belong", belong);
        }
        if (!StringUtils.isEmpty(winner)) {
            //构建条件
            wrapper.eq("winner", winner);
        }
        if (!StringUtils.isEmpty(productName)) {
            //构建条件
            wrapper.like("product_name", productName);
        }
        if (!StringUtils.isEmpty(receiverName)) {
            wrapper.like("receiver_name", receiverName);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //调用方法实现条件查询分页
        orderItemService.page(pageOrderItem, wrapper);

        long total = pageOrderItem.getTotal();//总记录数
        List<OrderItem> records = pageOrderItem.getRecords(); //数据list集合
        return R.ok().data("total", total).data("orderItems", records);
    }

    //添加订单信息接口的方法
    @PostMapping("addOrderItem")
    public R addOrderItem(@RequestBody OrderItem orderItem) {
        boolean save = orderItemService.save(orderItem);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据订单信息id进行查询
    @GetMapping("getOrderItem/{id}")
    public R getOrderItem(@PathVariable String id) {
        OrderItem orderItem = orderItemService.getById(id);
        return R.ok().data("orderItem", orderItem);
    }

    //订单信息修改功能
    @PostMapping("updateOrderItem")
    public R updateOrderItem(@RequestBody OrderItem orderItem) {
        boolean flag = orderItemService.updateById(orderItem);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

