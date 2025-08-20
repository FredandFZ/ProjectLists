<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
    <div class="container">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link class="nav-link" :class="{ active: $route.path === '/home' }" to="/home">
              <i class="bi bi-house me-2"></i>首页
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :class="{ active: $route.path === '/books' }" to="/books">
              <i class="bi bi-book me-2"></i>图书管理
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :class="{ active: $route.path === '/borrow' }" to="/borrow">
              <i class="bi bi-arrow-left-right me-2"></i>借还管理
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :class="{ active: $route.path === '/readers' }" to="/readers">
              <i class="bi bi-people me-2"></i>读者管理
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :class="{ active: $route.path === '/statistics' }" to="/statistics">
              <i class="bi bi-graph-up me-2"></i>统计报表
            </router-link>
          </li>
        </ul>
        
      </div>
    </div>
  </nav>
</template>

<script setup>

</script>

<style scoped>
.nav-link {
  font-weight: 500;
}

.nav-link.active {
  color: #0d6efd !important;
  font-weight: 600;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

.dropdown-item.text-danger:hover {
  background-color: #f8d7da;
}
</style>
