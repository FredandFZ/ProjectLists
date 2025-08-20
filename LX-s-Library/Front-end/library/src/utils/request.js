import axios from 'axios'

const request = axios.create({
    baseURL: '/api',
    timeout: 5000
})

// // 获取CSRF token的函数
// function getCSRFToken() {
//     // 从cookie中获取CSRF token
//     const name = 'csrftoken'
//     let cookieValue = null
//     if (document.cookie && document.cookie !== '') {
//         const cookies = document.cookie.split(';')
//         for (let i = 0; i < cookies.length; i++) {
//             const cookie = cookies[i].trim()
//             if (cookie.substring(0, name.length + 1) === (name + '=')) {
//                 cookieValue = decodeURIComponent(cookie.substring(name.length + 1))
//                 break
//             }
//         }
//     }
//     return cookieValue
// }

request.interceptors.request.use(config => {
    // 添加CSRF token到请求头
    // const csrfToken = getCSRFToken()
    // if (csrfToken) {
    //     config.headers['X-CSRFToken'] = csrfToken
    // }
    
    if (config.noToken) {
        return config
    }
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
}, error => {
    console.log(error)
    return Promise.reject(error)
})

request.interceptors.response.use(
    (response) => {
      const res = response.data
  
      // Update localStorage if response contains new token
      if (response.headers['new-token']) {
        localStorage.setItem('token', response.headers['new-token'])
      }
  
      // Return the response data directly
      return res
  
    },
    (error) => {
      console.error('Response error:', error)
      
      // 处理401未授权错误
      if (error.response && error.response.status === 401) {
        // 检查当前是否在登录页面，如果是则不进行重定向
        const currentPath = window.location.pathname
        if (currentPath !== '/login' && currentPath !== '/') {
          // 清除本地存储的token和登录状态
          localStorage.removeItem('token')
          localStorage.removeItem('isLoggedIn')
          localStorage.removeItem('username')
          sessionStorage.removeItem('isLoggedIn')
          sessionStorage.removeItem('username')
          
          // 跳转到登录页面
          window.location.href = '/login'
        }
      }
      
      return Promise.reject(error)
    }
  )

export default request