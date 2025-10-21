<template>
  <div class="confession-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2>闲聊灌水</h2>
      <p class="subtitle">分享有趣事，愉悦校园生活</p>
    </div>

    <!-- 用户信息区域 -->
    <div class="user-info-section">
      <div class="user-avatar" @click="showUserProfile">
        <el-avatar :size="80" :src="currentUser.avatar || '/default-avatar.png'" />
        <div class="user-name">{{ currentUser.username }}</div>
      </div>
      <div class="user-stats" @click="showUserStats">
        <div class="stat-item">
          <span class="stat-number">{{ postCount }}</span>
          <span class="stat-label">发帖</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ likeCount }}</span>
          <span class="stat-label">获赞</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ commentCount }}</span>
          <span class="stat-label">评论</span>
        </div>
      </div>
    </div>

    <!-- 发帖按钮 -->
    <div class="post-button-section">
      <el-button type="primary" size="large" @click="showPostDialog" class="post-btn">
        <span class="post-icon">☁️</span>
        发帖
      </el-button>
    </div>

    <!-- 帖子列表 -->
    <div class="posts-container">
      <div v-for="post in postsList" :key="post.id" class="post-item">
        <!-- 帖子头部 -->
        <div class="post-header">
          <div class="post-user-info">
            <el-avatar 
              :size="50" 
              :src="post.status === 1 ? '/anonymous-avatar.png' : post.userAvatar" 
              @click="post.status === 1 ? null : showUserInfo(post.userId)"
            />
            <div class="post-user-details">
              <div class="post-username">
                {{ post.status === 1 ? '匿名用户' : post.username }}
              </div>
              <div class="post-time">{{ formatTime(post.createTime) }}</div>
            </div>
          </div>
          <div class="post-actions" v-if="post.userId === currentUser.id">
            <el-dropdown @command="handlePostAction">
              <el-button type="text" size="small">
                <span class="more-icon">⋯</span>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="{action: 'delete', postId: post.id}">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <!-- 帖子内容 -->
        <div class="post-content">
          <div class="post-title">{{ post.title }}</div>
          <div class="post-text">{{ post.content }}</div>
            
          <!-- 图片展示区域 -->
          <div v-if="post.image && post.image.length > 0" class="post-images">
            <div class="image-grid">
              <div 
                v-for="(imageUrl, index) in parseImages(post.image)" 
                :key="index" 
                class="image-item"
                @click="previewImage(parseImages(post.image), index)"
              >
                <img 
                  :src="imageUrl" 
                  :alt="`图片${index + 1}`"
                  @error="handleImageError"
                  @load="handleImageLoad"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- 帖子操作 -->
        <div class="post-actions-bar">
          <div class="action-item" @click="toggleLike(post.id)">
            <span class="action-icon" :class="{ 'liked': post.isLiked }">
              {{ post.isLiked ? '❤️' : '🤍' }}
            </span>
            <span class="action-text">{{ post.likes }}</span>
          </div>
          <div class="action-item" @click="toggleComment(post.id)">
            <span class="action-icon">💬</span>
            <span class="action-text">{{ post.comments }}</span>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="comments-section" v-if="post.showComments">
          <div class="comment-input">
            <el-input 
              v-model="post.commentText" 
              placeholder="写下你的评论..." 
              @keyup.enter="addComment(post.id)"
            >
              <template #append>
                <el-button @click="addComment(post.id)">发送</el-button>
              </template>
            </el-input>
          </div>
          <div class="comments-list">
            <div v-for="comment in post.commentList" :key="comment.id" class="comment-item">
              <el-avatar 
                :size="30" 
                :src="comment.status === 1 ? '/anonymous-avatar.png' : (comment.userAvatar || '/default-avatar.png')"
                @click="comment.status === 1 ? null : showUserInfo(comment.userId)"
              />
              <div class="comment-content">
                <div class="comment-user">
                  {{ comment.status === 1 ? '匿名用户' : comment.username }}
                </div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-time">{{ formatTime(comment.createTime) }}</div>
              </div>
              <div class="comment-actions" v-if="comment.userId === currentUser.id">
                <el-button type="text" size="small" @click="deleteComment(post.id, comment.id)">删除</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>



            <!-- 用户帖子信息对话框 -->
     <!-- 发帖页面弹出层 -->
                     <!-- 发帖页面弹出层 -->
      <div v-if="showPostFile" class="postFile-overlay" @click="closePostFile"></div>
      <div class="postContent" :class="{ 'post-open': showPostFile }">
        <!-- 发帖页面头部 -->
        <div class="post-header-bar">
          <span class="post-title">发布表白</span>
          <button class="close-btn" @click="closePostFile">×</button>
        </div>
        
                   <!-- 发帖表单 -->
         <el-form :model="postForm" :rules="postRules" ref="postFormRef" label-width="80px" class="post-form-container">
           <el-form-item label="标题" prop="title">
             <el-input v-model="postForm.title" placeholder="请输入帖子标题" />
           </el-form-item>
           
           <el-form-item label="内容" prop="content">
             <el-input 
               v-model="postForm.content" 
               type="textarea" 
               :rows="6"
               placeholder="勇敢表达你的心意..."
               maxlength="1000"
               show-word-limit
             />
           </el-form-item>
           
           <el-form-item label="图片上传">
             <el-upload
               class="image-uploader"
               action="/api/upload"
               :headers="{'token': token}"
               list-type="picture-card"
               :on-success="handleImageSuccess"
               :on-remove="handleImageRemove"
               :before-upload="beforeImageUpload"
               :on-preview="handleImagePreview"
               :file-list="uploadedFiles"
             >
               <el-icon><Plus /></el-icon>
             </el-upload>
             <div class="upload-tip">支持 JPG、PNG 格式，单张图片不超过 1MB</div>
           </el-form-item>
           
           <el-form-item label="匿名发布">
             <el-switch v-model="postForm.status" :active-value="1" :inactive-value="0" />
             <span class="anonymous-tip">开启后将隐藏你的身份信息</span>
           </el-form-item>
           
           <el-form-item>
             <el-button type="primary" @click="submitPost" class="submit-btn">发布</el-button>
           </el-form-item>
         </el-form>
      </div>

   



           <!-- 用户信息对话框 -->
     <el-dialog v-model="userInfoDialogVisible" title="用户信息" width="90vw" :close-on-click-modal="false">
               <div v-if="selectedUser" class="user-info-content">
         <div class="user-info-avatar">
           <el-avatar :size="80" :src="selectedUser.avatar || '/default-avatar.png'" />
        </div>
        <div class="user-info-details">
          <div class="info-item">
            <span class="info-label">用户名：</span>
            <span class="info-value">{{ selectedUser.username }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">手机号：</span>
            <span class="info-value">{{ selectedUser.phone }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">注册时间：</span>
            <span class="info-value">{{ formatCreateTime(selectedUser.createTime) }}</span>
          </div>
        </div>
      </div>
    </el-dialog>

           <!-- 用户资料编辑对话框 -->
     <el-dialog v-model="profileDialogVisible" title="编辑个人资料" width="90vw" :close-on-click-modal="false">
      <el-form :model="profileForm" :rules="profileRules" ref="profileFormRef" label-width="80px">
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :headers="{'token': token}"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="profileForm.avatar" :src="profileForm.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="profileForm.username" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="profileForm.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="profileDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateProfile">保存</el-button>
      </template>
    </el-dialog>

           <!-- 用户统计对话框 -->
     <el-dialog
       v-model="userStatsDialogVisible"
       title="我的发帖统计"
       width="95vw"
       :close-on-click-modal="false"
     >
      <div class="user-stats-content">
        <div class="stats-overview">
          <div class="stat-card">
            <div class="stat-title">总发帖数</div>
            <div class="stat-value">{{ userStats.totalPosts }}</div>
          </div>
          <div class="stat-card">
            <div class="stat-title">待审核</div>
            <div class="stat-value pending">{{ userStats.pendingPosts }}</div>
          </div>
          <div class="stat-card">
            <div class="stat-title">已通过</div>
            <div class="stat-value approved">{{ userStats.approvedPosts }}</div>
          </div>
          <div class="stat-card">
            <div class="stat-title">被拒绝</div>
            <div class="stat-value rejected">{{ userStats.rejectedPosts }}</div>
          </div>
        </div>
        
        <div class="recent-posts">
          <h4>最近发帖</h4>
          <div class="post-list">
            <div v-for="post in userStats.recentPosts" :key="post.id" class="post-item-mini">
                             <div class="post-info">
                 <div class="post-title-mini">{{ post.title }}</div>
                 <div class="post-type">{{ post.typeText }}</div>
               </div>
                             <div class="post-status">
                 <el-tag 
                   :type="getStatusType(post.judgeStatus)" 
                   size="small"
                 >
                   {{ getStatusText(post.judgeStatus) }}
                 </el-tag>
                 <span class="post-time">{{ formatTime(post.createTime) }}</span>
                                 <!-- 显示拒绝原因 -->
                                 <div v-if="(post.judgeStatus === 2 || post.judgeStatus === 3) && post.rejectReason" class="reject-reason">
                  <el-tooltip :content="post.rejectReason" placement="top">
                    <span class="reason-text">拒绝原因: {{ post.rejectReason }}</span>
                  </el-tooltip>
                </div>
               </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
    
    <!-- 图片预览组件 -->
    <el-image-viewer
      v-if="imagePreviewVisible"
      :url-list="imagePreviewList"
      :initial-index="imagePreviewIndex"
      @close="imagePreviewVisible = false"
      hide-on-click-modal
    />
  </div>
  </template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElImageViewer } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { postAPI, commentAPI, likeAPI, userAPI } from '@/api/chat'

// 响应式数据
const postDialogVisible = ref(false)
const userInfoDialogVisible = ref(false)
const profileDialogVisible = ref(false)

const showPostFile=ref(false)

const userStatsDialogVisible = ref(false) // 新增：用户统计对话框的可见性
         // 表单引用
  const postFormRef = ref()
  const profileFormRef = ref()

// 相应式数据获取用户的发帖数，点赞数，评论数
const postCount = ref(0)
const likeCount = ref(0)
const commentCount = ref(0)

// 从localStorage获取token
const token = ref('')

// 新增：用户统计数据
const userStats = ref({
  totalPosts: 0,
  pendingPosts: 0,
  approvedPosts: 0,
  rejectedPosts: 0,
  postType:'',
  recentPosts: []
})


   const closePostFile = () => {
   showPostFile.value = false
 }
const getpostCount = async () => {
  // 确保用户ID存在
 
  const result = await postAPI.getUserPostCount(currentUser.value.id)
  if (result.code === 1) {
    postCount.value = result.data
  } else {
    ElMessage.error('获取发帖数失败：' + result.msg)
  }
}

const getcommentCount = async () => {
  // 确保用户ID存在
  
  const result = await commentAPI.getUserCommentCount(currentUser.value.id)
  if (result.code === 1) {
    commentCount.value = result.data
  } else {
    ElMessage.error('获取评论数失败：' + result.msg)
  }
}

const getlikeCount = async () => {
  // 确保用户ID存在
 
  
  
  const result = await likeAPI.getUserReceivedLikeCount(currentUser.value.id)
  
  if (result.code === 1) {
    likeCount.value = result.data
  } else {
    ElMessage.error('获取获赞数失败：' + result.msg)
  }
}

// 当前用户信息
const currentUser = ref({
  id: '',
  username: '',
  avatar: '',
  phone: ''
})

// 从后端获取用户信息
const loadCurrentUser = async () => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser && loginUser.id) {
    // 从后端获取最新的用户信息
    const response = await userAPI.getUserInfo(loginUser.id)
    if (response.code === 1) {
      currentUser.value = {
        id: response.data.id,
        username: response.data.username,
        avatar: response.data.image || '/default-avatar.png',
        phone: response.data.phone || '',
        createTime: response.data.createTime // 确保createTime存在
      }
      // 同步更新localStorage中的用户信息
      const updatedLoginUser = {
        ...loginUser,
        username: response.data.username,
        avatar: response.data.image || '/default-avatar.png',
        phone: response.data.phone || '',
        createTime: response.data.createTime // 同步更新createTime
      }
      localStorage.setItem('loginUser', JSON.stringify(updatedLoginUser))
    } else {
      // 如果后端获取失败，使用localStorage的备用数据
      currentUser.value = {
        id: loginUser.id,
        username: loginUser.username,
        avatar: loginUser.image || '/default-avatar.png',
        phone: loginUser.phone || '',
        createTime: loginUser.createTime // 使用localStorage的createTime
      }
    }
  }
}

   // 发帖表单
 const postForm = ref({
   title: '',
   content: '',
   status: 0,
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






// 个人资料表单
const profileForm = ref({
  username: '',
  phone: '',
  avatar: ''
})




const profileRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 帖子列表
const postsList = ref([])

// 选中的用户信息
const selectedUser = ref(null)

// 方法
   const showPostDialog = () => {
   showPostFile.value = true
   // 清空表单
   postForm.value.title = ''
   postForm.value.content = ''
   postForm.value.status = 0
   postForm.value.images = []
   uploadedFiles.value = []
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

// 解析图片字符串为数组
const parseImages = (imageStr) => {
if (!imageStr) return []

if (typeof imageStr === 'string') {
  return imageStr.split(',').filter(url => url.trim())
}

if (Array.isArray(imageStr)) {
  return imageStr
}

return []
}

// 图片预览相关状态
const imagePreviewVisible = ref(false)
const imagePreviewList = ref([])
const imagePreviewIndex = ref(0)

// 图片预览
const previewImage = (images, index) => {
imagePreviewList.value = images
imagePreviewIndex.value = index
imagePreviewVisible.value = true
}

// 上传图片预览
const handleImagePreview = (file) => {
if (file.url || file.response?.data || file.response) {
  const imageUrl = file.url || file.response?.data || file.response
  imagePreviewList.value = [imageUrl]
  imagePreviewIndex.value = 0
  imagePreviewVisible.value = true
}
}

// 图片加载错误处理
const handleImageError = (event) => {
console.error('图片加载失败:', event.target.src)
event.target.style.display = 'none'

const existingError = event.target.parentNode.querySelector('.image-error-tip')
if (!existingError) {
  const errorDiv = document.createElement('div')
  errorDiv.className = 'image-error-tip'
  errorDiv.innerHTML = `
    <div style="display:flex;flex-direction:column;align-items:center;justify-content:center;width:100%;height:100%;background:#f8f9fa;color:#999;font-size:12px;border:1px dashed #ddd;border-radius:8px;">
      <div style="font-size:16px;margin-bottom:4px;">📷</div>
      <div>图片加载失败</div>
    </div>
  `
  event.target.parentNode.appendChild(errorDiv)
}
}

// 图片加载成功处理
const handleImageLoad = (event) => {
console.log('图片加载成功:', event.target.src)
}



         const submitPost = () => {
      postFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const postData = {
              userId: currentUser.value.id,
              type: 6 , // 闲聊灌水类型
              title: postForm.value.title.trim(),
              content: postForm.value.content.trim(),
              status: postForm.value.status,
              judgeStatus: 0, // 设置为待审核状态
              image: postForm.value.images.length > 0 ? postForm.value.images.join(',') : null
            }
            
            const response = await postAPI.addPost(postData)
            
            if (response.code === 1) {
              ElMessage.success('发布成功！审核中，请耐心等待！')
              showPostFile.value = false
              
              // 重新加载帖子列表
              await loadPosts()
              
              // 更新用户统计
              await getpostCount()
              await getlikeCount()
              await getcommentCount()
            } else {
              ElMessage.error('发布失败：' + response.msg)
            }
          } catch (error) {
            console.error('发布帖子错误:', error)
            ElMessage.error('发布失败，请稍后重试')
          }
        }
      })
    }



