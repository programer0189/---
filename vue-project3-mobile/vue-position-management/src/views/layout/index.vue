<script setup>
// 这个组件是主组件
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { postAPI } from '@/api/chat.js'
import { useRouter } from 'vue-router'
import { isLoggedIn, getCurrentUser, clearUserAuth } from '@/utils/auth.js'

const showSidebar = ref(false) // 控制侧边栏的显示/隐藏
const showPostDialog = ref(false) // 控制发帖对话框的显示/隐藏

// 发帖表单数据
const postForm = ref({
  title: '',
  content: '',
  type: 1, // 默认选择表白类型
  status: 0, // 默认非匿名
  images: []
})

// 管理上传组件的文件列表
const uploadedFiles = ref([])

// 表单验证规则
const postRules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' },
    { min: 5, message: '内容至少5个字', trigger: 'blur' }
  ]
}

// 帖子类型选项
const postTypes = [
  { value: 1, label: '表白' },
  { value: 2, label: '代取快递' },
  { value: 3, label: '学习交流' },
  { value: 4, label: '活动组织' },
  { value: 5, label: '失物招领' },
  { value: 6, label: '闲聊灌水' }
]

// 从认证工具获取用户信息
const currentUser = ref(null)
const router = useRouter()

// 监听localStorage变化
const checkLoginStatus = () => {
  if (!isLoggedIn()) {
    // 用户已登出，跳转到登录页面
    currentUser.value = null
    router.push('/login')
  }
}

// 添加storage事件监听器
window.addEventListener('storage', checkLoginStatus)

const toggleSidebar = () => {
  showSidebar.value = !showSidebar.value
}

const closeSidebar = () => {
  showSidebar.value = false
}

// 点击外部区域关闭侧边栏
const handleClickOutside = (event) => {
  if (showSidebar.value && !event.target.closest('.sidebar') && !event.target.closest('.sidebar-btn')) {
    showSidebar.value = false
  }
}

// 处理侧边栏链接点击
const handleSidebarClick = (route, event) => {
  if (!isLoggedIn()) {
    event.preventDefault()
    ElMessage.warning('请先登录')
    closeSidebar()
    return
  }
  // 已登录用户正常跳转
  closeSidebar()
}

// 显示发帖对话框
const openPostDialog = () => {
  // 检查用户是否登录
  if (!isLoggedIn()) {
    ElMessage.warning('请先登录')
    return
  }
  
  showPostDialog.value = true
  // 清空表单
  postForm.value.title = ''
  postForm.value.content = ''
  postForm.value.type = 1
  postForm.value.status = 0
  postForm.value.images = []
  uploadedFiles.value = []
}

// 关闭发帖对话框
const closePostDialog = () => {
  showPostDialog.value = false
}

// 图片上传成功处理
const handleImageSuccess = (response, file, fileList) => {
  if (response.code === 1) {
    const imageUrl = response.data
    postForm.value.images.push(imageUrl)
    uploadedFiles.value = fileList
    ElMessage.success('图片上传成功！')
  } else {
    ElMessage.error('图片上传失败：' + response.msg)
  }
}

// 图片移除处理
const handleImageRemove = (file, fileList) => {
  const index = postForm.value.images.findIndex(url => 
    url === file.url || url === file.response?.data || url === file.response
  )
  
  if (index > -1) {
    postForm.value.images.splice(index, 1)
  }
  
  uploadedFiles.value = fileList
  ElMessage.success('图片已移除')
}

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt1M = file.size / 1024 / 1024 < 1

  if (!isJPG) {
    ElMessage.error('图片只能是 JPG/PNG 格式!')
  }
  if (!isLt1M) {
    ElMessage.error('图片大小不能超过 1MB!')
  }
  return isJPG && isLt1M
}

// 提交发帖
const submitPost = async () => {
  try {
    // 验证表单
    if (!postForm.value.title.trim()) {
      ElMessage.error('请输入标题')
      return
    }
    if (!postForm.value.content.trim()) {
      ElMessage.error('请输入内容')
      return
    }
    if (postForm.value.content.trim().length < 5) {
      ElMessage.error('内容至少5个字')
      return
    }

    const user = getCurrentUser()
    if (!user || !user.id) {
      ElMessage.error('用户信息获取失败')
      return
    }

    const postData = {
      userId: user.id,
      type: postForm.value.type,
      title: postForm.value.title.trim(),
      content: postForm.value.content.trim(),
      status: postForm.value.status,
      judgeStatus: 0, // 设置为待审核状态
      image: postForm.value.images.length > 0 ? postForm.value.images.join(',') : null
    }
    
    const response = await postAPI.addPost(postData)
    
    if (response.code === 1) {
      ElMessage.success('发布成功！审核中，请耐心等待！')
      showPostDialog.value = false
      
      // 清空表单
      postForm.value.title = ''
      postForm.value.content = ''
      postForm.value.type = 1
      postForm.value.status = 0
      postForm.value.images = []
      uploadedFiles.value = []
    } else {
      ElMessage.error('发布失败：' + response.msg)
    }
  } catch (error) {
    console.error('发布帖子错误:', error)
    ElMessage.error('发布失败，请稍后重试')
  }
}

