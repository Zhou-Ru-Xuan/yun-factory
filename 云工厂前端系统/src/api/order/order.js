import request from '@/utils/request'
export default {
    //添加订单信息
    addOrderItem(orderItem) {
        return request({
            url: `/orderItem/addOrderItem`,
            method: 'post',
            data: orderItem
        })
    },
    //删除订单信息
    deleteOrderItemById(id) {
        return request({
            url: `/orderItem/${id}`,
            method: 'delete'
        })
    },
    //修改订单信息
    updateOrderItem(orderItem) {
        return request({
            url: `/orderItem/updateOrderItem`,
            method: 'post',
            data: orderItem
        })
    },

    //获取分页条件查询结果
    getOrderItemListPage(current, limit, orderItemQuery) {
        return request({
            url: `/orderItem/pageOrderItemCondition/${current}/${limit}`,
            method: 'post',
            data: orderItemQuery
        })
    },
    //根据id查询订单信息
    getOrderItem(id) {
        return request({
            url: `/orderItem/getOrderItem/${id}`,
            method: 'get'
        })
    },



}