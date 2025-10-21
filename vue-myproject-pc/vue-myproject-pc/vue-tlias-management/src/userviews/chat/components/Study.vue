<template>
  <div class="study-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2>学习交流</h2>
      <p class="subtitle">分享学习心得，交流学习经验</p>
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
        <span class="post-icon">📖</span>
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
              :src="post.status === 1 ? '/anonymous-avatar.png' : (post.userAvatar || '/default-avatar.png')" 
              @click="post.status === 1 ? null : showUserInfo(post.userId)"
            />
            <div class="post-user-details">
              <div class="post-username">
                {{ post.status === 1 ? '匿名用户' : post.username }}
              </div>
              <div class="post-time">{{ formatTime(post.createTime) }}</div>
              <!-- 添加审核状态显示 -->
              <div class="post-status" v-if="post.userId === currentUser.id">
                <el-tag 
                  :type="getStatusType(post.judgeStatus)" 
                  size="small"
                >
                  {{ getStatusText(post.judgeStatus) }}
                </el-tag>
              </div>
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
          <div class="action-item" @click="sharePost(post.id)">
            <span class="action-icon">📤</span>
            <span class="action-text">分享</span>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="comments-section" v-if="post.showComments">
          <div class="comment-input">
            <el-input 
              v-model="post.commentText" 
              placeholder="写下你的评论..."
              type="textarea"
              :rows="2"
              maxlength="200"
              show-word-limit
            />
            <div class="comment-actions">
              <el-button type="primary" size="small" @click="submitComment(post.id)">
                发表评论
              </el-button>
            </div>
          </div>
          
          <div class="comments-list">
            <div v-for="comment in post.comments" :key="comment.id" class="comment-item">
              <div class="comment-user">
                <el-avatar :size="30" :src="comment.userAvatar || '/default-avatar.png'" />
                <span class="comment-username">{{ comment.username }}</span>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 用户统计对话框 -->
    <el-dialog
      v-model="userStatsDialogVisible"
      title="我的发帖统计"
      width="600px"
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

    <!-- 发帖对话框 -->
    <el-dialog v-model="postDialogVisible" title="发布学习交流" width="600px">
      <el-form :model="postForm" :rules="postRules" ref="postFormRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="postForm.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input 
            v-model="postForm.content" 
            type="textarea" 
            :rows="8"
            placeholder="分享你的学习心得、问题或资源..."
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="匿名发布">
          <el-switch v-model="postForm.status" :active-value="1" :inactive-value="0" />
          <span class="anonymous-tip">开启后将隐藏你的身份信息</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="postDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPost">发布</el-button>
      </template>
    </el-dialog>



    <!-- 用户信息对话框 -->
    <el-dialog v-model="userInfoDialogVisible" title="用户信息" width="400px">
      <div v-if="selectedUser" class="user-info-content">
        <div class="user-info-avatar">
          <el-avatar :size="100" :src="selectedUser.avatar || '/default-avatar.png'" />
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
            <span class="info-value">{{ formatTime(selectedUser.createTime) }}</span>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 用户资料编辑对话框 -->
    <el-dialog v-model="profileDialogVisible" title="编辑个人资料" width="500px">
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { postAPI, commentAPI, likeAPI, userAPI } from '@/api/chat'

// 响应式数据
const postDialogVisible = ref(false)
const userInfoDialogVisible = ref(false)
const profileDialogVisible = ref(false)
const userStatsDialogVisible = ref(false) // 新增：用户统计对话框
// 表单引用
const postFormRef = ref()
const profileFormRef = ref()

// 相应式数据获取用户的发帖数，点赞数，评论数
const postCount = ref(0)
const likeCount = ref(0)
const commentCount = ref(0)

// 从localStorage获取token
const token = ref('')

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
        phone: response.data.phone || ''
      }
      // 同步更新localStorage中的用户信息
      const updatedLoginUser = {
        ...loginUser,
        username: response.data.username,
        avatar: response.data.image || '/default-avatar.png',
        phone: response.data.phone || ''
      }
      localStorage.setItem('loginUser', JSON.stringify(updatedLoginUser))
    } else {
      // 如果后端获取失败，使用localStorage的备用数据
      currentUser.value = {
        id: loginUser.id,
        username: loginUser.username,
        avatar: loginUser.image || '/default-avatar.png',
        phone: loginUser.phone || ''
      }
    }
  }
}



