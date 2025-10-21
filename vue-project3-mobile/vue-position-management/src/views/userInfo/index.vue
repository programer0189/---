<script setup>
import { ref, onMounted } from 'vue'
import { userAPI } from '@/api/chat.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { getCurrentUser, setUserAuth } from '@/utils/auth.js'

const router = useRouter()

// 用户信息
const userInfo = ref({
  username: '',
  avatar: '',
  gender: '',
  phone: '',
  age: '',
  createTime: '',
  email: ''
})



// 默认头像
const defaultAvatar = "https://myprojrct-javaweb.oss-cn-beijing.aliyuncs.com/001.jpg"
// 获取用户信息
const getUserInfo = async () => {
  try {
    const loginUser = getCurrentUser() || {}
    if (loginUser.id) {
      const response = await userAPI.getUserInfo(loginUser.id)
      if (response.code === 1) {
        // 直接设置用户信息，避免闪烁
        userInfo.value = response.data
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '未知'
  const date = new Date(time)
  return date.toLocaleDateString('zh-CN')
}

// 格式化性别
const formatGender = (gender) => {
  if (!gender) return '未设置'
  return gender === 'male' ? '男' : gender === 'female' ? '女' : gender
}

// 跳转到我的发帖页面
const goToUserPosts = () => {
  router.push('/userPosts')
}


// 退出登录
const logout = async () => {
  await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  localStorage.removeItem('loginUser')
  ElMessage.success('退出登录成功')
  router.push('/login')
}

// 注销账号
const deleteAccount = async () => {
  const loginUser = getCurrentUser() || {}
  if (!loginUser.id) {
    ElMessage.error('用户信息不存在')
    return
  }

  await ElMessageBox.confirm(
    '注销账号将永久删除您的所有数据，此操作不可恢复！确定要继续吗？', 
    '危险操作', 
    {
      confirmButtonText: '确定注销',
      cancelButtonText: '取消',
      type: 'error',
      confirmButtonClass: 'el-button--danger'
    }
  )
  
  const response = await userAPI.deleteUser(loginUser.id)
  if (response.code === 1) {
    ElMessage.success('账号注销成功')
    localStorage.removeItem('loginUser')
    router.push('/login')
  } else {
    ElMessage.error(response.msg || '账号注销失败')
  }
}

// 页面加载时获取用户信息
onMounted(async () => {
  // 异步获取用户信息
  await getUserInfo()
  // 更新用户信息后，同步到本地存储
  setUserAuth(getCurrentUser())
})


</script>

<template>
  <div class="userInfo-container">
    <!-- 主要内容 -->
    <div class="userInfo-content">
      <!-- 用户头像和基本信息 -->
      <div class="userInfo-header">
        <div class="avatar-section">
          <img 
            :src="userInfo.avatar || defaultAvatar" 
            :alt="userInfo.username || '用户头像'"
            class="user-avatar"
        
          />
        </div>
        <div class="user-basic-info">
          <h2 class="username">
            <span v-if="userInfo.username">{{ userInfo.username }}</span>
       
          </h2>
          <p class="user-id">
            <span v-if="userInfo.id">UID: {{ userInfo.id }}</span>
         
          </p>
        </div>
      </div>

      <!-- 用户详细信息 -->
      <div class="userInfo-details">
        <div class="info-section">
          <h3 class="section-title">基本信息</h3>
          
          <!-- 用户详细信息 -->
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">性别</span>
              <span class="info-value">
                <span v-if="userInfo.gender">{{ formatGender(userInfo.gender) }}</span>
              
              </span>
            </div>
            <div class="info-item">
              <span class="info-label">手机号</span>
              <span class="info-value">
                <span v-if="userInfo.phone">{{ userInfo.phone }}</span>
             
              </span>
            </div>
            <div class="info-item">
              <span class="info-label">注册时间</span>
              <span class="info-value">
                <span v-if="userInfo.createTime">{{ formatTime(userInfo.createTime) }}</span>
                <span v-else class="skeleton-text">加载中...</span>
              </span>
            </div>
            <div class="info-item" @click="goToUserPosts">
              <span class="info-label">我的发帖</span>
                             <span class="info-value">查看发帖记录</span>
            </div>
          </div>
        </div>
      </div>


      

      <!-- 操作按钮区域 -->
      <div class="action-section">
        <div class="action-buttons">
          <button @click="logout" class="logout-btn">
            <span class="btn-icon">🚪</span>
            退出登录
          </button>
          <button @click="deleteAccount" class="delete-btn">
            <span class="btn-icon">🗑️</span>
            注销账号
          </button>
        </div>
        <p class="action-tip">注销账号将永久删除您的所有数据，请谨慎操作</p>
      </div>

     
    </div>
 
  </div>
</template>

<style scoped>
/* 骨架屏加载效果 */
.skeleton-text {
  color: #c0c4cc;
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
  border-radius: 4px;
  padding: 2px 4px;
  min-width: 60px;
  display: inline-block;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}

/* 加载状态样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  gap: 20px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #284aa7;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.loading-text {
  color: #6c757d;
  font-size: 16px;
  margin: 0;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.userInfo-container {
  min-height: 100vh;
  background-color: #ffffff;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 已登录状态样式 */
.userInfo-content {
  max-width: 100vw;
  margin: 0 auto;
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.userInfo-header {
  background: #f8f9fa;
  color: #333;
  padding: 40px 30px;
  text-align: center;
  border-bottom: 1px solid #e9ecef;
}

.avatar-section {
  margin-bottom: 20px;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid #e9ecef;
  object-fit: cover;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.user-basic-info {
  text-align: center;
}

.username {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #2c3e50;
}

.user-id {
  font-size: 14px;
  color: #6c757d;
  margin: 0;
}

.userInfo-details {
  padding: 30px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #e9ecef;
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
  cursor: pointer;
}

.info-item:hover {
  background-color: #f8f9fa;
  border-color: #dee2e6;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.info-label {
  font-weight: 500;
  color: #6c757d;
  font-size: 14px;
}

.info-value {
  font-weight: 600;
  color: #495057;
  font-size: 14px;
}



/* 操作按钮区域 */
.action-section {
  padding: 30px;
  border-top: 1px solid #e9ecef;
  background-color: #f8f9fa;
}

.action-buttons {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  justify-content: center;
}

.logout-btn, .delete-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}



.logout-btn {
  background: #6c757d;
  color: white;
}

.logout-btn:hover {
  background: #5a6268;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(108, 117, 125, 0.3);
}

.delete-btn {
  background: #dc3545;
  color: white;
}

.delete-btn:hover {
  background: #c82333;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(220, 53, 69, 0.3);
}

.btn-icon {
  font-size: 16px;
}

.action-tip {
  text-align: center;
  color: #999;
  font-size: 12px;
  margin: 0;
  font-style: italic;
}





/* 响应式设计 */
@media (max-width: 768px) {
  .userInfo-container {
    padding: 10px;
  }
  
  .userInfo-content {
    border-radius: 12px;
  }
  
  .userInfo-header {
    padding: 30px 20px;
  }
  
  .user-avatar {
    width: 100px;
    height: 100px;
  }
  
  .username {
    font-size: 24px;
  }
  
  .userInfo-details {
    padding: 20px;
  }
  
  .action-section {
    padding: 20px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 15px;
    align-items: center;
  }
  
  .logout-btn, .delete-btn {
    width: auto;
    min-width: 120px;
    justify-content: center;
  }
  

}

@media (max-width: 480px) {
  .userInfo-header {
    padding: 25px 15px;
  }
  
  .user-avatar {
    width: 80px;
    height: 80px;
  }
  
  .username {
    font-size: 20px;
  }
  
  .userInfo-details {
    padding: 15px;
  }
  
  .action-section {
    padding: 15px;
  }
  
  .logout-btn, .delete-btn {
    min-width: 100px;
    font-size: 13px;
    padding: 10px 20px;
  }
  

}
</style>