const toggleLike = async (postId) => {
  // 先找到对应的帖子
  const post = postsList.value.find(p => p.id === postId)
  if (!post) {
    ElMessage.error('帖子不存在')
    return
  }
  
  if (post.isLiked) {
    // 取消点赞
    await likeAPI.unlikePost(postId, currentUser.value.id) 
    post.isLiked = false
    
    // 重新获取帖子的点赞数量
    const likeCountResponse = await likeAPI.getPostLikeCount(postId)
    post.likes = likeCountResponse.data || 0
    
    // 如果当前用户是帖子作者，更新自己的获赞数
    if (post.userId === currentUser.value.id) {
      await getlikeCount()
    } else {
      // 获取帖子作者的最新获赞数
      const authorLikeCountResponse = await likeAPI.getUserReceivedLikeCount(post.userId)
      console.log(`帖子作者 ${post.userId} 的获赞数已更新为: ${authorLikeCountResponse.data}`)
    }
    
    ElMessage.success('取消点赞成功！')
  } else {
    // 添加点赞
    await likeAPI.likePost(postId, currentUser.value.id) 
    post.isLiked = true
    
    // 重新获取帖子的点赞数量
    const likeCountResponse = await likeAPI.getPostLikeCount(postId)
    post.likes = likeCountResponse.data || 0
    
    // 如果当前用户是帖子作者，更新自己的获赞数
    if (post.userId === currentUser.value.id) {
      await getlikeCount()
    } else {
      // 获取帖子作者的最新获赞数
      const authorLikeCountResponse = await likeAPI.getUserReceivedLikeCount(post.userId)
      console.log(`帖子作者 ${post.userId} 的获赞数已更新为: ${authorLikeCountResponse.data}`)
    }
    
    ElMessage.success('点赞成功！')
  }
}

