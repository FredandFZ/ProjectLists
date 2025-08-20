<template>
  <div class="backlog-container">
    <div class="backlog-top">
      <div class="step-bar">
        <el-steps :active="activeStep" align-center finish-status="success">
          <el-step v-for="(item, idx) in steps" :key="idx" :title="item" />
        </el-steps>
      </div>
      <div class="step-btns" v-if="isCreator">
        <el-button
          type="primary"
          :disabled="activeStep === 0"
          @click="handlePrev"
        >
          <el-icon><ArrowLeft /></el-icon>
          Go Back
        </el-button>
        <el-button
          type="primary"
          :disabled="activeStep === steps.length - 1"
          @click="handleNext"
        >
          Go Next
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
    </div>
    <!-- 底部内容区域 -->
    <div class="backlog-content">
      <div class="iterations-container">
        <div v-for="iteration in iterations" :key="iteration.id" class="iteration-section" style="width: 100%; overflow-x: auto;">
          <div class="interation-header">
            <span>{{ iteration.title }}</span>
          </div>
          <el-table
            :data="iteration.tasks"
            row-key="id"
            border
            max-height="400"
            style="width: 100%;"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            @selection-change="handleSelectionChange"
            ref="backlogTable"
          >
            <el-table-column prop="type" label="Type" width="150" header-align="left">
              <template #default="scope">
                <span class="type-inline">
                  <el-icon v-if="scope.row.type === 'task'">
                    <Document />
                  </el-icon>
                  <el-icon v-else-if="scope.row.type === 'subtask'">
                    <DocumentAdd />
                  </el-icon>
                  <el-tooltip v-if="scope.row.type === 'task'" content="Add Subtask" placement="top">
                    <span
                      class="add-subtask-btn"
                      @click.stop="handleAddSubTask(scope.row, iteration)"
                    >
                      <el-icon><Plus /></el-icon>
                    </span>
                  </el-tooltip>
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="code" label="Code" min-width="120" header-align="center" align="center">
              <template #default="scope">
                <span>{{ scope.row.code }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="creator" label="Creator" min-width="100" />
            <el-table-column prop="content" label="Content" min-width="200">
              <template #default="scope">
                <div
                  class="editable-cell"
                  @dblclick="handleEdit(scope.row, 'content')"
                >
                  <span v-if="!scope.row.isEditing || scope.row.editingField !== 'content'">
                    {{ scope.row.content }}
                  </span>
                  <el-input
                    v-else
                    v-model="scope.row.content"
                    size="small"
                    @blur="handleSave(scope.row, 'content')"
                    @keyup.enter="handleSave(scope.row, 'content')"
                  />
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="Status" min-width="120">
              <template #default="scope">
                <div class="status-cell">
                  <el-select
                    v-if="scope.row.isEditing && scope.row.editingField === 'status'"
                    v-model="scope.row._statusStr"
                    size="small"
                    @blur="handleSave(scope.row, 'status')"
                    @change="handleSave(scope.row, 'status')"
                    @visible-change="val => { if (!val) handleSave(scope.row, 'status') }"
                    style="width: 100px;"
                    filterable={false}
                    placeholder="Select Status"
                  >
                    <el-option label="TO DO" value="TO DO" />
                    <el-option label="IN PROGRESS" value="IN PROGRESS" />
                    <el-option label="DONE" value="DONE" />
                  </el-select>
                  <el-tag
                    v-else
                    :type="scope.row.status === 2 ? 'success' : (scope.row.status === 1 ? 'warning' : 'info')"
                    class="status-tag"
                    @dblclick="handleEdit(scope.row, 'status')"
                    style="cursor:pointer;"
                  >
                    {{ statusMap[scope.row.status] }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="assigneeId" label="Assignee" min-width="140">
              <template #default="scope">
                <div class="assignee-cell">
                  <el-select
                    v-if="scope.row.isEditing && scope.row.editingField === 'assignee'"
                    v-model="scope.row._assigneeId"
                    size="small"
                    filterable
                    placeholder="选择成员"
                    style="width: 120px;"
                    @change="handleSave(scope.row, 'assignee')"
                    @blur="handleSave(scope.row, 'assignee')"
                    @visible-change="val => { if (!val) handleSave(scope.row, 'assignee') }"
                  >
                    <el-option
                      v-for="member in members"
                      :key="member.id"
                      :label="member.username"
                      :value="Number(member.id)"
                    >
                      <div class="assignee-option">
                        <Avatar :username="member.username" :size="20" />
                        <span>{{ member.username }}</span>
                      </div>
                    </el-option>
                  </el-select>
                  <div v-else class="assignee-display" @dblclick="handleEdit(scope.row, 'assignee')">
                    <Avatar :username="getMember(scope.row.assigneeId)?.username || scope.row.assigneeId" :size="20" v-if="getMember(scope.row.assigneeId) !== null"/>
                    <span>{{ scope.row.assigneeId === null ? 'Unknown' : (getMember(scope.row.assigneeId)?.username || scope.row.assigneeId) }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="Create Time" min-width="100">
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="Operation" width="100">
              <template #default="scope">
                <el-button
                  link
                  type="danger"
                  size="middle"
                  @click="handleDelete(scope.row)"
                >
                  Delete
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="new-task-container">
            <el-button
              class="new-task-btn"
              @click="handleAddNewTask(iteration)"
            >
              <el-icon><Plus /></el-icon>
              Add Task
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElSteps, ElStep, ElButton, ElIcon, ElMessageBox } from 'element-plus'
import { ArrowLeft, ArrowRight, Plus, Document, DocumentAdd, User } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useRoute, useRouter } from 'vue-router'
import Avatar from '@/components/Avatar.vue'

const steps = [
  'Empathise',
  'Discover',
  'Define',
  'Ideate',
  'Prototype',
  'Feedback'
]
const activeStep = ref(0)
const route = useRoute()
const isCreator = ref(false)

const iterations = ref([])

const selectedRows = ref([])

const handleSelectionChange = (val) => {
  selectedRows.value = val
}

// 获取当前项目的status
const fetchStatus = async () => {
  try {
    const projectId = route.params.id
    const res = await request.get(`/projects/${projectId}/status`)
    if (res.code === 1) {
      activeStep.value = res.data.status
      console.log(activeStep.value)
    }
  } catch (e) {
    activeStep.value = 0
    isCreator.value = false
  }
}

// 更新项目状态
const updateProjectStatus = async (newStatus) => {
  try {
    const projectId = route.params.id
    const res = await request.put(`/projects/${projectId}/status`, {
      status: newStatus
    })
    if (res.code === 1) {
      activeStep.value = newStatus
    }
  } catch (error) {
    console.error('Failed to update project status:', error)
  }
}

// 新建iteration方法
const createIteration = async (status) => {
  const projectId = route.params.id
  await request.post(`/projects/${projectId}/iterations`, { 
    projectStatus: status,
    userId: localStorage.getItem('userId')
  })
}

const handlePrev = async () => {
  if (activeStep.value > 0) {
    try {
      await ElMessageBox.confirm(
        'Are you sure you want to go back?',
        'Confirm',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }
      )
      const newStatus = activeStep.value - 1
      await updateProjectStatus(newStatus)
      await createIteration(newStatus)
      await fetchIterations()
    } catch (e) {}
  }
}

