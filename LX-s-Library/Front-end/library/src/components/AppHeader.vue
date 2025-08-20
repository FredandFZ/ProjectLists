<template>
  <header class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
      <!-- 管理系统名称 -->
      <a class="navbar-brand fw-bold" href="#">
        <i class="bi bi-book me-2"></i>
        图书管理系统
      </a>
      
      <!-- 用户区域 -->
      <ul class="navbar-nav ms-auto">
        <!-- 用户信息和退出登录 -->
        <li class="nav-item dropdown">
          <a 
            class="nav-link dropdown-toggle d-flex align-items-center" 
            href="#" 
            @click.prevent="toggleDropdown"
          >
            <i class="bi bi-person-circle me-2"></i>
            {{ username }}
          </a>
          <ul 
            class="dropdown-menu dropdown-menu-end" 
            :class="{ 'show': isDropdownOpen }"
            @click.stop
          >
            <li><a class="dropdown-item" href="#"><i class="bi bi-person me-2"></i>个人资料</a></li>
            <li><a class="dropdown-item" href="#"><i class="bi bi-gear me-2"></i>设置</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item text-danger" href="#" @click="handleLogout"><i class="bi bi-box-arrow-right me-2"></i>退出登录</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { logout } from '@/api/auth'

const router = useRouter()
const username = ref('')
const isDropdownOpen = ref(false)

// 获取用户名
const getUsername = () => {
  // 优先从localStorage获取，如果没有则从sessionStorage获取
  const storedUsername = localStorage.getItem('username') || sessionStorage.getItem('username')
  username.value = storedUsername || '用户'
}

// 切换下拉菜单
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

// 关闭下拉菜单
const closeDropdown = () => {
  isDropdownOpen.value = false
}

// 处理退出登录
const handleLogout = async () => {
  // 添加确认提示
  if (!confirm('确定要退出登录吗？')) {
    return
  }
  
  try {
    // 调用退出登录API
    await logout()
    
    // 清除本地存储的登录信息
    localStorage.removeItem('token')
    localStorage.removeItem('isLoggedIn')
    localStorage.removeItem('username')
    sessionStorage.removeItem('isLoggedIn')
    sessionStorage.removeItem('username')
    
    // 跳转到登录页
    router.push('/login')
  } catch (error) {
    console.error('退出登录错误:', error)
    // 即使API调用失败，也清除本地存储并跳转
    localStorage.removeItem('token')
    localStorage.removeItem('isLoggedIn')
    localStorage.removeItem('username')
    sessionStorage.removeItem('isLoggedIn')
    sessionStorage.removeItem('username')
    router.push('/login')
  }
}

// 点击外部关闭下拉菜单
const handleClickOutside = (event) => {
  const dropdown = event.target.closest('.dropdown')
  if (!dropdown) {
    closeDropdown()
  }
}

// 组件挂载时获取用户名并添加事件监听
onMounted(() => {
  getUsername()
  document.addEventListener('click', handleClickOutside)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.navbar-brand {
  font-size: 1.5rem;
}

.nav-link {
  color: rgba(255, 255, 255, 0.9) !important;
  cursor: pointer;
}

.nav-link:hover {
  color: white !important;
}

.dropdown-menu {
  min-width: 200px;
  display: none;
}

.dropdown-menu.show {
  display: block;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

.dropdown-item.text-danger:hover {
  background-color: #f8d7da;
}

/* 确保下拉菜单在其他元素之上 */
.dropdown-menu {
  z-index: 1000;
}
</style> 