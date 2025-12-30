import request from '@/utils/request'

export default {
    login(userinfo) {
        return request({
            url: `/user/userinfo/login`,
            method: 'post',
            data: userinfo
        })
    },
    getUserInfo() {
        return request({
            url: `/user/userinfo/info`,
            method: 'get'
        })
    },
    saveUserAuah(userAuah) {
        return request({
            url: `/user/userinfo/saveUserAuth`,
            method: 'post',
            data: userAuah
        })
    }
}