const handleNext = async () => {
  if (activeStep.value < steps.length - 1) {
    try {
      await ElMessageBox.confirm(
        'Are you sure you want to go to the next step?',
        'Confirm',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }
      )
      const newStatus = activeStep.value + 1
      await updateProjectStatus(newStatus)
      await createIteration(newStatus)
      await fetchIterations()
    } catch (e) {}
  }
}

const checkIsCreator = () => {
  const projectId = route.params.id
  const creatorId = localStorage.getItem(`project_${projectId}_creatorId`)
  const currentUserId = localStorage.getItem('userId')
  isCreator.value = creatorId === currentUserId
}

const backlogTable = ref(null)

// 获取项目成员
const members = ref([])
const loadMembers = () => {
  try {
    const projectId = route.params.id
    const memberStr = localStorage.getItem(`project_${projectId}_members`)
    if (memberStr) {
      members.value = JSON.parse(memberStr)
    } else {
      members.value = []
    }
  } catch {
    members.value = []
  }
}

onMounted(async () => {
  await fetchStatus()
  await fetchIterations()
  checkIsCreator()
  loadMembers()
})

// 检查是否有权限删除任务
const canDeleteTask = (task) => {
  const currentUserId = localStorage.getItem('userId')
  // 如果是项目创建者，可以删除任何任务
  if (isCreator.value) {
    return true
  }
  // 如果是任务创建者，可以删除自己的任务
  return String(task.creatorId) === String(currentUserId)
}

