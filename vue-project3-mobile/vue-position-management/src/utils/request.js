import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { getCurrentUser, clearUserAuth } from './auth.js'

//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 800000
})

// 请求拦截器
request.interceptors.request.use(
  (config) => { //成功回调
    const user = getCurrentUser()
    if (user && user.token) {
      //请求头添加token
      config.headers.token = user.token
    }
    return config
  },
  (error) => { //失败回调
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 未授权，清除用户信息并跳转到登录页
          ElMessage.error("登录已过期，请重新登录")
          clearUserAuth()
          router.push('/login')
          break
        case 403:
          ElMessage.error("没有权限访问")
          break
        case 500:
          ElMessage.error("服务器内部错误")
          break
        default:
          ElMessage.error("请求失败")
      }
    } else {
      ElMessage.error("网络错误，请检查网络连接")
    }
    return Promise.reject(error)
  }
)

export default request