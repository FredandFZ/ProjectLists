import axios from 'axios'
import request from '@/utils/request'

const docRequest = axios.create({
  baseURL: 'https://docs.googleapis.com/v1',
  timeout: 600000
})

// 从后端获取 token
export const getDocTokens = async () => {
  try {
    const res = await request.get('/google/tokens')
    if (res.code === 1) {
      const accessToken = res.data.access_token
      localStorage.setItem('google_access_token', accessToken)

      return true
    }
    return false
  } catch (error) {
    console.error('获取 Google tokens 失败:', error)
    return false
  }
}

// 清除本地 token
export const clearDocTokens = () => {
  localStorage.removeItem('google_access_token')
  localStorage.removeItem('google_refresh_token')
}

// 刷新 token 的方法
const refreshAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('google_refresh_token')
    if (!refreshToken) {
      throw new Error('No refresh token available')
    }

    const response = await request.get('/google/tokens')

    if (response.code === 1) {
      const accessToken = response.data.accessToken
      // 更新本地 tokens
      localStorage.setItem('google_access_token', accessToken)
      return accessToken
    } else {
      throw new Error('Token refresh failed')
    }
  } catch (error) {
    console.error('刷新 token 失败:', error)
    clearDocTokens()
    throw error
  }
}

// 请求拦截器
docRequest.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem('google_access_token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  (error) => {
    console.error('Google Docs request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
docRequest.interceptors.response.use(
  (response) => {
    const res = response.data
    return res
  },
  async (error) => {
    // 如果是 401 错误，尝试刷新 token
    if (error.response && error.response.status === 401) {
      try {
        const newToken = await refreshAccessToken()
        // 重试原始请求
        error.config.headers['Authorization'] = `Bearer ${newToken}`
        return docRequest(error.config)
      } catch (refreshError) {
        console.error('Token refresh failed:', refreshError)
        clearDocTokens()
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  }
)

// 封装常用的 Google Docs API 方法
export const docApi = {
  // 获取 token
  getDocTokens,
  
  // 清除 token
  clearDocTokens,
  
  // 创建新文档
  createDoc: (data) => docRequest.post('/documents', data),
  
  // 获取文档
  getDoc: (docId) => docRequest.get(`/documents/${docId}`),
  
  // 更新文档
  updateDoc: (docId, data) => docRequest.patch(`/documents/${docId}`, data),
  
  // 删除文档
  deleteDoc: (docId) => docRequest.delete(`/documents/${docId}`),
  
  // 获取文档列表
  getDocs: () => docRequest.get('/documents')
}

export default docRequest 