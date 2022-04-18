package org.zhouruxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zhouruxuan.common.JwtUtils;
import org.zhouruxuan.common.MyExcption;
import org.zhouruxuan.entity.User;
import org.zhouruxuan.mapper.UserMapper;
import org.zhouruxuan.service.PermissionService;
import org.zhouruxuan.service.RoleService;
import org.zhouruxuan.service.UserService;

/**

 * 服务实现类

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("permissionServiceImpl")
    private PermissionService permissionService;

//    @Autowired
//    private RedisTemplate redisTemplate;

    @Override
    public String login(User user) {
        System.out.println(user);
        String username = user.getUsername();
        String password = user.getPassword();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

//        queryWrapper.allEq(new HashMap<String,Object>() {{
//            put("username",username);
//            put("password", password);
//        }
//        });
        queryWrapper.eq("username",username).eq("password",password);
        User userInDb = baseMapper.selectOne(queryWrapper);

        if (userInDb == null)
            throw new MyExcption(20001, "账号或密码错误");
        return JwtUtils.getJwtToken(userInDb);
    }



    @Override
    public Boolean existUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Integer cnt = baseMapper.selectCount(queryWrapper);
        return cnt>0;
    }

    @Override
    public User selectByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }
//
//    /**
//     * 根据用户名获取用户登录信息
//     *
//     * @param username
//     * @return
//     */
//    public Map<String, Object> getUserInfo(String username) {
//        Map<String, Object> result = new HashMap<>();
//        User user = this.selectByUsername(username);
//        if (null == user) {
//            //throw new Exception(ResultCodeEnum.FETCH_USERINFO_ERROR);
//        }
//
//        //根据用户id获取角色
//        assert user != null;
//        List<Role> roleList = roleService.selectRoleByUserId(user.getId());
//        List<String> roleNameList = roleList.stream().map(Role::getRoleName).collect(Collectors.toList());
//        if(roleNameList.size() == 0) {
//            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
//            roleNameList.add("");
//        }
//
//        //根据用户id获取操作权限值
//        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
////        redisTemplate.opsForValue().set(username, permissionValueList);
//
//        result.put("name", user.getUsername());
//        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//        result.put("roles", roleNameList);
//        result.put("permissionValueList", permissionValueList);
//        return result;
//    }
//
//    /**
//     * 根据用户名获取动态菜单
//     * @param username
//     * @return
//     */
//    public List<JSONObject> getMenu(String username) {
//        User user = this.selectByUsername(username);
//
//        //根据用户id获取用户菜单权限
//        List<JSONObject> permissionList = permissionService.selectPermissionByUserId(user.getId());
//        return permissionList;
//    }

}
