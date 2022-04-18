package org.zhouruxuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zhouruxuan.entity.User;

/**

 *  服务类

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
public interface UserService extends IService<User> {

    String login(User user);

    Boolean existUsername(String username);

    // 从数据库中取出用户信息
    User selectByUsername(String username);

//    /**
//     * 根据用户名获取用户登录信息
//     * @param username
//     * @return
//     */
//    Map<String, Object> getUserInfo(String username);
//
//    /**
//     * 根据用户名获取动态菜单
//     * @param username
//     * @return
//     */
//    List<JSONObject> getMenu(String username);
}
