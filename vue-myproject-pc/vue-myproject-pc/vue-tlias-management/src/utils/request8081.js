import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 创建axios实例对象，用于访问8082端口的后端
const request8081 = axios.create({
  baseURL: '/mobile-api',
  timeout: 600000
})

// 请求拦截器
request8081.interceptors.request.use(
  (config) => { // 成功回调
    const loginUser = JSON.parse(localStorage.getItem('loginUser'))
    if (loginUser && loginUser.token) {
      // 请求头添加token
      config.headers.token = loginUser.token
    }
    return config
  },
  (error) => { // 失败回调
    return Promise.reject(error)
  }
)

// 响应拦截器
request8081.interceptors.response.use(
  (response) => { 
    return response.data
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      ElMessage.error("请先登录")
      router.push('/login')
    } else {
      ElMessage.error("请求失败：" + (error.message || '未知错误'))
    }
    return Promise.reject(error)
  }
)

export default request8081
