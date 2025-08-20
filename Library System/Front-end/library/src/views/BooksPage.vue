<template>
  <div class="books-page">
    <!-- Header -->
    <AppHeader />
    
    <!-- Navigator -->
    <AppNavigator />
    
    <!-- 主要内容区域 -->
    <div class="container mt-4">
      <div class="row">
        <div class="col-12">
          <!-- 页面标题和操作按钮 -->
          <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">
              <i class="bi bi-book me-2"></i>图书管理
            </h2>
            <button class="btn btn-primary" @click="showAddModal = true">
              <i class="bi bi-plus-circle me-2"></i>新增图书
            </button>
          </div>

          <!-- 搜索和筛选区域 -->
          <div class="card mb-4">
            <div class="card-body">
              <div class="row g-3">
                <div class="col-md-4">
                  <input 
                    type="text" 
                    class="form-control" 
                    placeholder="搜索图书名称、作者或ISBN..."
                    v-model="searchQuery"
                    @input="handleSearch"
                  >
                </div>
                <div class="col-md-3">
                  <select class="form-select" v-model="selectedCategory" @change="handleSearch">
                    <option value="">所有分类</option>
                    <option v-for="category in categories" :key="category.id" :value="category.id">
                      {{ category.name }}
                    </option>
                  </select>
                </div>
                <div class="col-md-3">
                  <select class="form-select" v-model="selectedStatus" @change="handleSearch">
                    <option value="">所有状态</option>
                    <option value="available">可借阅</option>
                    <option value="borrowed">已借出</option>
                  </select>
                </div>
                <div class="col-md-2">
                  <button class="btn btn-outline-secondary w-100" @click="resetFilters">
                    <i class="bi bi-arrow-clockwise me-1"></i>重置
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 图书列表 -->
          <div class="row" v-if="books.length > 0">
            <div class="col-lg-3 col-md-4 col-sm-6 mb-4" v-for="book in books" :key="book.id">
              <div class="card h-100 book-card">
                <div class="card-body">
                  <div class="d-flex justify-content-between align-items-start mb-2">
                    <h6 class="card-title text-truncate mb-0" :title="book.name">{{ book.name }}</h6>
                    <div class="dropdown">
                      <button class="btn btn-sm btn-outline-secondary" type="button" @click="toggleDropdown(book.id)">
                        <i class="bi bi-three-dots-vertical"></i>
                      </button>
                      <ul class="dropdown-menu" :class="{ show: activeDropdown === book.id }">
                        <li><a class="dropdown-item" href="#" @click="editBook(book)">
                          <i class="bi bi-pencil me-2"></i>编辑
                        </a></li>
                        <li><a class="dropdown-item text-danger" href="#" @click="deleteBook(book.id)">
                          <i class="bi bi-trash me-2"></i>删除
                        </a></li>
                      </ul>
                    </div>
                  </div>
                  
                  <p class="card-text text-muted small mb-2">
                    <i class="bi bi-person me-1"></i>{{ book.author }}
                  </p>
                  
                  <p class="card-text text-muted small mb-2">
                    <i class="bi bi-upc-scan me-1"></i>{{ book.isbn }}
                  </p>
                  
                  <p class="card-text text-muted small mb-2">
                    <i class="bi bi-tag me-1"></i>{{ book.category_name }}
                  </p>
                  
                  <p class="card-text text-muted small mb-3">
                    <i class="bi bi-building me-1"></i>{{ book.publisher }}
                  </p>
                  
                  <div class="d-flex justify-content-between align-items-center">
                    <span class="badge" :class="book.status === 'available' ? 'bg-success' : 'bg-warning'">
                      {{ book.status === 'available' ? '可借阅' : '已借出' }}
                    </span>
                    <small class="text-muted">{{ formatDate(book.publish_date) }}</small>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div class="text-center py-5" v-else-if="!loading">
            <i class="bi bi-book display-1 text-muted"></i>
            <h4 class="mt-3 text-muted">暂无图书数据</h4>
            <p class="text-muted">点击上方"新增图书"按钮添加第一本图书</p>
          </div>

          <!-- 加载状态 -->
          <div class="text-center py-5" v-if="loading">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">加载中...</span>
            </div>
            <p class="mt-2 text-muted">正在加载图书数据...</p>
          </div>

          <!-- 分页 -->
          <nav v-if="totalPages > 1" aria-label="图书分页">
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

    <!-- 新增/编辑图书模态框 -->
    <div class="modal fade" id="bookModal" tabindex="-1" ref="bookModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              <i class="bi bi-book me-2"></i>
              {{ isEditing ? `编辑图书：${bookForm.name}` : '新增图书' }}
            </h5>
            <button type="button" class="btn-close" @click="closeBookModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveBook">
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label">图书名称 <span class="text-danger">*</span></label>
                  <input 
                    type="text" 
                    class="form-control" 
                    v-model="bookForm.name"
                    required
                    @keydown.enter.prevent="saveBook"
                  >
                </div>
                <div class="col-md-6">
                  <label class="form-label">作者 <span class="text-danger">*</span></label>
                  <input 
                    type="text" 
                    class="form-control" 
                    v-model="bookForm.author"
                    required
                    @keydown.enter.prevent="saveBook"
                  >
                </div>
                <div class="col-md-6">
                  <label class="form-label">ISBN <span class="text-danger">*</span></label>
                  <input 
                    type="text" 
                    class="form-control" 
                    v-model="bookForm.isbn"
                    required
                    @keydown.enter.prevent="saveBook"
                  >
                </div>
                <div class="col-md-6">
                  <label class="form-label">分类 <span class="text-danger">*</span></label>
                  <select class="form-select" v-model="bookForm.category" required @keydown.enter.prevent="saveBook">
                    <option value="">请选择分类</option>
                    <option v-for="category in categories" :key="category.id" :value="category.id">
                      {{ category.name }}
                    </option>
                  </select>
                </div>
                <div class="col-md-6">
                  <label class="form-label">出版社</label>
                  <input 
                    type="text" 
                    class="form-control" 
                    v-model="bookForm.publisher"
                    @keydown.enter.prevent="saveBook"
                  >
                </div>
                <div class="col-md-6">
                  <label class="form-label">出版日期</label>
                  <input 
                    type="date" 
                    class="form-control" 
                    v-model="bookForm.publish_date"
                    @keydown.enter.prevent="saveBook"
                  >
                </div>
                <div class="col-12">
                  <label class="form-label">图书描述</label>
                  <textarea 
                    class="form-control" 
                    rows="3"
                    v-model="bookForm.description"
                    @keydown.enter.prevent="saveBook"
                  ></textarea>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <div class="text-muted small me-auto">
              <i class="bi bi-info-circle me-1"></i>提示：按Enter键可快速保存
            </div>
            <button type="button" class="btn btn-secondary" @click="closeBookModal">取消</button>
            <button type="button" class="btn btn-primary" @click="saveBook" :disabled="saving">
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
            <p>确定要删除这本图书吗？此操作不可撤销。</p>
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
import { getBooks } from '@/api/books'
import request from '@/utils/request'

