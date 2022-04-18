package org.zhouruxuan.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zhouruxuan.common.JwtUtils;
import org.zhouruxuan.common.R;
import org.zhouruxuan.entity.User;
import org.zhouruxuan.service.IndexService;
import org.zhouruxuan.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/admin/acl/index")
@CrossOrigin
public class IndexController {

    @Autowired
    private IndexService indexService;
    @Autowired
    private UserService userService;

    /**
     * 根据token获取用户信息
     */
    @GetMapping("info")
    public R info(HttpServletRequest request){
        //获取当前登录用户用户名
        String userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        System.out.println(userIdByJwtToken);
        User byId = userService.getById(userIdByJwtToken);
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(byId.getUsername());
        return R.ok().data(userInfo);
    }

//    /**
//     * 获取菜单
//     * @return
//     */
//    @GetMapping("menu")
//    public R getMenu(HttpServletRequest request){
//        //获取当前登录用户用户名
//        String userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
//        User byId = userService.getById(userIdByJwtToken);
////        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        List<JSONObject> permissionList = indexService.getMenu(byId.getUsername());
//        return R.ok().data("permissionList", permissionList);
//    }

    /**
     * 获取菜单
     * @return
     */
    @GetMapping("menu/{username}")
    public R getMenu(@PathVariable String username){
        //获取当前登录用户用户名
//        String userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
//        User byId = userService.getById(userIdByJwtToken);
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> permissionList = indexService.getMenu(username);

        return R.ok().data("permissionList", permissionList);
    }

    @PostMapping("logout")
    public R logout(){
        return R.ok();
    }

}
