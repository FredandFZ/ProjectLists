<template>
  <div class="homepage">
    <!-- Header组件 -->
    <AppHeader />
    
    <!-- 导航栏组件 -->
    <AppNavigator />
    
    <!-- 主要内容区域 -->
    <main class="container mt-4">
      <div class="row">
        <!-- 欢迎卡片 -->
        <div class="col-12 mb-4">
          <div class="card border-0 shadow-sm">
            <div class="card-body text-center py-5">
              <i class="bi bi-book text-primary" style="font-size: 4rem;"></i>
              <h2 class="mt-3 mb-2">欢迎使用图书管理系统</h2>
              <p class="text-muted">高效管理图书资源，提升图书馆运营效率</p>
            </div>
          </div>
        </div>
        
        <!-- 功能快捷入口 -->
        <div class="col-md-3 mb-4">
          <div class="card h-100 border-0 shadow-sm">
            <div class="card-body text-center">
              <i class="bi bi-book text-primary mb-3" style="font-size: 2.5rem;"></i>
              <h5 class="card-title">图书管理</h5>
              <p class="card-text text-muted">添加、编辑、删除图书信息</p>
              <router-link to="/books" class="btn btn-outline-primary">进入管理</router-link>
            </div>
          </div>
        </div>
        
        <div class="col-md-3 mb-4">
          <div class="card h-100 border-0 shadow-sm">
            <div class="card-body text-center">
              <i class="bi bi-arrow-left-right text-success mb-3" style="font-size: 2.5rem;"></i>
              <h5 class="card-title">借还管理</h5>
              <p class="card-text text-muted">处理图书借阅和归还</p>
              <router-link to="/borrow" class="btn btn-outline-success">进入管理</router-link>
            </div>
          </div>
        </div>
        
        <div class="col-md-3 mb-4">
          <div class="card h-100 border-0 shadow-sm">
            <div class="card-body text-center">
              <i class="bi bi-people text-info mb-3" style="font-size: 2.5rem;"></i>
              <h5 class="card-title">读者管理</h5>
              <p class="card-text text-muted">管理读者信息和权限</p>
              <router-link to="/readers" class="btn btn-outline-info">进入管理</router-link>
            </div>
          </div>
        </div>
        
        <div class="col-md-3 mb-4">
          <div class="card h-100 border-0 shadow-sm">
            <div class="card-body text-center">
              <i class="bi bi-graph-up text-warning mb-3" style="font-size: 2.5rem;"></i>
              <h5 class="card-title">统计报表</h5>
              <p class="card-text text-muted">查看系统统计数据</p>
              <router-link to="/statistics" class="btn btn-outline-warning">查看报表</router-link>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue'
import AppNavigator from '@/components/AppNavigator.vue'
// 组合式API写法，不需要export default
// 导入的组件会自动注册
</script>

<style scoped>
.homepage {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.card {
  transition: transform 0.2s ease-in-out;
}

.card:hover {
  transform: translateY(-5px);
}

.nav-link {
  font-weight: 500;
}

.nav-link.active {
  color: #0d6efd !important;
  font-weight: 600;
}
</style> 