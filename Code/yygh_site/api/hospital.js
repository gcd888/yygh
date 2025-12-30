import request from '@/utils/request'

export default {
    getHospitalList(searchObj) {
        return request({
            url: `user/hosp/hospital/list`,
            method: 'get',
            params: searchObj
        })
    },
    findByName(name) {
        return request({
            url: `user/hosp/hospital/${name}`,
            method: 'get'
        })
    },
    getHospitalDetail(hoscode) {
        return request({
            url: `user/hosp/hospital/detail/${hoscode}`,
            method: 'get'
        })
    },
    findDepartment(hoscode) {
        return request({
            url: `user/hosp/department/findAll/${hoscode}`,
            method: 'get'
        })
    },
    getBookingScheduleRule(hoscode,depcode,pageNum,pageSize) {
        return request({
            url: `user/hosp/schedule/getUserSchedulePage/${hoscode}/${depcode}/${pageNum}/${pageSize}`,
            method: 'get'
        })
    },
    findScheduleList(hoscode,depcode,workDate) {
        return request({
            url: `user/hosp/schedule/getUserScheduleDetail/${hoscode}/${depcode}/${workDate}`,
            method: 'get'
        })
    },
    getSchedule(scheduleId) {
        return request({
            url: `user/hosp/schedule/getScheduleById/${scheduleId}`,
            method: 'get'
        })
    }
}