const toggleComment = (postId) => {
  const post = postsList.value.find(p => p.id === postId)
  if (post) {
    post.showComments = !post.showComments
    if (post.showComments && post.commentList.length === 0) {
      // 加载评论
      loadComments(postId)
    }
  }
}

const addComment = async (postId) => {
  const post = postsList.value.find(p => p.id === postId)
  if (post && post.commentText.trim()) {
    const commentData = {
      postId: postId,
      userId: currentUser.value.id,
      content: post.commentText
    }
    
    await commentAPI.addComment(commentData)
    
    // 重新加载评论
    await loadComments(postId)
    post.commentText = ''
    post.comments++
    commentCount.value++
    
    ElMessage.success('评论成功！')
  }
}

const deleteComment = async (postId, commentId) => {
  await commentAPI.deleteComment(commentId, currentUser.value.id)
  
  const post = postsList.value.find(p => p.id === postId)
  if (post) {
    const commentIndex = post.commentList.findIndex(c => c.id === commentId)
    if (commentIndex > -1) {
      post.commentList.splice(commentIndex, 1)
      post.comments--
      commentCount.value--
      ElMessage.success('评论已删除！')
    }
  }
}

const handlePostAction = (command) => {
  if (command.action === 'delete') {
    ElMessageBox.confirm('确定要删除这条帖子吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      await postAPI.deletePost(command.postId, currentUser.value.id)
      const postIndex = postsList.value.findIndex(p => p.id === command.postId)
      if (postIndex > -1) {
        postsList.value.splice(postIndex, 1)
        postCount.value--
        ElMessage.success('删除成功！')
      }
    })
  }
}

