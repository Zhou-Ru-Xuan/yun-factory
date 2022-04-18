import request from '@/utils/request'

export default {
    //添加订单排产信息
    addOrderScheduling(orderScheduling) {
        return request({
            url: `/orderScheduling/addOrderScheduling`,
            method: 'post',
            data: orderScheduling
        })
    },
    //删除订单排产信息
    deleteOrderSchedulingById(id) {
        return request({
            url: `/orderScheduling/${id}`,
            method: 'delete'
        })
    },
    //修改订单排产信息
    updateOrderScheduling(orderScheduling) {
        return request({
            url: `/orderScheduling/updateOrderScheduling`,
            method: 'post',
            data: orderScheduling
        })
    },

    //获取分页条件查询结果
    getOrderSchedulingListPage(current, limit, orderSchedulingQuery) {
        return request({
            url: `/orderScheduling/pageOrderSchedulingCondition/${current}/${limit}`,
            method: 'post',
            data: orderSchedulingQuery
        })
    },
    //根据id查询订单排产信息
    getOrderScheduling(id) {
        return request({
            url: `/orderScheduling/getOrderScheduling/${id}`,
            method: 'get'
        })
    },



}