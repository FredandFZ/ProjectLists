<template>
  <div class="project-detail">
    <!-- 左侧项目信息 -->
    <div class="left-panel">
      <div class="back-button" @click="$router.push('/my-projects')">
        <el-icon><ArrowLeft /></el-icon>
        <span>Back</span>
      </div>
      <div class="project-header">
        <h1>{{ project.name }}</h1>
        <!-- <el-tag :type="getStatusType(project.status)" size="large">
          {{ getStatusText(project.status) }}
        </el-tag> -->
      </div>

      <div class="project-image" v-if="project.imageUrl">
        <el-image
          :src="project.imageUrl"
          fit="scale-down"
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

      <div class="project-info">
        <div class="info-item">
          <h3 style="color: #2F4E73;">Project Owner</h3>
          <p>{{ creatorName }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2F4E73;">Deadline</h3>
          <p>{{ project.deadline }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2F4E73;">Area</h3>
          <p>{{ project.area }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2F4E73;">Category</h3>
          <p>{{ project.category }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2F4E73;">Description</h3>
          <p>{{ project.description }}</p>
        </div>
      </div>

      <!-- 添加按钮区域 -->
      <div class="action-buttons">
        <el-button type="danger" @click="handleLeaveProject" :loading="loading.leave" class="custom-button">
          Leave Project
        </el-button>
        <el-button 
          v-if="isCreator" 
          type="danger" 
          @click="handleDismissProject" 
          :loading="loading.dismiss"
          class="custom-button"
        >
          Dismiss Project
        </el-button>
      </div>
    </div>

    <!-- 右侧功能区 -->
    <div class="right-panel">
      <div class="nav-links">
        <router-link :to="`/my-projects/${project.id}/channel`" class="nav-link">Channel</router-link>
        <router-link :to="`/my-projects/${project.id}/backlog`" class="nav-link">Backlog</router-link>
        <router-link :to="`/my-projects/${project.id}/workpiece`" class="nav-link">WorkPiece</router-link>
        <router-link :to="`/my-projects/${project.id}/member`" class="nav-link">Member</router-link>
        <router-link :to="`/my-projects/${project.id}/map`" class="nav-link">Map</router-link>
      </div>
      
      <!-- 内容区域 -->
      <div class="content-area">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Picture } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessageBox, ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const project = ref({})
const creatorName = ref('')
const members = ref([])
const creatorId = ref(0)
const isCreator = ref(false)
const loading = ref({
  leave: false,
  dismiss: false
})

// 获取项目详情
const fetchProjectDetail = async () => {
  try {
    const projectId = route.params.id
    const res = await request.get(`/projects/${projectId}`)
    if (res.code === 1) {
      project.value = res.data["project"]
      creatorName.value = res.data["creatorName"]
      members.value = res.data["members"]
      creatorId.value = res.data["project"]["creatorId"]
      
      // 存储项目相关信息到localStorage
      localStorage.setItem(`project_${projectId}_creatorId`, creatorId.value)
      localStorage.setItem(`project_${projectId}_info`, JSON.stringify(project.value))
      localStorage.setItem(`project_${projectId}_members`, JSON.stringify(members.value))

      // 获取项目详情后立即判断用户身份
      checkIsCreator()
    }
  } catch (error) {
    console.error('Failed to fetch project details:', error)
  }
}

// 检查当前用户是否为创建者
const checkIsCreator = () => {
  const currentUserId = Number(localStorage.getItem('userId'))
  isCreator.value = currentUserId === project.value.creatorId
}

// 退出项目
const handleLeaveProject = async () => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to leave this project?',
      'Warning',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    )
    
    loading.value.leave = true
    const userId = localStorage.getItem('userId')
    const res = await request.post('/projects/leave', {
      projectId: route.params.id,
      userId: userId
    }, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
    if (res.code === 1) {
      ElMessage.success('Successfully left the project')
      router.push('/my-projects')
    } else {
      ElMessage.error(res.message || 'Failed to leave project')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to leave project:', error)
      ElMessage.error('Failed to leave project')
    }
  } finally {
    loading.value.leave = false
  }
}

// 解散项目
const handleDismissProject = async () => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to dismiss this project? This action cannot be undone!',
      'Warning',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'error'
      }
    )
    
    loading.value.dismiss = true
    const res = await request.delete(`/projects/${route.params.id}/dismiss`)
    if (res.code === 1) {
      ElMessage.success('Project has been successfully dismissed')
      router.push('/my-projects')
    } else {
      ElMessage.error(res.message || 'Failed to dismiss project')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to dismiss project:', error)
      ElMessage.error('Failed to dismiss project')
    }
  } finally {
    loading.value.dismiss = false
  }
}

