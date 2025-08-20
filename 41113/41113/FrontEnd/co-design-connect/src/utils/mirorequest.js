import axios from 'axios'
import request from '@/utils/request'

const miroRequest = axios.create({
  baseURL: 'https://api.miro.com/v2',
  timeout: 600000
})

// 从后端获取 token
export const getMiroTokens = async () => {
  try {
    const res = await request.get('/miro/tokens')
    if (res.code === 1) {
      const accessToken = res.data.accessToken
      if (accessToken) {
        localStorage.setItem('miro_access_token', accessToken)
        return true
      }
    }
    return false
  } catch (error) {
    console.error('获取 Miro tokens 失败:', error)
    return false
  }
}

// 清除本地 token
export const clearMiroTokens = () => {
  localStorage.removeItem('miro_access_token')
}


// 刷新 token 的方法
const refreshAccessToken = async () => {
  try {
    const refreshToken = localStorage.getItem('miro_refresh_token')
    if (!refreshToken) {
      throw new Error('No refresh token available')
    }

    const response = await request.get('/miro/tokens')

    if (response.code === 1) {
      const accessToken  = response.data.access_token
      
      // 更新本地 tokens
      localStorage.setItem('miro_access_token', accessToken)

      return accessToken
    } else {
      throw new Error('Token refresh failed')
    }
  } catch (error) {
    console.error('刷新 token 失败:', error)
    clearMiroTokens()
    throw error
  }
}


// 请求拦截器
miroRequest.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem('miro_access_token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  (error) => {
    console.error('Miro request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
miroRequest.interceptors.response.use(
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
        return miroRequest(error.config)
      } catch (refreshError) {
        console.error('Token refresh failed:', refreshError)
        clearMiroTokens()
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  }
)

// 封装常用的 Miro API 方法
export const miroApi = {
  // 获取 token
  getMiroTokens,
  
  // 清除 token
  clearMiroTokens,
  
  // 获取所有 boards
  getBoards: () => miroRequest.get('/boards'),
  
  // 获取特定 board
  getBoard: (boardId) => miroRequest.get(`/boards/${boardId}`),
  
  // 创建新 board
  createBoard: (data) => miroRequest.post('/boards', data),
  
  // 更新 board
  updateBoard: (boardId, data) => miroRequest.patch(`/boards/${boardId}`, data),
  
  // 删除 board
  deleteBoard: (boardId) => miroRequest.delete(`/boards/${boardId}`)
}

export default miroRequest
