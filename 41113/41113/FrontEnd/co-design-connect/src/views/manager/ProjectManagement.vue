<template>
  <div class="project-management">
    <el-table 
      v-loading="loading"
      :data="projects" 
      style="width: 100%" 
      border
    >
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column prop="name" label="Project Name" />
      <el-table-column prop="category" label="Category" sortable />
      <el-table-column prop="creator" label="Creator" />
      <el-table-column prop="status" label="Status">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Create Time" />
      <el-table-column label="Operation" width="120">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="handleDelete(row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const projects = ref([])
const loading = ref(false)

// 获取所有项目
const fetchProjects = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    params.append('userId', localStorage.getItem('userId'))
    const response = await request.get('/manager/projects',{
      params: params,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
    if (response.code === 1) {
      projects.value = response.data.map(data => ({
        ...data.project,
        creator: data.creatorName
      }))
    } else {
      ElMessage.error('Failed to get project list: ' + response.message)
    }
  } catch (error) {
    ElMessage.error('Failed to get project list: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 状态映射
const statusMap = {
  0: 'Empathise',
  1: 'Discover',
  2: 'Define',
  3: 'Ideate',
  4: 'Prototype',
  5: 'Feedback'
}

// 根据状态返回不同的标签类型
const getStatusType = (status) => {
  const typeMap = {
    0: 'info',
    1: 'primary',
    2: 'success',
    3: 'warning',
    4: 'danger',
    5: 'success'
  }
  return typeMap[status] || 'info'
}

// 获取状态显示文本
const getStatusText = (status) => {
  return statusMap[status] || 'Unknown status'
}

// 删除项目
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete the project "${row.name}"?`,
    'Warning',
    {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const res = await request.delete(`/projects/${row.id}/dismiss`,{
        params: {
          userId: localStorage.getItem('userId')
        }
      })
      if (res.code === 1) {
        ElMessage.success('Delete successfully')
        // 重新获取项目列表
        projects.value = projects.value.filter(project => project.id !== row.id)
      } else {
        ElMessage.error('Delete failed: ' + res.message)
      }
    } catch (error) {
      ElMessage.error('Delete failed: ' + (error.response?.data?.message || error.message))
    }
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: 'Delete cancelled',
    })
  })
}

// 组件挂载时获取项目列表
onMounted(() => {
  fetchProjects()
})
</script>

<style scoped>
.project-management {
  padding: 20px;
}
</style> 