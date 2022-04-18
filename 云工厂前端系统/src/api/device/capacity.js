import request from '@/utils/request'


export default {
    //添加产能信息
    addCapacity(capacity) {
        return request({
            url: `/capacity/addCapacity`,
            method: 'post',
            data: capacity
        })
    },
    //删除产能信息
    deleteCapacityById(id) {
        return request({
            url: `/capacity/${id}`,
            method: 'delete'
        })
    },
    //修改产能信息
    updateCapacity(capacity) {
        return request({
            url: `/capacity/updateCapacity`,
            method: 'post',
            data: capacity
        })
    },

    //获取分页条件查询结果
    getCapacityListPage(current, limit, capacityQuery) {
        return request({
            url: `/capacity/pageCapacityCondition/${current}/${limit}`,
            method: 'post',
            data: capacityQuery
        })
    },
    //根据id查询产能信息
    getCapacity(id) {
        return request({
            url: `/capacity/getCapacity/${id}`,
            method: 'get'
        })
    },
    //根据id查询产能信息
    getProducts(capacityQuery) {
        return request({
            url: `/capacity/getProducts`,
            method: 'post',
            data: capacityQuery
        })
    },
}