// 清除项目相关存储
const clearProjectStorage = () => {
  const projectId = route.params.id
  localStorage.removeItem(`project_${projectId}_creatorId`)
  localStorage.removeItem(`project_${projectId}_info`)
  localStorage.removeItem(`project_${projectId}_members`)
}

// 在组件卸载时清除存储
onUnmounted(() => {
  clearProjectStorage()
})

onMounted(() => {
  fetchProjectDetail()
  // 如果当前路径只包含项目ID，则重定向到channel页面
  if (route.path === `/my-projects/${route.params.id}`) {
    router.push(`/my-projects/${route.params.id}/channel`)
  }
})
</script>

<style scoped>
.project-detail {
  display: flex;
  width: 100%;
  height: 100%;
  background-color: #fff;
}

.left-panel {
  flex: 0 0 300px;
  padding: 20px;
  border-right: 1px solid #e4e7ed;
  overflow: hidden;
  min-width: 300px;
  max-width: 300px;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e4e7ed;
}

.project-header h1 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.project-image {
  width: 100%;
  height: 200px;
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
}

.project-image-placeholder {
  width: 100%;
  height: 200px;
  margin-bottom: 20px;
  border-radius: 4px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #dcdfe6;
}

.project-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item h3 {
  margin: 0 0 4px 0;
  font-size: 14px;
  color: #303133;
  font-weight: bold;
  letter-spacing: 0.5px;
}

.info-item p {
  margin: 0;
  color: #333;
  line-height: 1.5;
}

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.nav-links {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 50px;
  border-bottom: 1px solid #e4e7ed;
}

.nav-link {
  flex: 1;
  text-align: center;
  padding: 0 24px;
  line-height: 50px;
  color: #2F4E73;
  text-decoration: none;
  font-size: 17px;
  font-weight: 500;
  border-radius: 6px 6px 0 0;
  background: linear-gradient(90deg, #e3f0ff 0%, #f8fbff 100%);
  margin: 0 4px;
  transition: all 0.3s, box-shadow 0.2s;
  position: relative;
  box-shadow: 0 2px 8px 0 rgba(64,158,255,0.04);
}

.nav-link:hover {
  color: #fff;
  background: linear-gradient(90deg, #2F4E73 0%, #66b1ff 100%);
  box-shadow: 0 4px 16px 0 rgba(64,158,255,0.12);
}

.nav-link.router-link-active {
  color: #fff;
  background: linear-gradient(90deg, #2F4E73 0%, #66b1ff 100%);
  font-weight: bold;
  box-shadow: 0 6px 20px 0 rgba(64,158,255,0.18);
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 24px;
  right: 24px;
  height: 3px;
  background-color: #fff;
  border-radius: 2px 2px 0 0;
}

.content-area {
  flex: 1;
  padding: 20px;
  height: 100%;
  overflow: hidden;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  cursor: pointer;
  padding: 8px 0;
  margin-bottom: 16px;
  transition: color 0.3s;
}

.back-button:hover {
  color: #2F4E73;
}

.back-button .el-icon {
  font-size: 16px;
}

.action-buttons {
  margin-top: 20px;
  padding: 10px;
  display: flex;
  gap: 15px;
  justify-content: center;
  width: 100%;
}

.action-buttons .custom-button {
  flex: 1;
  max-width: 120px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 8px;
  padding: 10px 20px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.action-buttons .custom-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-buttons .custom-button:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style> 