package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Product;
import org.zhouruxuan.entity.ProductType;
import org.zhouruxuan.entity.vo.ProductTypeQuery;
import org.zhouruxuan.service.ProductService;
import org.zhouruxuan.service.ProductTypeService;

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
@RequestMapping("/productType")
@CrossOrigin
public class ProductTypeController {
    @Autowired
    @Qualifier("productTypeServiceImpl")
    ProductTypeService productTypeService;

    @Autowired
    @Qualifier("productServiceImpl")
    ProductService productService;

    @GetMapping("findAll")
    public R findAllProductTypes() {
        return R.ok().data("productTypes", productTypeService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
    @DeleteMapping("{id}")
    public R removeProductType(@PathVariable String id) {
        ProductType productType = productTypeService.getById(id);
        //1.删除产品类型
        boolean flag = productTypeService.removeById(id);
        //2.删除该类型的所有产品
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("type_name", productType.getTypeName());
        productService.remove(wrapper);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageProductType/{current}/{limit}")
    public R pageListProductType(@PathVariable long current,
                                 @PathVariable long limit) {
        //创建page对象
        Page<ProductType> pageProductType = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageProductType对象里面
        productTypeService.page(pageProductType, null);

        long total = pageProductType.getTotal();//总记录数
        List<ProductType> records = pageProductType.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("productTypes", records);
        return R.ok().data(map);

    }

    // 条件查询带分页的方法
    @PostMapping("pageProductTypeCondition/{current}/{limit}")
    public R pageProductTypeCondition(@PathVariable long current, @PathVariable long limit,
                                      @RequestBody(required = false) ProductTypeQuery productTypeQuery) {
        //创建page对象
        Page<ProductType> pageProductType = new Page<>(current, limit);

        //构建条件
        QueryWrapper<ProductType> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String typeName = productTypeQuery.getTypeName();
        String begin = productTypeQuery.getBegin();
        String end = productTypeQuery.getEnd();
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
        productTypeService.page(pageProductType, wrapper);

        long total = pageProductType.getTotal();//总记录数
        List<ProductType> records = pageProductType.getRecords(); //数据list集合
        return R.ok().data("total", total).data("productTypes", records);
    }

    //添加产品类型接口的方法
    @PostMapping("addProductType")
    public R addProductType(@RequestBody ProductType productType) {
        boolean save = productTypeService.save(productType);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据产品类型id进行查询
    @GetMapping("getProductType/{id}")
    public R getProductType(@PathVariable String id) {
        ProductType productType = productTypeService.getById(id);
        return R.ok().data("productType", productType);
    }

    //产品类型修改功能
    @PostMapping("updateProductType")
    public R updateProductType(@RequestBody ProductType productType) {
        //1.获取原产品类型的信息
        ProductType byId = productTypeService.getById(productType.getId());
        //2.更新产品类型
        boolean flag = productTypeService.updateById(productType);
        //3.判断产品名称是否改变，如果改变，则更新相应产品的信息
        if (!byId.getTypeName().equals(productType.getTypeName())) {
            QueryWrapper<Product> wrapper = new QueryWrapper<>();
            wrapper.eq("type_name", byId.getTypeName());
            List<Product> list = productService.list(wrapper);
            for (Product product : list) {
                product.setTypeName(productType.getTypeName());
            }
            if (list.size() == 1)
                productService.updateById(list.get(0));
            else if (list.size() > 1) {
                productService.saveOrUpdateBatch(list, list.size());
            }
        }
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}

