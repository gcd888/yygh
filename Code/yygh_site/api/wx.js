import request from '@/utils/request'

export default {
    getWxParam() {
        return request({
            url: `/user/userinfo/wx/param`,
            method: 'get'
        })
    },
    createNative(orderId) {
        return request({
            url: `/user/order/weipay/createNative/${orderId}`,
            method: 'get'
        })
    },
    queryPayStatus(orderId) {
        return request({
            url: `/user/order/weipay/queryPayStatus/${orderId}`,
            method: 'get'
        })
    },
    cancelOrder(orderId) {
        return request({
            url: `/user/order/orderInfo/cancelOrder/${orderId}`,
            method: 'get'
        })
    },
}
