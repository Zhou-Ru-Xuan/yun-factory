import request from '@/utils/request'

export default {

    login(data) {
        return request({
            url: '/admin/acl/user/login',
            method: 'post',
            data
        })
    },

    getInfo(token) {
        return request({
            url: '/admin/acl/index/info',
            method: 'get',
            params: { token }
        })
    },

    logout() {
        return request({
            url: '/admin/acl/user/logout',
            method: 'post'
        })
    },

    getUserListPage(current, limit, userQuery) {
        return request({
            url: `/admin/acl/user/pageUserCondition/${current}/${limit}`,
            method: 'post',
            data: userQuery
        })
    },
    //删除用户
    deleteUserById(id) {
        return request({
            url: `/admin/acl/user/${id}`,
            method: 'delete'
        })
    },
    //添加用户
    addUser(User) {
        return request({
            url: `/admin/acl/user/addUser`,
            method: 'post',
            data: User
        })
    },
    //根据id查询用户
    getUserInfo(id) {
        return request({
            url: `/admin/acl/user/getUser/${id}`,
            method: 'get'
        })
    },
    //修改用户
    updateUserInfo(User) {
        return request({
            url: `/admin/acl/user/updateUser`,
            method: 'post',
            data: User
        })
    },
    //查询云工厂
    getFactoryListPage(current, limit, factoryQuery) {
        return request({
            url: `/factory/pageFactoryCondition/${current}/${limit}`,
            method: 'post',
            data: factoryQuery
        })
    },
    //根据id查询云工厂
    getFactoryInfo(id) {
        return request({
            url: `/factory/getFactory/${id}`,
            method: 'get'
        })
    },
    //添加云工厂
    addFactory(Factory) {
        return request({
            url: `/factory/addFactory`,
            method: 'post',
            data: Factory
        })
    }, //删除云工厂
    deleteFactoryByUsername(username) {
        return request({
            url: `/factory/${username}`,
            method: 'delete'
        })
    },
    //修改用户
    updateFactoryInfo(Factory) {
        return request({
            url: `/factory/updateFactory`,
            method: 'post',
            data: Factory
        })
    },
}