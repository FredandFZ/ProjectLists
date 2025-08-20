import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/views/HomePage.vue'
import LoginPage from '@/views/auth/LoginPage.vue'
import RegisterPage from '@/views/auth/RegisterPage.vue'
import ResetPasswordPage from '@/views/auth/ResetPasswordPage.vue'
import Profile from '@/views/auth/Profile.vue'
import MyProjects from '@/views/project/MyProjects.vue'
import GetMyProjects from '@/views/project/GetMyProjects.vue'
import CreateProject from '@/views/project/CreateProject.vue'
import JoinProject from '@/views/project/JoinProject.vue'
import ProjectDetails from '@/views/project/projectManage/ProjectDetails.vue'
import Channel from '@/views/project/projectManage/Channel.vue'
import Backlog from '@/views/project/projectManage/Backlog.vue'
import WorkPiece from '@/views/project/projectManage/WorkPiece.vue'
import Member from '@/views/project/projectManage/Member.vue'
import FolderDetails from '@/views/project/projectManage/FolderDetails.vue'
import MiroBoard from '@/views/project/projectManage/MiroBoard.vue'
import Map from '@/views/project/projectManage/Map.vue'
import Manager from '@/views/manager/Manager.vue'


const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
  },
  {
    path: '/my-projects',
    name: 'my-projects',
    component: MyProjects,
    children: [
      {
        path: '',
        name: 'get-my-projects',
        component: GetMyProjects
      },
      {
        path: 'create',
        name: 'create-project',
        component: CreateProject
      },
      {
        path: 'join',
        name: 'join-project',
        component: JoinProject
      },
      {
        path: ':id',
        name: 'project-details',
        component: ProjectDetails,
        children: [
          {
            path: 'channel',
            name: 'channel',
            component: Channel
          },
          {
            path: 'backlog',
            name: 'backlog',
            component: Backlog
          },
          {
            path: 'workpiece',
            name: 'workpiece',
            component: WorkPiece
          },
          {
            path: 'member',
            name: 'member',
            component: Member
          },
          {
            path: 'map',
            name: 'map',
            component: Map
          }
        ]
      },
      {
        path: 'workpiece/:projectId/:statusId/:iterationId',
        name: 'workpiece-iteration',
        component: FolderDetails
      },
      {
        path: 'board/:boardId',
        name: 'miro-board',
        component: MiroBoard
      }
    ]
  },
  {
    path: '/manager',
    name: 'manager',
    component: Manager,
    children: [
      {
        path: 'invite',
        name: 'manager-invite',
        component: () => import('@/views/manager/Invitation.vue')
      },
      {
        path: 'users',
        name: 'manager-users',
        component: () => import('@/views/manager/UserManagement.vue')
      },
      {
        path: 'projects',
        name: 'manager-projects',
        component: () => import('@/views/manager/ProjectManagement.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage
  },
  {
    path: '/reset-password',
    name: 'ResetPassword',
    component: ResetPasswordPage
  },
  {
    path: '/profile/:userId',
    name: 'profile',
    component: Profile
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // TODO: 实现实际的用户认证检查
  next()
})

export default router