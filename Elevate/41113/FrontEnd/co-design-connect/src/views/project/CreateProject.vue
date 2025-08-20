<template>
      <div class="content">
        <div class="create-project-container">
          <div class="form-header">
            <router-link to="/my-projects" class="back-link">
              <el-icon><ArrowLeft /></el-icon>
              <span>Back</span>
            </router-link>
            <h1>Create New Project</h1>
          </div>

          <div class="form-container">
            <div class="form-group">
              <label>Project Name</label>
              <el-input 
                v-model="projectName" 
                placeholder="Enter project name"
              />
            </div>

            <div class="form-group">
              <label>Area</label>
              <el-input 
                v-model="area" 
                placeholder="Enter project area"
              />
            </div>

            <div class="form-group">
              <label>Category</label>
              <el-input 
                v-model="category" 
                placeholder="Enter project category"
              />
            </div>

            <div class="form-group">
              <label>Description</label>
              <el-input
                v-model="description"
                type="textarea"
                :rows="6"
                placeholder="Enter project description"
                resize="vertical"
              />
            </div>

            <div class="form-group status-deadline-group">
              <div class="status-container">
                <label>Status</label>
                <el-select v-model="status" placeholder="Select status" class="status-select">
                  <el-option label="Empathise" :value="0" />
                  <el-option label="Discover" :value="1" />
                  <el-option label="Define" :value="2" />
                  <el-option label="Ideate" :value="3" />
                  <el-option label="Prototype" :value="4" />
                  <el-option label="Feedback" :value="5" />
                </el-select>
              </div>

              <div class="deadline-container">
                <label>Deadline</label>
                <el-date-picker
                  v-model="deadline"
                  type="date"
                  placeholder="Select deadline"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  class="deadline-picker"
                />
              </div>
            </div>

            <div class="form-group">
              <label>Project Image</label>
              <el-upload
                class="image-upload"
                action="/projects/create"
                :auto-upload="false"
                :show-file-list="true"
                :limit="1"
                :on-exceed="handleExceed"
                :on-change="handleImageChange"
              >
                <el-button type="primary" class="upload-btn">
                  <el-icon><Upload /></el-icon>
                  <span>Select Image</span>
                </el-button>
                <template #tip>
                  <div class="el-upload__tip">
                    Only one image can be uploaded
                  </div>
                </template>
              </el-upload>
            </div>

            <el-button 
              type="primary" 
              class="submit-btn" 
              @click="createProject"
              :loading="loading"
            >
              {{ loading ? '创建中...' : '创建项目' }}
            </el-button>
          </div>
        </div>
      </div>
</template>

<script setup>
import { ref } from 'vue'
import { ArrowLeft, Upload } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const projectName = ref('')
const area = ref('')
const category = ref('')
const description = ref('')
const status = ref(0)
const deadline = ref('')
const projectImage = ref(null)
const loading = ref(false)

const handleExceed = () => {
  ElMessage.warning('只能上传一张图片')
}

const handleImageChange = (file) => {
  projectImage.value = file.raw
}

const createProject = async () => {
  loading.value = true
  try {
    const formData = new FormData()
    formData.append('name', projectName.value)
    formData.append('creatorId', localStorage.getItem('userId'))
    formData.append('area', area.value)
    formData.append('category', category.value)
    formData.append('description', description.value)
    formData.append('status', status.value)
    formData.append('deadline', deadline.value)
    if (projectImage.value) {
      formData.append('image', projectImage.value)
    }

    const response = await request({
      url: '/projects/create',
      method: 'post',
      data: formData,
      processData: false,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.code === 1) {

      const projectId = response.data.id
      const projectStatus = response.data.status
      const res = await request.post(`/projects/${projectId}/iterations`, {
        projectStatus: projectStatus,
        userId: localStorage.getItem('userId')
      })
      if (res.code === 1) {
        ElMessage.success('项目创建成功')
        router.push(`/my-projects`)
      } else {
        ElMessage.error(res.message || '项目创建失败')
      }
    } else {
      ElMessage.error(response.message || '项目创建失败')
    }
  } catch (error) {
    console.error('创建项目时出错：', error)
    ElMessage.error('创建项目失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
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
}

.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
}

.content {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  padding: 40px 20px;
  background-color: #f5f7fa;
  margin : 0 auto;
}

.create-project-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.form-header {
  margin-bottom: 2rem;
}

.back-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #5e6d82;
  font-size: 0.9rem;
  margin-bottom: 1rem;
  transition: color 0.3s ease;
}

.back-link:hover {
  color: #138366;
}

.back-link .el-icon {
  margin-right: 0.5rem;
}

h1 {
  color: #2c3e50;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0;
}

.form-container {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-top: 1rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  color: #5e6d82;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.el-input {
  width: 100%;
}

:deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

:deep(.el-input__wrapper:hover) {
  border-color: #138366;
}

.status-select {
  width: 100%;
}

.image-upload {
  display: block;
}

.upload-btn {
  width: 100%;
  border-radius: 6px;
  background-color: #f5f7fa;
  border: 1px dashed #d9d9d9;
  color: #5e6d82;
}

.upload-btn:hover {
  border-color: #138366;
  color: #138366;
}

.upload-btn .el-icon {
  margin-right: 0.5rem;
}

.submit-btn {
  width: 100%;
  margin-top: 2rem;
  height: 40px;
  background-color: #138366;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background-color: #138366;
  transform: translateY(-1px);
}

@media (max-width: 768px) {
  .create-project-container {
    padding: 1rem;
  }

  .form-container {
    padding: 1.5rem;
  }
}

.el-textarea {
  width: 100%;
}

:deep(.el-textarea__inner) {
  background-color: #f5f7fa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
  font-family: inherit;
  padding: 12px;
  line-height: 1.5;
}

:deep(.el-textarea__inner:hover) {
  border-color: #138366;
}

:deep(.el-textarea__inner:focus) {
  border-color: #138366;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.deadline-picker {
  width: 100%;
}

:deep(.el-date-editor.el-input) {
  width: 100%;
}

.status-deadline-group {
  display: flex;
  gap: 20px;
  margin-bottom: 1.5rem;
}

.status-container,
.deadline-container {
  flex: 1;
}

.status-select,
.deadline-picker {
  width: 100%;
}

:deep(.el-date-editor.el-input) {
  width: 100%;
}
</style> 