import request from '@/utils/request'

export default {
  fetchById(bizId) {
    return request({
      url: '/biz/echo/'+bizId,
      method: 'get'
    })
  },
  listBiz(page,size) {
    return request({
      url: '/biz/list/'+page+'/'+size,
      method: 'get'
    })
  },
  addBiz(biz) {
    return request({
      url: '/biz/add',
      method: 'post',
      params:biz
    })
  },updateBiz(biz) {
    return request({
      url: '/biz/update',
      method: 'post',
      params:biz
    })
  }
}