// 退出登录
const logout = async () => {
  await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  
  clearUserAuth()
  ElMessage.success('退出登录成功')
  router.push('/login')
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  
  // 获取用户信息并验证登录状态
  const user = getCurrentUser()
  if (user) {
    // 用户已登录，不需要跳转
  } else {
    // 用户未登录，跳转到登录页面
    router.push('/login')
  }
  // 移除强制跳转逻辑，允许未登录用户访问页面
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  window.removeEventListener('storage', checkLoginStatus)
})
</script>

<template>
  <div class="container">
    <header class="header">
      <!-- 侧边栏按钮 - 居左 -->
      <div class="sidebar-container">
        <button class="sidebar-btn" @click="toggleSidebar">
          =
        </button>
      </div>
      
      <!-- 标题 - 居中 -->
      <h1 class="site-title">校智通</h1>
      
      <!-- 右侧空白区域，保持布局平衡 -->
      <div class="right-space"></div>
    </header>
    
    <!-- 侧边栏页面 -->
    <div v-if="showSidebar" class="sidebar-overlay" @click="closeSidebar"></div>
    <div class="sidebar" :class="{ 'sidebar-open': showSidebar }">
     
      
      <div class="sidebar-content">
        <router-link to="/app/home" @click="closeSidebar" class="sidebar-item">
          🏠 首页
        </router-link>
        <router-link to="/app/confession" @click="handleSidebarClick('/app/confession', $event)" class="sidebar-item">
          💕 我要表白
        </router-link>
        <router-link to="/app/activity" @click="handleSidebarClick('/app/activity', $event)" class="sidebar-item">
          🎉 校园活动
        </router-link>
        <router-link to="/app/study" @click="handleSidebarClick('/app/study', $event)" class="sidebar-item">
          📚 学习资料
        </router-link>
        <router-link to="/app/lost" @click="handleSidebarClick('/app/lost', $event)" class="sidebar-item">
          🔍 失物招领
        </router-link>
        <router-link to="/app/express" @click="handleSidebarClick('/app/express', $event)" class="sidebar-item">
          📦 快递代取
        </router-link>
        <router-link to="/app/chat" @click="handleSidebarClick('/app/chat', $event)" class="sidebar-item">
          💬 闲聊灌水
        </router-link>
      </div>
    </div>
    
    <!-- 主要内容区域，显示子路由内容 -->
  
      <router-view></router-view>
   
  </div>

  <div class="footer">
    <router-link to="/app/home" class="home">
      <el-icon><HomeFilled /></el-icon>
      <span class="homeText">首页</span>
    </router-link>

    <div class="submitPost" @click="openPostDialog"><el-icon><Plus /></el-icon></div>

    <router-link to="/app/userInfo" class="userInfo">
      <el-icon><UserFilled /></el-icon>
      <span class="userInfotext">我的</span>
    </router-link>
  </div>

  <!-- 快速发帖对话框 -->
  <div v-if="showPostDialog" class="post-dialog-overlay" @click="closePostDialog"></div>
  <div class="post-dialog" :class="{ 'post-dialog-open': showPostDialog }">
    <!-- 发帖对话框头部 -->
    <div class="post-dialog-header">
      <span class="post-dialog-title">快速发帖</span>
      <button class="post-dialog-close-btn" @click="closePostDialog">×</button>
    </div>
    
    <!-- 发帖表单 -->
    <div class="post-form">
      <div class="form-item">
        <label class="form-label">帖子类型</label>
        <el-select v-model="postForm.type" placeholder="请选择帖子类型" class="type-select">
          <el-option
            v-for="type in postTypes"
            :key="type.value"
            :label="type.label"
            :value="type.value"
          />
        </el-select>
      </div>
      
      <div class="form-item">
        <label class="form-label">标题</label>
        <el-input 
          v-model="postForm.title" 
          placeholder="请输入帖子标题" 
          class="title-input"
        />
      </div>
      
      <div class="form-item">
        <label class="form-label">内容</label>
        <el-input 
          v-model="postForm.content" 
          type="textarea" 
          :rows="4"
          placeholder="请输入帖子内容..."
          maxlength="500"
          show-word-limit
          class="content-input"
        />
      </div>
      
      <div class="form-item">
        <label class="form-label">图片上传</label>
        <el-upload
          class="image-uploader"
          action="/api/upload"
          :headers="{'token': token}"
          list-type="picture-card"
          :on-success="handleImageSuccess"
          :on-remove="handleImageRemove"
          :before-upload="beforeImageUpload"
          :file-list="uploadedFiles"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
        <div class="upload-tip">支持 JPG、PNG 格式，单张图片不超过 1MB</div>
      </div>
      
      <div class="form-item">
        <label class="form-label">匿名发布</label>
        <el-switch v-model="postForm.status" :active-value="1" :inactive-value="0" />
        <span class="anonymous-tip">开启后将隐藏你的身份信息</span>
      </div>
      
      <div class="form-actions">
        <el-button @click="closePostDialog" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="submitPost" class="submit-btn">发布</el-button>
      </div>
    </div>
  </div>
