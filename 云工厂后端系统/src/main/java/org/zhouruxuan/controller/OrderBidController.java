package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Factory;
import org.zhouruxuan.entity.OrderBid;
import org.zhouruxuan.entity.OrderItem;
import org.zhouruxuan.entity.vo.OrderBidQuery;
import org.zhouruxuan.service.FactoryService;
import org.zhouruxuan.service.OrderBidService;
import org.zhouruxuan.service.OrderItemService;

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
@RequestMapping("/orderBid")
@CrossOrigin
public class OrderBidController {
    @Autowired
    @Qualifier("orderBidServiceImpl")
    OrderBidService orderBidService;

    @Autowired
    @Qualifier("orderItemServiceImpl")
    OrderItemService orderItemService;


    @Autowired
    @Qualifier("factoryServiceImpl")
    FactoryService factoryService;

    @GetMapping("findAll")
    public R findAllOrderBids() {
        return R.ok().data("items", orderBidService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeOrderBid(@PathVariable String id) {
        boolean flag = orderBidService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageOrderBid/{current}/{limit}")
    public R pageListOrderBid(@PathVariable long current,
                              @PathVariable long limit) {
        //创建page对象
        Page<OrderBid> pageOrderBid = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageOrderBid对象里面
        orderBidService.page(pageOrderBid, null);

        long total = pageOrderBid.getTotal();//总记录数
        List<OrderBid> records = pageOrderBid.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("orderBids", records);
        return R.ok().data(map);

    }

    @PostMapping("getOrderBidByUsernameAndOrderId")
    public R getOrderBidByUsernameAndOrderId(@RequestBody OrderBidQuery orderBidQuery) {
        QueryWrapper<OrderBid> wrapper = new QueryWrapper<>();
        String username = orderBidQuery.getUsername();
        String orderId = orderBidQuery.getOrderId();
        wrapper.eq("username", username).eq("order_id", orderId);
        OrderBid one = orderBidService.getOne(wrapper);
        return R.ok().data("orderBid", one);
    }

    // 条件查询带分页的方法
    @PostMapping("pageOrderBidCondition/{current}/{limit}")
    public R pageOrderBidCondition(@PathVariable long current, @PathVariable long limit,
                                   @RequestBody(required = false) OrderBidQuery orderBidQuery) {
        //创建page对象
        Page<OrderBid> pageOrderBid = new Page<>(current, limit);

        //构建条件
        QueryWrapper<OrderBid> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        Integer orderBidState = orderBidQuery.getOrderBidState();
        String orderId = orderBidQuery.getOrderId();
        String factoryName = orderBidQuery.getFactoryName();
        String username = orderBidQuery.getUsername();
        String begin = orderBidQuery.getBegin();
        String end = orderBidQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(orderBidState)) {
            //构建条件
            wrapper.eq("order_bid_state", orderBidState);
        }
        if (!StringUtils.isEmpty(orderId)) {
            //构建条件
            wrapper.like("order_id", orderId);
        }
        if (!StringUtils.isEmpty(factoryName)) {
            //构建条件
            wrapper.like("factory_name", factoryName);
        }
        if (!StringUtils.isEmpty(username)) {
            //构建条件
            wrapper.like("username", username);
        }

        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //调用方法实现条件查询分页
        orderBidService.page(pageOrderBid, wrapper);

        long total = pageOrderBid.getTotal();//总记录数
        List<OrderBid> records = pageOrderBid.getRecords(); //数据list集合
        return R.ok().data("total", total).data("orderBids", records);
    }

    //添加竞标信息接口的方法
    @PostMapping("addOrderBid")
    public R addOrderBid(@RequestBody OrderBid orderBid) {
        //1.根据用户名获取工厂名称，填入orderBid中
        QueryWrapper<Factory> factoryQueryWrapper = new QueryWrapper<>();
        factoryQueryWrapper.eq("username", orderBid.getUsername());
        Factory one = factoryService.getOne(factoryQueryWrapper);
        orderBid.setFactoryName(one.getFactoryName());
        //2.添加orderBid或修改竞标价格
        boolean save = orderBidService.saveOrUpdate(orderBid);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据竞标信息id进行查询
    @GetMapping("getOrderBid/{id}")
    public R getOrderBid(@PathVariable String id) {
        OrderBid orderBid = orderBidService.getById(id);
        return R.ok().data("orderBid", orderBid);
    }

    //竞标信息修改功能
    @PostMapping("updateOrderBid")
    public R updateOrderBid(@RequestBody OrderBid orderBid) {
        boolean flag = orderBidService.updateById(orderBid);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 经销商确定选标后，将所有该订单的竞标项更新
     *
     * @return
     */
    @PostMapping("updateOrderBidForComfirm/{id}/{orderId}")
    public R updateOrderBidForComfirm(@PathVariable String id, @PathVariable String orderId) {
        //1.获取竞标项信息
        OrderBid orderBid = orderBidService.getById(id);
        //2.更新该订单竞标项的信息，将状态修改为中标与未中标
        QueryWrapper<OrderBid> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        List<OrderBid> list = orderBidService.list(wrapper);

        for (OrderBid bid : list) {
            if (bid.getId().equals(id))
                bid.setOrderBidState(1);
            else
                bid.setOrderBidState(0);
        }
//        orderBid.setOrderBidState(1);
        //批处理修改OrderBid状态
        boolean flag = orderBidService.saveOrUpdateBatch(list, list.size());

        //3.更新订单winner信息，state信息
        OrderItem orderItem = orderItemService.getById(orderId);
        orderItem.setOrderState(3);
        orderItem.setWinner(orderBid.getUsername());
        orderItemService.updateById(orderItem);
        return flag ? R.ok() : R.error();

    }

}

