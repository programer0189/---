import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn, clearUserAuth } from '@/utils/auth.js'

import LayoutView from '@/views/layout/index.vue'
import confessionView from '@/views/confession/index.vue'
import activityView from '@/views/activity/index.vue'
import expressView from '@/views/express/index.vue'
import studyView from '@/views/study/index.vue'
import lostView from '@/views/lost/index.vue'
import chatView from '@/views/chat/index.vue'
import homeView from '@/views/home/index.vue'
import userInfoView from '@/views/userInfo/index.vue'
import loginView from '@/views/login/index.vue'
import UserpostsView from '@/views/userPosts/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/login"
    },
    {
      path: "/login",
      name: 'login',
      component: loginView
    },
    {
      path: "/app",
      name: 'layout',
      component: LayoutView,
      children: [
        {
          path: "/app/home",
          name: 'home',
          component: homeView
        },
        {
          path: "/app/confession",
          name: 'confession',
          component: confessionView
        },
        {
          path: "/app/chat",
          name: 'chat',
          component: chatView
        },
        {
          path: "/app/study",
          name: 'study',
          component: studyView
        },
        {
          path: "/app/lost",
          name: 'lost',
          component: lostView
        },
        {
          path: "/app/express",
          name: 'express',
          component: expressView
        },
        {
          path: "/app/activity",
          name: 'activity',
          component: activityView
        },
        {
          path: "/app/userInfo",
          name: 'userInfo',
          component: userInfoView
        }
      ]
    },
    
    {
      path: '/:pathMatch(.*)*',
      redirect: '/login'
    },
    {
      path: "/userPosts",
      name: 'userPosts',
      component: UserpostsView
    }
  ]
})

// 全局前置守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  // 如果访问登录页面，直接放行
  if (to.path === '/login') {
    // 如果已经登录，重定向到首页
    if (isLoggedIn()) {
      next('/app/home')
      return
    }
    next()
    return
  }

  // 检查用户是否已登录
  if (!isLoggedIn()) {
    // 清除可能损坏的用户信息
    clearUserAuth()
    next('/login')
    return
  }

  // 已登录用户，正常放行
  next()
})

export default router