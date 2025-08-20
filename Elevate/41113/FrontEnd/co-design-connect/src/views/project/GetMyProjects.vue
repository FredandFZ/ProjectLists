<template>
  <div class="project-list">
    <div class="header-container">
      <h2>My Projects</h2>
      <div class="button-group">
        <el-button type="primary" @click="$router.push('/my-projects/create')">Create Project</el-button>
        <el-button type="success" @click="$router.push('/my-projects/join')">Join Project</el-button>
      </div>
    </div>

    <div class="search-container">
      <el-select v-model="searchType" placeholder="选择搜索方式" style="width: 140px; margin-right: 10px;">
        <el-option label="Name" :value="0" />
        <el-option label="Category" :value="1" />
        <el-option label="Area" :value="2" />
      </el-select>
      <el-input v-model="searchQuery" placeholder="Search projects..." style="width: 250px; margin-right: 10px;"
        @keyup.enter="handleSearch" clearable />
      <el-button type="primary" @click="handleSearch">Search</el-button>
      <el-button type="danger" @click="handleClear">Clear</el-button>
    </div>

    <div class="projects-grid">
      <el-card v-for="project in projects" :key="project.id" class="project-card"
        @click="$router.push(`/my-projects/${project.id}`)">
        <div class="project-header">
          <h2 style="font-weight: bold;">{{ project.name }}</h2>
          <el-tag :type="getStatusType(project.status)">{{ getStatusText(project.status) }}</el-tag>
        </div>

        <div class="project-info">
          <p><strong style="font-weight: bold; color: #2F4E73;">Area:</strong> {{ project.area }}</p>
          <p><strong style="font-weight: bold; color: #2F4E73;">Category:</strong> {{ project.category }}</p>
        </div>

        <div class="project-image" v-if="project.imageUrl">
          <el-image :src="project.imageUrl" fit="fill" />
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
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search, Picture } from '@element-plus/icons-vue'
import request from '@/utils/request'

const searchType = ref(0) // 0-名称, 1-类别, 2-地区
const searchQuery = ref('')
const projects = ref([])

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
const fetchProjects = async (type = null, value = '') => {
  try {
    const userId = localStorage.getItem('userId')
    const params = { userId }
    if (type !== null && value) {
      params.searchType = type
      params.searchValue = value
    }
    const res = await request.get('/projects/my', { params })
    if (res.code === 1) {
      projects.value = res.data
    }
  } catch (error) {
    console.error('Failed to fetch projects:', error)
    // 使用示例数据作为后备方案
    projects.value = mockProjects
  }
}

const handleSearch = () => {
  fetchProjects(searchType.value, searchQuery.value)
}

const handleClear = () => {
  searchQuery.value = ''
  searchType.value = 0
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

onMounted(() => {
  fetchProjects()
})
</script>

<style scoped>
.project-list {
  height: 100%;
  padding: 20px;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-container h2 {
  margin: 0;
}

.button-group {
  display: flex;
  gap: 10px;
}

.search-container {
  max-width: 600px;
  margin: 0 auto 30px;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.project-card {
  transition: transform 0.3s;
}

.project-card:hover {
  transform: translateY(-5px);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.project-header h2 {
  margin: 0;
  font-size: 18px;
}

.project-info {
  margin-bottom: 15px;
}

.project-info p {
  margin: 5px 0;
}

.project-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 4px;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
}

.project-image-placeholder {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 4px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #dcdfe6;
}
</style>