const showUserInfo = async (userId) => {
  // 如果是当前用户，直接使用currentUser
  if (userId === currentUser.value.id) {
    selectedUser.value = currentUser.value;
  } else {
    // 从后端获取其他用户信息
    const response = await userAPI.getUserInfo(userId);
    if (response.code === 1) {
      selectedUser.value = response.data;
    } else {
      ElMessage.error('获取用户信息失败：' + response.msg);
      return;
    }
  }
  userInfoDialogVisible.value = true;
}

const showUserProfile = () => {
  profileForm.value.username = currentUser.value.username
  profileForm.value.phone = currentUser.value.phone
  profileForm.value.avatar = currentUser.value.avatar
  profileDialogVisible.value = true
}

const updateProfile = () => {
  profileFormRef.value.validate(async (valid) => {
    if (valid) {
      // 调用后端API更新用户信息
      const response = await userAPI.updateUserInfo(currentUser.value.id, {
        username: profileForm.value.username,
        phone: profileForm.value.phone,
        image: profileForm.value.avatar
      });
      
      if (response.code === 1) {
        // 更新成功后，同步更新currentUser和localStorage
        currentUser.value.username = profileForm.value.username
        currentUser.value.phone = profileForm.value.phone
        currentUser.value.avatar = profileForm.value.avatar
        
        // 更新localStorage
        const loginUser = JSON.parse(localStorage.getItem('loginUser'))
        if (loginUser) {
          loginUser.username = currentUser.value.username
          loginUser.phone = currentUser.value.phone
          loginUser.avatar = currentUser.value.avatar
          localStorage.setItem('loginUser', JSON.stringify(loginUser))
        }
        
        ElMessage.success('个人资料更新成功！')
        profileDialogVisible.value = false
      } else {
        ElMessage.error('更新失败：' + response.msg);
      }
    }
  })
}