// 获取Bootstrap Modal类
const Modal = window.bootstrap ? window.bootstrap.Modal : null

// 响应式数据
const books = ref([])
const categories = ref([])
const loading = ref(false)
const saving = ref(false)
const deleting = ref(false)
const showAddModal = ref(false)
const isEditing = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)
const totalCount = ref(0)
const searchQuery = ref('')
const selectedCategory = ref('')
const selectedStatus = ref('')
const bookToDelete = ref(null)
const activeDropdown = ref(null)

// 表单数据
const bookForm = reactive({
  id: null,
  name: '',
  author: '',
  isbn: '',
  category: '',
  publisher: '',
  publish_date: '',
  description: ''
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
let bookModal = null
let deleteModal = null

// 方法
const fetchBooks = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      page_size: pageSize.value
    }
    
    if (searchQuery.value) {
      params.search = searchQuery.value
    }
    if (selectedCategory.value) {
      params.category = selectedCategory.value
    }
    if (selectedStatus.value) {
      params.status = selectedStatus.value
    }
    
    const response = await getBooks(params)
    books.value = response.results || response
    totalCount.value = response.count || response.length
  } catch (error) {
    console.error('获取图书列表失败:', error)
    alert('获取图书列表失败')
  } finally {
    loading.value = false
  }
}

