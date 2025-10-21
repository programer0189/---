import { createRouter, createWebHistory } from 'vue-router'

// 管理端视图
import LayoutView from '@/views/layout/index.vue'
import IndexView from '@/views/index/index.vue'
import ClazzView from '@/views/news/index.vue'
import DeptView from '@/views/dept/index.vue'
import EmpView from '@/views/emp/index.vue'
import LogView from '@/views/log/index.vue'
import noticeView from '@/views/notice/index.vue'
import EmpReportView from '@/views/report/emp/index.vue'
import chatlogView from '@/views/chatlog/index.vue'

import navigateView from '@/views/navigate/index.vue'
import mapInfoView from '@/views/mapInfo/index.vue'
import mapReportView from '@/views/mapreport/index.vue'
import LoginView from '@/views/login/index.vue'
import UserView from '@/views/user/index.vue'
import CommentView from '@/views/comment/index.vue'
import MobilePostsView from '@/views/mobile/posts/index.vue'
import MobileUsersView from '@/views/mobile/users/index.vue'

// 用户端视图
import HomeView from '@/userviews/home/index.vue'
import UserMapView from '@/userviews/mapmessage/index.vue'
import UserNavigateView from '@/userviews/navigate/index.vue'
import UserNewsView from '@/userviews/news/index.vue'
import chatView from '@/userviews/chat/index.vue'

// 聊天界面子组件
import ConfessionView from '@/userviews/chat/components/Confession.vue'
import ExpressView from '@/userviews/chat/components/Express.vue'
import StudyView from '@/userviews/chat/components/Study.vue'
import ActivityView from '@/userviews/chat/components/Activity.vue'
import LostView from '@/userviews/chat/components/Lost.vue'
import ChatView from '@/userviews/chat/components/Chat.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 登录页面
    { path: '/login', component: LoginView },
    
    // 根路径重定向 - 根据用户角色决定重定向到哪个页面
    { path: '/', redirect: '/login' },
    
    // 管理端路由 - 设置返回目标为admin/index
    {
      path: '/admin',
      component: LayoutView,
      redirect: '/admin/index',
      meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' },
      children: [
        { path: 'index', component: IndexView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'news', component: ClazzView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'notice', component: noticeView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'dept', component: DeptView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'emp', component: EmpView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'log', component: LogView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'empReport', component: EmpReportView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'mapReport', component: mapReportView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
      
        { path: 'navigate', component: navigateView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'mapInfo', component: mapInfoView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path:  'user', component: UserView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'chatlog', component: chatlogView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'comment', component: CommentView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        // 移动端管理路由
        { path: 'mobile/posts', component: MobilePostsView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'mobile/comments', component: CommentView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } },
        { path: 'mobile/users', component: MobileUsersView, meta: { requiresAuth: true, requiresAdmin: true, returnTo: '/admin/index' } }
      ]
    },

    // 用户端路由 - 设置返回目标为home
    { 
      path: '/home', 
      component: HomeView, 
      meta: { requiresAuth: true, requiresUser: true, returnTo: '/home' } 
    },
    { 
      path: '/home/mapmessage', 
      component: UserMapView, 
      meta: { requiresAuth: true, requiresUser: true, returnTo: '/home' } 
    },
    { 
      path: '/home/navigate', 
      component: UserNavigateView, 
      meta: { requiresAuth: true, requiresUser: true, returnTo: '/home' } 
    },
    { 
      path: '/home/news', 
      component: UserNewsView, 
      meta: { requiresAuth: true, requiresUser: true, returnTo: '/home' } 
    },
   
    { 
      path: '/home/chat', 
      component: chatView,
      meta: { requiresAuth: true, requiresUser: true, returnTo: '/home' },
      children: [
        { path: '', redirect: '/home/chat/confession' },
        { path: 'confession', component: ConfessionView },
        { path: 'express', component: ExpressView },
        { path: 'study', component: StudyView },
        { path: 'activity', component: ActivityView },
        { path: 'lost', component: LostView },
        { path: 'chat', component: ChatView }
      ]
    },
   
    // 移除所有旧路径重定向，避免干扰用户端导航
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 如果访问登录页面，直接通过
  if (to.path === '/login') {
    next()
    return
  }

  // 检查是否已登录
  const loginUser = localStorage.getItem('loginUser')
  if (!loginUser) {
    next('/login')
    return
  }

  try {
    const user = JSON.parse(loginUser)
    
    // 检查访问管理端路由的权限
    if (to.meta.requiresAdmin && user.role !== 'admin') {
      // 普通用户尝试访问管理端，重定向到用户端首页
      next('/home')
      return
    }
    
    // 检查访问用户端路由的权限
    if (to.meta.requiresUser && user.role !== 'user') {
      // 管理员尝试访问用户端，重定向到管理端首页
      next('/admin/index')
      return
    }
    
    // 权限检查通过
    next()
  } catch (error) {
    // 解析用户信息失败，清除本地存储并重定向到登录页
    localStorage.removeItem('loginUser')
    next('/login')
  }
})

// 全局后置钩子 - 处理返回逻辑
router.afterEach((to, from) => {
  // 为所有页面设置返回目标
  if (to.meta.returnTo) {
    // 将返回目标存储在sessionStorage中，供页面使用
    sessionStorage.setItem('returnTo', to.meta.returnTo)
  }
})

export default router