const handleAvatarSuccess = (response) => {
  if (response.code === 1) {
    profileForm.value.avatar = response.data
    // 立即更新currentUser和localStorage，避免刷新页面丢失头像
    currentUser.value.avatar = response.data
    const loginUser = JSON.parse(localStorage.getItem('loginUser'))
    if (loginUser) {
      loginUser.avatar = response.data
      localStorage.setItem('loginUser', JSON.stringify(loginUser))
    }
    ElMessage.success('头像上传成功！')
  } else {
    ElMessage.error('头像上传失败：' + response.msg);
  }
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '未知时间'
  
  // 如果是字符串格式的日期，先转换为Date对象
  let date
  if (typeof time === 'string') {
    date = new Date(time)
  } else if (time instanceof Date) {
    date = time
  } else {
    return '未知时间'
  }
  
  // 检查日期是否有效
  if (isNaN(date.getTime())) {
    return '未知时间'
  }
  
  const now = new Date()
  const diff = now - date
  
  // 小于1分钟
  if (diff < 60000) {
    return '刚刚'
  }
  // 小于1小时
  if (diff < 3600000) {
    return Math.floor(diff / 60000) + '分钟前'
  }
  // 小于24小时
  if (diff < 86400000) {
    return Math.floor(diff / 3600000) + '小时前'
  }
  // 小于30天
  if (diff < 2592000000) {
    return Math.floor(diff / 86400000) + '天前'
  }
  
  // 超过30天显示具体日期
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 格式化注册时间
const formatCreateTime = (createTime) => {
  if (!createTime) return '未知时间'
  
  // 如果是字符串格式的日期，先转换为Date对象
  let date
  if (typeof createTime === 'string') {
    // 处理不同的日期格式
    if (createTime.includes('T')) {
      // ISO格式：2024-01-01T00:00:00
      date = new Date(createTime)
    } else if (createTime.includes('-')) {
      // 标准日期格式：2024-01-01
      date = new Date(createTime + 'T00:00:00')
    } else {
      date = new Date(createTime)
    }
  } else if (createTime instanceof Date) {
    date = createTime
  } else {
    return '未知时间'
  }
  
  // 检查日期是否有效
  if (isNaN(date.getTime())) {
    console.warn('Invalid date:', createTime)
    return '未知时间'
  }
  
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 加载帖子列表
const loadPosts = async () => {
  // 确保用户ID存在
  if (!currentUser.value || !currentUser.value.id) {
    console.warn('用户ID不存在，跳过加载帖子')
    return
  }
  
  const response = await postAPI.getPostsByType(6) // 闲聊灌水类型
  postsList.value = response.data.map(post => ({
    ...post,
    isLiked: false,
    showComments: false,
    commentText: '',
    commentList: []
  }))
  
  // 检查当前用户是否点赞，并加载帖子的点赞数量和评论数量
  for (const post of postsList.value) {
    // 检查当前用户是否点赞
    const likeResponse = await likeAPI.checkUserLike(post.id, currentUser.value.id)
    post.isLiked = likeResponse.data
    
    // 加载帖子的点赞数量
    const likeCountResponse = await likeAPI.getPostLikeCount(post.id)
    post.likes = likeCountResponse.data || 0
    
    // 加载帖子的评论数量
    const commentCountResponse = await commentAPI.getPostCommentCount(post.id)
    post.comments = commentCountResponse.data || 0
  }
}

// 加载评论
const loadComments = async (postId) => {
  const response = await commentAPI.getCommentsByPostId(postId)
  const post = postsList.value.find(p => p.id === postId)
  if (post) {
    post.commentList = response.data
  }
}

// 显示用户统计对话框
const showUserStats = () => {
  userStatsDialogVisible.value = true;
  loadUserStats(); // 加载用户统计数据
}
// 加载用户统计数据
const loadUserStats = async () => {
  try {
    const postsResponse = await postAPI.getUserPosts(currentUser.value.id)
    
    if (postsResponse.code === 1) {
      const posts = postsResponse.data || []
      
             const pendingPosts = posts.filter(p => p.judgeStatus === 0)
       const approvedPosts = posts.filter(p => p.judgeStatus === 1)
       const rejectedPosts = posts.filter(p => p.judgeStatus === 2)
       const deletePosts=posts.filter(p=>p.judgeStatus===3)
      
      userStats.value = {
        totalPosts: posts.length-deletePosts.length,
        pendingPosts: pendingPosts.length,
        approvedPosts: approvedPosts.length,
        rejectedPosts: rejectedPosts.length,
        recentPosts: posts
          .sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
          .slice(0, 5)
          .map(post => {
            console.log('处理帖子:', post.id, '类型:', post.type, '类型文本:', getpoststype(post.type))
            return {
              ...post,
              typeText: getpoststype(post.type) // 添加帖子类型文本
            }
          })
      }
    } else {
      ElMessage.error('获取用户帖子失败：' + postsResponse.msg)
    }
  } catch (error) {
    ElMessage.error('获取用户统计失败，请稍后重试')
  }
}

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case 0:
      return 'info'; // 待审核
    case 1:
      return 'success'; // 已通过
    case 2:
      return 'danger'; // 被拒绝
    case 3:
      return 'danger'; // 被删除
    default:
      return 'info';
  }
};

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 0:
      return '待审核';
    case 1:
      return '已通过';
    case 2:
      return '被拒绝';
    case 3:
      return '被删除';
    default:
      return '未知';
  }
};

