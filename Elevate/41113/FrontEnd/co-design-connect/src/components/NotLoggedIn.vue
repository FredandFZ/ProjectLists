<template>
  <div class="not-logged-in-container">
    <el-result
      icon="warning"
      title="Please Login First"
      sub-title="You need to login to access this page"
      class="custom-result"
    >
      <template #extra>
        <el-button type="primary" @click="goToLogin">Go to Login</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.not-logged-in-container {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  padding: 20px;
}

.custom-result :deep(.el-result__title) {
  font-weight: bold;
  color: #409EFF;
}
</style> 