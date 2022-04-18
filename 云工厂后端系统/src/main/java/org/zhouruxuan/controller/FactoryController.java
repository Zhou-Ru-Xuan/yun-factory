package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.Factory;
import org.zhouruxuan.entity.vo.FactoryQuery;
import org.zhouruxuan.service.FactoryService;

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
@RequestMapping("/factory")
@CrossOrigin
public class FactoryController {
    @Autowired
    @Qualifier("factoryServiceImpl")
    FactoryService factoryService;


    @GetMapping("findAll")
    public R findAllFactorys() {
        return R.ok().data("items", factoryService.list(null));
    }

    @GetMapping("existFactoryName")
    public R existFactoryName(String factoryName){
        Boolean flag = factoryService.existFactoryName(factoryName);
        return R.ok().data("flag",flag);
    }

    /**
     * 逻辑删除
     *
     * @param username
     * @return
     */
    @DeleteMapping("{username}")
    public R removeFactory(@PathVariable String username) {
        boolean flag=factoryService.removeByMap(new HashMap<String,Object>(){{
            put("username",username);
        }});
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageFactory/{current}/{limit}")
    public R pageListFactory(@PathVariable long current,
                          @PathVariable long limit) {
        //创建page对象
        Page<Factory> pageFactory = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageFactory对象里面
        factoryService.page(pageFactory, null);

        long total = pageFactory.getTotal();//总记录数
        List<Factory> records = pageFactory.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("factorys", records);
        return R.ok().data(map);

    }

    //4 条件查询带分页的方法
    @PostMapping("pageFactoryCondition/{current}/{limit}")
    public R pageFactoryCondition(@PathVariable long current, @PathVariable long limit,
                               @RequestBody(required = false) FactoryQuery factoryQuery) {
        //创建page对象
        Page<Factory> pageFactory = new Page<>(current, limit);

        //构建条件
        QueryWrapper<Factory> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String factoryName = factoryQuery.getFactoryName();
        String begin = factoryQuery.getBegin();
        String end = factoryQuery.getEnd();
        String username = factoryQuery.getUsername();
        String factoryState = factoryQuery.getFactoryState();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(factoryName)) {
            //构建条件
            wrapper.like("factory_name", factoryName);
        }
        if (!StringUtils.isEmpty(username)) {
            wrapper.eq("username", username);
        }
        if (!StringUtils.isEmpty(factoryState)) {
            wrapper.eq("factory_state", factoryState);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //调用方法实现条件查询分页
        factoryService.page(pageFactory, wrapper);

        long total = pageFactory.getTotal();//总记录数
        List<Factory> records = pageFactory.getRecords(); //数据list集合
        return R.ok().data("total", total).data("factorys", records);
    }

    //添加用户接口的方法
    @PostMapping("addFactory")
    public R addFactory(@RequestBody Factory factory) {

        boolean save = factoryService.save(factory);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据用户id进行查询
    @GetMapping("getFactory/{id}")
    public R getFactory(@PathVariable String id) {
        Factory factory = factoryService.getById(id);
        return R.ok().data("factory", factory);
    }

    //用户修改功能
    @PostMapping("updateFactory")
    public R updateFactory(@RequestBody Factory factory) {
        boolean flag = factoryService.updateById(factory);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}

