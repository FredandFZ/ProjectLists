import './assets/main.css'

// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 新增路由导入
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


const app = createApp(App)
app.use(router) // 注册路由
app.use(ElementPlus)
app.mount('#app')
