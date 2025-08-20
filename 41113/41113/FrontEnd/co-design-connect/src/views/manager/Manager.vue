<template>
  <div class="manager-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar class="sidebar" />
      <div class="content">
        <div class="not-manager" v-if="!isManager">
          <el-empty
            description="You do not have permission to access this page"
            image-size="120"
          >
            <el-icon style="font-size: 48px; color: #f56c6c; margin-bottom: 16px;">
              <CircleCloseFilled />
            </el-icon>
            <div style="margin-top: 12px;">
              <el-button type="primary" @click="goHome">Back to Home</el-button>
            </div>
          </el-empty>
        </div>
        <div class="manager" v-else>
          <div class="nav-links">
            <router-link to="/manager/invite" class="nav-link" active-class="router-link-active">Invitation</router-link>
            <router-link to="/manager/users" class="nav-link" active-class="router-link-active">User Management</router-link>
            <router-link to="/manager/projects" class="nav-link" active-class="router-link-active">Project Management</router-link>
          </div>
          <div class="content-area">
            <router-view></router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { CircleCloseFilled } from '@element-plus/icons-vue'

const isManager = ref(false)
const mangaerEmail = ref('testfz@qq.com')
const router = useRouter();
const route = useRoute();

const checkManager = () => {
  if (localStorage.getItem('userEmail') === mangaerEmail.value) {
    isManager.value = true
  }
}

onMounted(() => {
  checkManager();
  if (route.path === '/manager') {
    router.push('/manager/invite')
  }
})

function goHome() {
  router.push('/')
}
</script>

<style scoped>
.manager-page {
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
  padding: 0;
  display: flex;
  flex-direction: column;
}

.manager {
  flex: 1;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.nav-links {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 0 20px;
  height: 50px;
  border-bottom: 1px solid #e4e7ed;
  background: #fff;
  width: 100%;
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
  height: 100%;
  background: #fff;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.06);
  overflow: auto;
}

.not-manager {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 24px;
  color: #666;
  background: #fff;
}
</style>



