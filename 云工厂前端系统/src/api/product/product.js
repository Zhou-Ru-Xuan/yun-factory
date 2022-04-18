import request from '@/utils/request'


export default {
    //添加产品信息
    addProduct(product) {
        return request({
            url: `/product/addProduct`,
            method: 'post',
            data: product
        })
    },
    //删除产品信息
    deleteProductById(id) {
        return request({
            url: `/product/${id}`,
            method: 'delete'
        })
    },
    //修改产品信息
    updateProduct(product) {
        return request({
            url: `/product/updateProduct`,
            method: 'post',
            data: product
        })
    },

    //获取分页条件查询结果
    getProductListPage(current, limit, productQuery) {
        return request({
            url: `/product/pageProductCondition/${current}/${limit}`,
            method: 'post',
            data: productQuery
        })
    },
    //根据id查询产品信息
    getProduct(id) {
        return request({
            url: `/product/getProduct/${id}`,
            method: 'get'
        })
    },
    getProducts() {
        return request({
            url: `/product/findAll`,
            method: 'get'
        })
    },

    //添加产品类型
    addProductType(productType) {
        return request({
            url: `/productType/addProductType`,
            method: 'post',
            data: productType
        })
    }, //删除产品类型
    deleteProductTypeById(id) {
        return request({
            url: `/productType/${id}`,
            method: 'delete'
        })
    },
    //修改产品类型
    updateProductType(productType) {
        return request({
            url: `/productType/updateProductType`,
            method: 'post',
            data: productType
        })
    },

    //查询所有的产品类型
    getProductTypes() {
        return request({
            url: '/productType/findAll',
            method: 'get',
        })
    },

    //分页查询产品类型
    getProductTypeListPage(current, limit, productTypeQuery) {
        return request({
            url: `/productType/pageProductTypeCondition/${current}/${limit}`,
            method: 'post',
            data: productTypeQuery
        })
    },
    //根据id查询产品类型
    getProductType(id) {
        return request({
            url: `/productType/getProductType/${id}`,
            method: 'get'
        })
    },
}