<template>
  <div class="header">
    <div class="logo">
      <h1>Co-Design-Connect</h1>
    </div>
    <div class="user-info">
      <el-dropdown v-if="userInfo" @command="handleCommand">
        <div class="user-link">
          <Avatar :username="userInfo.username" :size="32" />
          <span class="username">{{ userInfo.username }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">Profile</el-dropdown-item>
            <el-dropdown-item command="logout" divided>Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <router-link to="/login" class="login-link" v-else>
        <el-button type="text">Sign in</el-button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed , onUnmounted} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import Avatar from '@/components/Avatar.vue'

const router = useRouter()
const userInfo = ref(null)

// 获取用户信息的方法
const getUserInfo = async () => {
  try {
    const userId = localStorage.getItem('userId')
    const res = await request.get(`/user/info?userId=${userId}`)
    if (res.code === 1) {
      userInfo.value = res.data
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('userEmail', res.data.email)
    }
  } catch (error) {
    console.error('Failed to fetch user info:', error)
    userInfo.value = null
  }
}

// 处理下拉菜单命令
const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push(`/profile/${userInfo.value.id}`)
  } else if (command === 'logout') {
    try {
      // 清除本地存储的用户信息
      localStorage.removeItem('userId')
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('userEmail')
      // 清除用户信息
      userInfo.value = null
      // 显示成功消息
      ElMessage.success('Logout successfully')
      // 跳转到登录页
      router.push('/')
    } catch (error) {
      console.error('Logout failed:', error)
      ElMessage.error('Logout failed')
    }
  }
}

onMounted(() => {
  getUserInfo()
})

onUnmounted(() => {
  userInfo.value = null
})

</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo h1 {
  color: #e74c3c;
  margin: 0;
  font-size: 24px;
  font-family: 'Comic Sans MS', cursive, sans-serif;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #333;
}

.username {
  margin-left: 8px;
}

.login-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}
</style> 