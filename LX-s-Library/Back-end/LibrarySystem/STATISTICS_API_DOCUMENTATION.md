# 图书馆管理系统统计接口文档

## 概述

本文档描述了图书馆管理系统的数据统计接口，这些接口用于展示管理数据和系统运行状况。所有统计接口都需要管理员权限才能访问。

## 认证要求

所有统计接口都需要：
- 用户已登录（`IsAuthenticated`）
- 用户具有管理员权限（`IsAdminUser`）

## 接口列表

### 1. 仪表板统计数据

**接口地址：** `GET /api/statistics/dashboard`

**功能描述：** 获取仪表板基础统计数据，包含用户、图书、借阅等核心指标。

**响应示例：**
```json
{
  "success": true,
  "data": {
    "users": {
      "total": 150,
      "active": 145,
      "staff": 5
    },
    "books": {
      "total": 1000,
      "available": 850,
      "borrowed": 150
    },
    "categories": {
      "total": 20
    },
    "borrow_records": {
      "total": 500,
      "active": 150,
      "returned": 320,
      "overdue": 30
    },
    "today": {
      "borrows": 15,
      "returns": 8
    }
  }
}
```

### 2. 图书统计数据

**接口地址：** `GET /api/statistics/books`

**功能描述：** 获取图书相关统计数据，包含分类分布、热门图书、状态分布等。

**响应示例：**
```json
{
  "success": true,
  "data": {
    "category_distribution": [
      {
        "name": "计算机科学",
        "book_count": 200
      },
      {
        "name": "文学",
        "book_count": 150
      }
    ],
    "popular_books": [
      {
        "name": "Python编程",
        "author": "张三",
        "borrow_count": 25
      }
    ],
    "status_distribution": [
      {
        "status": "available",
        "count": 850
      },
      {
        "status": "borrowed",
        "count": 150
      }
    ],
    "recent_books": [
      {
        "name": "新书1",
        "author": "作者1",
        "created_at": "2024-01-15T10:30:00Z"
      }
    ]
  }
}
```

### 3. 借阅统计数据

**接口地址：** `GET /api/statistics/borrows`

**查询参数：**
- `days` (可选): 统计天数，默认为30天

**功能描述：** 获取借阅相关统计数据，包含借阅趋势、逾期统计等。

**响应示例：**
```json
{
  "success": true,
  "data": {
    "daily_borrows": [
      {
        "date": "2024-01-01",
        "count": 10
      },
      {
        "date": "2024-01-02",
        "count": 15
      }
    ],
    "daily_returns": [
      {
        "date": "2024-01-01",
        "count": 8
      },
      {
        "date": "2024-01-02",
        "count": 12
      }
    ],
    "overdue_stats": {
      "total_overdue": 30,
      "overdue_rate": 20.0
    },
    "time_range": {
      "start_date": "2024-01-01",
      "end_date": "2024-01-30",
      "days": 30
    }
  }
}
```

### 4. 用户统计数据

**接口地址：** `GET /api/statistics/users`

**功能描述：** 获取用户相关统计数据，包含活跃用户、注册趋势等。

**响应示例：**
```json
{
  "success": true,
  "data": {
    "active_users": [
      {
        "username": "user1",
        "borrow_count": 50
      },
      {
        "username": "user2",
        "borrow_count": 45
      }
    ],
    "daily_registrations": [
      {
        "date": "2024-01-01",
        "count": 5
      },
      {
        "date": "2024-01-02",
        "count": 3
      }
    ],
    "user_type_distribution": {
      "total": 150,
      "active": 145,
      "staff": 5,
      "inactive": 5
    },
    "recent_users": [
      {
        "username": "newuser1",
        "email": "newuser1@example.com",
        "date_joined": "2024-01-15T10:30:00Z",
        "is_active": true
      }
    ]
  }
}
```

### 5. 综合统计数据

**接口地址：** `GET /api/statistics/comprehensive`

**功能描述：** 获取综合统计数据，包含所有重要指标的汇总。

**响应示例：**
```json
{
  "success": true,
  "data": {
    "overview": {
      "total_users": 150,
      "total_books": 1000,
      "total_borrows": 500
    },
    "today": {
      "borrows": 15,
      "returns": 8,
      "new_users": 2
    },
    "this_month": {
      "borrows": 300,
      "returns": 280,
      "new_users": 25
    },
    "rates": {
      "overdue_rate": 20.0,
      "book_utilization_rate": 15.0
    },
    "popular_categories": [
      {
        "name": "计算机科学",
        "book_count": 200,
        "borrow_count": 150
      }
    ]
  }
}
```

## 错误响应

当接口发生错误时，会返回以下格式的响应：

```json
{
  "success": false,
  "error": "错误描述信息"
}
```

常见错误：
- `401 Unauthorized`: 用户未登录
- `403 Forbidden`: 用户没有管理员权限
- `500 Internal Server Error`: 服务器内部错误

## 使用建议

1. **仪表板数据**: 建议在管理后台首页使用 `dashboard_statistics` 接口
2. **趋势分析**: 使用 `borrow_statistics` 接口分析借阅趋势
3. **用户管理**: 使用 `user_statistics` 接口了解用户活跃度
4. **图书管理**: 使用 `book_statistics` 接口分析图书分布和热门程度
5. **综合报告**: 使用 `comprehensive_statistics` 接口生成综合报告

## 性能优化建议

1. 对于频繁访问的统计数据，建议使用缓存
2. 大数据量时可以考虑分页或限制返回数量
3. 时间范围查询时建议设置合理的默认值
4. 可以考虑使用异步任务预计算统计数据 