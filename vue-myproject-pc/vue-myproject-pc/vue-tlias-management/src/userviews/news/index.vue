<script setup>
import { ref, onMounted, computed, shallowRef } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CampusIndex from '@/news_vue/campus/index.vue'
import CampusLibrary from '@/news_vue/campus/library.vue'
import CampusClub from '@/news_vue/campus/club.vue'
import CampusCanteen from '@/news_vue/campus/canteen.vue'
import EventIndex from '@/news_vue/event/index.vue'
import EventCompetition from '@/news_vue/event/competition.vue'
import EventSigner from '@/news_vue/event/signer.vue'
import AcademicLecture from '@/news_vue/academic/lecture.vue'
import AcademicIndex from '@/news_vue/academic/index.vue'
import AcademicResearch from '@/news_vue/academic/research.vue'
import NoticeHoliday from '@/news_vue/notice/holiday.vue'
import NoticeExam from '@/news_vue/notice/exam.vue'
import NoticeSafety from '@/news_vue/notice/safety.vue'
import NoticeNetwork from '@/news_vue/notice/network.vue'
const loginName = ref('')
onMounted(() => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser && loginUser.username) {
    loginName.value = loginUser.username
  }
})

// 退出登录
const logout = () => {
  ElMessage.success('退出成功')
  localStorage.removeItem('loginUser')
  loginName.value = ''
  router.push('/login')
}
const route = useRoute()
const router = useRouter()
const currentUser=ref(null)
// 当前显示的内容：null 表示显示标题列表
const currentNewsComponent = ref(null)
// 动态导入的组件
const dynamicComponent = shallowRef(null)

// 从路由参数获取信息
const currentCategory = ref('campus')
const selectedNewsId = ref(null)
const selectedNewsTitle = ref('')

// 新闻数据（只包含基本信息，不包含详细内容）
const newsData = {
  campus: [
    { id: 1, title: '校园活动通知', summary: '8月15日东南门广场举办绿色校园主题活动，包括植树和讲座。', component: CampusIndex },
    { id: 2, title: '图书馆新增电子资源', summary: '为提升学生学习体验，图书馆新增多种电子资源。', component: CampusLibrary },
    { id: 3, title: '学生社团招新活动', summary: '新学期伊始，各大学生社团开始招新活动。', component: CampusClub },
    { id: 13, title: '食堂新菜品推荐', summary: '食堂推出多款新菜品，丰富师生用餐选择。', component: CampusCanteen }
  ],
  event: [
    { id: 4, title: '春季运动会报名开始', summary: '一年一度的春季运动会即将开始，现开始接受报名。', component: EventIndex },
    { id: 5, title: '创新创业大赛启动', summary: '为激发学生创新精神，学校启动创新创业大赛。', component: EventCompetition },
    { id: 6, title: '校园歌手大赛海选', summary: '展现青春风采的舞台，校园歌手大赛海选开始。', component: EventSigner }
  ],
  academic: [
    { id: 7, title: '学术讲座：人工智能发展', summary: '特邀专家分享AI领域最新进展，探讨未来发展。', component: AcademicLecture },
    { id: 8, title: '研究生学术论坛', summary: '促进学术交流与合作，研究生学术论坛即将举办。', component: AcademicIndex },
    { id: 9, title: '国际学术会议通知', summary: '2025年国际教育技术大会即将召开，现征集论文。', component: AcademicIndex },
    { id: 14, title: '学术研究：量子计算新突破', summary: '我校科研团队在量子计算领域取得重要突破。', component: AcademicResearch }
  ],
  notice: [
    { id: 10, title: '寒假放假安排通知', summary: '根据学校安排，寒假将于1月20日开始。', component: NoticeHoliday },
    { id: 11, title: '期末考试时间安排', summary: '本学期期末考试将于12月25日开始。', component: NoticeExam },
    { id: 12, title: '校园安全注意事项', summary: '为确保校园安全，请师生注意以下安全事项。', component: NoticeSafety },
    { id: 15, title: '校园网升级通知', summary: '校园网系统将进行升级维护，提升网络服务质量。', component: NoticeNetwork }
  ]
}

