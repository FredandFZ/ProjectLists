import request from '@/utils/request'
import { 
  mockDashboardData, 
  mockBookStats, 
  mockBorrowStats, 
  mockUserStats, 
  mockComprehensiveData,
  createMockResponse,
  mockApiDelay
} from '@/utils/mockData'

// 是否使用模拟数据
const USE_MOCK_DATA = true

// 获取仪表板基础统计数据
export function getDashboardStats() {
  if (USE_MOCK_DATA) {
    return mockApiDelay().then(() => createMockResponse(mockDashboardData))
  }
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}

// 获取图书统计数据
export function getBookStats(params) {
  if (USE_MOCK_DATA) {
    return mockApiDelay().then(() => createMockResponse(mockBookStats))
  }
  return request({
    url: '/statistics/books',
    method: 'get',
    params
  })
}

// 获取借阅统计数据
export function getBorrowStats(params) {
  if (USE_MOCK_DATA) {
    return mockApiDelay().then(() => createMockResponse(mockBorrowStats))
  }
  return request({
    url: '/statistics/borrows',
    method: 'get',
    params
  })
}

// 获取用户统计数据
export function getUserStats(params) {
  if (USE_MOCK_DATA) {
    return mockApiDelay().then(() => createMockResponse(mockUserStats))
  }
  return request({
    url: '/statistics/users',
    method: 'get',
    params
  })
}

// 获取综合统计数据
export function getComprehensiveStats() {
  if (USE_MOCK_DATA) {
    return mockApiDelay().then(() => createMockResponse(mockComprehensiveData))
  }
  return request({
    url: '/statistics/comprehensive',
    method: 'get'
  })
}

// 获取分类统计
export function getCategoryStats() {
  if (USE_MOCK_DATA) {
    return mockApiDelay().then(() => createMockResponse(mockBookStats.categories))
  }
  return request({
    url: '/statistics/categories',
    method: 'get'
  })
}

// 导出统计数据
export function exportStats(params) {
  if (USE_MOCK_DATA) {
    return mockApiDelay().then(() => {
      // 模拟导出文件
      const blob = new Blob(['模拟统计数据'], { type: 'text/csv' })
      return { data: blob }
    })
  }
  return request({
    url: '/statistics/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
} 