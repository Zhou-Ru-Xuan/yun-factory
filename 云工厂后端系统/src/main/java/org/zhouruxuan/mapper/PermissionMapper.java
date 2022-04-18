package org.zhouruxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.zhouruxuan.entity.Permission;

import java.util.List;

/**

 * 权限 Mapper 接口

 *
 * @author testjava
 * @since 2020-01-12
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    @SelectProvider(type=PermissionDynProvider.class,method = "selectPermissionValueByUserId")

    List<String> selectPermissionValueByUserId(String id);

    List<String> selectAllPermissionValue();

@SelectProvider(type=PermissionDynProvider.class,method = "selectPermissionByUserId")
    List<Permission> selectPermissionByUserId(String userId);

    class PermissionDynProvider{
        public String selectPermissionByUserId(String userId){
            return new SQL(){
                {
                    SELECT("cf_permission.id","cf_permission.pid","cf_permission.name","cf_permission.type","cf_permission.permission_value");
                    SELECT("cf_permission.path","cf_permission.component","cf_permission.icon","cf_permission.status","cf_permission.is_deleted");
                    SELECT("cf_permission.gmt_create","cf_permission.gmt_modified");
                    FROM("cf_permission,role_permission,user_role");
                    if(userId!=null)
                        WHERE("user_role.user_id = #{userId}");
                    WHERE("cf_permission.id = role_permission.permission_id");
                    WHERE("role_permission.role_id = user_role.role_id");
                    WHERE("cf_permission.is_deleted=0");
                    WHERE("role_permission.is_deleted=0");
                    WHERE("user_role.is_deleted=0");
                }
            }.toString();
        }
        public String selectPermissionValueByUserId(String userId){
            return new SQL(){
                {
                    SELECT("cf_permission.permission_value");

                    FROM("cf_permission,role_permission,user_role");
                    if(userId!=null)
                        WHERE("user_role.user_id = #{userId}");
                    WHERE("cf_permission.id = role_permission.permission_id");
                    WHERE("role_permission.role_id = user_role.role_id");
                    WHERE("cf_permission.is_deleted=0");
                    WHERE("role_permission.is_deleted=0");
                    WHERE("user_role.is_deleted=0");
                }
            }.toString();
        }
    }
}
