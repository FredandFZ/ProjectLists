import request from '@/utils/request'

// 获取图书列表
export function getBooks(params) {
  return request({
    url: '/books',
    method: 'get',
    params
  })
}

// 获取图书详情
export function getBook(id) {
  return request({
    url: `/books/${id}`,
    method: 'get'
  })
}

// 添加图书
export function addBook(data) {
  return request({
    url: '/books',
    method: 'post',
    data
  })
}

// 更新图书
export function updateBook(id, data) {
  return request({
    url: `/books/${id}`,
    method: 'put',
    data
  })
}

// 删除图书
export function deleteBook(id) {
  return request({
    url: `/books/${id}`,
    method: 'delete'
  })
}

// 批量删除图书
export function batchDeleteBooks(ids) {
  return request({
    url: '/books/batch-delete',
    method: 'post',
    data: { ids }
  })
}

// 图书搜索
export function searchBooks(keyword) {
  return request({
    url: '/books/search',
    method: 'get',
    params: { keyword }
  })
} 