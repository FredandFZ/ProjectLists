<template>
  <div class="statistics-page">
    <!-- Header组件 -->
    <AppHeader />
    
    <!-- 导航栏组件 -->
    <AppNavigator />
    
    <!-- 主要内容区域 -->
    <main class="container mt-4">
      <div class="statistics-container">
        <!-- 页面标题 -->
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h1 class="mb-0">
            <i class="bi bi-graph-up text-primary me-2"></i>
            统计信息
          </h1>
          <div class="btn-group" role="group">
            <button 
              v-for="tab in tabs" 
              :key="tab.id"
              @click="activeTab = tab.id"
              :class="['btn', activeTab === tab.id ? 'btn-primary' : 'btn-outline-primary']"
            >
              <i :class="tab.icon + ' me-1'"></i>
              {{ tab.name }}
            </button>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">加载中...</span>
          </div>
          <p class="mt-2">正在加载统计数据...</p>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="error" class="alert alert-danger" role="alert">
          <i class="bi bi-exclamation-triangle me-2"></i>
          {{ error }}
        </div>

        <!-- 仪表板内容 -->
        <div v-else>
          <!-- 仪表板概览 -->
          <div v-if="activeTab === 'dashboard'" class="dashboard-overview">
            <!-- 统计卡片 -->
            <div class="row g-4 mb-4">
              <div class="col-xl-3 col-md-6">
                <div class="card stat-card">
                  <div class="card-body">
                    <div class="d-flex justify-content-between">
                      <div>
                        <h6 class="card-subtitle mb-2 text-muted">总用户数</h6>
                        <h2 class="card-title mb-0">{{ dashboardData.users?.total || 0 }}</h2>
                        <small class="text-success">
                          <i class="bi bi-arrow-up"></i>
                          活跃: {{ dashboardData.users?.active || 0 }}
                        </small>
                      </div>
                      <div class="stat-icon">
                        <i class="bi bi-people-fill text-primary"></i>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-xl-3 col-md-6">
                <div class="card stat-card">
                  <div class="card-body">
                    <div class="d-flex justify-content-between">
                      <div>
                        <h6 class="card-subtitle mb-2 text-muted">总图书数</h6>
                        <h2 class="card-title mb-0">{{ dashboardData.books?.total || 0 }}</h2>
                        <small class="text-info">
                          可借: {{ dashboardData.books?.available || 0 }}
                        </small>
                      </div>
                      <div class="stat-icon">
                        <i class="bi bi-book-fill text-success"></i>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-xl-3 col-md-6">
                <div class="card stat-card">
                  <div class="card-body">
                    <div class="d-flex justify-content-between">
                      <div>
                        <h6 class="card-subtitle mb-2 text-muted">借阅记录</h6>
                        <h2 class="card-title mb-0">{{ dashboardData.borrow_records?.total || 0 }}</h2>
                        <small class="text-warning">
                          逾期: {{ dashboardData.borrow_records?.overdue || 0 }}
                        </small>
                      </div>
                      <div class="stat-icon">
                        <i class="bi bi-journal-check text-warning"></i>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-xl-3 col-md-6">
                <div class="card stat-card">
                  <div class="card-body">
                    <div class="d-flex justify-content-between">
                      <div>
                        <h6 class="card-subtitle mb-2 text-muted">今日借阅</h6>
                        <h2 class="card-title mb-0">{{ dashboardData.today?.borrows || 0 }}</h2>
                        <small class="text-secondary">
                          归还: {{ dashboardData.today?.returns || 0 }}
                        </small>
                      </div>
                      <div class="stat-icon">
                        <i class="bi bi-calendar-event text-info"></i>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 图表区域 -->
            <div class="row g-4">
              <div class="col-lg-8">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-graph-up me-2"></i>
                      借阅趋势
                    </h5>
                  </div>
                  <div class="card-body">
                    <canvas ref="borrowTrendChart" height="300"></canvas>
                  </div>
                </div>
              </div>

              <div class="col-lg-4">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-pie-chart me-2"></i>
                      图书状态分布
                    </h5>
                  </div>
                  <div class="card-body">
                    <canvas ref="bookStatusChart" height="300"></canvas>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 图书统计 -->
          <div v-else-if="activeTab === 'books'" class="books-stats">
            <div class="row g-4">
              <div class="col-lg-6">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-tags me-2"></i>
                      分类分布
                    </h5>
                  </div>
                  <div class="card-body">
                    <canvas ref="categoryChart" height="250"></canvas>
                  </div>
                </div>
              </div>

              <div class="col-lg-6">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-star me-2"></i>
                      热门图书排行
                    </h5>
                  </div>
                  <div class="card-body">
                    <div v-if="bookStats.popularBooks" class="popular-books">
                      <div 
                        v-for="(book, index) in bookStats.popularBooks" 
                        :key="book.id"
                        class="d-flex align-items-center mb-3"
                      >
                        <div class="rank-badge me-3">{{ index + 1 }}</div>
                        <div class="flex-grow-1">
                          <h6 class="mb-1">{{ book.title }}</h6>
                          <small class="text-muted">{{ book.author }}</small>
                        </div>
                        <div class="text-end">
                          <div class="fw-bold">{{ book.borrowCount }}次</div>
                          <small class="text-muted">借阅</small>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 借阅统计 -->
          <div v-else-if="activeTab === 'borrows'" class="borrows-stats">
            <div class="row g-4">
              <div class="col-12">
                <div class="card">
                  <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-calendar-range me-2"></i>
                      借阅趋势分析
                    </h5>
                    <div class="btn-group btn-group-sm">
                      <button 
                        v-for="period in timePeriods" 
                        :key="period.value"
                        @click="selectedPeriod = period.value"
                        :class="['btn', selectedPeriod === period.value ? 'btn-primary' : 'btn-outline-primary']"
                      >
                        {{ period.label }}
                      </button>
                    </div>
                  </div>
                  <div class="card-body">
                    <canvas ref="borrowTrendChart" height="300"></canvas>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 用户统计 -->
          <div v-else-if="activeTab === 'users'" class="users-stats">
            <div class="row g-4">
              <div class="col-lg-8">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-people me-2"></i>
                      用户注册趋势
                    </h5>
                  </div>
                  <div class="card-body">
                    <canvas ref="userTrendChart" height="300"></canvas>
                  </div>
                </div>
              </div>

              <div class="col-lg-4">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-person-badge me-2"></i>
                      用户类型分布
                    </h5>
                  </div>
                  <div class="card-body">
                    <canvas ref="userTypeChart" height="250"></canvas>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 综合统计 -->
          <div v-else-if="activeTab === 'comprehensive'" class="comprehensive-stats">
            <div class="row g-4">
              <div class="col-12">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-speedometer2 me-2"></i>
                      系统概览
                    </h5>
                  </div>
                  <div class="card-body">
                    <div class="row g-3">
                      <div class="col-md-3">
                        <div class="text-center">
                          <div class="display-6 text-primary">{{ comprehensiveData.overview?.totalBooks || 0 }}</div>
                          <div class="text-muted">总图书数</div>
                        </div>
                      </div>
                      <div class="col-md-3">
                        <div class="text-center">
                          <div class="display-6 text-success">{{ comprehensiveData.overview?.totalUsers || 0 }}</div>
                          <div class="text-muted">总用户数</div>
                        </div>
                      </div>
                      <div class="col-md-3">
                        <div class="text-center">
                          <div class="display-6 text-info">{{ comprehensiveData.overview?.totalBorrows || 0 }}</div>
                          <div class="text-muted">总借阅数</div>
                        </div>
                      </div>
                      <div class="col-md-3">
                        <div class="text-center">
                          <div class="display-6 text-warning">{{ comprehensiveData.overview?.totalCategories || 0 }}</div>
                          <div class="text-muted">分类数量</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-lg-6">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-calendar-check me-2"></i>
                      本月统计
                    </h5>
                  </div>
                  <div class="card-body">
                    <div class="row g-3">
                      <div class="col-6">
                        <div class="text-center">
                          <div class="h4 text-primary">{{ comprehensiveData.monthly?.newBooks || 0 }}</div>
                          <div class="text-muted">新增图书</div>
                        </div>
                      </div>
                      <div class="col-6">
                        <div class="text-center">
                          <div class="h4 text-success">{{ comprehensiveData.monthly?.newUsers || 0 }}</div>
                          <div class="text-muted">新增用户</div>
                        </div>
                      </div>
                      <div class="col-6">
                        <div class="text-center">
                          <div class="h4 text-info">{{ comprehensiveData.monthly?.totalBorrows || 0 }}</div>
                          <div class="text-muted">借阅次数</div>
                        </div>
                      </div>
                      <div class="col-6">
                        <div class="text-center">
                          <div class="h4 text-warning">{{ comprehensiveData.monthly?.overdueBooks || 0 }}</div>
                          <div class="text-muted">逾期图书</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-lg-6">
                <div class="card">
                  <div class="card-header">
                    <h5 class="card-title mb-0">
                      <i class="bi bi-trophy me-2"></i>
                      热门分类排行
                    </h5>
                  </div>
                  <div class="card-body">
                    <div v-if="comprehensiveData.popularCategories" class="popular-categories">
                      <div 
                        v-for="(category, index) in comprehensiveData.popularCategories" 
                        :key="category.id"
                        class="d-flex align-items-center mb-3"
                      >
                        <div class="rank-badge me-3">{{ index + 1 }}</div>
                        <div class="flex-grow-1">
                          <h6 class="mb-1">{{ category.name }}</h6>
                          <small class="text-muted">{{ category.bookCount }}本图书</small>
                        </div>
                        <div class="text-end">
                          <div class="fw-bold">{{ category.borrowCount }}次</div>
                          <small class="text-muted">借阅</small>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { Chart, registerables } from 'chart.js'