// 发帖表单
const postForm = ref({
  title: '',
  content: '',
  status: 0
})



// 个人资料表单
const profileForm = ref({
  username: '',
  phone: '',
  avatar: ''
})

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

// 用户统计数据
const userStats = ref({
  totalPosts: 0,
  pendingPosts: 0,
  approvedPosts: 0,
  rejectedPosts: 0,
  recentPosts: []
})

// 方法
const showPostDialog = () => {
  postDialogVisible.value = true
  postForm.value.title = ''
  postForm.value.content = ''
  postForm.value.status = 0
}

const submitPost = () => {
  postFormRef.value.validate(async (valid) => {
    if (valid) {
      const postData = {
        userId: currentUser.value.id,
        type: 3, // 学习交流类型
        title: postForm.value.title,
        content: postForm.value.content,
        status: postForm.value.status,
        judgeStatus: 0 // 设置为待审核状态
      }
      
      await postAPI.addPost(postData)
      ElMessage.success('审核中，请耐心等待！')
      postDialogVisible.value = false
      
      // 重新加载帖子列表
      loadPosts()
      
      // 更新用户统计
      getpostCount()
      getlikeCount()
      getcommentCount()
    }
  })
}



const toggleLike = async (postId) => {
  const post = postsList.value.find(p => p.id === postId)
  if (post) {
    // 防止重复操作
    if (post.isLiked === undefined) {
      ElMessage.warning('正在加载点赞状态，请稍后再试')
      return
    }
    
    if (post.isLiked) {
      // 取消点赞
      await likeAPI.unlikePost(postId, currentUser.value.id)
      post.isLiked = false
      ElMessage.success('取消点赞成功！')
    } else {
      // 点赞
      await likeAPI.likePost(postId, currentUser.value.id)
      post.isLiked = true
      ElMessage.success('点赞成功！')
    }
    
    // 重新获取帖子的点赞数量，确保数据准确性
    const likeCountResponse = await likeAPI.getPostLikeCount(postId)
    post.likes = likeCountResponse.data || 0
    
    // 如果当前用户是帖子作者，需要更新自己的获赞数
    if (post.userId === currentUser.value.id) {
      // 更新当前用户的获赞数
      await getlikeCount()
    } else {
      // 如果当前用户不是帖子作者，需要更新帖子作者的获赞数
      // 获取帖子作者的最新获赞数
      const authorLikeCountResponse = await likeAPI.getUserReceivedLikeCount(post.userId)
      // 这里可以触发一个事件或者通过其他方式通知帖子作者更新其获赞数
      // 由于前端无法直接更新其他用户的统计，这里记录日志
      console.log(`帖子作者 ${post.userId} 的获赞数已更新为: ${authorLikeCountResponse.data}`)
    }
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

const sharePost = (postId) => {
  // 这里实现分享功能
  ElMessage.success('分享功能开发中...')
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

// 加载帖子列表
const loadPosts = async () => {
  const response = await postAPI.getPostsByType(3) // 学习交流类型
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

// 获取用户发帖数
const getpostCount = async () => {
  const result = await postAPI.getUserPostCount(currentUser.value.id)
  if (result.code === 1) {
    postCount.value = result.data
  } else {
    ElMessage.error('获取发帖数失败：' + result.msg)
  }
}

// 获取用户评论数
const getcommentCount = async () => {
  const result = await commentAPI.getUserCommentCount(currentUser.value.id)
  if (result.code === 1) {
    commentCount.value = result.data
  } else {
    ElMessage.error('获取评论数失败：' + result.msg)
  }
}

// 获取用户获赞数
const getlikeCount = async () => {
  const result = await likeAPI.getUserReceivedLikeCount(currentUser.value.id)
  if (result.code === 1) {
    likeCount.value = result.data
  } else {
    ElMessage.error('获取获赞数失败：' + result.msg)
  }
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
  const deletedPosts = posts.filter(p => p.judgeStatus === 3)
      
      userStats.value = {
        totalPosts: posts.length - deletedPosts.length,
        pendingPosts: pendingPosts.length,
        approvedPosts: approvedPosts.length,
        rejectedPosts: rejectedPosts.length,
        recentPosts: posts
          .sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
          .slice(0, 5)
          .map(post => ({
            ...post,
            typeText: getpoststype(post.type) // 添加帖子类型文本
          }))
      }
    } else {
      ElMessage.error('获取用户帖子失败：' + postsResponse.msg)
    }
  } catch (error) {
    ElMessage.error('获取用户统计失败，请稍后重试')
  }
}

// 获取帖子类型文本
const getpoststype = (type) => {
  switch (type) {
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
      return '未知类型'
  }
}
// 显示用户统计对话框
const showUserStats = () => {
  userStatsDialogVisible.value = true
  loadUserStats() // 加载统计数据
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

// 提交评论
const submitComment = async (postId) => {
  const post = postsList.value.find(p => p.id === postId);
  if (post && post.commentText.trim()) {
    const commentData = {
      postId: postId,
      userId: currentUser.value.id,
      content: post.commentText
    };
    await commentAPI.addComment(commentData);
    post.commentText = '';
    post.comments++;
    commentCount.value++;
    ElMessage.success('评论成功！');
    // 重新加载评论
    await loadComments(postId);
  }
};

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
.study-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h2 {
  color: #2f942f;
  font-size: 28px;
  margin-bottom: 10px;
}

.subtitle {
  color: #000000;
  font-size: 16px;
}

.user-info-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #56c85a 0%, #91c950 100%);
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
  gap: 30px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
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
  background: linear-gradient(45deg, #4caf50, #8bc34a);
  border: none;
  box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
  transition: all 0.3s;
}

.post-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
}

.post-icon {
  margin-right: 8px;
}

.posts-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.post-item {
  background: white;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid #f0f0f0;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.post-user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.post-user-details {
  display: flex;
  flex-direction: column;
}

.post-username {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.post-time {
  color: #999;
  font-size: 14px;
  margin-top: 2px;
}

.post-status {
  margin-top: 5px;
}

.post-actions {
  position: relative;
}

.more-icon {
  font-size: 20px;
  color: #999;
  cursor: pointer;
}

.post-content {
  margin-bottom: 20px;
}

.post-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
}

.post-text {
  color: #666;
  line-height: 1.6;
  font-size: 15px;
  margin-bottom: 15px;
}

.post-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.post-tag {
  margin: 0;
}

.post-actions-bar {
  display: flex;
  gap: 30px;
  padding: 15px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 15px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 20px;
  transition: all 0.3s;
}

.action-item:hover {
  background: #f5f5f5;
}

.action-icon {
  font-size: 18px;
}

.action-text {
  color: #666;
  font-size: 14px;
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
  margin-top: 15px;
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
  flex: 1;
}

.comment-user {
  font-weight: 600;
  color: #333;
  font-size: 14px;
  margin-bottom: 5px;
}

.comment-text {
  color: #666;
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
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}

/* 用户统计对话框样式 */
.user-stats-content {
  padding: 20px;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: #f5f5f5;
  border-radius: 10px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.stat-title {
  font-size: 16px;
  color: #666;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.pending {
  color: #e6a733; /* 待审核颜色 */
}

.approved {
  color: #4caf50; /* 已通过颜色 */
}

.rejected {
  color: #f44336; /* 被拒绝颜色 */
}

.recent-posts h4 {
  margin-bottom: 15px;
  color: #333;
  font-size: 18px;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.post-item-mini {
  background: #f0f0f0;
  border-radius: 8px;
  padding: 10px 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #555;
}

.post-info {
  display: flex;
  flex-direction: column;
  margin-right: 10px;
}

.post-title-mini {
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.post-type {
  font-size: 12px;
  color: #666;
  background-color: #e0e0e0;
  padding: 3px 8px;
  border-radius: 4px;
  display: inline-block;
}

.post-status {
  display: flex;
  align-items: center;
  gap: 5px;
}

.post-time {
  color: #999;
  font-size: 12px;
}

.getStatusType {
  font-size: 12px;
}

.getStatusText {
  font-size: 12px;
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
</style>
