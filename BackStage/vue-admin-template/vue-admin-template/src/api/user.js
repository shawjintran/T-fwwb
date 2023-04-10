import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/employ/login',
    method: 'post',
    // params:data
    data:data
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-admin-template/user/info',
    method: 'get',
    params: { token }
  })
  // const data=new Object()
  // return data
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
