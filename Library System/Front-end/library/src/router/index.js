import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../views/LoginPage.vue'
import HomePage from '../views/HomePage.vue'
import BooksPage from '../views/BooksPage.vue'
import BorrowPage from '../views/BorrowPage.vue'
import ReadersPage from '../views/ReadersPage.vue'
import StatisticsPage from '../views/StatisticsPage.vue'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: LoginPage
    },
    {
        path: '/home',
        component: HomePage,
        meta: { requiresAuth: true }
    },
    {
        path: '/books',
        component: BooksPage,
        meta: { requiresAuth: true }
    },
    {
        path: '/borrow',
        component: BorrowPage,
        meta: { requiresAuth: true }
    },
    {
        path: '/readers',
        component: ReadersPage,
        meta: { requiresAuth: true }
    },
    {
        path: '/statistics',
        component: StatisticsPage,
        meta: { requiresAuth: true }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const isLoggedIn = localStorage.getItem('isLoggedIn') || sessionStorage.getItem('isLoggedIn')
    
    if (to.meta.requiresAuth && !isLoggedIn) {
        next('/login')
    } else if (to.path === '/login' && isLoggedIn) {
        next('/home')
    } else {
        next()
    }
})

export default router