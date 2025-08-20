# 图书馆管理系统 API 文档

## 基础信息
- 基础URL: `http://localhost:8000/api/`
- 认证方式: JWT Token (Bearer Token)
- 数据格式: JSON

## 认证相关API

### 1. 用户注册
- **URL**: `POST /api/auth/register/`
- **描述**: 注册新用户
- **请求体**:
```json
{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "phone": "13800138000"
}
```
- **响应**: 返回注册成功信息和用户信息（不含密码）以及认证token

### 2. 用户登录
- **URL**: `POST /api/auth/login/`
- **描述**: 用户登录
- **请求体**:
```json
{
    "username": "testuser",
    "password": "password123"
}
```
- **响应**: 返回登录成功信息、用户信息和认证token

### 3. 用户登出
- **URL**: `POST /api/auth/logout/`
- **描述**: 用户登出
- **权限**: 需要登录

### 4. 获取用户信息
- **URL**: `GET /api/auth/user/`
- **描述**: 获取用户信息
- **权限**: 需要登录

### 5. 刷新令牌
- **URL**: `POST /api/auth/refresh/`
- **描述**: 刷新认证token
- **权限**: 需要登录

## 用户管理API

### 6. 获取用户列表
- **URL**: `GET /api/users/`
- **描述**: 获取所有用户列表
- **权限**: 需要管理员权限
- **查询参数**: 
  - `page`: 页码
  - `page_size`: 每页数量

### 7. 获取用户详情
- **URL**: `GET /api/users/{id}/`
- **描述**: 获取指定用户详情
- **权限**: 需要登录

### 8. 更新用户信息
- **URL**: `PUT /api/users/{id}/`
- **描述**: 更新用户信息
- **权限**: 需要登录（只能更新自己的信息）

## 图书分类API

### 9. 获取分类列表
- **URL**: `GET /api/categories/`
- **描述**: 获取所有图书分类
- **权限**: 公开访问

### 10. 创建分类
- **URL**: `POST /api/categories/`
- **描述**: 创建新的图书分类
- **权限**: 需要管理员权限
- **请求体**:
```json
{
    "name": "历史"
}
```

### 11. 更新分类
- **URL**: `PUT /api/categories/{id}/`
- **描述**: 更新图书分类
- **权限**: 需要管理员权限

### 12. 删除分类
- **URL**: `DELETE /api/categories/{id}/`
- **描述**: 删除图书分类
- **权限**: 需要管理员权限

## 图书管理API

### 13. 获取图书列表
- **URL**: `GET /api/books/`
- **描述**: 获取图书列表
- **权限**: 公开访问
- **查询参数**:
  - `category`: 按分类筛选
  - `status`: 按状态筛选（available/borrowed）
  - `search`: 搜索关键词

### 14. 获取图书详情
- **URL**: `GET /api/books/{id}/`
- **描述**: 获取指定图书详情
- **权限**: 公开访问

### 15. 创建图书
- **URL**: `POST /api/books/`
- **描述**: 添加新图书
- **权限**: 需要管理员权限
- **请求体**:
```json
{
    "name": "书名",
    "author": "作者",
    "isbn": "ISBN号",
    "category": 分类ID,
    "publisher": "出版社",
    "publish_date": "出版日期",
    "description": "图书描述"
}
```

### 16. 更新图书
- **URL**: `PUT /api/books/{id}/`
- **描述**: 更新图书信息
- **权限**: 需要管理员权限

### 17. 删除图书
- **URL**: `DELETE /api/books/{id}/`
- **描述**: 删除图书
- **权限**: 需要管理员权限

### 18. 搜索图书
- **URL**: `GET /api/books/search/`
- **描述**: 搜索图书
- **权限**: 公开访问
- **查询参数**:
  - `q`: 搜索关键词（书名、作者、ISBN）
  - `category`: 分类ID
  - `status`: 图书状态（available/borrowed）

## 借阅管理API

### 19. 借阅图书
- **URL**: `POST /api/books/{book_id}/borrow/`
- **描述**: 借阅指定图书
- **权限**: 需要登录
- **请求体**:
```json
{
    "due_date": "应还日期"
}
```

### 20. 归还图书
- **URL**: `POST /api/borrow-records/{record_id}/return/`
- **描述**: 归还借阅的图书
- **权限**: 需要登录

### 21. 获取借阅记录列表
- **URL**: `GET /api/borrow-records/`
- **描述**: 获取所有借阅记录
- **权限**: 需要管理员权限

### 22. 获取用户借阅记录
- **URL**: `GET /api/user/borrow-records/`
- **描述**: 获取当前用户的借阅记录
- **权限**: 需要登录

### 23. 获取逾期记录
- **URL**: `GET /api/overdue-records/`
- **描述**: 获取所有逾期记录
- **权限**: 需要管理员权限

## 响应格式

### 成功响应
```json
{
    "id": 1,
    "name": "示例数据",
    "created_at": "2024-01-01T00:00:00Z"
}
```

### 错误响应
```json
{
    "error": "错误信息",
    "detail": "详细错误描述"
}
```

### 分页响应
```json
{
    "count": 总数量,
    "next": "下一页URL",
    "previous": "上一页URL",
    "results": [
        // 数据列表
    ]
}
```

## 状态码说明
- `200`: 请求成功
- `201`: 创建成功
- `400`: 请求参数错误
- `401`: 未认证
- `403`: 权限不足
- `404`: 资源不存在
- `500`: 服务器内部错误

## 注意事项
1. 所有需要认证的API都需要在请求头中包含认证信息
2. 日期格式使用ISO 8601标准：`YYYY-MM-DDTHH:MM:SSZ`
3. 分页默认每页20条记录
4. 搜索功能支持模糊匹配
5. 借阅图书时会自动检查图书状态和用户借阅历史

## 使用示例

### 前端JavaScript示例

```javascript
// 用户登录
async function login(username, password) {
    const response = await fetch('/api/auth/login/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    });
    
    const data = await response.json();
    
    if (response.ok) {
        // 保存令牌
        localStorage.setItem('access_token', data.tokens.access);
        localStorage.setItem('refresh_token', data.tokens.refresh);
        return data;
    } else {
        throw new Error(data.message);
    }
}

// 获取用户信息
async function getUserInfo() {
    const token = localStorage.getItem('access_token');
    
    const response = await fetch('/api/auth/user/', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    
    return await response.json();
}

// 借阅图书
async function borrowBook(bookId, dueDate) {
    const token = localStorage.getItem('access_token');
    
    const response = await fetch(`/api/books/${bookId}/borrow/`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            due_date: dueDate
        })
    });
    
    return await response.json();
}
```

### Python示例

```python
import requests

# 用户登录
def login(username, password):
    response = requests.post('http://localhost:8000/api/auth/login/', json={
        'username': username,
        'password': password
    })
    
    if response.status_code == 200:
        data = response.json()
        return data['tokens']['access']
    else:
        raise Exception(response.json()['message'])

# 获取图书列表
def get_books(token):
    headers = {'Authorization': f'Bearer {token}'}
    response = requests.get('http://localhost:8000/api/books/', headers=headers)
    return response.json()

# 使用示例
token = login('testuser', 'password123')
books = get_books(token)
print(books) 