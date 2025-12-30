import request from '@/utils/request'
const API = "/administrator/userinfo"

export default {
    getPageList(pageNum,pageSize,searchObj) {
        return request({
          url: `${API}/${pageNum}/${pageSize}`,
          method: 'get',
          params:searchObj
        })
    },
    lock(id,status) {
        return request({
          url: `${API}/lock/${id}/${status}`,
          method: 'put'
        })
    },
    detail(id) {
        return request({
            url: `${API}/detail/${id}`,
            method: 'get'
        }) 
    },
    approval(id,authStatus) {
        return request({
          url: `${API}/approval/${id}/${authStatus}`,
          method: 'put'
        })
    },
}