const fetchCategories = async () => {
  try {
    const response = await request.get('/categories')
    categories.value = response.results || response
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchBooks()
}

const resetFilters = () => {
  searchQuery.value = ''
  selectedCategory.value = ''
  selectedStatus.value = ''
  currentPage.value = 1
  fetchBooks()
}

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    fetchBooks()
  }
}

const editBook = (book) => {
  isEditing.value = true
  // 确保日期格式正确
  const bookData = {
    ...book,
    publish_date: book.publish_date ? book.publish_date.split('T')[0] : ''
  }
  Object.assign(bookForm, bookData)
  activeDropdown.value = null
  
  // 使用新的showModal方法显示模态框
  bookModal = showModal('bookModal')
  
  if (bookModal) {
    // 延迟聚焦到第一个输入框
    setTimeout(() => {
      const firstInput = document.querySelector('#bookModal input[type="text"]')
      if (firstInput) {
        firstInput.focus()
      }
    }, 300)
  } else {
    console.error('无法显示模态框')
    alert('系统错误：无法显示模态框')
  }
}

const saveBook = async () => {
  // 表单验证
  if (!bookForm.name || !bookForm.author || !bookForm.isbn || !bookForm.category) {
    alert('请填写必填字段')
    return
  }
  
  saving.value = true
  try {
    if (isEditing.value) {
      await request.put(`/books/${bookForm.id}/`, bookForm)
      alert('图书更新成功！')
    } else {
      await request.post('/books/', bookForm)
      alert('图书添加成功！')
    }
    
    // 隐藏模态框
    if (bookModal && typeof bookModal.hide === 'function') {
      bookModal.hide()
    } else {
      // 备用方案：直接隐藏模态框
      const modalElement = document.getElementById('bookModal')
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
    fetchBooks()
  } catch (error) {
    console.error('保存图书失败:', error)
    alert('保存图书失败：' + (error.message || '未知错误'))
  } finally {
    saving.value = false
  }
}

const deleteBook = (bookId) => {
  bookToDelete.value = bookId
  activeDropdown.value = null
  deleteModal = showModal('deleteModal')
  
  if (!deleteModal) {
    console.error('无法显示删除模态框')
    alert('系统错误：无法显示删除模态框')
  }
}

const confirmDelete = async () => {
  deleting.value = true
  try {
    await request.delete(`/books/${bookToDelete.value}/`)
    
    // 隐藏模态框
    if (deleteModal && typeof deleteModal.hide === 'function') {
      deleteModal.hide()
    } else {
      // 备用方案：直接隐藏模态框
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
    
    fetchBooks()
    alert('图书删除成功')
  } catch (error) {
    console.error('删除图书失败:', error)
    alert('删除图书失败')
  } finally {
    deleting.value = false
    bookToDelete.value = null
  }
}

const resetForm = () => {
  isEditing.value = false
  Object.assign(bookForm, {
    id: null,
    name: '',
    author: '',
    isbn: '',
    category: '',
    publisher: '',
    publish_date: '',
    description: ''
  })
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const toggleDropdown = (bookId) => {
  activeDropdown.value = activeDropdown.value === bookId ? null : bookId
}

// 监听器
watch(showAddModal, (newVal) => {
  if (newVal) {
    resetForm()
    bookModal = showModal('bookModal')
    
    if (bookModal) {
      // 延迟聚焦到第一个输入框
      setTimeout(() => {
        const firstInput = document.querySelector('#bookModal input[type="text"]')
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
  // 监听模态框关闭事件
  const bookModalElement = document.getElementById('bookModal')
  if (bookModalElement) {
    bookModalElement.addEventListener('hidden.bs.modal', resetForm)
  }
  
  // 添加点击外部区域关闭下拉菜单的事件监听器
  document.addEventListener('click', (event) => {
    if (!event.target.closest('.dropdown')) {
      activeDropdown.value = null
    }
  })
  
  // 获取数据
  fetchCategories()
  fetchBooks()
})

// 备用的模态框显示方法
const showModal = (modalId) => {
  // 先移除所有残留遮罩，防止多次打开残留
  document.querySelectorAll('.modal-backdrop').forEach(b => b.remove())
  const modalElement = document.getElementById(modalId)
  if (modalElement) {
    // 尝试使用Bootstrap Modal API
    if (Modal) {
      const modal = new Modal(modalElement)
      modal.show()
      return modal
    } else {
      // 备用方案：直接设置显示样式
      modalElement.style.display = 'block'
      modalElement.classList.add('show')
      modalElement.setAttribute('aria-hidden', 'false')
      // 添加背景遮罩
      const backdrop = document.createElement('div')
      backdrop.className = 'modal-backdrop fade show'
      document.body.appendChild(backdrop)
      return { hide: () => {
        modalElement.style.display = 'none'
        modalElement.classList.remove('show')
        modalElement.setAttribute('aria-hidden', 'true')
        // 关闭时移除所有遮罩
        document.querySelectorAll('.modal-backdrop').forEach(b => b.remove())
      }}
    }
  }
  return null
}

const closeBookModal = () => {
  // 隐藏模态框
  if (bookModal && typeof bookModal.hide === 'function') {
    bookModal.hide()
  } else {
    // 备用方案：直接隐藏模态框
    const modalElement = document.getElementById('bookModal')
    if (modalElement) {
      modalElement.style.display = 'none'
      modalElement.classList.remove('show')
      modalElement.setAttribute('aria-hidden', 'true')
      // 关闭时移除所有遮罩
      document.querySelectorAll('.modal-backdrop').forEach(b => b.remove())
    }
  }
  // 重置表单
  resetForm()
}
</script>

<style scoped>
.books-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.book-card {
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid #dee2e6;
}

.book-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: #212529;
}

.card-text {
  font-size: 0.85rem;
}

.badge {
  font-size: 0.75rem;
}

.pagination .page-link {
  color: #0d6efd;
}

.pagination .page-item.active .page-link {
  background-color: #0d6efd;
  border-color: #0d6efd;
}

.pagination .page-item.disabled .page-link {
  color: #6c757d;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .col-sm-6 {
    flex: 0 0 50%;
    max-width: 50%;
  }
}

@media (max-width: 576px) {
  .col-sm-6 {
    flex: 0 0 100%;
    max-width: 100%;
  }
}

/* 下拉菜单样式 */
.dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  z-index: 1000;
  min-width: 120px;
}

.dropdown-menu.show {
  display: block;
}

/* 模态框样式优化 */
.modal-content {
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.modal-header {
  border-bottom: 1px solid #e9ecef;
  background-color: #f8f9fa;
  border-radius: 12px 12px 0 0;
}

.modal-footer {
  border-top: 1px solid #e9ecef;
  background-color: #f8f9fa;
  border-radius: 0 0 12px 12px;
}

.form-control:focus,
.form-select:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

/* 编辑按钮悬停效果 */
.dropdown-item:hover {
  background-color: #e9ecef;
}

.dropdown-item.text-danger:hover {
  background-color: #f8d7da;
  color: #721c24 !important;
}

/* 模态框备用显示样式 */
.modal.show {
  display: block !important;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1040;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-backdrop.fade {
  opacity: 0;
  transition: opacity 0.15s linear;
}

.modal-backdrop.show {
  opacity: 1;
}
</style> 