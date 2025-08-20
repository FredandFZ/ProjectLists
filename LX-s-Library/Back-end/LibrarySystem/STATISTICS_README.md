# 图书馆管理系统统计接口使用说明

## 概述

本系统提供了5个统计接口，用于展示图书馆管理系统的各种管理数据。这些接口可以帮助管理员了解系统运行状况、用户活跃度、图书使用情况等关键指标。

## 接口列表

### 1. 仪表板统计数据 (`/api/statistics/dashboard`)
- **用途**: 管理后台首页展示
- **数据**: 用户、图书、借阅记录的基础统计
- **更新频率**: 实时

### 2. 图书统计数据 (`/api/statistics/books`)
- **用途**: 图书管理分析
- **数据**: 分类分布、热门图书、状态分布
- **更新频率**: 实时

### 3. 借阅统计数据 (`/api/statistics/borrows`)
- **用途**: 借阅趋势分析
- **数据**: 每日借阅/归还趋势、逾期统计
- **参数**: `days` (统计天数，默认30天)
- **更新频率**: 实时

### 4. 用户统计数据 (`/api/statistics/users`)
- **用途**: 用户管理分析
- **数据**: 活跃用户、注册趋势、用户分布
- **更新频率**: 实时

### 5. 综合统计数据 (`/api/statistics/comprehensive`)
- **用途**: 综合报告生成
- **数据**: 所有重要指标的汇总
- **更新频率**: 实时

## 权限要求

所有统计接口都需要：
- 用户已登录
- 用户具有管理员权限

## 快速开始

### 1. 获取访问令牌
```bash
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 2. 调用统计接口
```bash
# 获取仪表板数据
curl -X GET http://localhost:8000/api/statistics/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN"

# 获取借阅趋势（最近7天）
curl -X GET "http://localhost:8000/api/statistics/borrows?days=7" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 使用场景

### 管理后台仪表板
```javascript
// 获取仪表板数据
const response = await fetch('/api/statistics/dashboard', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
const data = await response.json();

// 显示关键指标
console.log(`用户总数: ${data.data.users.total}`);
console.log(`图书总数: ${data.data.books.total}`);
console.log(`今日借阅: ${data.data.today.borrows}`);
```

### 借阅趋势图表
```javascript
// 获取借阅趋势数据
const response = await fetch('/api/statistics/borrows?days=30', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
const data = await response.json();

// 绘制图表
const chartData = data.data.daily_borrows.map(item => ({
  date: item.date,
  value: item.count
}));
```

### 用户活跃度分析
```javascript
// 获取用户统计数据
const response = await fetch('/api/statistics/users', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
const data = await response.json();

// 显示活跃用户排行
data.data.active_users.forEach((user, index) => {
  console.log(`${index + 1}. ${user.username}: ${user.borrow_count}次借阅`);
});
```

## 数据说明

### 用户统计
- `total`: 用户总数
- `active`: 活跃用户数
- `staff`: 管理员数量
- `inactive`: 非活跃用户数

### 图书统计
- `total`: 图书总数
- `available`: 可借阅图书数
- `borrowed`: 已借出图书数
- `category_distribution`: 分类分布
- `popular_books`: 热门图书排行

### 借阅统计
- `daily_borrows`: 每日借阅数量
- `daily_returns`: 每日归还数量
- `overdue_stats`: 逾期统计
- `overdue_rate`: 逾期率

### 综合指标
- `book_utilization_rate`: 图书利用率
- `overdue_rate`: 逾期率
- `today`: 今日数据
- `this_month`: 本月数据

## 性能优化

### 1. 缓存策略
```python
from django.core.cache import cache

def get_cached_statistics():
    cache_key = 'dashboard_statistics'
    data = cache.get(cache_key)
    
    if data is None:
        data = calculate_statistics()
        cache.set(cache_key, data, 300)  # 缓存5分钟
    
    return data
```

### 2. 异步计算
```python
from celery import shared_task

@shared_task
def update_statistics_cache():
    """异步更新统计数据缓存"""
    data = calculate_statistics()
    cache.set('dashboard_statistics', data, 300)
```

### 3. 分页处理
```python
# 对于大量数据的统计，可以考虑分页
def get_paginated_statistics(page=1, page_size=100):
    start = (page - 1) * page_size
    end = start + page_size
    return statistics[start:end]
```

## 错误处理

### 常见错误
- `401 Unauthorized`: 用户未登录
- `403 Forbidden`: 用户没有管理员权限
- `500 Internal Server Error`: 服务器内部错误

### 错误处理示例
```javascript
try {
  const response = await fetch('/api/statistics/dashboard', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}`);
  }
  
  const data = await response.json();
  if (!data.success) {
    throw new Error(data.error);
  }
  
  return data.data;
} catch (error) {
  console.error('获取统计数据失败:', error);
  // 显示错误信息给用户
}
```

## 测试

使用提供的测试脚本：
```bash
python test_statistics.py
```

测试脚本会：
1. 自动登录管理员账户
2. 测试所有统计接口
3. 测试带参数的接口
4. 显示测试结果

## 扩展建议

1. **添加更多统计维度**:
   - 按时间段统计
   - 按用户类型统计
   - 按图书分类统计

2. **增加导出功能**:
   - 导出Excel报告
   - 导出PDF报告
   - 定时发送邮件报告

3. **实时数据更新**:
   - 使用WebSocket推送实时数据
   - 添加数据变化通知

4. **自定义统计**:
   - 允许用户自定义统计维度
   - 支持自定义时间范围
   - 支持数据对比功能 