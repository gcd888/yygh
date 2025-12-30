import request from '@/utils/request'

export default {
    getChildListByPid(pid) {
        return request({
            url: `/admin/cmn/childList/${pid}`,
            method: 'get'
        })
    }
}