// 在前端删除数据
const deleteTaskFromIterations = (iterations, taskId) => {
  for (let iteration of iterations) {
    // 检查主任务
    const mainTaskIndex = iteration.tasks.findIndex(task => task.id === taskId)
    if (mainTaskIndex !== -1) {
      // 如果是主任务，直接删除整个任务（包括子任务）
      iteration.tasks.splice(mainTaskIndex, 1)
      return
    }
    // 检查子任务
    for (let task of iteration.tasks) {
      if (task.children) {
        const childTaskIndex = task.children.findIndex(child => child.id === taskId)
        if (childTaskIndex !== -1) {
          task.children.splice(childTaskIndex, 1)
          return
        }
      }
    }
  }
}

// 删除任务
const handleDelete = async (row) => {
  try {
    // 首先检查权限
    if (!canDeleteTask(row)) {
      ElMessageBox.alert(
        'You do not have permission to delete this task. Only the task creator or project owner can delete tasks.',
        'Permission Denied',
        {
          confirmButtonText: 'OK',
          type: 'warning'
        }
      )
      return
    }

    await ElMessageBox.confirm(
      'Are you sure you want to delete this task?',
      'Confirm',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    )
    
    // 保存原始数据，以便在删除失败时恢复
    const originalIterations = JSON.parse(JSON.stringify(iterations.value))
    
    // 执行前端删除
    deleteTaskFromIterations(iterations.value, row.id)
    
    // 发送删除请求到后端
    const projectId = route.params.id
    const res = await request.delete(`/projects/${projectId}/tasks/${row.id}`, {
      data: {
        userId: localStorage.getItem('userId')
      }
    })
    
    if (res.code !== 1) {
      // 如果后端删除失败，恢复数据
      iterations.value = originalIterations
      ElMessageBox.alert(
        'Failed to delete the task. Please try again.',
        'Error',
        {
          confirmButtonText: 'OK',
          type: 'error'
        }
      )
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete task:', error)
      // 发生错误时也恢复数据
      iterations.value = originalIterations
      ElMessageBox.alert(
        'An error occurred while deleting the task. Please try again.',
        'Error',
        {
          confirmButtonText: 'OK',
          type: 'error'
        }
      )
    }
  }
}

// 获取迭代数据
const fetchIterations = async () => {
  try {
    const projectId = route.params.id
    const res = await request.get(`/projects/${projectId}/iterations`, {
      params: {
        status: activeStep.value
      }
    })
    if (res.code === 1) {
      // 按id从大到小排序
      iterations.value = res.data.sort((a, b) => b.id - a.id)
    }
  } catch (error) {
    console.error('Failed to fetch iterations:', error)
    iterations.value = []
  }
}

