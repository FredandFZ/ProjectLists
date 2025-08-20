<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar class="sidebar" />
      <div class="content">
        <div class="project-list-vertical">
          <div class="search-container">
            <el-select v-model="searchType" placeholder="Search by" style="width: 140px; margin-right: 10px;">
              <el-option label="Name" :value="0" />
              <el-option label="Category" :value="1" />
              <el-option label="Area" :value="2" />
            </el-select>
            <el-input
              v-model="searchQuery"
              placeholder="Search projects..."
              style="width: 250px; margin-right: 10px;"
              @keyup.enter="handleSearch"
              clearable
              class="custom-search"
            />
            <el-button type="primary" @click="handleSearch">Search</el-button>
            <el-button type="danger" @click="handleClear">Clear</el-button>
          </div>
          
          <div class="projects-list">
            <el-card v-for="project in projects" :key="project.id" class="project-card">
              <div class="project-content">
                <div class="project-info">
                  <div class="project-header">
                    <h2 style="font-weight: bold;">{{ project.name }}</h2>
                    <el-tag :type="getStatusType(project.status)">{{ getStatusText(project.status) }}</el-tag>
                  </div>
                  <div class="project-details">
                    <p><strong style="font-weight: bold; color: #2F4E73;">Area:</strong> {{ project.area }}</p>
                    <p><strong style="font-weight: bold; color: #2F4E73;">Category:</strong> {{ project.category }}</p>
                    <p><strong style="font-weight: bold; color: #2F4E73;">Description:</strong> {{ project.description }}</p>
                  </div>
                </div>
                <div class="project-image" v-if="project.imageUrl">
                  <el-image
                    :src="project.imageUrl"
                    fit="fill"
                  />
                </div>
                <div v-else class="project-image-placeholder">
                  <el-empty
                    description="No image"
                    :image-size="100"
                  >
                    <template #image>
                      <el-icon :size="60" style="color: #909399;"><Picture /></el-icon>
                    </template>
                  </el-empty>
                </div>
              </div>
            </el-card>
          </div>

          <div class="pagination-container">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[5, 10, 20, 50]"
              :total="total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Picture } from '@element-plus/icons-vue'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
import request from '@/utils/request'

const searchType = ref(0) // 0-Name, 1-Category, 2-Area
const searchQuery = ref('')
const projects = ref([])
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 示例数据
const mockProjects = [
  {
    id: '1',
    title: 'Emergency Shelter and Relief Distribution for Flood Victims',
    status: 'Completed',
    area: 'Assam, India',
    category: 'Disaster Relief / Shelter and Basic Needs',
    imageUrl: '/images/project1.jpg'
  },
  {
    id: '2',
    title: 'Mobile Health Clinics for Displaced Communities',
    status: 'Ongoing',
    area: 'Gaziantep, Türkiye',
    category: 'Healthcare Access / Conflict Response',
    imageUrl: '/images/project2.jpg'
  },
  {
    id: '3',
    title: 'School Meals and Nutrition Program',
    status: 'Planned',
    area: 'Tigray, Ethiopia',
    category: 'Food Security / Child Welfare',
    imageUrl: '/images/project3.jpg'
  }
]

// 获取项目列表
const fetchProjects = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      searchType: searchType.value,
      searchValue: searchQuery.value
    }
    const res = await request.get('/projects/all', { params, noToken: true })
    if (res.code === 1) {
      projects.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('Failed to fetch projects:', error)
    projects.value = mockProjects
    total.value = mockProjects.length
  }
}

// 处理搜索输入
const handleSearch = () => {
  currentPage.value = 1
  fetchProjects()
}

// 处理页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchProjects()
}

// 处理每页条数改变
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchProjects()
}

// 获取状态对应的标签类型
const getStatusType = (status) => {
  const types = {
    0: 'info',     // Empathise
    1: 'warning',  // Discover
    2: 'success',  // Define
    3: 'primary',  // Ideate
    4: 'danger',   // Prototype
    5: 'success'   // Feedback
  }
  return types[status] || 'info'
}

// 获取状态显示文本
const getStatusText = (status) => {
  const texts = {
    0: 'Empathise',
    1: 'Discover',
    2: 'Define',
    3: 'Ideate',
    4: 'Prototype',
    5: 'Feedback'
  }
  return texts[status] || 'Unknown'
}

const handleClear = () => {
  searchQuery.value = ''
  searchType.value = 0
  currentPage.value = 1
  fetchProjects()
}

onMounted(() => {
  fetchProjects()
  // 可以在这里添加全局的错误处理
  window.addEventListener('unhandledrejection', (event) => {
    ElMessage.error('An error occurred. Please try again later.')
    console.error('Unhandled promise rejection:', event.reason)
  })
})
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.main-content {
  display: flex;
  margin-top: 60px;
  flex: 1;
  overflow-y: auto;
}

.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
}

.content {
  flex: 1;
  margin-left: 200px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.project-list-vertical {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.search-container {
  margin-bottom: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.projects-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.project-card {
  transition: transform 0.3s;
}

.project-card:hover {
  transform: translateX(5px);
}

.project-content {
  display: flex;
  gap: 20px;
}

.project-info {
  flex: 1;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.project-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.project-details p {
  margin: 8px 0;
  color: #666;
}

.project-image {
  width: 300px;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.project-image-placeholder {
  width: 300px;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #dcdfe6;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>