// 计算当前类别的新闻列表
const currentNewsList = computed(() => {
  return newsData[currentCategory.value] || []
})

// 显示新闻详情
async function showNews(news) {
  try {
    selectedNewsId.value = news.id
    selectedNewsTitle.value = news.title
    currentNewsComponent.value = news
    
    // 直接使用组件，不需要动态导入
    if (news.component) {
      dynamicComponent.value = news.component
      console.log('组件设置成功:', dynamicComponent.value)
    } else {
      console.warn('新闻没有组件')
      dynamicComponent.value = null
    }
  } catch (error) {
    console.error('加载新闻详情组件失败:', error)
    dynamicComponent.value = null
  }
}

// 返回新闻列表
function backToList() {
  currentNewsComponent.value = null
  dynamicComponent.value = null
  selectedNewsId.value = null
  selectedNewsTitle.value = ''
}

// 页面加载时检查路由参数
onMounted(async () => {
  // 如果从首页跳转过来，获取传递的参数
  if (route.query.category) {
    currentCategory.value = route.query.category
  }
  if (route.query.newsId) {
    selectedNewsId.value = parseInt(route.query.newsId)
    selectedNewsTitle.value = route.query.title || ''
    
    // 根据ID找到对应的新闻并显示
    const news = newsData[currentCategory.value]?.find(item => item.id === selectedNewsId.value)
    if (news) {
      await showNews(news)
    }
  }
})

// 切换新闻类别
function changeCategory(category) {
  currentCategory.value = category
  currentNewsComponent.value = null
  dynamicComponent.value = null
  selectedNewsId.value = null
  selectedNewsTitle.value = ''
}

// 获取当前日期（用于显示）
function getCurrentDay() {
  return new Date().getDate().toString().padStart(2, '0')
}

function getCurrentMonth() {
  const now = new Date()
  return `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}`
}
</script>

