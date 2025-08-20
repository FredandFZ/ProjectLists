import request from '@/utils/request'

// 获取借阅记录列表
export function getBorrowRecords(params) {
  return request({
    url: '/borrow-records',
    method: 'get',
    params
  })
}

// 借书
export function borrowBook(data) {
  return request({
    url: '/borrow/borrow',
    method: 'post',
    data
  })
}

// 还书
export function returnBook(data) {
  return request({
    url: '/borrow/return',
    method: 'post',
    data
  })
}

// 续借
export function renewBook(data) {
  return request({
    url: '/borrow/renew',
    method: 'post',
    data
  })
}

// 获取借阅统计
export function getBorrowStats() {
  return request({
    url: '/borrow/stats',
    method: 'get'
  })
}

// 获取逾期记录
export function getOverdueRecords(params) {
  return request({
    url: '/borrow/overdue',
    method: 'get',
    params
  })
}

// 发送催还通知
export function sendReminder(recordId) {
  return request({
    url: `/borrow/reminder/${recordId}`,
    method: 'post'
  })
} 