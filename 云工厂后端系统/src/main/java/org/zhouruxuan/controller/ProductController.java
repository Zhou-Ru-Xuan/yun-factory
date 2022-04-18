package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Product;
import org.zhouruxuan.entity.vo.ProductQuery;
import org.zhouruxuan.service.ProductService;

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
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    @Autowired
    @Qualifier("productServiceImpl")
    ProductService productService;


    @GetMapping("findAll")
    public R findAllProducts() {
        return R.ok().data("products", productService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeProduct(@PathVariable String id) {
        boolean flag = productService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageProduct/{current}/{limit}")
    public R pageListProduct(@PathVariable long current,
                          @PathVariable long limit) {
        //创建page对象
        Page<Product> pageProduct = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageProduct对象里面
        productService.page(pageProduct, null);

        long total = pageProduct.getTotal();//总记录数
        List<Product> records = pageProduct.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("products", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageProductCondition/{current}/{limit}")
    public R pageProductCondition(@PathVariable long current, @PathVariable long limit,
                               @RequestBody(required = false) ProductQuery productQuery) {
        //创建page对象
        Page<Product> pageProduct = new Page<>(current, limit);

        //构建条件
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String productNo = productQuery.getProductNo();
        String productName = productQuery.getProductName();
        String typeName = productQuery.getTypeName();
        String begin = productQuery.getBegin();
        String end = productQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(productNo)) {
            //构建条件
            wrapper.like("product_no", productNo);
        }
        if (!StringUtils.isEmpty(productName)) {
            wrapper.eq("product_name", productName);
        }
        if (!StringUtils.isEmpty(typeName)) {
            wrapper.eq("type_name", typeName);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //调用方法实现条件查询分页
        productService.page(pageProduct, wrapper);

        long total = pageProduct.getTotal();//总记录数
        List<Product> records = pageProduct.getRecords(); //数据list集合
        return R.ok().data("total", total).data("products", records);
    }

    //添加产品信息接口的方法
    @PostMapping("addProduct")
    public R addProduct(@RequestBody Product product) {
        boolean save = productService.save(product);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据产品信息id进行查询
    @GetMapping("getProduct/{id}")
    public R getProduct(@PathVariable String id) {
        Product product = productService.getById(id);
        return R.ok().data("product", product);
    }

    //产品信息修改功能
    @PostMapping("updateProduct")
    public R updateProduct(@RequestBody Product product) {
        boolean flag = productService.updateById(product);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