<template>
  <div class="container">
    <!-- 顶部标题栏 -->
    <header class="header">
      <h1 class="site-title">校智通</h1>
      <nav class="nav">
        <router-link to="/home">首页</router-link>
        <router-link to="/home/mapmessage">校园地图</router-link>
        <router-link to="/home/navigate">校园导航</router-link>
        <router-link to="/home/news">校园新闻</router-link>
        <router-link to="/home/chat">校园论坛</router-link>
      </nav>
      <div class="user-info">
        <span class="username">欢迎 {{loginName}}</span>
        <button class="logout-btn" @click="logout">退出</button>
      </div>
    </header>

    <!-- 主体内容 -->
    <div class="main">
      <el-container>
        <!-- 左侧分类 -->
        <el-aside width="200px">
          <div class="title_container">
            <div class="it1">
              <p class="w1">最</p>
              <p class="w2">新</p>
            </div>
            <div class="it2">
              <p class="w3">News</p>
              <p class="w4">消息</p>
            </div>
          </div>
          <el-menu :default-active="currentCategory" @select="changeCategory" class="nav-menu">
            <el-menu-item index="campus">校园新闻</el-menu-item>
            <el-menu-item index="event">活动公告</el-menu-item>
            <el-menu-item index="academic">学术交流</el-menu-item>
            <el-menu-item index="notice">通知通告</el-menu-item>
          </el-menu>
        </el-aside>

        <!-- 主区域 -->
        <el-main class="main-container">
          <div v-if="!currentNewsComponent">
            <!-- 面包屑导航 -->
            <div class="breadcrumb">
              当前您的位置: 首页 > {{ 
                currentCategory === 'campus' ? '校园新闻' : 
                currentCategory === 'event' ? '活动公告' : 
                currentCategory === 'academic' ? '学术交流' : '通知通告' 
              }}
            </div>
            
            <!-- 页面标题 -->
            <h2 class="page-title">
              {{ currentCategory === 'campus' ? '校园新闻' : 
                 currentCategory === 'event' ? '活动公告' : 
                 currentCategory === 'academic' ? '学术交流' : '通知通告' }}
            </h2>
            
            <!-- 新闻列表 -->
            <ul class="news-list">
              <li v-for="news in currentNewsList" :key="news.id"
                  @click="showNews(news)"
                  class="news-item"
              >
                <div class="news-date">
                  <span class="date-day">{{ getCurrentDay() }}</span>
                  <span class="date-month">{{ getCurrentMonth() }}</span>
                </div>
                <div class="news-content">
                  {{ news.title }}
                </div>
              </li>
            </ul>
          </div>

          <div v-else>
            <div class="news-detail-header">
              <el-button type="primary" size="small" @click="backToList" class="back-btn">
                返回列表
              </el-button>
            </div>
            
            <!-- 动态渲染新闻详情组件 -->
            <component 
              v-if="dynamicComponent" 
              :is="dynamicComponent" 
              class="news-detail-component"
            />
            
            <!-- 如果组件加载失败，显示基本信息 -->
            <div v-else class="news-detail-fallback">
              <h1 class="news-detail-title">{{ currentNewsComponent.title }}</h1>
              <div class="news-detail-meta">
                <span class="news-category">{{ 
                  currentCategory === 'campus' ? '校园新闻' : 
                  currentCategory === 'event' ? '活动公告' : 
                  currentCategory === 'academic' ? '学术交流' : '通知通告' 
                }}</span>
                <span class="news-id">新闻ID：{{ currentNewsComponent.id }}</span>
              </div>
              <div class="news-detail-summary">{{ currentNewsComponent.summary }}</div>
              <p class="news-loading">正在加载新闻详情...</p>
            </div>
          </div>
        </el-main>
      </el-container>
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      <p>© 2025 校智通. All rights reserved.</p>
    </footer>
  </div>
</template>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 50%, #dee2e6 100%);
  display: flex;
  flex-direction: column;
}

/* 顶部标题栏 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  padding: 0 30px;
  height: 90px;
  color: #fff;
  box-shadow: 0 4px 20px rgba(30, 58, 138, 0.3);
}

.site-title {
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.nav {
  display: flex;
  gap: 20px;
}

.nav a {
  color: #fff;
  text-decoration: none;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.2s ease;
  position: relative;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.nav a:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-1px);
}

.nav a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: #fff;
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav a:hover::after {
  width: 80%;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  font-size: 15px;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.logout-btn {
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 5px 16px;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 500;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 主体布局 */
.main {
  flex: 1;
  padding: 20px;
  box-sizing: border-box;
}

.title_container {
  display: flex;
  flex-direction: row;
  margin-bottom: 20px;
  padding: 20px 15px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(187, 20, 20, 0.08);
}

/* 最新 */
.it1 {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #dc2626, #ef4444);
  color: #fff;
  margin-right: 12px;
  border-radius: 4px;
  width: 80px;
  height: 80px;
  box-shadow: 0 4px 12px rgba(220, 38, 38, 0.3);
}

