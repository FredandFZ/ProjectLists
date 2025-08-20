import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 引入Bootstrap CSS
import 'bootstrap/dist/css/bootstrap.min.css'
// 引入Bootstrap Icons
import 'bootstrap-icons/font/bootstrap-icons.css'
// 引入Bootstrap JS
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

const app = createApp(App)
app.use(router)
app.mount('#app')