// 添加新建任务的处理函数
const handleAddNewTask = async (iteration) => {
  try {
    const projectId = route.params.id
    // 构造新任务数据
    const newTaskData = {
      taskId: 0,  // 主任务taskId为0
      creatorId: localStorage.getItem('userId'),
      content: 'Double click to edit task content',
      status: 0,
      assigneeId: 0,
      createTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
      iterationId: iteration.id  // 添加iterationId
    }
    // 发送请求到后端
    const res = await request.post(`/projects/${projectId}/tasks`, newTaskData)
    if (res.code === 1 && res.data) {
      // 后端返回新任务，添加到前端
      iteration.tasks.push(res.data)
      // 滚动到新添加的行
      // nextTick(() => {
      //   const table = backlogTable.value
      //   if (table) {
      //     const scrollBody = table.$el.querySelector('.el-scrollbar__body')
      //     if (scrollBody) {
      //       scrollBody.scrollTo({
      //         top: scrollBody.scrollHeight,
      //         behavior: 'smooth'
      //       })
      //     }
      //   }
      // })
    } else {
      ElMessageBox.alert('Failed to add task, please try again', 'Error', {
        confirmButtonText: 'OK',
        type: 'error'
      })
    }
  } catch (error) {
    ElMessageBox.alert('An error occurred while adding a task, please try again', 'Error', {
      confirmButtonText: 'OK',
      type: 'error'
    })
  }
}

// 添加日期格式化函数
const formatDate = (dateString) => {
  if (!dateString) return ''
  // 只返回YYYY-MM-DD
  return dateString.slice(0, 10)
}

// 获取成员信息
const getMember = (userId) => {
  if (userId === null) return null
  const member = members.value.find(m => Number(m.id) === Number(userId))
  return member || null
}

// 添加编辑相关的函数
const handleEdit = (row, field) => {
  // 如果是 code 字段，直接返回，不允许编辑
  if (field === 'code') {
    return
  }
  
  if (!canDeleteTask(row)) {
    ElMessageBox.alert(
      'You do not have permission to edit this task. Only the task creator or project owner can edit tasks.',
      'Permission Denied',
      {
        confirmButtonText: 'OK',
        type: 'warning'
      }
    )
    return
  }
  row.isEditing = true
  row.editingField = field
  row.originalValue = row[field]
  if (field === 'status') {
    row._statusStr = statusMap[row.status]
  }
  if (field === 'assignee') {
    row._assigneeId = row.assigneeId
  }
}

const handleSave = async (row, field) => {
  try {
    if (field === 'status') {
      const newStatusNum = statusReverseMap[row._statusStr]
      if (newStatusNum === row.status) {
        row.isEditing = false
        row.editingField = null
        return
      }
      const projectId = route.params.id
      const res = await request.put(`/projects/${projectId}/tasks/${row.id}`, {
        status: newStatusNum,  // 直接使用number类型
        userId: localStorage.getItem('userId')
      })
      if (res.code === 1) {
        row.status = newStatusNum
        row.isEditing = false
        row.editingField = null
      } else {
        row._statusStr = statusMap[row.status]
        ElMessageBox.alert('Failed to update the task. Please try again.', 'Error', {
          confirmButtonText: 'OK',
          type: 'error'
        })
      }
    } else if (field === 'assignee') {
      if (row._assigneeId === row.assigneeId) {
        row.isEditing = false
        row.editingField = null
        return
      }
      const projectId = route.params.id
      const res = await request.put(`/projects/${projectId}/tasks/${row.id}`, {
        assigneeId: Number(row._assigneeId),  // 确保assignee也是number类型
        userId: localStorage.getItem('userId')
      })
      if (res.code === 1) {
        row.assigneeId = row._assigneeId
        row.isEditing = false
        row.editingField = null
      } else {
        ElMessageBox.alert('Failed to update the assignee. Please try again.', 'Error', {
          confirmButtonText: 'OK',
          type: 'error'
        })
      }
    } else {
      if (row[field] === row.originalValue) {
        row.isEditing = false
        row.editingField = null
        return
      }
      const projectId = route.params.id
      const res = await request.put(`/projects/${projectId}/tasks/${row.id}`, {
        [field]: row[field],
        userId: localStorage.getItem('userId')
      })
      if (res.code === 1) {
        row.isEditing = false
        row.editingField = null
      } else {
        row[field] = row.originalValue
        ElMessageBox.alert(
          'Failed to update the task. Please try again.',
          'Error',
          {
            confirmButtonText: 'OK',
            type: 'error'
          }
        )
      }
    }
  } catch (error) {
    row[field] = row.originalValue
    ElMessageBox.alert(
      'An error occurred while updating the task. Please try again.',
      'Error',
      {
        confirmButtonText: 'OK',
        type: 'error'
      }
    )
  } finally {
    row.isEditing = false
    row.editingField = null
  }
}

