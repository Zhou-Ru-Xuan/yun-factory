package org.zhouruxuan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.JwtUtils;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.User;
import org.zhouruxuan.entity.vo.UserQuery;
import org.zhouruxuan.service.RoleService;
import org.zhouruxuan.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 * 前端控制器

 *
 * @author zhouruxuan
 * @since 2021-07-12
 */
@RestController
@RequestMapping("/admin/acl/user")
@CrossOrigin
public class UserController {
    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;
    @Autowired
    @Qualifier("roleServiceImpl")
    RoleService roleService;

    @PostMapping("login")
    public R login(@RequestBody User user) {
        //登录，返回token值
        String token = userService.login(user);
        return R.ok().data("token", token);
    }

    @PostMapping("logout")
    public R logout() {
        //登录，返回token值
//        String token = userService.login(user);
        return R.ok();
    }

    @GetMapping("existUsername")
    public R existUsername(String username) {
        Boolean flag = userService.existUsername(username);
        return R.ok().data("flag", flag);
    }
//
//    @PostMapping("register")
//    public R register(@RequestBody User user) {
//        //登录，返回token值
//        userService.register(user);
//        return R.ok();
//    }

    @GetMapping("info")
    public R info(HttpServletRequest request) {
        String userId = JwtUtils.getUserIdByJwtToken(request);
        User user = userService.getById(userId);
//        Map<String, Object> userInfo = userService.getUserInfo(user.getUsername());
//        return R.ok().data(userInfo);
        return R.ok().data("user", user);
//                .data("name", "超级管理员").data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    //    @Cacheable(key = "'findAllUsers'", value = "users")
    @GetMapping("findAll")
    public R findAllUsers() {
        return R.ok().data("items", userService.list(null));
    }

    /**
     * 逻辑删除
     *
     * @param id 产能信息id
     * @return 统一结果R
     */
//    @CacheEvict(value = "users", allEntries = true)
    @DeleteMapping("{id}")
    public R removeUser(@PathVariable String id) {
        boolean flag = userService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("pageUser/{current}/{limit}")
    public R pageListUser(@PathVariable long current,
                          @PathVariable long limit) {
        //创建page对象
        Page<User> pageUser = new Page<>(current, limit);

        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageUser对象里面
        userService.page(pageUser, null);

        long total = pageUser.getTotal();//总记录数
        List<User> records = pageUser.getRecords(); //数据list集合

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("users", records);
        return R.ok().data(map);

    }

    //4 条件查询带分页的方法
    @PostMapping("pageUserCondition/{current}/{limit}")
    public R pageUserCondition(@PathVariable long current, @PathVariable long limit,
                               @RequestBody(required = false) UserQuery userQuery) {
        //创建page对象
        Page<User> pageUser = new Page<>(current, limit);

        //构建条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String username = userQuery.getUsername();
        String password = userQuery.getPassword();
        String phone = userQuery.getPhone();
        String realname = userQuery.getRealname();
        String begin = userQuery.getBegin();
        String end = userQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(username)) {
            //构建条件
            wrapper.like("username", username);
        }
        if (!StringUtils.isEmpty(password)) {
            wrapper.eq("password", password);
        }
        if (!StringUtils.isEmpty(phone)) {
            wrapper.eq("phone", phone);
        }
        if (!StringUtils.isEmpty(realname)) {
            wrapper.eq("realname", realname);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        //调用方法实现条件查询分页
        userService.page(pageUser, wrapper);

        long total = pageUser.getTotal();//总记录数
        List<User> records = pageUser.getRecords(); //数据list集合
        return R.ok().data("total", total).data("users", records);
    }

    //添加用户接口的方法
    @PostMapping("addUser")
    public R addUser(@RequestBody User user) {
        boolean save = userService.save(user);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //根据用户id进行查询
    @GetMapping("getUser/{id}")
    public R getUser(@PathVariable String id) {
        User user = userService.getById(id);
        return R.ok().data("user", user);
    }

    //用户修改功能
//    @CacheEvict(value = "users", allEntries = true)
    @PostMapping("updateUser")
    public R updateUser(@RequestBody User user) {
        boolean flag = userService.updateById(user);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    User userQueryVo) {
        Page<User> pageParam = new Page<>(page, limit);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userQueryVo.getUsername())) {
            wrapper.like("username", userQueryVo.getUsername());
        }

        IPage<User> pageModel = userService.page(pageParam, wrapper);
        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "新增管理用户")
    @PostMapping("save")
    public R save(@RequestBody User user) {
        user.setPassword(user.getPassword());
        userService.save(user);
        return R.ok();
    }

    @ApiOperation(value = "修改管理用户")
    @PutMapping("update")
    public R updateById(@RequestBody User user) {
        userService.updateById(user);
        return R.ok();
    }

    @ApiOperation(value = "删除管理用户")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        userService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id列表删除管理用户")
    @DeleteMapping("batchRemove")
    public R batchRemove(@RequestBody List<String> idList) {
        userService.removeByIds(idList);
        return R.ok();
    }

    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public R toAssign(@PathVariable String userId) {
        Map<String, Object> roleMap = roleService.findRoleByUserId(userId);
        return R.ok().data(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public R doAssign(@RequestParam String userId, @RequestParam String[] roleId) {
        roleService.saveUserRoleRelationShip(userId, roleId);
        return R.ok();
    }
}

