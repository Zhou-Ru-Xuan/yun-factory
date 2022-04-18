package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Capacity;
import org.zhouruxuan.entity.Product;
import org.zhouruxuan.entity.vo.CapacityQuery;
import org.zhouruxuan.service.CapacityService;
import org.zhouruxuan.service.ProductService;

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
@RequestMapping("/capacity")
@CrossOrigin
public class CapacityController {
    @Autowired
    @Qualifier("capacityServiceImpl")
    CapacityService capacityService;

    @Autowired
    @Qualifier("productServiceImpl")
    ProductService productService;


    @GetMapping("findAll")
    public R findAllCapacitys() {
        return R.ok().data("items", capacityService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeCapacity(@PathVariable String id) {
        boolean flag = capacityService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageCapacity/{current}/{limit}")
    public R pageListCapacity(@PathVariable long current,
                              @PathVariable long limit) {
        //创建page对象
        Page<Capacity> pageCapacity = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageCapacity对象里面
        capacityService.page(pageCapacity, null);

        long total = pageCapacity.getTotal();//总记录数
        List<Capacity> records = pageCapacity.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("capacitys", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageCapacityCondition/{current}/{limit}")
    public R pageCapacityCondition(@PathVariable long current, @PathVariable long limit,
                                   @RequestBody(required = false) CapacityQuery capacityQuery) {
        //创建page对象
        Page<Capacity> pageCapacity = new Page<>(current, limit);

        //构建条件
        QueryWrapper<Capacity> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql

        String productName = capacityQuery.getProductName();
        Integer power = capacityQuery.getPower();
        String deviceName = capacityQuery.getDeviceName();
        String begin = capacityQuery.getBegin();
        String end = capacityQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(productName)) {
            //构建条件
            wrapper.like("product_name", productName);
        }
        if (!StringUtils.isEmpty(power)) {
            //构建条件
            wrapper.eq("power", power);
        }
        if (!StringUtils.isEmpty(deviceName)) {
            //构建条件
            wrapper.eq("device_name", deviceName);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //调用方法实现条件查询分页
        capacityService.page(pageCapacity, wrapper);

        long total = pageCapacity.getTotal();//总记录数
        List<Capacity> records = pageCapacity.getRecords(); //数据list集合
        return R.ok().data("total", total).data("capacitys", records);
    }

    //添加产能信息接口的方法
    @PostMapping("addCapacity")
    public R addCapacity(@RequestBody Capacity capacity) {
        boolean save = capacityService.save(capacity);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据产能信息id进行查询
    @GetMapping("getCapacity/{id}")
    public R getCapacity(@PathVariable String id) {
        Capacity capacity = capacityService.getById(id);
        return R.ok().data("capacity", capacity);
    }

    //产能信息修改功能
    @PostMapping("updateCapacity")
    public R updateCapacity(@RequestBody Capacity capacity) {
        boolean flag = capacityService.updateById(capacity);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @PostMapping("getProducts")
    public R getProducts(@RequestBody CapacityQuery capacityQuery) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();

        wrapper.notInSql("product.product_name", "select product.product_name from capacity,product where capacity.belong = '" + capacityQuery.getBelong()  + "' and  product.product_name = capacity.product_name and  capacity.device_name = '" + capacityQuery.getDeviceName()+"'");
        List<Product> products = productService.list(wrapper);
        return R.ok().data("products", products);
    }
}