const getpoststype = (type) => {
  // 处理 null、undefined 或非数字的情况
  if (type === null || type === undefined || isNaN(type)) {
    return '表白墙'
  }
  
  switch (Number(type)) {
    case 1:
      return '我要表白'
    case 2:
      return '代取快递'
    case 3:
      return '学习交流'
    case 4:
      return '活动组织'
    case 5:
      return '失物招领'
    case 6:
      return '闲聊灌水'
    default:
      return '表白墙' // 默认显示为表白墙，而不是"未知类型"
  }
}

onMounted(async () => {
  // 从localStorage获取token
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser && loginUser.token) {
    token.value = loginUser.token
  }

  await loadCurrentUser() // 等待用户信息加载完成
  loadPosts()
  getpostCount()
  getlikeCount()
  getcommentCount()
})
</script>

<style scoped>
.confession-container {
   margin:25px;
   min-width: 80vw;
   padding-bottom: 80px; /* 防止底部导航栏遮挡 */
 }

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h2 {
  color: #1d6de5;
  font-size: 28px;
  margin-bottom: 10px;
}

.subtitle {
  color: #171414;
  font-size: 16px;
}

.user-info-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #1259e7 0%, #1a1717 100%);
  padding: 20px;
  border-radius: 15px;
  margin-bottom: 30px;
  color: white;
}

.user-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s;
}

.user-avatar:hover {
  transform: scale(1.05);
}

.user-name {
  margin-top: 10px;
  font-weight: 600;
  font-size: 18px;
}

.user-stats {
  display: flex;
  gap: 15px;
  background:transparent;
}

.stat-item {
  text-align: center;
  background:transparent;
  border:none;
  color:white;
}

.stat-number {
  display: block;
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 8px;
  color:white;
}

.stat-label {
  font-size: 14px;
  opacity: 0.8;
}

.post-button-section {
  text-align: center;
  margin-bottom: 30px;
}

.post-btn {
  padding: 15px 40px;
  font-size: 18px;
  border-radius: 25px;
  background: linear-gradient(45deg, #1b2fc4, #009688);
  border: none;
  box-shadow: 0 4px 15px rgba(233, 30, 99, 0.3);
  transition: all 0.3s;
}

.post-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(233, 30, 99, 0.4);
}

.post-icon {
  margin-right: 8px;
}

 .posts-container {
 display: flex;
 flex-direction: column;
 justify-content: center;
 gap: 10px;

 width: 100%;
 height: 100%;

}

 .post-item {
 background: white;
 border-radius: 15px;

 box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
 border: 1px solid #f0f0f0;
 width: 100%;
 display: block;
}


