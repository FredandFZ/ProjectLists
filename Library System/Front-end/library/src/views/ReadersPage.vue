<template>
  <div class="readers-page">
    <!-- Header组件 -->
    <AppHeader />
    
    <!-- 导航栏组件 -->
    <AppNavigator />
    
    <!-- 主要内容区域 -->
    <div class="container mt-4">
      <div class="row">
        <div class="col-12">
          <!-- 页面标题和操作按钮 -->
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">
              <i class="bi bi-people me-2"></i>读者管理
            </h2>
            <button class="btn btn-primary" @click="showAddModal = true">
              <i class="bi bi-plus-circle me-2"></i>新增读者
            </button>
          </div>

          <!-- 搜索和筛选区域 -->
          <div class="card mb-4">
            <div class="card-body">
              <div class="row g-3">
                <div class="col-md-8">
                  <input 
                    type="text" 
                    class="form-control" 
                    placeholder="搜索读者姓名..."
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

          <!-- 读者表格 -->
          <div class="card">
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-hover">
                  <thead class="table-light">
                    <tr>
                      <th>ID</th>
                      <th>姓名</th>
                      <th>邮箱</th>
                      <th>电话</th>
                      <th>注册时间</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="reader in readers" :key="reader.id">
                      <td>{{ reader.id }}</td>
                      <td>
                        <div class="d-flex align-items-center">
                          <div class="avatar me-2">
                            <i class="bi bi-person-circle text-primary"></i>
                          </div>
                          <span class="fw-medium">{{ reader.name }}</span>
                        </div>
                      </td>
                      <td>{{ reader.email || '-' }}</td>
                      <td>{{ reader.phone || '-' }}</td>
                      <td>{{ formatDate(reader.created_at) }}</td>
                      <td>
                        <div class="btn-group" role="group">
                          <button 
                            class="btn btn-sm btn-outline-primary" 
                            @click="editReader(reader)"
                            title="编辑"
                          >
                            <i class="bi bi-pencil"></i>
                          </button>
                          <button 
                            class="btn btn-sm btn-outline-danger" 
                            @click="deleteReaderHandler(reader.id)"
                            title="删除"
                          >
                            <i class="bi bi-trash"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- 空状态 -->
              <div class="text-center py-5" v-if="readers.length === 0 && !loading">
                <i class="bi bi-people display-1 text-muted"></i>
                <h4 class="mt-3 text-muted">暂无读者数据</h4>
                <p class="text-muted">点击上方"新增读者"按钮添加第一个读者</p>
              </div>

              <!-- 加载状态 -->
              <div class="text-center py-5" v-if="loading">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">加载中...</span>
                </div>
                <p class="mt-2 text-muted">正在加载读者数据...</p>
              </div>
            </div>
          </div>

          <!-- 分页 -->
          <nav v-if="totalPages > 1" aria-label="读者分页" class="mt-4">
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

    <!-- 新增/编辑读者模态框 -->
    <div class="modal fade" id="readerModal" tabindex="-1" ref="readerModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="bi bi-person me-2"></i>
              {{ isEditing ? `编辑读者：${readerForm.name}` : '新增读者' }}
            </h5>
            <button type="button" class="btn-close" @click="closeReaderModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveReader">
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label">姓名 <span class="text-danger">*</span></label>
                  <input 
                    type="text" 
                    class="form-control" 
                    v-model="readerForm.name"
                    required
                    @keydown.enter.prevent="saveReader"
                  >
                </div>
                <div class="col-md-6">
                  <label class="form-label">邮箱</label>
                  <input 
                    type="email" 
                    class="form-control" 
                    v-model="readerForm.email"
                    @keydown.enter.prevent="saveReader"
                  >
                </div>
                <div class="col-md-6">
                  <label class="form-label">电话</label>
                  <input 
                    type="tel" 
                    class="form-control" 
                    v-model="readerForm.phone"
                    @keydown.enter.prevent="saveReader"
                  >
                </div>
                <div class="col-12">
                  <label class="form-label">备注</label>
                  <textarea 
                    class="form-control" 
                    rows="3"
                    v-model="readerForm.notes"
                    @keydown.enter.prevent="saveReader"
                  ></textarea>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <div class="text-muted small me-auto">
              <i class="bi bi-info-circle me-1"></i>提示：按Enter键可快速保存
            </div>
            <button type="button" class="btn btn-secondary" @click="closeReaderModal">取消</button>
            <button type="button" class="btn btn-primary" @click="saveReader" :disabled="saving">
              <span v-if="saving" class="spinner-border spinner-border-sm me-2"></span>
              {{ isEditing ? '更新' : '保存' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 删除确认模态框 -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title text-danger">
              <i class="bi bi-exclamation-triangle me-2"></i>确认删除
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
          </div>
          <div class="modal-body">
            <p>确定要删除这个读者吗？此操作不可撤销。</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn btn-danger" @click="confirmDelete" :disabled="deleting">
              <span v-if="deleting" class="spinner-border spinner-border-sm me-2"></span>
              确认删除
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import AppHeader from '@/components/AppHeader.vue'
import AppNavigator from '@/components/AppNavigator.vue'
import { getReaders, addReader, updateReader, deleteReader } from '@/api/readers'

// 获取Bootstrap Modal类
const Modal = window.bootstrap ? window.bootstrap.Modal : null

// 响应式数据
const readers = ref([])
const loading = ref(false)
const saving = ref(false)
const deleting = ref(false)
const showAddModal = ref(false)
const isEditing = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)
const searchQuery = ref('')
const readerToDelete = ref(null)

// 表单数据
const readerForm = reactive({
  id: null,
  name: '',
  email: '',
  phone: '',
  notes: ''
})

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

// 模态框实例
let readerModal = null
let deleteModal = null

// 方法
const fetchReaders = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      page_size: pageSize.value
    }
    
    if (searchQuery.value) {
      params.search = searchQuery.value
    }
    
    const response = await getReaders(params)
    readers.value = response.results || response
    totalCount.value = response.count || response.length
  } catch (error) {
    console.error('获取读者列表失败:', error)
    alert('获取读者列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchReaders()
}

const resetFilters = () => {
  searchQuery.value = ''
  currentPage.value = 1
  fetchReaders()
}

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    fetchReaders()
  }
}