import AppHeader from '@/components/AppHeader.vue'
import AppNavigator from '@/components/AppNavigator.vue'
import { 
  getDashboardStats, 
  getBookStats, 
  getBorrowStats, 
  getUserStats, 
  getComprehensiveStats 
} from '@/api/statistics'

// 注册Chart.js组件
Chart.register(...registerables)

// 响应式数据
const loading = ref(true)
const error = ref(null)
const activeTab = ref('dashboard')
const selectedPeriod = ref(30)

// 统计数据
const dashboardData = ref({})
const bookStats = ref({})
const borrowStats = ref({})
const userStats = ref({})
const comprehensiveData = ref({})

// 图表引用
const borrowTrendChart = ref(null)
const bookStatusChart = ref(null)
const categoryChart = ref(null)
const userTrendChart = ref(null)
const userTypeChart = ref(null)

// 标签页配置
const tabs = [
  { id: 'dashboard', name: '仪表板', icon: 'bi bi-speedometer2' },
  { id: 'books', name: '图书统计', icon: 'bi bi-book' },
  { id: 'borrows', name: '借阅统计', icon: 'bi bi-journal-check' },
  { id: 'users', name: '用户统计', icon: 'bi bi-people' },
  { id: 'comprehensive', name: '综合统计', icon: 'bi bi-graph-up' }
]