/* 最新二字 */
.w1, .w2 {
  font-size: 28px;
  font-family: "STZhongsong", "华文中宋", serif;
  font-weight: bold;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.it2 {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* news */
.w3 {
  font-size: 52px;
  font-family: "Arial", "Helvetica", sans-serif;
  line-height: 1;
  color: #6b7280;
  font-weight: 300;
  letter-spacing: -1px;
}

/* 消息 */
.w4 {
  font-size: 32px;
  line-height: 1.1;
  color: #1f2937;
  font-weight: 600;
  font-family: "Microsoft YaHei", "微软雅黑", "PingFang SC", sans-serif;
  margin-top: 2px;
}

.nav-menu {
  height: 680px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 5px;
  padding: 10px 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
}

.category-title {
  padding: 1px;
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: #333;
  border-bottom: 1px solid #151b20;
  text-align: right;
}

.main-container {
  height: 720px;
  /* overflow: auto; */
  background-color: transparent;
  padding: 20px;
}

.breadcrumb {
  color: #374151;
  font-size: 14px;
  margin-left: 65px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #e5e7eb;
  text-align: right;
  font-weight: 500;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #1f2937;
  margin-left: 65px;
  margin-bottom: 25px;
  font-family: "Microsoft YaHei", "微软雅黑", "PingFang SC", sans-serif;
  position: relative;
}

.page-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 65px;
  width: 60px;
  height: 3px;
  background: rgba(62, 0, 0, 0.473);
  border-radius: 2px;
}

.news-list {

  margin-left: 10px;
}

.news-item {
  display: flex;
  align-items: flex-start;
  padding: 18px 0;



  margin-bottom: 8px;
  border-bottom: 1px solid #aeaeae;
}



.news-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 24px;
  min-width: 65px;
  flex-shrink: 0;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  padding: 12px 8px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.date-day {
  font-size: 26px;
  font-weight: 700;
  color: #121315;
  line-height: 1;
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
}

.date-month {
  font-size: 13px;
  color: #1f385c80;
  margin-top: 4px;
  font-weight: 500;
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
}

.news-content {
  flex: 1;
  font-size: 18px;
  color: #1f2937;
  line-height: 1.6;
  padding-top: 10px;
  font-weight: 500;
  font-family: "Microsoft YaHei", "微软雅黑", "PingFang SC", sans-serif;
}

/* 侧边栏样式修改 */
:deep(.el-aside) {
  background-color: transparent;
  margin-right: 15px;

  height: auto;
}

:deep(.el-menu) {
  background-color: transparent;

  padding: 0 5px;
}

:deep(.el-menu-item) {
  background-color: transparent;
  border-radius: 5px;

  transition: all 0.3s ease;
  color: #2b2c2d;
  font-size: 16px;
  font-weight: 500;
  font-family: "Microsoft YaHei", "微软雅黑", "PingFang SC", sans-serif;
  margin-bottom: 20px;
  height: 45px;
  line-height: 60px;
 
}


:deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: #ffffff;
  
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.3);
}

/* 页脚 */
.footer {
  text-align: center;
  padding: 25px;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  box-shadow: 0 -4px 20px rgba(15, 23, 42, 0.1);
  position: relative;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, #3b82f6, transparent);
}

/* 新闻详情页样式 返回按钮*/
.news-detail-header {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 20px;
  padding-left: 65px;
}

.back-btn {
  background-color: #3b82f6;

  color: white;
  padding: 8px 15px;
  font-size: 16px;
  font-weight: 600;
 
}

.back-btn:hover {

  text-decoration: underline;
}

.news-detail-component {
  margin-left: 50px;
}

.news-detail-fallback {
  padding: 20px 0;
  margin-left: 80px;
}

.news-detail-title {
  font-size: 36px;
  font-weight: 800;
  color: #1f2937;
  margin-bottom: 15px;
  font-family: "Microsoft YaHei", "微软雅黑", "PingFang SC", sans-serif;
  position: relative;
}

.news-detail-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 60px;
  height: 3px;
  background: rgba(62, 0, 0, 0.473);
}

.news-detail-meta {
  display: flex;
  align-items: center;
  gap: 50px;
  margin-bottom: 15px;
  color: #4b5563;
  font-size: 18px;
  font-weight: 500;
  font-family: "Microsoft YaHei", "微软雅黑", "PingFang SC", sans-serif;
}

.news-detail-summary {
  font-size: 20px;
  color: #374151;
  line-height: 1.8;
  margin-bottom: 25px;
  font-family: "Microsoft YaHei", "微软雅黑", "PingFang SC", sans-serif;
}

.news-loading {
  font-size: 18px;
  color: #6b7280;
  font-style: italic;
  margin-top: 15px;
}
</style>