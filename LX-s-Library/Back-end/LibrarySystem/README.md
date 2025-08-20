# 图书馆管理系统 - 后端

这是一个基于Django REST Framework的图书馆管理系统后端API，提供用户认证、图书管理、借阅管理等功能。

## 功能特性

- 🔐 JWT认证系统（用户注册、登录、登出）
- 👥 用户管理（普通用户和管理员）
- 📚 图书管理（增删改查、分类管理）
- 📖 借阅管理（借书、还书、借阅记录）
- 🔍 图书搜索功能
- 📊 逾期记录管理
- 🌐 跨域支持

## 技术栈

- **Django 3.2.25** - Web框架
- **Django REST Framework 3.14.0** - API框架
- **Django REST Framework Simple JWT 5.3.0** - JWT认证
- **MySQL** - 数据库
- **Django CORS Headers** - 跨域支持

## 安装和配置

### 1. 克隆项目

```bash
git clone <repository-url>
cd LibrarySystem
```

### 2. 创建虚拟环境

```bash
# Windows
python -m venv venv
venv\Scripts\activate

# Linux/Mac
python3 -m venv venv
source venv/bin/activate
```

### 3. 安装依赖

```bash
pip install -r requirements.txt
```

### 4. 配置数据库

确保MySQL服务已启动，并创建数据库：

```sql
CREATE DATABASE library CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 5. 配置环境变量

编辑 `LibrarySystem/settings.py` 文件，修改数据库配置：

```python
DATABASES = {
    "default": {
        "ENGINE": "django.db.backends.mysql",
        "NAME": "library",
        "USER": "your_username",
        "PASSWORD": "your_password",
        "HOST": "localhost",
        "PORT": "3306",
    }
}
```

### 6. 运行数据库迁移

```bash
python manage.py makemigrations
python manage.py migrate
```

### 7. 创建超级用户（可选）

```bash
python manage.py createsuperuser
```

### 8. 启动开发服务器

```bash
python manage.py runserver
```

服务器将在 `http://localhost:8000` 启动。

## API使用

### 认证流程

1. **用户注册**
   ```bash
   POST /api/auth/register/
   {
       "username": "testuser",
       "email": "test@example.com",
       "password": "password123",
       "phone": "13800138000"
   }
   ```

2. **用户登录**
   ```bash
   POST /api/auth/login/
   {
       "username": "testuser",
       "password": "password123"
   }
   ```

3. **使用令牌访问API**
   ```bash
   GET /api/books/
   Authorization: Bearer <access_token>
   ```

4. **刷新令牌**
   ```bash
   POST /api/auth/refresh/
   {
       "refresh": "<refresh_token>"
   }
   ```

### 主要API端点

- **认证相关**
  - `POST /api/auth/register/` - 用户注册
  - `POST /api/auth/login/` - 用户登录
  - `POST /api/auth/logout/` - 用户登出
  - `GET /api/auth/user/` - 获取用户信息
  - `POST /api/auth/refresh/` - 刷新令牌

- **用户管理**
  - `GET /api/users/` - 获取用户列表
  - `GET /api/users/{id}/` - 获取用户详情
  - `PUT /api/users/{id}/` - 更新用户信息

- **图书分类**
  - `GET /api/categories/` - 获取分类列表
  - `POST /api/categories/` - 创建分类

- **图书管理**
  - `GET /api/books/` - 获取图书列表
  - `GET /api/books/{id}/` - 获取图书详情
  - `POST /api/books/` - 创建图书
  - `PUT /api/books/{id}/` - 更新图书
  - `DELETE /api/books/{id}/` - 删除图书
  - `GET /api/books/search/` - 搜索图书

- **借阅管理**
  - `POST /api/books/{book_id}/borrow/` - 借阅图书
  - `POST /api/borrow-records/{record_id}/return/` - 归还图书
  - `GET /api/user/borrow-records/` - 获取用户借阅记录
  - `GET /api/overdue-records/` - 获取逾期记录

## 测试

运行测试脚本验证认证功能：

```bash
python test_auth.py
```

## 项目结构

```
LibrarySystem/
├── LibrarySystem/          # Django项目配置
│   ├── settings.py        # 项目设置
│   ├── urls.py           # 主URL配置
│   └── wsgi.py           # WSGI配置
├── myapp1/               # 主应用
│   ├── models.py         # 数据模型
│   ├── serializers.py    # 序列化器
│   ├── views/            # 视图
│   │   └── auth.py       # 认证视图
│   ├── urls.py           # 应用URL配置
│   └── admin.py          # 管理后台配置
├── manage.py             # Django管理脚本
├── requirements.txt      # 依赖包列表
├── test_auth.py         # 认证功能测试脚本
├── API_DOCUMENTATION.md # API文档
└── README.md            # 项目说明
```

## 开发说明

### 添加新的API端点

1. 在 `myapp1/models.py` 中定义数据模型
2. 在 `myapp1/serializers.py` 中创建序列化器
3. 在 `myapp1/views/` 中编写视图函数
4. 在 `myapp1/urls.py` 中添加URL路由

### 权限控制

- 使用 `@permission_classes([AllowAny])` 允许公开访问
- 使用 `@permission_classes([IsAuthenticated])` 要求用户登录
- 使用 `@permission_classes([IsAdminUser])` 要求管理员权限

### 错误处理

所有API都包含适当的错误处理和状态码：

- `200 OK` - 请求成功
- `201 Created` - 创建成功
- `400 Bad Request` - 请求参数错误
- `401 Unauthorized` - 未认证
- `403 Forbidden` - 权限不足
- `404 Not Found` - 资源不存在
- `500 Internal Server Error` - 服务器错误

## 部署

### 生产环境配置

1. 修改 `settings.py` 中的 `DEBUG = False`
2. 配置生产数据库
3. 设置 `ALLOWED_HOSTS`
4. 配置静态文件服务
5. 使用 `gunicorn` 或 `uwsgi` 部署

### 环境变量

建议使用环境变量管理敏感配置：

```bash
export SECRET_KEY="your-secret-key"
export DATABASE_URL="mysql://user:password@localhost/library"
export DEBUG="False"
```

## 贡献

欢迎提交Issue和Pull Request来改进项目。

## 许可证

本项目采用MIT许可证。 