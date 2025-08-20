<template>
  <div>
    <el-table :data="users" style="width: 100%" border v-loading="loading">
      <el-table-column prop="id" label="ID" sortable width="80" />
      <el-table-column prop="username" label="Name"  />
      <el-table-column prop="email" label="Email" />
      <el-table-column prop="type" label="Type" sortable>
        <template #default="scope">
          <el-tag
            :type="scope.row.type === 0 ? 'success' : 'warning'"
            effect="light"
          >
            {{ getUserType(scope.row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Create Time" width="180" />
      <el-table-column label="Operation" width="120">
        <template #default="scope">
          <el-button
            type="danger"
            size="small"
            @click="handleDelete(scope.row)"
          >
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const users = ref([])
const loading = ref(false)

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    params.append('userId', localStorage.getItem('userId'))
    const response = await request.get('/manager/users',{
      params: params,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
    if (response.code === 1) {
      users.value = response.data
    } else {
      ElMessage.error('Failed to get user list: ' + response.message)
    }
  } catch (error) {
    ElMessage.error('Failed to get user list: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 用户类型映射
const getUserType = (type) => {
  const typeMap = {
    0: 'Organization Partner',
    1: 'Local Partner'
  }
  return typeMap[type] || 'Unknown Type'
}

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete user ${row.name}?`,
    'Warning',
    {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
        const params = new URLSearchParams()
        params.append('userId', localStorage.getItem('userId'))
        await request.delete(`/manager/users/${row.id}`,{
          params: params,
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        })
        const index = users.value.findIndex(user => user.id === row.id)
        if (index !== -1) {
          users.value.splice(index, 1)
        }
        ElMessage({
          type: 'success',
          message: 'Delete successfully',
        })
      } catch (error) {
        ElMessage.error('Delete user failed: ' + (error.response?.data?.message || error.message))
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Delete canceled',
      })
    })
}

// 组件挂载时获取用户列表
onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.el-table {
  margin-top: 20px;
}
</style> 