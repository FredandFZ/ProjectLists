import request from '@/utils/request'

// 获取读者列表
export function getReaders(params) {
  return request({
    url: '/readers',
    method: 'get',
    params
  })
}

// 获取读者详情
export function getReader(id) {
  return request({
    url: `/readers/${id}`,
    method: 'get'
  })
}

// 添加读者
export function addReader(data) {
  return request({
    url: '/readers',
    method: 'post',
    data
  })
}

// 更新读者信息
export function updateReader(id, data) {
  return request({
    url: `/readers/${id}`,
    method: 'put',
    data
  })
}

// 删除读者
export function deleteReader(id) {
  return request({
    url: `/readers/${id}`,
    method: 'delete'
  })
}

// 批量删除读者
export function batchDeleteReaders(ids) {
  return request({
    url: '/readers/batch-delete',
    method: 'post',
    data: { ids }
  })
}

// 读者搜索
export function searchReaders(keyword) {
  return request({
    url: '/readers/search',
    method: 'get',
    params: { keyword }
  })
}

// 获取读者借阅历史
export function getReaderBorrowHistory(readerId, params) {
  return request({
    url: `/readers/${readerId}/borrow-history`,
    method: 'get',
    params
  })
}

// 更新读者状态（启用/禁用）
export function updateReaderStatus(id, status) {
  return request({
    url: `/readers/${id}/status`,
    method: 'put',
    data: { status }
  })
} 