.post-header {
 display: flex;
 justify-content: space-between;
 align-items: center;
 margin-bottom: 15px;
 padding-bottom: 15px;

 width: 100%;
 margin-left: 15px;
 flex-wrap: nowrap;
}

.post-user-info {
 display: flex;
 
 flex-direction: column;
 gap: 10px;
 flex: 1;
 min-width: 0;
 flex-wrap: nowrap;
}

.post-user-details {
 display: flex;
 flex-direction: column;
 gap: 6px;
 flex: 1;
 min-width: 0;
 flex-wrap: nowrap;
}

.post-username {
 font-weight: 600;
 color: #333;
 font-size: 16px;
 line-height: 1.3;
}



.post-time {
  color: #000000 !important;
  font-size: 14px !important;
  line-height: 1.3 !important;
  display: block !important;
  visibility: visible !important;
  opacity: 1 !important;
}

.post-actions {
  display: flex;
  align-items: center;
  margin-left: 15px;
}

.more-icon {
  font-size: 18px;
  color: #999;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  margin-right: 30px;
  width: 20px;
  transition: all 0.2s;
}

.more-icon:hover {
  background-color: #f5f5f5;
  color: #666;
}

 .post-content {
 margin-bottom: 40px;
 width: 100%;
 display: block;
 margin-left: 15px;
}

 .post-title {
 font-size: 18px;
 font-weight: 600;
 color: #333;
 margin-bottom: 15px;
 line-height: 1.4;
 display: block;
 width: 100%;
}

 .post-text {
 color: #666;
 line-height: 1.6;
 font-size: 15px;
 margin-bottom: 20px;
 display: block;
 width: 100%;
}

 .post-images {
 margin-top: 20px;
 display: block;
 width: 100%;
}

 .image-grid {
 width: 100%;
 margin-top: 20px;
 display: grid;
 grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
 gap: 10px;
}

 .image-item {
 position: relative;
 width: 120px;
 height: 120px;
 border-radius: 8px;
 overflow: hidden;
 cursor: pointer;
 border: 1px solid #eee;
 box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

 .post-actions-bar {
 display: flex;
 gap: 30px;
 padding: 15px 0;
 border-top: 1px solid #f0f0f0;
 border-bottom: 1px solid #f0f0f0;
 margin-bottom: 15px;
 width: 100%;
 justify-content: flex-start;
 flex-wrap: nowrap;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 12px 18px;
  border-radius: 25px;
  transition: all 0.3s;
}

.action-item:hover {
  background: #f5f5f5;
}

.action-icon {
  font-size: 20px;
}

.action-text {
  color: #666;
  font-size: 15px;
  font-weight: 500;
}

.liked {
  animation: heartBeat 0.3s;
}

@keyframes heartBeat {
  0% { transform: scale(1); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}

 .comments-section {
 margin-top: 20px;
 padding-top: 15px;
 border-top: 1px solid #f0f0f0;
 width: 100%;
 display: block;
}

.comment-input {
  margin-bottom: 15px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.comment-item {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 10px;
}

.comment-content {
  display: flex;
  flex-direction: column;
}

.comment-user {
  font-weight: 600;
  color: #333;
  font-size: 14px;
  margin-bottom: 8px;
}

.comment-text {
  color: #000000;
  font-size: 14px;
  line-height: 1.4;
  margin-bottom: 5px;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.comment-actions {
  margin-left: auto;
}

.anonymous-tip {
  margin-left: 10px;
  color: #999;
  font-size: 14px;
}

/* 对话框样式 */
.user-info-content {
  text-align: center;
}

.user-info-avatar {
  margin-bottom: 20px;
}

.user-info-details {
  text-align: left;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-label {
  color: #666;
  font-weight: 600;
}

.info-value {
  color: #333;
}

.avatar-uploader {
  text-align: center;
}

   .avatar-uploader .avatar {
   width: 80px;
   height: 80px;
   border-radius: 50%;
 }
 
 .avatar-uploader .el-upload {
   border: 1px dashed #d9d9d9;
   border-radius: 50%;
   cursor: pointer;
   position: relative;
   overflow: hidden;
   width: 80px;
   height: 80px;
   display: flex;
   align-items: center;
   justify-content: center;
 }

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

   .avatar-uploader-icon {
   font-size: 24px;
   color: #8c939d;
   width: 80px;
   height: 80px;
   line-height: 80px;
   text-align: center;
 }

   /* 用户统计对话框样式 */
 .user-stats-content {
   padding: 15px;
 }

   .stats-overview {
   display: grid;
   grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
   gap: 15px;
   margin-bottom: 25px;
 }

   .stat-card {
   background: #f8f9fa;
   border-radius: 8px;
   padding: 15px;
   text-align: center;
   box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
 }

.stat-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.pending {
  color: #ff9800; /* 待审核 */
}

.approved {
  color: #4caf50; /* 已通过 */
}

.rejected {
  color: #f44336; /* 被拒绝 */
}

.recent-posts h4 {
  font-size: 18px;
  color: #333;
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.post-item-mini {
  background: #fdfdfd;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 10px 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

.post-item-mini:hover {
  background-color: #f0f0f0;
}

.post-info {
  display: flex;
  flex-direction: column;
  margin-right: 10px;
}

.post-title-mini {
  font-weight: 600;
  color: #333;
  font-size: 14px;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.post-type {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
}

.post-status {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  color: #666;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.reject-reason {
  margin-top: 5px;
}

   .reason-text {
   font-size: 11px;
   color: #f56c6c;
   background: #fef0f0;
   padding: 2px 6px;
   border-radius: 4px;
   border: 1px solid #fbc4c4;
   cursor: help;
 }
 
                             /* 发帖页面弹出层样式 */
    .postFile-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100vw;
      height: 100vh;
      background: rgba(0, 0, 0, 0.5);
      z-index: 2000;
      display: flex;
      justify-content: center;
    }
    
 .postContent {
         background: white;
         width: 100vw;
         height: 100vh;
       
     
         position: fixed;
         top: -120vh; /* 初始位置在屏幕上方外 */
         left: 0;
         transition: top 0.4s ease; /* 使用和侧边栏一样的动画 */
         overflow-y: auto;
         z-index: 2001;
       }
     
     .postContent.post-open {
       top: 0; /* 展开时移动到屏幕内 */
     }
    


 
     .post-header-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    margin-top: 20px;
    
    border-bottom: 1px solid #eee;
  }
  
  .post-header-bar .post-title {
    font-size: 25px;
    font-weight: 600;
    text-align: center;
    color: #000000;
    flex: 1;
  }
 
 .close-btn {
   background: none;
   border: none;
   font-size: 26px;
   color: #999;
   cursor: pointer;
   padding: 5px;
   border-radius: 50%;
   width: 30px;
   height: 30px;
 

 }
 
 .close-btn:hover {
   background: #f5f5f5;
 }
 
     .post-form-container {
    padding: 10px;
  }
  
.el-form-item {
    margin-bottom: 10px;
  }
  
  .post-form-container .el-form-item__label {
    font-weight: 500;
    color: #333;
    font-size: 20px;
  }
  
  .post-form-container .el-input__inner,
  .post-form-container .el-textarea__inner {
    border-radius: 8px;
    transition: border-color 0.3s;
  }
  
  .post-form-container .el-input__inner:focus,
  .post-form-container .el-textarea__inner:focus {
    border-color: #e91e63;
  }
  
  .anonymous-tip {
    margin-left: 10px;
    color: #999;
    font-size: 14px;
  }
 
 .submit-btn {
   background: linear-gradient(45deg, #40a1e2, #e85ba9);
   color: white;
   width: 100vw;
   border: none;
   padding: 15px;
   border-radius: 15px;
   font-size: 16px;
   font-weight: 600;
   cursor: pointer;
   transition: all 0.3s;
   margin-top: 10px;
 }
 
 .submit-btn:hover {
   transform: translateY(-2px);
   box-shadow: 0 4px 15px rgba(233, 30, 99, 0.3);
 }

   /* 图片上传样式 */
   .image-uploader {
     width: 100%;
   }
   
   .upload-tip {
     font-size: 12px;
     color: #999;
     margin-top: 8px;
   }
   
   /* 帖子图片展示样式 */
   .post-images {
     margin-top: 15px;
     display: block;
     width: 100%;
   }
   
    .image-grid {
 display: grid;
 grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
 gap: 10px;
 max-width: 100%;
 width: 100%;
}
   
    .image-item {
 position: relative;
 width: 120px;
 height: 120px;
 cursor: pointer;
 border-radius: 8px;
 overflow: hidden;
 border: 1px solid #eee;
 box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
 transition: transform 0.2s;
}
   
   .image-item:hover {
     transform: scale(1.05);
   }
   
   .image-item img {
     width: 100%;
     height: 100%;
     object-fit: cover;
     border-radius: 8px;
   }
   
   /* 单张图片时显示更大 */
   .image-grid:has(.image-item:only-child) .image-item {
     max-width: 300px;
     max-height: 300px;
   }
   
   /* 两张图片时并排显示 */
   .image-grid:has(.image-item:nth-child(2):last-child) {
     grid-template-columns: repeat(2, 1fr);
   }
   
   /* 三张及以上图片时网格显示 */
   .image-grid:has(.image-item:nth-child(3):last-child),
   .image-grid:has(.image-item:nth-child(n+4)) {
     grid-template-columns: repeat(3, 1fr);
   }
   
   /* 图片错误提示样式 */
   .image-error-tip {
     position: absolute;
     top: 0;
     left: 0;
     width: 100%;
     height: 100%;
     z-index: 1;
   }

   </style>