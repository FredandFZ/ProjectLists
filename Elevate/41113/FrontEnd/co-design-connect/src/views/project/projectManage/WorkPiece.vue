<template>
  <div class="work-piece-container">
    <el-loading v-if="loading" />
    <div v-else class="status-section" v-for="status in folderData" :key="status.statusId">
      <h2 class="status-title">Status: {{ getStatusName(status.statusId) }}</h2>
      <div class="folders-container">
        <el-card 
          v-for="iteration in status.iterations" 
          :key="iteration.iterationId"
          class="folder-card"
          shadow="hover"
          @click="handleCardClick(iteration)"
        >
          <div class="folder-content">
            <el-icon class="folder-icon"><Folder /></el-icon>
            <span class="folder-name">Iteration {{ iteration.iterationId }}</span>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Folder } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const folderData = ref([])
const route = useRoute()
const router = useRouter()

// status映射
const statusMap = {
  0: 'Empathise',
  1: 'Discover',
  2: 'Define',
  3: 'Ideate',
  4: 'Prototype',
  5: 'Feedback'
}



// 获取迭代列表
const getFolders = async () => {
  const projectId = route.params.id
  const res = await request.get(`/projects/${projectId}/folders`)
  if (res.code === 1) {
    return res.data
  }
  else {
    ElMessage.error('Failed to get iteration list'+res.message)
  }
}

// 获取所有数据
const fetchAllData = async () => {
  try {
    // 获取所有iteration
    const iterationList = await getFolders()
    
    // 按照statusId分组
    const groupedByStatus = iterationList.reduce((acc, iteration) => {
      const statusId = iteration.status
      if (!acc[statusId]) {
        acc[statusId] = []
      }
      acc[statusId].push({
        iterationId: iteration.iterationId,
        statusId: iteration.status
      })
      return acc
    }, {})
    
    // 转换为所需格式
    const formattedData = Object.entries(groupedByStatus).map(([statusId, iterations]) => ({
      statusId: parseInt(statusId),
      iterations: iterations
    }))
    
    console.log(formattedData)
    folderData.value = formattedData
    console.log(folderData.value)
  } catch (error) {
    console.error('Failed to fetch data:', error)
    ElMessage.error('Failed to fetch data, please try again later')
    folderData.value = mockData.value
  }
}

// 获取状态名称
const getStatusName = (statusId) => {
  return statusMap[statusId] || `Status ${statusId}`
}

// 处理卡片点击事件
const handleCardClick = (iteration) => {
  router.push(`/my-projects/workpiece/${route.params.id}/${iteration.statusId}/${iteration.iterationId}`)
}

// 页面加载时获取数据
onMounted(() => {
  fetchAllData()
  console.log(folderData.value)
})

// 模拟数据（可以删除）
const mockData = ref([
  {
    statusId: 1,
    iterations: [
      {
        id: 1,
        iterationId: 1,
        statusId: 1
      },
      {
        id: 2,
        iterationId: 2,
        statusId: 1
      },
      {
        id: 3,
        iterationId: 3,
        statusId: 1
      },
      {
        id: 4,
        iterationId: 4,
        statusId: 1
      },
      {
        id: 5,
        iterationId: 5,
        statusId: 1
      },
      {
        id: 6,
        iterationId: 6,
        statusId: 1
      },
      {
        id: 7,
        iterationId: 7,
        statusId: 1
      },
      {
        id: 8,
        iterationId: 8,
        statusId: 1
      },
      {
        id: 9,
        iterationId: 9,
        statusId: 1
      },
      {
        id: 10,
        iterationId: 10,
        statusId: 1
      }
    ]
  },
  {
    statusId: 2,
    iterations: [
      {
        id: 11,
        iterationId: 4,
        statusId: 2
      },
      {
        id: 12,
        iterationId: 5,
        statusId: 2
      }
    ]
  },
  {
    statusId: 3,
    iterations: [
      {
        id: 13,
        iterationId: 6,
        statusId: 3
      },
      {
        id: 14,
        iterationId: 7,
        statusId: 3
      },
      {
        id: 15,
        iterationId: 8,
        statusId: 3
      }
    ]
  },
  {
    statusId: 4,
    iterations: [
      {
        id: 16,
        iterationId: 9,
        statusId: 4
      },
      {
        id: 17,
        iterationId: 10,
        statusId: 4
      },
      {
        id: 18,
        iterationId: 11,
        statusId: 4
      }
    ]
  },
  {
    statusId: 5,
    iterations: [
      {
        id: 19,
        iterationId: 12,
        statusId: 5
      },
      {
        id: 20,
        iterationId: 13,
        statusId: 5
      }
    ]
  },
  {
    statusId: 6,
    iterations: [
      {
        id: 21,
        iterationId: 14,
        statusId: 6
      },
      {
        id: 22,
        iterationId: 15,
        statusId: 6
      },
      {
        id: 23,
        iterationId: 16,
        statusId: 6
      }
    ]
  }
])
</script>

<style scoped>
.work-piece-container {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.status-section {
  margin-bottom: 30px;
  padding: 20px;
  border-radius: 8px;
  background-color: #f5f7fa;
}

.status-section:nth-child(odd) {
  background-color: #f0f9eb;
}

.status-section:nth-child(3n) {
  background-color: #fdf6ec;
}

.status-section:nth-child(3n+1) {
  background-color: #f4f4f5;
}

.status-section:nth-child(3n+2) {
  background-color: #f0f9ff;
}

.status-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #303133;
  padding-left: 10px;
  border-left: 4px solid #2F4E73;
}

.folders-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding-bottom: 10px;
}

.folder-card {
  min-width: 150px;
  max-width: 150px;
  cursor: pointer;
}

.folder-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}

.folder-icon {
  font-size: 40px;
  color: #2F4E73;
  margin-bottom: 10px;
}

.folder-name {
  font-size: 14px;
  color: #606266;
}

/* 自定义滚动条样式 */
.folders-container::-webkit-scrollbar {
  height: 6px;
}

.folders-container::-webkit-scrollbar-thumb {
  background-color: #909399;
  border-radius: 3px;
}

.folders-container::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>
