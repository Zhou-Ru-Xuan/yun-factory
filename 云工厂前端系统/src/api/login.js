import request from '@/utils/request'


export function login(data) {
    return request({
        url: '/admin/acl/user/login',
        method: 'post',
        data
    })
}

// 获取用户信息
export function getInfo(token) {
    return request({
        url: '/admin/acl/index/info',
        method: 'get',
        params: { token }
    })
}

// 登出
export function logout() {
    //debugger
    return request({
        url: '/admin/acl/index/logout',
        method: 'post'
    })
}

// 获取菜单权限数据
export function getMenu() {
    return request({
        url: '/admin/acl/index/menu',
        method: 'get'
    })
}