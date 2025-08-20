import request from '@/utils/request'

// 获取CSRF token
export function getCSRFToken() {
  return request({
    url: '/auth/csrf-token',
    method: 'get',
    noToken: true
  })
}

// 登录接口
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data,
    noToken: true // 登录接口不需要token
  })
}

// 登出接口
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/auth/user-info',
    method: 'get'
  })
}

// 刷新token
export function refreshToken() {
  return request({
    url: '/auth/refresh',
    method: 'post'
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/auth/change-password',
    method: 'post',
    data
  })
} 