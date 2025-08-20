<template>
  <div class="login-page">
    <div class="container-fluid">
      <div class="row min-vh-100">
        <!-- 左侧装饰区域 -->
        <div class="col-lg-6 d-none d-lg-block bg-primary">
          <div class="d-flex align-items-center justify-content-center h-100">
            <div class="text-center text-white">
              <i class="bi bi-book text-white mb-4" style="font-size: 6rem;"></i>
              <h1 class="display-4 fw-bold mb-3">图书管理系统</h1>
              <p class="lead">高效管理图书资源，提升图书馆运营效率</p>
              <div class="mt-5">
                <div class="row text-center">
                  <div class="col-4">
                    <i class="bi bi-book text-white mb-2" style="font-size: 2rem;"></i>
                    <p class="mb-0">图书管理</p>
                  </div>
                  <div class="col-4">
                    <i class="bi bi-people text-white mb-2" style="font-size: 2rem;"></i>
                    <p class="mb-0">读者管理</p>
                  </div>
                  <div class="col-4">
                    <i class="bi bi-graph-up text-white mb-2" style="font-size: 2rem;"></i>
                    <p class="mb-0">统计分析</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 右侧登录表单 -->
        <div class="col-lg-6 d-flex align-items-center justify-content-center">
          <div class="w-100" style="max-width: 400px;">
            <div class="text-center mb-4">
              <h2 class="fw-bold text-primary">欢迎登录</h2>
              <p class="text-muted">请输入您的账号信息</p>
            </div>
            
            <div class="card border-0 shadow-sm">
              <div class="card-body p-4">
                <form @submit.prevent="handleLogin">
                  <!-- 用户名输入 -->
                  <div class="mb-3">
                    <label for="username" class="form-label">
                      <i class="bi bi-person me-2"></i>用户名
                    </label>
                    <input 
                      type="text" 
                      class="form-control form-control-lg" 
                      id="username"
                      v-model="loginForm.username"
                      placeholder="请输入用户名"
                      required
                    >
                  </div>
                  
                  <!-- 密码输入 -->
                  <div class="mb-3">
                    <label for="password" class="form-label">
                      <i class="bi bi-lock me-2"></i>密码
                    </label>
                    <div class="input-group">
                      <input 
                        :type="showPassword ? 'text' : 'password'" 
                        class="form-control form-control-lg" 
                        id="password"
                        v-model="loginForm.password"
                        placeholder="请输入密码"
                        required
                      >
                      <button 
                        class="btn btn-outline-secondary" 
                        type="button"
                        @click="togglePassword"
                      >
                        <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                      </button>
                    </div>
                  </div>
                  
                  <!-- 记住我选项 -->
                  <div class="mb-3 form-check">
                    <input 
                      type="checkbox" 
                      class="form-check-input" 
                      id="rememberMe"
                      v-model="loginForm.rememberMe"
                    >
                    <label class="form-check-label" for="rememberMe">
                      记住我
                    </label>
                  </div>
                  
                  <!-- 登录按钮 -->
                  <div class="d-grid mb-3">
                    <button 
                      type="submit" 
                      class="btn btn-primary btn-lg"
                      :disabled="isLoading"
                    >
                      <span v-if="isLoading" class="spinner-border spinner-border-sm me-2"></span>
                      <i v-else class="bi bi-box-arrow-in-right me-2"></i>
                      {{ isLoading ? '登录中...' : '登录' }}
                    </button>
                  </div>
                  
                  <!-- 错误提示 -->
                  <div v-if="errorMessage" class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    {{ errorMessage }}
                  </div>
                </form>
                
                <!-- 其他选项 -->
                <div class="text-center">
                  <a href="#" class="text-decoration-none text-muted">
                    <small>忘记密码？</small>
                  </a>
                </div>
              </div>
            </div>
            
            <!-- 底部信息 -->
            <div class="text-center mt-4">
              <p class="text-muted small">
                © 刘昕 图书管理系统. 保留所有权利.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'

const router = useRouter()

// 响应式数据
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

const showPassword = ref(false)
const isLoading = ref(false)
const errorMessage = ref('')

// 切换密码显示/隐藏
const togglePassword = () => {
  showPassword.value = !showPassword.value
}

// 处理登录
const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    errorMessage.value = '请填写完整的登录信息'
    return
  }
  
  isLoading.value = true
  errorMessage.value = ''
  
  try {
    // 调用登录API
    const response = await login(loginForm)
    console.log(response.code)
    if (response.code === 1) {
      // 保存token
      localStorage.setItem('token', response.token)
      
      // 保存登录状态和用户名
      if (loginForm.rememberMe) {
        localStorage.setItem('isLoggedIn', 'true')
        localStorage.setItem('username', loginForm.username)
      } else {
        sessionStorage.setItem('isLoggedIn', 'true')
        sessionStorage.setItem('username', loginForm.username)
      }
      
      // 跳转到首页
      router.push('/home')
    } 
    else {
      errorMessage.value = response.message || '用户名或密码错误'
    }
  } catch (error) {
    console.error('登录错误:', error)
    if (error.response) {
      errorMessage.value = error.response.data?.message || '登录失败，请稍后重试'
    } else {
      errorMessage.value = '网络错误，请检查网络连接'
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.form-control:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

.btn-primary {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

.btn-primary:hover {
  background-color: #0b5ed7;
  border-color: #0a58ca;
}

.card {
  border-radius: 1rem;
}

.form-control-lg {
  border-radius: 0.5rem;
}

.input-group .btn {
  border-radius: 0 0.5rem 0.5rem 0;
}

/* 响应式调整 */
@media (max-width: 991.98px) {
  .login-page {
    background: linear-gradient(135deg, #0d6efd 0%, #0b5ed7 100%);
  }
  
  .card {
    background-color: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
  }
}
</style>