// 新增子任务方法
const handleAddSubTask = async (parentTask, iteration) => {
  if (parentTask.type !== 'task') return; // 只允许主任务添加
  
  try {
    const projectId = route.params.id
    // 构造子任务数据
    const newSubtaskData = {
      creatorId: localStorage.getItem('userId'),
      content: 'Double click to edit subtask content',
      taskId: parentTask.id,  // 子任务使用父任务的id
      status: 0,
      assigneeId: 0,
      createTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
      iterationId: iteration.id  // 使用传入的iteration的id
    }
    
    // 使用相同的API端口
    const res = await request.post(`/projects/${projectId}/tasks`, newSubtaskData)
    
    if (res.code === 1 && res.data) {
      // 后端返回新子任务，添加到前端
      if (!parentTask.children) parentTask.children = []
      parentTask.children.push(res.data)
    } else {
      ElMessageBox.alert('Failed to add subtask, please try again', 'Error', {
        confirmButtonText: 'OK',
        type: 'error'
      })
    }
  } catch (error) {
    ElMessageBox.alert('An error occurred while adding a subtask, please try again', 'Error', {
      confirmButtonText: 'OK',
      type: 'error'
    })
  }
};

const statusMap = { 0: 'TO DO', 1: 'IN PROGRESS', 2: 'DONE' }
const statusReverseMap = { 'TO DO': 0, 'IN PROGRESS': 1, 'DONE': 2 }

</script>

<style scoped>
.backlog-container {
  background: #fff;
  padding: 24px;
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.backlog-top {
  margin-bottom: 24px;
  text-align: center;
}

.step-bar {
  margin-bottom: 16px;
}

.step-btns {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 32px;
}

.backlog-content {
  flex: 1 1 auto;
  overflow-y: auto;
  min-height: 0;
  border-radius: 12px;
  background-color: #fafafa;
  padding: 24px;
}

.iterations-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.iteration-section {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 16px;
}

.interation-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.interation-header span {
  font-size: 1.5em;
  color: #303133;
}

.step-bar :deep(.el-step__head.is-process) {
  color: orange !important;
  border-color: orange !important;
}

.step-bar :deep(.el-step__title.is-process) {
  color: orange !important;
}

.step-bar :deep(.el-step__icon.is-process) {
  background-color: orange !important;
  border-color: orange !important;
  color: #fff !important;
}

.new-task-container {
  width: 100%;
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.new-task-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 40px;
  font-size: 14px;
  color: #333;
  background-color: #fff;
  border: 1px solid #333;
  transition: all 0.3s ease;
}

.new-task-btn:hover {
  color: #409EFF;
  border-color: #409EFF;
  background-color: rgba(64, 158, 255, 0.1);
}

.new-task-btn .el-icon {
  font-size: 16px;
}

.editable-cell {
  cursor: pointer;
  padding: 4px 0;
}

.editable-cell:hover {
  background-color: rgba(64, 158, 255, 0.1);
}

.editable-cell .el-input {
  width: 100%;
  height: 30px;
  font-size: 14px;
}

.type-inline {
  display: inline-flex;
  align-items: center;
}
.add-subtask-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  border: 1px solid black;
  border-radius: 4px;
  color: black;
  font-size: 16px;
  background: #fff;
  cursor: pointer;
  margin-left: 6px;
  transition: background 0.2s, color 0.2s, border-color 0.2s;
}
.add-subtask-btn:hover {
  background: #409EFF;
  color: #fff;
  border-color: #409EFF;
}

.status-cell {
  display: flex;
  align-items: center;
  justify-content: center;
}
.status-tag {
  min-width: 70px;
  text-align: center;
  font-size: 13px;
  letter-spacing: 1px;
}

.assignee-cell {
  display: flex;
  align-items: center;
  justify-content: center;
}
.assignee-option {
  display: flex;
  align-items: center;
  gap: 6px;
}
.assignee-avatar {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 4px;
}
.assignee-display {
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>
