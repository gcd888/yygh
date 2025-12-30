import request from '@/utils/request'

export default {
    submitOrder(scheduleId,patientId) {
        return request({
            url: `/user/order/orderInfo/submitOrder/${scheduleId}/${patientId}`,
            method: 'post'
        })
    },
    getPageList(pageNum,pageSize,searchObj) {
        return request({
            url: `/user/order/orderInfo/getOrderInfoPage/${pageNum}/${pageSize}`,
            method: 'get',
            params: searchObj
        })
    },
    getStatusList() {
        return request({
            url: `/user/order/orderInfo/getOrderStatusList`,
            method: 'get'
        })
    },
    getOrders(orderId) {
        return request({
            url: `/user/order/orderInfo/getOrders/${orderId}`,
            method: 'get'
        })  
    }
}
