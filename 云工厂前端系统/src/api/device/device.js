import request from '@/utils/request'
export default {
    //添加设备信息
    addDevice(device) {
        return request({
            url: `/device/addDevice`,
            method: 'post',
            data: device
        })
    },
    //删除设备信息
    deleteDeviceById(id) {
        return request({
            url: `/device/${id}`,
            method: 'delete'
        })
    },
    //修改设备信息
    updateDevice(device) {
        return request({
            url: `/device/updateDevice`,
            method: 'post',
            data: device
        })
    },

    //获取分页条件查询结果
    getDeviceListPage(current, limit, deviceQuery) {
        return request({
            url: `/device/pageDeviceCondition/${current}/${limit}`,
            method: 'post',
            data: deviceQuery
        })
    },
    //根据id查询设备信息
    getDevice(id) {
        return request({
            url: `/device/getDevice/${id}`,
            method: 'get'
        })
    },
    //根据用户名查询设备信息
    getDeviceByUsername(username) {
        return request({
            url: `/device/getDevice/${username}`,
            method: 'get'
        })
    },
    //根据用户名和订单id查询设备类型
    getDeviceByBelongAndOrderId(belong, orderId) {
        return request({
            url: `/device/getDeviceByBelongAndOrderId/${belong}/${orderId}`,
            method: 'post',
        })
    },
    // //根据条件查询设备类型
    // getDeviceByCondition(deviceQuery) {
    //     return request({
    //         url: `/device/getDeviceByCondition`,
    //         method: 'post',
    //         data: deviceQuery
    //     })
    // },

    //添加设备类型
    addDeviceType(deviceType) {
        return request({
            url: `/deviceType/addDeviceType`,
            method: 'post',
            data: deviceType
        })
    }, //删除设备类型
    deleteDeviceTypeById(id) {
        return request({
            url: `/deviceType/${id}`,
            method: 'delete'
        })
    },
    //修改设备类型
    updateDeviceType(deviceType) {
        return request({
            url: `/deviceType/updateDeviceType`,
            method: 'post',
            data: deviceType
        })
    },

    //查询所有的设备类型
    getDeviceTypes() {
        return request({
            url: '/deviceType/findAll',
            method: 'get',
        })
    },

    //分页查询设备类型
    getDeviceTypeListPage(current, limit, deviceTypeQuery) {
        return request({
            url: `/deviceType/pageDeviceTypeCondition/${current}/${limit}`,
            method: 'post',
            data: deviceTypeQuery
        })
    },
    //根据id查询设备类型
    getDeviceType(id) {
        return request({
            url: `/deviceType/getDeviceType/${id}`,
            method: 'get'
        })
    },



}