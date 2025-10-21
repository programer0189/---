<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElCarousel, ElCarouselItem, ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { postAPI } from '@/api/chat.js'
import { getCurrentUser } from '@/utils/auth.js'

const router = useRouter()

const carouselItems = ref([
  { image: new URL('@/assets/gate.jpg', import.meta.url).href, title: '校园风光 - 东南门' },
  { image: new URL('@/assets/library.jpg', import.meta.url).href, title: '艺术楼掠影' },
  { image: new URL('@/assets/lib.jpg', import.meta.url).href, title: '图书馆一角' },
])

// 各模块的帖子数据
const confessionPosts = ref([])
const activityPosts = ref([])
const studyPosts = ref([])
const lostPosts = ref([])
const expressPosts = ref([])
const chatPosts = ref([])

// 获取用户信息
const loginUser = getCurrentUser()

// 检查登录状态
const isLoggedIn = computed(() => {
  return !!loginUser
})

// 处理更多按钮点击
const handleMoreClick = (route, event) => {
  if (!isLoggedIn.value) {
    event.preventDefault()
    ElMessage.warning('请先注册账号')
    router.push('/login')
    return
  }
  // 已登录用户正常跳转
}

// 获取各模块的帖子数据
const fetchPosts = async () => {
  try {
    // 获取表白模块帖子 (type: 1)
    const confessionResponse = await postAPI.getPostsByType(1)
    if (confessionResponse && confessionResponse.code === 1 && confessionResponse.data) {
      confessionPosts.value = confessionResponse.data.slice(0, 5)
    }
    
    // 获取活动模块帖子 (type: 4)
    const activityResponse = await postAPI.getPostsByType(4)
    if (activityResponse && activityResponse.code === 1 && activityResponse.data) {
      activityPosts.value = activityResponse.data.slice(0, 5)
    }
    
    // 获取学习模块帖子 (type: 3)
    const studyResponse = await postAPI.getPostsByType(3)
    if (studyResponse && studyResponse.code === 1 && studyResponse.data) {
      studyPosts.value = studyResponse.data.slice(0, 5)
    }
    
    // 获取失物招领模块帖子 (type: 5)
    const lostResponse = await postAPI.getPostsByType(5)
    if (lostResponse && lostResponse.code === 1 && lostResponse.data) {
      lostPosts.value = lostResponse.data.slice(0, 5)
    }
    
    // 获取快递代取模块帖子 (type: 2)
    const expressResponse = await postAPI.getPostsByType(2)
    if (expressResponse && expressResponse.code === 1 && expressResponse.data) {
      expressPosts.value = expressResponse.data.slice(0, 5)
    }
    
    // 获取闲聊模块帖子 (type: 6)
    const chatResponse = await postAPI.getPostsByType(6)
    if (chatResponse && chatResponse.code === 1 && chatResponse.data) {
      chatPosts.value = chatResponse.data.slice(0, 5)
    }
  } catch (error) {
    console.error('获取帖子数据失败:', error)
    // 如果API调用失败，设置空数组避免页面错误
    confessionPosts.value = []
    activityPosts.value = []
    studyPosts.value = []
    lostPosts.value = []
    expressPosts.value = []
    chatPosts.value = []
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  return `${month}/${day}`
}

// 页面加载时获取帖子数据
onMounted(() => {
  fetchPosts()
})
</script>

<template>
  <div class="home-container">
    <!-- 轮播图 -->
    <div class="carousel-box">
      <el-carousel :interval="3000" height="250px" :loop="true" :arrow="false">
        <el-carousel-item v-for="item in carouselItems" :key="item.title">
          <img :src="item.image" class="carousel-image" :alt="item.title" />
          <div class="carousel-caption">{{ item.title }}</div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 我要表白 -->
    <div class="body-section">
      <div class="body-section-first">
        <div class="header-row">
          <div class="title_container">
            <div class="it1">
              <p class="w1">我</p>
              <p class="w2">要</p>
            </div>
            <div class="it2">
              <p class="w3">Confession</p>
              <p class="w4">表白</p>
            </div>
          </div>
          <router-link to="/app/confession" class="body-link" @click="handleMoreClick('/app/confession', $event)">
            <span class="body-link-text">更多</span>
            <el-icon><MoreFilled /></el-icon>
          </router-link>
        </div>
        <div class="content">
          <p class="content-text">表达你真挚的情感！</p>
          
          
                     <!-- 帖子列表 -->
           <div class="posts-list">
             <div v-for="post in confessionPosts" :key="post.id" class="post-item">
               <div class="post-date">{{ formatTime(post.createTime) }}</div>
               <div class="post-info">
                 <div class="post-title">{{ post.title || '无标题' }}</div>
               </div>
             </div>
             <div v-if="confessionPosts.length === 0" class="no-posts">
               暂无帖子
             </div>
           </div>
          <div class="content-stats">
            <span class="stat-item">📝 今日发布: {{ confessionPosts.length }}</span>
        </div>
        </div>
      </div>
    </div>

    <!-- 校园活动 -->
    <div class="body-section">
      <div class="body-section-first">
        <div class="header-row">
          <div class="title_container">
            <div class="it1">
              <p class="w1">校</p>
              <p class="w2">园</p>
            </div>
            <div class="it2">
              <p class="w3">Activity</p>
              <p class="w4">活动</p>
            </div>
          </div>
          <router-link to="/app/activity" class="body-link" @click="handleMoreClick('/app/activity', $event)">
            <span class="body-link-text">更多</span>
            <el-icon><MoreFilled /></el-icon>
          </router-link>
        </div>
        <div class="content">
          <p class="content-text">发现校园里的精彩活动！</p>
       
          
                     <!-- 帖子列表 -->
           <div class="posts-list">
             <div v-for="post in activityPosts" :key="post.id" class="post-item">
               <div class="post-date">{{ formatTime(post.createTime) }}</div>
               <div class="post-info">
                 <div class="post-title">{{ post.title || '无标题' }}</div>
               </div>
             </div>
             <div v-if="activityPosts.length === 0" class="no-posts">
               暂无帖子
             </div>
           </div>


          <div class="content-stats">
            <span class="stat-item">🎯 进行中: {{ activityPosts.length }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 学习资料 -->
    <div class="body-section">
      <div class="body-section-first">
        <div class="header-row">
          <div class="title_container">
            <div class="it1">
              <p class="w1">学</p>
              <p class="w2">习</p>
            </div>
            <div class="it2">
              <p class="w3">Study</p>
              <p class="w4">资料</p>
            </div>
          </div>
          <router-link to="/app/study" class="body-link" @click="handleMoreClick('/app/study', $event)">
            <span class="body-link-text">更多</span>
            <el-icon><MoreFilled /></el-icon>
          </router-link>
        </div>
        <div class="content">
          <p class="content-text">让学习变得更加高效和有趣!</p>
         
          
                     <!-- 帖子列表 -->
           <div class="posts-list">
             <div v-for="post in studyPosts" :key="post.id" class="post-item">
               <div class="post-date">{{ formatTime(post.createTime) }}</div>
               <div class="post-info">
                 <div class="post-title">{{ post.title || '无标题' }}</div>
               </div>
             </div>
             <div v-if="studyPosts.length === 0" class="no-posts">
               暂无帖子
             </div>
           </div>
          <div class="content-stats">
            <span class="stat-item">📖 资料总数: {{ studyPosts.length }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 失物招领 -->
    <div class="body-section">
      <div class="body-section-first">
        <div class="header-row">
          <div class="title_container">
            <div class="it1">
              <p class="w1">失</p>
              <p class="w2">物</p>
            </div>
            <div class="it2">
              <p class="w3">Lost</p>
              <p class="w4">招领</p>
            </div>
          </div>
          <router-link to="/app/lost" class="body-link" @click="handleMoreClick('/app/lost', $event)">
            <span class="body-link-text">更多</span>
            <el-icon><MoreFilled /></el-icon>
          </router-link>
        </div>
        <div class="content">
          <p class="content-text">失物招领！</p>
        
          
                     <!-- 帖子列表 -->
           <div class="posts-list">
             <div v-for="post in lostPosts" :key="post.id" class="post-item">
               <div class="post-date">{{ formatTime(post.createTime) }}</div>
               <div class="post-info">
                 <div class="post-title">{{ post.title || '无标题' }}</div>
               </div>
             </div>
             <div v-if="lostPosts.length === 0" class="no-posts">
               暂无帖子
             </div>
           </div>
          <div class="content-stats">
            <span class="stat-item">🔍 待认领: {{ lostPosts.length }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 快递代取 -->
    <div class="body-section">
      <div class="body-section-first">
        <div class="header-row">
          <div class="title_container">
            <div class="it1">
              <p class="w1">快</p>
              <p class="w2">递</p>
            </div>
            <div class="it2">
              <p class="w3">Express</p>
              <p class="w4">代取</p>
            </div>
          </div>
          <router-link to="/app/express" class="body-link" @click="handleMoreClick('/app/express', $event)">
            <span class="body-link-text">更多</span>
            <el-icon><MoreFilled /></el-icon>
          </router-link>
        </div>
        <div class="content">
          <p class="content-text">发布快递代取信息，互帮互助！</p>
        
          
                     <!-- 帖子列表 -->
           <div class="posts-list">
             <div v-for="post in expressPosts" :key="post.id" class="post-item">
               <div class="post-date">{{ formatTime(post.createTime) }}</div>
               <div class="post-info">
                 <div class="post-title">{{ post.title || '无标题' }}</div>
               </div>
             </div>
             <div v-if="expressPosts.length === 0" class="no-posts">
               暂无帖子
             </div>
           </div>
          <div class="content-stats">
            <span class="stat-item">📦 待代取: {{ expressPosts.length }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 闲聊灌水 -->
    <div class="body-section">
      <div class="body-section-first">
        <div class="header-row">
          <div class="title_container">
            <div class="it1">
              <p class="w1">闲</p>
              <p class="w2">聊</p>
            </div>
            <div class="it2">
              <p class="w3">Chat</p>
              <p class="w4">灌水</p>
            </div>
          </div>
          <router-link to="/app/chat" class="body-link" @click="handleMoreClick('/app/chat', $event)">
            <span class="body-link-text">更多</span>
            <el-icon><MoreFilled /></el-icon>
          </router-link>
        </div>
        <div class="content">
          <p class="content-text">分享校园生活趣事！</p>
         
          
                     <!-- 帖子列表 -->
           <div class="posts-list">
             <div v-for="post in chatPosts" :key="post.id" class="post-item">
               <div class="post-date">{{ formatTime(post.createTime) }}</div>
               <div class="post-info">
                 <div class="post-title">{{ post.title || '无标题' }}</div>
               </div>
             </div>
             <div v-if="chatPosts.length === 0" class="no-posts">
               暂无帖子
             </div>
           </div>

          <div class="content-stats">
            <span class="stat-item">💭 今日话题: {{ chatPosts.length }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.body-section {
  background: #ffffff;
  border-radius: 12px;
  margin: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #e8e8e8;
}

.header-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1.5px solid #f0f0f0;
}

.body-title h2 {
  margin: 0;
  font-size: 1.4rem;
  color: #2c3e50;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.title_container {
  display: flex;
  flex-direction: row;
}

/* 左侧中文标题 */
.it1 {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f45d8f 0%, #c44569 100%);
  color: #ffffff;
  margin-right: 8px;
  border-radius: 2px;
  width: 50px;

}

/* 中文标题文字 */
.w1, .w2 {
  font-size: 32px;
  font-family: "STZhongsong", "华文中宋", serif;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* 右侧英文标题 */
.it2 {
  display: flex;
  flex-direction: column;
}

/* 英文标题 */
.w3 {
  font-size: 32px;
  font-family: Arial, Helvetica, sans-serif;
  line-height: 1;
  color: hwb(0 82% 18% / 0.961);
}

/* 中文副标题 */
.w4 {
  font-size: 35px;
  line-height: 1.1;
  color: #000000;
  font-weight: bold;
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
  border-bottom: 1px solid #ec1e25;
}

.body-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #3498db;
  font-size: 0.9rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.body-link:hover {
  background: #f8f9fa;
  color: #2980b9;
}

.body-link-text {
  font-size: 0.9rem;
}

.body-link .el-icon {
  font-size: 1rem;
}


.content {
  text-align: left;
  padding: 1rem;
}

.content-text {
  margin-top: 5px;
  color: #555;
  font-size: 1rem;
  line-height: 1.7;
  text-align: center;
  font-weight: 400;
}

.content-stats {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  justify-content: center;
}

.stat-item {
  background: #f0f0f0;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  font-size: 0.85rem;
  color: #333333;
  border: 1px solid #e0e0e0;
  font-weight: 500;
}

.carousel-box { 
  background: white;
  border-radius: 12px;
  padding-bottom: 0px;
  position: relative;
}




.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
:deep(.el-carousel__indicators) {
  display: none !important;
}
/* 轮播图隐藏箭头和指示器 */
:deep(.el-carousel__arrow) {
  display: none !important;
}
.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
  color: #fff;
  padding: 18px;
  font-size: 16px;
  font-weight: 500;
}


.section-title {
  text-align: center;
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 2rem;
  font-weight: 600;
}

.category-section {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #ffffff;
}

.category-header h3 {
  margin: 0;
  font-size: 1.3rem;
  color: #2c3e50;
  font-weight: 600;
}

.more-link {
  color: #3498db;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: color 0.3s ease;
}

.more-link:hover {
  color: #2980b9;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.news-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.8rem 1rem;
  background: #f8f9fa;
  border-radius: 12px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
}

.news-item:hover {
  background: #e9ecef;
  border-color: #ced4da;
  transform: translateX(5px);
}



.news-title {
  flex-grow: 1;
  font-size: 1rem;
  color: #2c3e50;
  font-weight: 500;
  line-height: 1.4;
}

.news-time {
  font-size: 0.8rem;
  color: #666;
  white-space: nowrap;
}

.home-container {
  width: 100vw;
  padding-bottom: 80px; /* 添加底部边距，避免被footer遮挡 */
}

/* 帖子列表样式 */
.posts-list {
  margin-top: 1px;
  max-height: 300px;
  overflow-y: auto;
}

.post-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  background: transparent;
  border: none;
  border-radius: 0;
  padding: 12px 0;
  margin-bottom: 0;
  transition: none;
  border-bottom: 1px solid #f0f0f0;
}



.post-item:hover {
  background: transparent;
  border-color: transparent;
  transform: none;
  box-shadow: none;
}

.post-date {
  font-size: 25px;
  font-weight: bold;
  color: #333;
  min-width: 70px;
  margin-right: 20px;
  line-height: 1.2;
}

.post-info {
  flex: 1;
  display: flex;
  align-items: center;
}

.post-title {
  font-size: 20px;
  font-weight: 500;
  color: #333;
  line-height: 1.4;
}

.post-meta {
  display: none;
}

.post-time {
  display: none;
}

.post-author {
  display: none;
}

.no-posts {
  text-align: center;
  color: #999;
  font-size: 12px;
  padding: 20px;
  font-style: italic;
}


</style>