</template>

<style>
.container {
  min-height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;

}

.header {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  color: white;
  padding: 1rem;
  display: flex;
  align-items: center;
  position: relative;
}

.site-title {
  font-size: 35px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.footer {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: #faf9f9;
  color: #666;
  height: 65px;
  width: 100vw;
  position: fixed;
  bottom: 0;



}

.home, .userInfo {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 8px 16px;
  border-radius: 12px;
  min-width: 60px;
  text-decoration: none;
}
.home:hover, .userInfo:hover {
  background: #b1b0b0;
  border-radius: 12px;
}



.submitPost {
  display: flex;
  justify-content: center;
  color: rgb(0, 0, 0);
  align-items: center;
  width: 50px;
  height: 50px;
  margin-bottom: 50px;
  background: #ed117bcb;
  border-radius: 12px;

  position: relative;
  top: 0;
  border: 2px solid #e0e0e0;
}



.submitPost .el-icon {
  font-size: 35px;
  font-weight: bold;
  color: #ffffff;
  font-weight: 900;
}

/* 侧边栏样式 */
.sidebar {
  position: fixed;

  left: -50vw; /* 初始位置在屏幕左侧外 */
  width: 50vw; /* 占据屏幕一半宽度 */
  height: 100vh;
  background: white;

  transition: left 0.4s ease;
  overflow-y: auto;
 border-radius:2px;
 z-index: 1000;
  
}
.sidebar-btn{
  background:transparent;
  color: white;
  border: none;
  font-size: 35px;

}
.sidebar-open {
  left: 0; /* 展开时移动到屏幕内 */
}

/* 侧边栏遮罩层 */
.sidebar-overlay {
  position: fixed;
  
  width: 100vw;
  height: 100vh;
  background: rgba(55, 55, 55, 0.5);
 
}







/* 侧边栏内容 */
.sidebar-content {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.sidebar-item {
  width: 100%;
  text-align: center;
  padding: 20px;
  color: #5e5d5d;
  text-decoration: none;
  border-bottom: 1px solid #d7d7d7;

  font-size: 20px;
 
  overflow: auto;

  border-radius: 8px;
  margin: 4px 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.sidebar-item:hover {
  color: #96c4f2;
  transform: translateY(-2px);


}






.main-content {
  flex: 1;
  padding: 2rem;
  padding-bottom: 90px;
}

.home .el-icon, .userInfo .el-icon {
  font-size: 24px;
  margin-bottom: 4px;
  color: #2c3e50;
  display: block;
}

.homeText, .userInfotext {
  font-size: 12px;
  font-weight: 500;
  color: #333;
  display: block;
}

/* 发帖对话框样式 */
.post-dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  z-index: 2000;
}

.post-dialog {
  position: fixed;
  top: -100vh;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: white;
  z-index: 2001;
  transition: top 0.4s ease;
  overflow-y: auto;
}

.post-dialog-open {
  top: 0;
}

.post-dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
  background: #f8f9fa;
}

.post-dialog-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.post-dialog-close-btn {
  background: none;
  border: none;
  font-size: 28px;
  color: #999;
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.post-dialog-close-btn:hover {
  background: #f0f0f0;
}

.post-form {
  padding: 20px;
}

.form-item {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.type-select {
  width: 100%;
}

.title-input,
.content-input {
  width: 100%;
}

.content-input .el-textarea__inner {
  border-radius: 8px;
  resize: none;
}

.image-uploader {
  width: 100%;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.anonymous-tip {
  margin-left: 10px;
  color: #666;
  font-size: 14px;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 30px;
}

.cancel-btn {
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 8px;
}

.submit-btn {
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(45deg, #40a1e2, #e85ba9);
  border: none;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(64, 161, 226, 0.3);
}
</style>