// 时间周期配置
const timePeriods = [
  { label: '7天', value: 7 },
  { label: '30天', value: 30 },
  { label: '90天', value: 90 }
]

// 图表实例
let charts = {}

// 初始化数据
const initData = async () => {
  try {
    loading.value = true
    error.value = null
    
    // 并行加载所有数据
    const [dashboard, books, borrows, users, comprehensive] = await Promise.all([
      getDashboardStats(),
      getBookStats(),
      getBorrowStats({ days: selectedPeriod.value }),
      getUserStats(),
      getComprehensiveStats()
    ])
    
    dashboardData.value = dashboard.data || {}
    bookStats.value = books.data || {}
    borrowStats.value = borrows.data || {}
    userStats.value = users.data || {}
    comprehensiveData.value = comprehensive.data || {}
    
    // 等待DOM更新后初始化图表
    await nextTick()
    initCharts()
    
  } catch (err) {
    error.value = '加载统计数据失败: ' + (err.message || '未知错误')
    console.error('统计数据加载失败:', err)
  } finally {
    loading.value = false
  }
}

// 初始化图表
const initCharts = () => {
  // 借阅趋势图表
  if (borrowTrendChart.value && borrowStats.value.trend) {
    const ctx = borrowTrendChart.value.getContext('2d')
    charts.borrowTrend = new Chart(ctx, {
      type: 'line',
      data: {
        labels: borrowStats.value.trend.map(item => item.date),
        datasets: [
          {
            label: '借阅数量',
            data: borrowStats.value.trend.map(item => item.borrows),
            borderColor: 'rgb(75, 192, 192)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            tension: 0.1
          },
          {
            label: '归还数量',
            data: borrowStats.value.trend.map(item => item.returns),
            borderColor: 'rgb(255, 99, 132)',
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            tension: 0.1
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top',
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    })
  }

  // 图书状态分布图表
  if (bookStatusChart.value && dashboardData.value.books) {
    const ctx = bookStatusChart.value.getContext('2d')
    charts.bookStatus = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['可借', '已借出', '其他'],
        datasets: [{
          data: [
            dashboardData.value.books.available || 0,
            dashboardData.value.books.borrowed || 0,
            (dashboardData.value.books.total || 0) - (dashboardData.value.books.available || 0) - (dashboardData.value.books.borrowed || 0)
          ],
          backgroundColor: [
            'rgba(75, 192, 192, 0.8)',
            'rgba(255, 99, 132, 0.8)',
            'rgba(255, 205, 86, 0.8)'
          ]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
          }
        }
      }
    })
  }

  // 分类分布图表
  if (categoryChart.value && bookStats.value.categories) {
    const ctx = categoryChart.value.getContext('2d')
    charts.category = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: bookStats.value.categories.map(item => item.name),
        datasets: [{
          label: '图书数量',
          data: bookStats.value.categories.map(item => item.count),
          backgroundColor: 'rgba(54, 162, 235, 0.8)'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    })
  }

  // 用户注册趋势图表
  if (userTrendChart.value && userStats.value.registrationTrend) {
    const ctx = userTrendChart.value.getContext('2d')
    charts.userTrend = new Chart(ctx, {
      type: 'line',
      data: {
        labels: userStats.value.registrationTrend.map(item => item.date),
        datasets: [{
          label: '新注册用户',
          data: userStats.value.registrationTrend.map(item => item.count),
          borderColor: 'rgb(153, 102, 255)',
          backgroundColor: 'rgba(153, 102, 255, 0.2)',
          tension: 0.1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top',
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    })
  }

  // 用户类型分布图表
  if (userTypeChart.value && userStats.value.userTypes) {
    const ctx = userTypeChart.value.getContext('2d')
    charts.userType = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: userStats.value.userTypes.map(item => item.type),
        datasets: [{
          data: userStats.value.userTypes.map(item => item.count),
          backgroundColor: [
            'rgba(255, 99, 132, 0.8)',
            'rgba(54, 162, 235, 0.8)',
            'rgba(255, 205, 86, 0.8)',
            'rgba(75, 192, 192, 0.8)'
          ]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
          }
        }
      }
    })
  }
}

