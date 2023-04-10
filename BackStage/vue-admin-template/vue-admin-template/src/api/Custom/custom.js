import request from '@/utils/request'

export function addCustom(params) {
  return request({
    url: '/api/employ/custom',
    method: 'post',
    params
  })
}
