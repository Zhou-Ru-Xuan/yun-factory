package org.zhouruxuan.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.zhouruxuan.entity.User;
import org.zhouruxuan.service.IndexService;
import org.zhouruxuan.service.PermissionService;
import org.zhouruxuan.service.RoleService;
import org.zhouruxuan.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据用户名获取用户登录信息
     *
     * @param username 用户名
     * @return 用户登录信息
     */
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.selectByUsername(username);
        if (null == user) {
            //throw new Exception(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }

        //根据用户id获取角色
        assert user != null;
//        List<Role> roleList = roleService.selectRoleByUserId(user.getId());
        List<String> roleNameList = new ArrayList<>();
        roleNameList.add(user.getRoleName());
//                = roleList.stream().map(Role::getRoleName).collect(Collectors.toList());
//        if(roleNameList.size() == 0) {
//            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
//            roleNameList.add("");
//        }

        //根据用户id获取操作权限值
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
//        redisTemplate.opsForValue().set(username, permissionValueList);
        result.put("user",user);
        result.put("name", user.getUsername());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("roles", roleNameList);
        result.put("permissionValueList", permissionValueList);
        return result;
    }

    /**
     * 根据用户名获取动态菜单
     * @param username
     * @return
     */
    public List<JSONObject> getMenu(String username) {
        User user = userService.selectByUsername(username);

        //根据用户id获取用户菜单权限
        List<JSONObject> permissionList = permissionService.selectPermissionByUserId(user.getId());
        return permissionList;
    }


}
