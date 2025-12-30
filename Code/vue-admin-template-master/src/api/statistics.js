import request from '@/utils/request'

export default {
    //带查询条件的分页
    getCountMap(searchObj) {
        return request({
          url: `/api/statistics/StatisticsOrder`,
          method: 'get',
          params: searchObj
        })
    },
    
}