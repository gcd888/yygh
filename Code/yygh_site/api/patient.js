import request from '@/utils/request'

export default {
    findList() {
        return request({
            url: `/user/userinfo/patient/findAll`,
            method: 'get'
        })
    },
    savePatient(patient) {
        return request({
            url: `/user/userinfo/patient/savePatient`,
            method: 'post',
            data: patient
        })
    },
    getById(id){
        return request({
            url: `/user/userinfo/patient/detail/${id}`,
            method: 'get'
        })
    },
    updateById(patient) {
        return request({
            url: `/user/userinfo/patient/updatePatient`,
            method: 'put',
            data: patient
        })
    },
    removeById(id) {
        return request({
            url: `/user/userinfo/patient/delPatient/${id}`,
            method: 'delete'
        })
    }
}
