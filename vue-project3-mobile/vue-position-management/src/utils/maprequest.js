import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 创建 axios 实例（指向地图接口后端，代理路径为 /map → localhost:8081）
const mapRequest = axios.create({
  baseURL: '/api', // 对应 vite.config.js 中的代理路径
  timeout: 600000
})

// 请求拦截器
mapRequest.interceptors.request.use(
  (config) => {
    const loginUser = JSON.parse(localStorage.getItem('loginUser'))
    if (loginUser && loginUser.token) {
      config.headers.token = loginUser.token
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
mapRequest.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response.status === 401) {
      ElMessage.error("请先登录")
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default mapRequest
