import request from '@/utils/request'

const API = "/admin/hosp/hospitalSet"


export default {
    //带查询条件的分页
    getHospSetPage(pageNum,pageSize,searchObj) {
        return request({
          url: `${API}/page/${pageNum}/${pageSize}`,
          method: 'post',
          // 如果携带是普通参数，params；如果是携带的json，data
          data:searchObj
        })
    },
    //根据id删除信息
    deleteHospSetById(id) {
      return request({
        url: `${API}/deleteById/${id}`,
        method: 'delete'
      })
    },
    //新增医院设置信息
    saveHospSet(Obj) {
      return request({
        url: `${API}/save`,
        method: 'post',
        data:Obj
      })
    },
    // 根据id查询医院设置信息
    queryHospSetById(id) {
      return request({
        url: `${API}/queryById/${id}`,
        method: 'get'
      })
    },
    //修改医院设置信息
    updateHospSetById(Obj) {
      return request({
        url: `${API}/updateById`,
        method: 'post',
        data:Obj
      })
    },
    //批量删除医院设置信息
    deleteHospSetByIds(ids) {
      return request({
        url: `${API}/deleteByIds`,
        method: 'delete',
        data:ids
      })
    },
    //锁定/解锁医院设置信息
    lockHospSetById(id,status) {
      return request({
        url: `${API}/lockById/${id}/${status}`,
        method: 'put'
      })
    }
}