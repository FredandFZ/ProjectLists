<template>
  <div class="register-container">
    <!-- 品牌Logo -->
    <h1 class="brand-logo">Co-Design Connect</h1>

    <!-- 注册表单容器 -->
    <div class="register-card">
      <!-- 头像展示 -->
      <div style="display: flex; justify-content: center; margin-bottom: 1rem;">
        <Avatar :username="formData.username" :size="48" />
      </div>
      <!-- 表单标题 -->
      <h2 class="form-title">User Registration</h2>

      <form @submit.prevent="handleSubmit">
        <!-- Username Input -->
        <div class="input-group">
          <input type="text" v-model.trim="formData.username" placeholder="Enter username" class="form-control" required
            autocomplete="username" @input="validateUsername" @keypress="preventIllegalChars" />
          <div v-if="usernameError" class="error-message">{{ usernameError }}</div>
        </div>

        <!-- 邮箱输入 -->
        <div class="input-group">
          <input type="email" v-model.trim="formData.email" placeholder="Enter your email" class="form-control" required
            autocomplete="username" />
        </div>

        <!-- 密码输入 -->
        <div class="input-group password-group">
          <input :type="showPassword ? 'text' : 'password'" v-model.trim="formData.password"
            placeholder="Enter password (8-20 characters)" class="form-control" required autocomplete="new-password" />
          <button type="button" class="password-toggle" @click="showPassword = !showPassword">
            {{ showPassword ? 'Hide' : 'Show' }}
          </button>
        </div>

        <!-- 确认密码 -->
        <div class="input-group password-group">
          <input :type="showConfirmPassword ? 'text' : 'password'" v-model.trim="formData.confirmPassword"
            placeholder="Confirm password" class="form-control" required autocomplete="new-password" />
          <button type="button" class="password-toggle" @click="showConfirmPassword = !showConfirmPassword">
            {{ showConfirmPassword ? 'Hide' : 'Show' }}
          </button>
        </div>

        <!-- 邀请码 -->
        <div class="input-group">
          <input type="text" v-model.trim="formData.inviteCode" placeholder="Invited Code" class="form-control" />
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn">
          Register
        </button>
      </form>

      <!-- 辅助链接 -->
      <div class="auth-links">
        <RouterLink to="/login" class="link">Already have an account? Login</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import Avatar from '@/components/Avatar.vue'

const router = useRouter()
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const usernameError = ref('')

const formData = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  inviteCode: ''
})

// Prevent illegal characters from being entered
const preventIllegalChars = (event) => {
  // Allow English letters, numbers and spaces
  const allowedChars = /^[a-zA-Z0-9\s]$/
  if (!allowedChars.test(event.key)) {
    event.preventDefault()
  }
}

// Enhanced username validation
const validateUsername = () => {
  const username = formData.username
  if (!username) {
    usernameError.value = 'Username is required'
    return false
  }

  // Check for illegal characters
  const illegalChars = /[^a-zA-Z0-9\s]/
  if (illegalChars.test(username)) {
    usernameError.value = 'Username can only contain English letters, numbers and spaces'
    return false
  }

  // Check for starting/ending with space
  if (username.startsWith(' ') || username.endsWith(' ')) {
    usernameError.value = 'Username cannot start or end with a space'
    return false
  }

  // Length validation (3-20 characters)
  if (username.length < 3 || username.length > 20) {
    usernameError.value = 'Username must be between 3 and 20 characters'
    return false
  }

  // Check for common reserved words
  const reservedWords = ['admin', 'root', 'system', 'user', 'guest']
  if (reservedWords.includes(username.toLowerCase())) {
    usernameError.value = 'This username is reserved and cannot be used'
    return false
  }

  usernameError.value = ''
  return true
}

const validateForm = () => {
  // Username validation
  if (!validateUsername()) return false

  // Password consistency validation
  if (formData.password !== formData.confirmPassword) {
    alert('Passwords do not match')
    return false
  }

  // Password complexity validation
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/
  if (!passwordRegex.test(formData.password)) {
    alert('Password must contain letters and numbers, length 8-20 characters')
    return false
  }

  return true
}

const handleSubmit = async () => {
  if (!validateForm()) return

  try {
    const params = new URLSearchParams()
    params.append('username', formData.username)
    params.append('email', formData.email)
    params.append('password', formData.password)
    if (formData.inviteCode) {
      params.append('inviteCode', formData.inviteCode)
    }

    const response = await request.post('/register', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    if (response.code === 1) {
      alert('Registration successful')
      router.push('/login')
    } else {
      alert(response.message || 'Registration failed')
    }
  } catch (error) {
    console.error('Registration error:', error)
    const errorMessage = error.response?.data?.message || error.message || 'Registration failed, please try again'
    alert(errorMessage)
  }
}
</script>

<style scoped>
/* 注册页面特定样式 */
.register-container {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: #f8f9fa;
  padding: 2rem;
}

.register-card {
  background: white;
  width: min(90%, 600px);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 1.5rem;
  color: #333;
  text-align: center;
  margin-bottom: 2rem;
}

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
  color: #106A52;
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
  background: #138366;
}

.submit-btn:active {
  transform: scale(0.98);
}

.auth-links {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
}

.link {
  color: #138366;
  font-size: 0.875rem;
  text-decoration: none;
  transition: color 0.3s ease;
}

.link:hover {
  color: #106A52;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .register-card {
    padding: 1.5rem 1rem;
  }

  .form-control {
    padding: 1rem;
  }
}

/* Add error message styles */
.error-message {
  color: #dc3545;
  font-size: 0.875rem;
  margin-top: 0.25rem;
  margin-left: 0.25rem;
}
</style>