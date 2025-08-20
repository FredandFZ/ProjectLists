<template>
  <div class="borrow-page">
    <!-- Header组件 -->
    <AppHeader />
    
    <!-- 导航栏组件 -->
    <AppNavigator />
    
    <!-- 主要内容区域 -->
    <div class="container mt-4">
      <div class="row">
        <div class="col-12">
          <!-- 页面标题 -->
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">
              <i class="bi bi-book me-2"></i>借阅管理
            </h2>
          </div>

          <!-- 搜索和筛选区域 -->
          <div class="card mb-4">
            <div class="card-body">
              <div class="row g-3">
                <div class="col-md-8">
                  <input 
                    type="text" 
                    class="form-control" 
                    placeholder="搜索图书名称、读者姓名或ISBN..."
                    v-model="searchQuery"
                    @input="handleSearch"
                  >
                </div>
                <div class="col-md-4">
                  <button class="btn btn-outline-secondary w-100" @click="resetFilters">
                    <i class="bi bi-arrow-clockwise me-1"></i>重置
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 借阅记录表格 -->
          <div class="card">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead class="table-light">
                    <tr>
                      <th>ID</th>
                      <th>图书信息</th>
                      <th>读者信息</th>
                      <th>借阅日期</th>
                      <th>应还日期</th>
                      <th>实际归还日期</th>
                      <th>状态</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="record in borrowRecords" :key="record.id">
                      <td>{{ record.id }}</td>
                      <td>
                        <div class="d-flex align-items-center">
                          <div class="book-icon me-2">
                            <i class="bi bi-book text-primary"></i>
                          </div>
                          <div>
                            <div class="fw-medium">{{ record.book_name || '未知图书' }}</div>
                            <small class="text-muted">图书ID: {{ record.book }}</small>
                          </div>
                        </div>
                      </td>
                      <td>
                        <div class="d-flex align-items-center">
                          <div class="avatar me-2">
                            <i class="bi bi-person-circle text-success"></i>
                          </div>
                          <div>
                            <div class="fw-medium">{{ record.user_username || '未知用户' }}</div>
                            <small class="text-muted">用户ID: {{ record.user }}</small>
                          </div>
                        </div>
                      </td>
                      <td>{{ formatDate(record.borrow_date) }}</td>
                      <td>
                        <span :class="getDueDateClass(record.due_date)">
                          {{ formatDate(record.due_date) }}
                        </span>
                      </td>
                      <td>{{ formatDate(record.return_date) || '-' }}</td>
                      <td>
                        <span :class="getStatusClass(record)">
                          {{ record.status_display || getStatusText(record) }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- 空状态 -->
              <div class="text-center py-5" v-if="borrowRecords.length === 0 && !loading">
                <i class="bi bi-book display-1 text-muted"></i>
                <h4 class="mt-3 text-muted">暂无借阅记录</h4>
                <p class="text-muted">当前没有找到任何借阅记录</p>
              </div>

              <!-- 加载状态 -->
              <div class="text-center py-5" v-if="loading">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">加载中...</span>
                </div>
                <p class="mt-2 text-muted">正在加载借阅记录...</p>
              </div>
            </div>
          </div>

          <!-- 分页 -->
          <nav v-if="totalPages > 1" aria-label="借阅记录分页" class="mt-4">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: currentPage === 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">
                  <i class="bi bi-chevron-left"></i>
                </a>
              </li>
              
              <li class="page-item" v-for="page in visiblePages" :key="page" :class="{ active: page === currentPage }">
                <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
              </li>
              
              <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">
                  <i class="bi bi-chevron-right"></i>
                </a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import AppHeader from '@/components/AppHeader.vue'
import AppNavigator from '@/components/AppNavigator.vue'
import { getBorrowRecords } from '@/api/borrow'

// 响应式数据
const borrowRecords = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)
const searchQuery = ref('')

// 计算属性
const totalPages = computed(() => Math.ceil(totalCount.value / pageSize.value))

const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, currentPage.value + 2)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

// 方法
const fetchBorrowRecords = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      page_size: pageSize.value
    }
    
    if (searchQuery.value) {
      params.search = searchQuery.value
    }
    
    const response = await getBorrowRecords(params)
    borrowRecords.value = response.results || response
    totalCount.value = response.count || response.length
  } catch (error) {
    console.error('获取借阅记录失败:', error)
    alert('获取借阅记录失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchBorrowRecords()
}

const resetFilters = () => {
  searchQuery.value = ''
  currentPage.value = 1
  fetchBorrowRecords()
}

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    fetchBorrowRecords()
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const getStatusText = (record) => {
  // 优先使用后端提供的状态显示
  if (record.status_display) {
    return record.status_display
  }
  
  // 如果没有return_date，说明还在借阅中
  if (!record.return_date) {
    if (record.is_overdue) {
      return '已逾期'
    } else {
      return '借阅中'
    }
  }
  
  return '已归还'
}

const getStatusClass = (record) => {
  // 如果有return_date，说明已归还
  if (record.return_date) {
    return 'badge bg-success'
  }
  
  // 使用后端提供的逾期状态
  if (record.is_overdue) {
    return 'badge bg-danger'
  }
  
  return 'badge bg-primary'
}

const getDueDateClass = (dueDate) => {
  if (!dueDate) return ''
  
  const due = new Date(dueDate)
  const now = new Date()
  
  if (due < now) {
    return 'text-danger fw-bold'
  } else if (due.getTime() - now.getTime() < 7 * 24 * 60 * 60 * 1000) { // 7天内
    return 'text-warning fw-bold'
  }
  
  return ''
}

// 生命周期
onMounted(() => {
  fetchBorrowRecords()
})
</script>

<style scoped>
.borrow-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.avatar {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
}

.book-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
}

.table th {
  font-weight: 600;
  color: #495057;
  border-bottom: 2px solid #dee2e6;
}

.table td {
  vertical-align: middle;
}

.badge {
  font-size: 0.75rem;
}

.pagination .page-link {
  color: #0d6efd;
}

.text-danger {
  color: #dc3545 !important;
}

.text-warning {
  color: #ffc107 !important;
}

.text-success {
  color: #198754 !important;
}

.fw-bold {
  font-weight: bold !important;
}
</style> 