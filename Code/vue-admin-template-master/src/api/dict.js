import request from '@/utils/request'

const API = "/admin/cmn"


export default {
    //带查询条件的分页
    getChildListByPid(pid) {
        return request({
          url: `${API}/childList/${pid}`,
          method: 'get'
        })
    },
    
}