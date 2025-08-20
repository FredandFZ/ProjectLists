<template>
  <div class="login-container">
    <!-- 品牌Logo -->
    <h1 class="brand-logo">Co-Design Connect</h1>

    <!-- 登录表单容器 -->
    <div class="login-card">
      <!-- 表单标题 -->
      <h2 class="form-title">User Login</h2>

      <!-- 使用@submit.prevent阻止默认提交 -->
      <form @submit.prevent="handleSubmit">
        <!-- 邮箱输入 -->
        <div class="input-group">
          <input type="email" v-model.trim="formData.email" placeholder="Enter your email" class="form-control" required
            autocomplete="username" />
        </div>

        <!-- 密码输入 -->
        <div class="input-group password-group">
          <input :type="showPassword ? 'text' : 'password'" v-model.trim="formData.password"
            placeholder="Enter your password" class="form-control" required autocomplete="current-password" />
          <button type="button" class="password-toggle" @click="showPassword = !showPassword">
            {{ showPassword ? 'Hide' : 'Show' }}
          </button>
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn">
          Login
        </button>
      </form>

      <!-- 辅助链接 -->
      <div class="auth-links">
        <RouterLink to="/reset-password" class="link">Forgot Password?</RouterLink>
        <RouterLink to="/register" class="link">Register Now</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

// 初始化路由
const router = useRouter()
const showPassword = ref(false)

// 响应式表单数据（使用reactive替代多个ref）
const formData = reactive({
  email: '',
  password: ''
})

// 表单提交处理
const handleSubmit = async () => {
  try {
    const params = new URLSearchParams()
    params.append('email', formData.email)
    params.append('password', formData.password)

    const res = await request.post('/login', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    if (res.code === 1){
      //保存信息并跳转主页
      localStorage.setItem('token', res.data.accessToken)
      localStorage.setItem('userId', res.data.id)
      router.push('/')
    } else {
      alert(res.message || 'Failed to login, please try again')
    }
  } catch (error) {
    console.error('Failed to login:', error)
    alert(error.message || 'Failed to login, please try again')
  }
}
</script>

<style scoped>
/* 容器样式 */
.login-container {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: #f8f9fa;
  padding: 2rem;
}

/* 品牌标识 */
.brand-logo {
  color: #106A52;
  font-size: 3.5rem;
  text-align: center;
  margin-bottom: 2.5rem;
}

/* 登录卡片 */
.login-card {
  background: white;
  width: min(90%, 600px);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* 表单标题 */
.form-title {
  font-size: 1.5rem;
  color: #333;
  text-align: center;
  margin-bottom: 2rem;
}

/* 输入组样式 */
.input-group {
  margin-bottom: 1.5rem;
}

.password-group {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  font-size: 0.875rem;
  padding: 4px 8px;
  border-radius: 4px;
  background: transparent;
  transition: all 0.3s ease;
}

.password-toggle:hover {
  color: #282D38;
  background: rgba(225, 37, 27, 0.1);
}

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #e9ecef;
  border-radius: 6px;
  transition: border-color 0.3s ease;
}

.password-group .form-control {
  padding-right: 70px;
}

.form-control:focus {
  border-color: #106A52;
  outline: none;
}

/* 提交按钮 */
.submit-btn {
  width: 100%;
  padding: 0.75rem;
  background: #106A52;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition:
    background 0.3s ease,
    transform 0.1s ease;
}

.submit-btn:hover {
  background: #282D38;
}

.submit-btn:active {
  transform: scale(0.98);
}

/* 辅助链接 */
.auth-links {
  margin-top: 1.5rem;
  display: flex;
  justify-content: space-between;
}

.link {
  color: #6c757d;
  font-size: 0.875rem;
  text-decoration: none;
  transition: color 0.3s ease;
}

.link:hover {
  color: #282D38;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .login-card {
    padding: 1.5rem 1rem;
  }

  .form-control {
    padding: 1rem;
  }
}
</style>