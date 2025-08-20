<template>
  <div class="invitation-container">
    <el-card class="invitation-card">
      <template #header>
        <div class="card-header">
          <el-icon><Message /></el-icon>
          <span>Send Verification Code</span>
        </div>
      </template>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="Email" prop="email">
          <el-input 
            v-model="form.email"
            placeholder="Please enter your email address"
            :prefix-icon="Message"
          />
        </el-form-item>

        <el-form-item label="User Type" prop="type">
          <el-select 
            v-model="form.type" 
            placeholder="Please select user type"
            style="width: 100%"
          >
            <el-option label="Organization Partner" :value="0" />
            <el-option label="Local Partner" :value="1" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading"
            @click="handleSendCode"
          >
            <el-icon><Position /></el-icon>
            Send Verification Code
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Message, Position } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  email: '',
  type: null
})

const rules = {
  email: [
    { required: true, message: 'Please enter your email address', trigger: 'blur' },
    { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
  ],
  type: [
    { required: true, message: 'Please select user type', trigger: 'change' }
  ]
}

const handleSendCode = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const params = new URLSearchParams()
    params.append('email', form.email)
    params.append('type', form.type)
    params.append('userId', localStorage.getItem('userId'))

    const res = await request.post('/manager/sendInvitationCode', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
    if (res.code === 1) {
      ElMessage.success('Invitation code has been sent to your email')
    } else {
      ElMessage.error("Error: " + res.message)
    }
  } catch (error) {
    console.error('Send failed:', error)
    ElMessage.error('Send failed, please try again')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.invitation-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #f5f7fa;
}

.invitation-card {
  width: 500px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: bold;
}

:deep(.el-card__header) {
  padding: 15px 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style> 