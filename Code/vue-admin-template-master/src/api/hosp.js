import request from '@/utils/request'

export default {
    getPageList(pageNum,pageSize,searchObj) {
        return request({
          url: `/admin/hospital/${pageNum}/${pageSize}`,
          method: 'get',
          params:searchObj
        })
    },
    getChildList(pid) {
        return request({
          url: `/admin/cmn/childList/${pid}`,
          method: 'get',
        })
    },
    updateStatus(id,status){
      return request({
        url:`/admin/hospital/updateStatus/${id}/${status}`,
        method:'put',
      })
    },
    getHospById(id) {
      return request({
        url: `/admin/hospital/getHospById/${id}`,
        method: 'get',
      })
    },
    getDeptByHoscode(hoscode) {
      return request({
        url: `/admin/hosp/department/getDeptList/${hoscode}`,
        method: 'get',
      })
    },
    getSchedulePage(pageNum,pageSize,hoscode,depcode) {
      return request({
        url: `/admin/hosp/schedule/${hoscode}/${depcode}/${pageNum}/${pageSize}`,
        method: 'get'
      })
  },
  //查询排班详情
  getScheduleDetail(hoscode,depcode,workDate) {
      return request ({
          url: `/admin/hosp/schedule/${hoscode}/${depcode}/${workDate}`,
          method: 'get'
      })
  }
}