// 监听标签页切换
const handleTabChange = async () => {
  // 销毁现有图表
  Object.values(charts).forEach(chart => {
    if (chart) chart.destroy()
  })
  charts = {}
  
  // 重新初始化图表
  await nextTick()
  initCharts()
}

// 监听时间周期变化
const handlePeriodChange = async () => {
  try {
    const borrows = await getBorrowStats({ days: selectedPeriod.value })
    borrowStats.value = borrows.data || {}
    
    // 更新借阅趋势图表
    if (charts.borrowTrend) {
      charts.borrowTrend.destroy()
      await nextTick()
      initCharts()
    }
  } catch (err) {
    console.error('更新借阅统计失败:', err)
  }
}

// 监听响应式数据变化
watch(activeTab, handleTabChange)
watch(selectedPeriod, handlePeriodChange)

// 组件挂载时初始化
onMounted(() => {
  initData()
})
</script>

<style scoped>
.statistics-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.statistics-container {
  padding: 0;
}

.stat-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  font-size: 2.5rem;
  opacity: 0.7;
}

.card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.card-header {
  background-color: #fff;
  border-bottom: 1px solid #e9ecef;
  border-radius: 12px 12px 0 0 !important;
}

.rank-badge {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 0.9rem;
}

.popular-books .rank-badge:nth-child(1) {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
}

.popular-books .rank-badge:nth-child(2) {
  background: linear-gradient(135deg, #c0c0c0 0%, #e5e5e5 100%);
}

.popular-books .rank-badge:nth-child(3) {
  background: linear-gradient(135deg, #cd7f32 0%, #daa520 100%);
}

.popular-categories .rank-badge:nth-child(1) {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
}

.popular-categories .rank-badge:nth-child(2) {
  background: linear-gradient(135deg, #c0c0c0 0%, #e5e5e5 100%);
}

.popular-categories .rank-badge:nth-child(3) {
  background: linear-gradient(135deg, #cd7f32 0%, #daa520 100%);
}

.btn-group .btn {
  border-radius: 8px;
  margin: 0 2px;
}

.display-6 {
  font-weight: 600;
}

@media (max-width: 768px) {
  .btn-group {
    flex-wrap: wrap;
  }
  
  .btn-group .btn {
    margin-bottom: 5px;
  }
}
</style> 