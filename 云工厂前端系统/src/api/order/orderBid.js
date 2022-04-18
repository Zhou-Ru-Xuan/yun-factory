import request from '@/utils/request'

export default {
    //添加订单信息
    addOrderBid(orderBid) {
        return request({
            url: `/orderBid/addOrderBid`,
            method: 'post',
            data: orderBid
        })
    },
    //删除订单信息
    deleteOrderBidById(id) {
        return request({
            url: `/orderBid/${id}`,
            method: 'delete'
        })
    },
    //修改订单信息
    updateOrderBid(orderBid) {
        return request({
            url: `/orderBid/updateOrderBid`,
            method: 'post',
            data: orderBid
        })
    },

    //获取分页条件查询结果
    getOrderBidListPage(current, limit, orderBidQuery) {
        return request({
            url: `/orderBid/pageOrderBidCondition/${current}/${limit}`,
            method: 'post',
            data: orderBidQuery
        })
    },
    //根据id查询订单信息
    getOrderBid(id) {
        return request({
            url: `/orderBid/getOrderBid/${id}`,
            method: 'get'
        })
    },
    updateOrderBidForComfirm(id, orderId) {
        return request({
            url: `/orderBid/updateOrderBidForComfirm/${id}/${orderId}`,
            method: 'post'
        })

    },
    getOrderBidByUsernameAndOrderId(orderBidQuery)  {
        return request({
            url: `/orderBid/getOrderBidByUsernameAndOrderId`,
            method: 'post',
            data: orderBidQuery
        })
    }


}