const editReader = (reader) => {
  isEditing.value = true
  Object.assign(readerForm, reader)
  
  readerModal = showModal('readerModal')
  
  if (readerModal) {
    setTimeout(() => {
      const firstInput = document.querySelector('#readerModal input[type="text"]')
      if (firstInput) {
        firstInput.focus()
      }
    }, 300)
  } else {
    console.error('无法显示模态框')
    alert('系统错误：无法显示模态框')
  }
}

const saveReader = async () => {
  if (!readerForm.name) {
    alert('请填写读者姓名')
    return
  }
  
  saving.value = true
  try {
    if (isEditing.value) {
      await updateReader(readerForm.id, readerForm)
      alert('读者信息更新成功！')
    } else {
      await addReader(readerForm)
      alert('读者添加成功！')
    }
    
    if (readerModal && typeof readerModal.hide === 'function') {
      readerModal.hide()
    } else {
      const modalElement = document.getElementById('readerModal')
      if (modalElement) {
        modalElement.style.display = 'none'
        modalElement.classList.remove('show')
        modalElement.setAttribute('aria-hidden', 'true')
        const backdrop = document.querySelector('.modal-backdrop')
        if (backdrop) {
          backdrop.remove()
        }
      }
    }
    resetForm()
    fetchReaders()
  } catch (error) {
    console.error('保存读者失败:', error)
    alert('保存读者失败：' + (error.message || '未知错误'))
  } finally {
    saving.value = false
  }
}

const deleteReaderHandler = (readerId) => {
  readerToDelete.value = readerId
  deleteModal = showModal('deleteModal')
  
  if (!deleteModal) {
    console.error('无法显示删除模态框')
    alert('系统错误：无法显示删除模态框')
  }
}

const confirmDelete = async () => {
  deleting.value = true
  try {
    await deleteReader(readerToDelete.value)
    
    if (deleteModal && typeof deleteModal.hide === 'function') {
      deleteModal.hide()
    } else {
      const modalElement = document.getElementById('deleteModal')
      if (modalElement) {
        modalElement.style.display = 'none'
        modalElement.classList.remove('show')
        modalElement.setAttribute('aria-hidden', 'true')
        const backdrop = document.querySelector('.modal-backdrop')
        if (backdrop) {
          backdrop.remove()
        }
      }
    }
    
    fetchReaders()
    alert('读者删除成功')
  } catch (error) {
    console.error('删除读者失败:', error)
    alert('删除读者失败')
  } finally {
    deleting.value = false
    readerToDelete.value = null
  }
}

const resetForm = () => {
  isEditing.value = false
  Object.assign(readerForm, {
    id: null,
    name: '',
    email: '',
    phone: '',
    notes: ''
  })
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 监听器
watch(showAddModal, (newVal) => {
  if (newVal) {
    resetForm()
    readerModal = showModal('readerModal')
    
    if (readerModal) {
      setTimeout(() => {
        const firstInput = document.querySelector('#readerModal input[type="text"]')
        if (firstInput) {
          firstInput.focus()
        }
      }, 300)
    } else {
      console.error('无法显示模态框')
      alert('系统错误：无法显示模态框')
    }
    showAddModal.value = false
  }
})

// 生命周期
onMounted(() => {
  const readerModalElement = document.getElementById('readerModal')
  if (readerModalElement) {
    readerModalElement.addEventListener('hidden.bs.modal', resetForm)
  }
  
  fetchReaders()
})

// 备用的模态框显示方法
const showModal = (modalId) => {
  document.querySelectorAll('.modal-backdrop').forEach(b => b.remove())
  const modalElement = document.getElementById(modalId)
  if (modalElement) {
    if (Modal) {
      const modal = new Modal(modalElement)
      modal.show()
      return modal
    } else {
      modalElement.style.display = 'block'
      modalElement.classList.add('show')
      modalElement.setAttribute('aria-hidden', 'false')
      const backdrop = document.createElement('div')
      backdrop.className = 'modal-backdrop fade show'
      document.body.appendChild(backdrop)
      return { hide: () => {
        modalElement.style.display = 'none'
        modalElement.classList.remove('show')
        modalElement.setAttribute('aria-hidden', 'true')
        document.querySelectorAll('.modal-backdrop').forEach(b => b.remove())
      }}
    }
  }
  return null
}

const closeReaderModal = () => {
  if (readerModal && typeof readerModal.hide === 'function') {
    readerModal.hide()
  } else {
    const modalElement = document.getElementById('readerModal')
    if (modalElement) {
      modalElement.style.display = 'none'
      modalElement.classList.remove('show')
      modalElement.setAttribute('aria-hidden', 'true')
      document.querySelectorAll('.modal-backdrop').forEach(b => b.remove())
    }
  }
  resetForm()
}
</script>

<style scoped>
.readers-page {
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

.btn-group .btn {
  padding: 0.25rem 0.5rem;
}

.pagination .page-link {
  color: #0d6efd;
}

.modal-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

.form-label {
  font-weight: 500;
  color: #495057;
}

.text-danger {
  color: #dc3545 !important;
}
</style> 