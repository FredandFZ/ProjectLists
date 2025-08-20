<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <h2>Profile Information</h2>
        </div>
      </template>
      
      <div class="profile-content" v-if="userInfo">
        <div class="info-item">
          <span class="label">Username:</span>
          <div class="value">
            <el-input 
              v-if="isEditing" 
              v-model="editedUsername" 
              placeholder="Please enter username"
            />
            <span v-else>{{ userInfo.username }}</span>
          </div>
        </div>
        
        <div class="info-item">
          <span class="label">Email:</span>
          <span class="value">{{ userInfo.email }}</span>
        </div>
        
        <div class="info-item">
          <span class="label">User Type:</span>
          <span class="value">{{ userInfo.userType }}</span>
        </div>

        <div class="actions">
          <template v-if="isEditing">
            <el-button type="primary" @click="saveChanges">Save</el-button>
            <el-button @click="cancelEdit">Cancel</el-button>
          </template>
          <el-button v-else type="primary" @click="startEdit">Edit</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const userInfo = ref(null)
const isEditing = ref(false)
const editedUsername = ref('')

// 获取用户信息
const getUserInfo = async () => {
  try {
    const userId = route.params.userId
    const res = await request.get(`/user/info?userId=${userId}`)
    if (res.code === 1) {
      userInfo.value = res.data
      editedUsername.value = res.data.username
    } else {
      ElMessage.error('Failed to get user information')
    }
  } catch (error) {
    console.error('Failed to get user information:', error)
    ElMessage.error('Failed to get user information')
  }
}

// 开始编辑
const startEdit = () => {
  isEditing.value = true
  editedUsername.value = userInfo.value.username
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
  editedUsername.value = userInfo.value.username
}

// 保存更改
const saveChanges = async () => {
  try {
    const res = await request.put('/user/update', {
      userId: route.params.userId,
      username: editedUsername.value
    })
    
    if (res.code === 1) {
      userInfo.value.username = editedUsername.value
      isEditing.value = false
      ElMessage.success('Update successful')
    } else {
      ElMessage.error('Update failed')
    }
  } catch (error) {
    console.error('Failed to update user information:', error)
    ElMessage.error('Update failed')
  }
}

onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 20px;
}

.profile-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  color: #333;
}

.profile-content {
  padding: 20px 0;
}

.info-item {
  display: flex;
  margin-bottom: 20px;
  align-items: center;
}

.label {
  width: 100px;
  font-weight: bold;
  color: #666;
}

.value {
  flex: 1;
}

.actions {
  margin-top: 30px;
  display: flex;
  gap: 10px;
}
</style>
