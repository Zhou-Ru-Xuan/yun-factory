package org.zhouruxuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zhouruxuan.entity.Role;

import java.util.List;
import java.util.Map;

/**

 *  服务类

 *
 * @author zhouruxuan
 * @since 2021-07-13
 */
public interface RoleService extends IService<Role> {
    //根据用户获取角色数据
    Map<String, Object> findRoleByUserId(String userId);

    //根据用户分配角色
    void saveUserRoleRelationShip(String userId, String[] roleId);

    List<Role> selectRoleByUserId(String id);
}
