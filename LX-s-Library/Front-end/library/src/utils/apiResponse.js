// API响应状态码
export const API_STATUS = {
  SUCCESS: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_ERROR: 500
}

// 处理API响应
export function handleApiResponse(response) {
  if (response.code === API_STATUS.SUCCESS || response.code === API_STATUS.CREATED) {
    return {
      success: true,
      data: response.data,
      message: response.message
    }
  } else {
    return {
      success: false,
      message: response.message || '请求失败',
      code: response.code
    }
  }
}

// 处理API错误
export function handleApiError(error) {
  console.error('API Error:', error)
  
  if (error.response) {
    // 服务器返回错误状态码
    const { status, data } = error.response
    return {
      success: false,
      message: data?.message || `请求失败 (${status})`,
      code: status
    }
  } else if (error.request) {
    // 网络错误
    return {
      success: false,
      message: '网络连接失败，请检查网络设置',
      code: 0
    }
  } else {
    // 其他错误
    return {
      success: false,
      message: error.message || '未知错误',
      code: -1
    }
  }
}

// 创建成功响应
export function createSuccessResponse(data, message = '操作成功') {
  return {
    success: true,
    data,
    message
  }
}

// 创建失败响应
export function createErrorResponse(message = '操作失败', code = -1) {
  return {
    success: false,
    message,
    code
  }
} 