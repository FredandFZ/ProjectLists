<template>
  <div class="login-container">
    <!-- 品牌Logo -->
    <h1 class="brand-logo">Co-Design Connect</h1>

    <!-- 重置密码表单容器 -->
    <div class="login-card">
      <!-- 表单标题 -->
      <h2 class="form-title">Reset Password</h2>

      <form @submit.prevent="handleSubmit">
        <!-- 邮箱输入 -->
        <div class="input-group">
          <input type="email" v-model.trim="formData.email" placeholder="Enter your email" class="form-control" required
            :disabled="isCodeSent" />
          <button type="button" class="code-button" @click="sendVerificationCode"
            :disabled="isCodeSent && countdown > 0">
            {{ countdown > 0 ? `Retry in ${countdown}s` : 'Send Code' }}
          </button>
        </div>

        <!-- 验证码输入 -->
        <div class="input-group">
          <input type="text" v-model.trim="formData.verificationCode" placeholder="Enter verification code"
            class="form-control" required maxlength="6" />
        </div>

        <!-- 新密码输入 -->
        <div class="input-group password-group">
          <input :type="showPassword ? 'text' : 'password'" v-model.trim="formData.newPassword"
            placeholder="Enter new password (8-20 characters)" class="form-control" required />
          <button type="button" class="password-toggle" @click="showPassword = !showPassword">
            {{ showPassword ? 'Hide' : 'Show' }}
          </button>
        </div>

        <!-- 确认新密码 -->
        <div class="input-group password-group">
          <input :type="showConfirmPassword ? 'text' : 'password'" v-model.trim="formData.confirmPassword"
            placeholder="Confirm new password" class="form-control" required />
          <button type="button" class="password-toggle" @click="showConfirmPassword = !showConfirmPassword">
            {{ showConfirmPassword ? 'Hide' : 'Show' }}
          </button>
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn">
          Confirm Change
        </button>
      </form>

      <!-- 辅助链接 -->
      <div class="auth-links">
        <RouterLink to="/login" class="link">Back to Login</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const isCodeSent = ref(false)
const countdown = ref(0)

const formData = reactive({
  email: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: ''
})

// 发送验证码
const sendVerificationCode = async () => {
  if (!formData.email) {
    alert('Please enter your email address')
    return
  }

  try {
    const params = new URLSearchParams()
    params.append('email', formData.email)
    await request.post('/password/resetCode', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    // 发送成功后开始倒计时
    isCodeSent.value = true
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)

  } catch (error) {
    console.error('Failed to send verification code:', error)
    alert(error.message || 'Failed to send verification code, please try again')
  }
}

const validateForm = () => {
  // Password consistency validation
  if (formData.newPassword !== formData.confirmPassword) {
    alert('Passwords do not match')
    return false
  }

  // Password complexity validation
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/
  if (!passwordRegex.test(formData.newPassword)) {
    alert('Password must contain letters and numbers, length 8-20 characters')
    return false
  }

  return true
}

// 提交表单
const handleSubmit = async () => {
  if (!validateForm()) return

  try {
    const params = new URLSearchParams()
    params.append('email', formData.email)
    params.append('verificationCode', formData.verificationCode)
    params.append('newPassword', formData.newPassword)

    const response = await request.post('/password/update', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })

    if (response.code === 1) {
      alert('Password changed successfully')
      router.push('/login')
    } else {
      alert(response.message || 'Failed to reset password, please try again')
    }
  } catch (error) {
    console.error('Failed to reset password:', error)
    alert(error.message || 'Failed to reset password, please try again')
  }
}
</script>

<style scoped>
/* 复用登录页面的基础样式 */
.login-container {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: #f8f9fa;
  padding: 2rem;
}

.brand-logo {
  color: #106A52;
  font-size: 3.5rem;
  text-align: center;
  margin-bottom: 2.5rem;
}

.login-card {
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
  position: relative;
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

/* 验证码按钮样式 */
.code-button {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #106A52;
  font-size: 0.875rem;
  padding: 4px 8px;
  border-radius: 4px;
  background: transparent;
  border: 1px solid #106A52;
  cursor: pointer;
  transition: all 0.3s ease;
}

.code-button:hover:not(:disabled) {
  background: #106A52;
  color: white;
}

.code-button:disabled {
  color: #999;
  border-color: #999;
  cursor: not-allowed;
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
  background: #106A52;
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
  color: #6c757d;
  font-size: 0.875rem;
  text-decoration: none;
  transition: color 0.3s ease;
}

.link:hover {
  color: #106A52;
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