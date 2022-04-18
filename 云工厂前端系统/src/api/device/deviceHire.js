import request from '@/utils/request'
export default {
    //添加设备租用信息
    addDeviceHire(deviceHire) {
        return request({
            url: `/deviceHire/addDeviceHire`,
            method: 'post',
            data: deviceHire
        })
    },
    //删除设备租用信息
    deleteDeviceHireById(id) {
        return request({
            url: `/deviceHire/${id}`,
            method: 'delete'
        })
    },
    //修改设备租用信息
    updateDeviceHire(deviceHire) {
        return request({
            url: `/deviceHire/updateDeviceHire`,
            method: 'post',
            data: deviceHire
        })
    },

    //获取分页条件查询结果
    getDeviceHireListPage(current, limit, deviceHireQuery) {
        return request({
            url: `/deviceHire/pageDeviceHireCondition/${current}/${limit}`,
            method: 'post',
            data: deviceHireQuery
        })
    },
    //根据id查询设备租用信息
    getDeviceHire(id) {
        return request({
            url: `/deviceHire/getDeviceHire/${id}`,
            method: 'get'
        })
    },
    //根据用户名查询设备租用信息
    getDeviceHireByUsername(username) {
        return request({
            url: `/deviceHire/getDeviceHire/${username}`,
            method: 'get'
        })
    },



}