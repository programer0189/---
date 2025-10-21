<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElCarousel, ElCarouselItem, ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
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

const value = ref(new Date())
// 在 <script setup> 顶部添加
const eventsList = ref([
  {
    id: 4,
    title: '春季运动会报名开始',
    summary: '一年一度的春季运动会即将开始，现开始接受报名。',
    image: new URL('@/assets/sports.jpg', import.meta.url).href
  },
  {
    id: 5,
    title: '创新创业大赛启动',
    summary: '激发学生创新精神，学校启动创新创业大赛。',
    image: new URL('@/assets/competition.jpg', import.meta.url).href
  },
  {
    id: 6,
    title: '校园歌手大赛海选',
    summary: '展现青春风采的舞台，校园歌手大赛海选开始。',
    image: new URL('@/assets/signer.jpg', import.meta.url).href
  }
])

const goToEvent = (event) => {
  router.push({
    path: '/home/news',
    query: {
      category: 'event',
      newsId: event.id,
      title: event.title
    }
  })
}

// 定义轮播图
const carouselItems = ref([
  { image: new URL('@/assets/gate.jpg', import.meta.url).href, title: '校园风光 - 东南门' },
  { image: new URL('@/assets/library.jpg', import.meta.url).href, title: '艺术楼掠影' },
  { image: new URL('@/assets/lib.jpg', import.meta.url).href, title: '图书馆一角' },
])

// 定义新闻
const newsItems = ref([
  { id: 1, title: '校园活动通知', summary: '8月15日东南门广场举办绿色校园主题活动，包括植树和讲座。', images: [new URL('@/assets/south.jpg', import.meta.url).href] },
  { id: 2, title: '学术讲座预告', summary: '8月20日图书馆报告厅探讨人工智能未来趋势。', images: [new URL('@/assets/library.jpg', import.meta.url).href] },
  { id: 3, title: '招生信息更新', summary: '2025秋季新增数据科学专业，9月1日截止报名。', images: [new URL('@/assets/view.jpg', import.meta.url).href] },
  { id: 4, title: '校庆活动', summary: '10月10日举办校友返校和文艺演出。', images: [new URL('@/assets/sports.jpg', import.meta.url).href]},
  { id: 5, title: '课程安排', summary: '新增Python课程，每周三下午2点至4点。', images: [new URL('@/assets/class.jpg', import.meta.url).href] },
  { id: 6, title: '科研成果发布', summary: '研发高效太阳能电池，8月25日展示。', images: [new URL('@/assets/effort.jpg', import.meta.url).href] },
  { id: 7, title: '新生指南', summary: '8月28-30日报到，带录取通知书。', images: [new URL('@/assets/greener.jpg', import.meta.url).href] },
  { id: 8, title: '校际活动', summary: '9月10日新生参观图书馆。', images: [new URL('@/assets/library.jpg', import.meta.url).href, new URL('@/assets/sports.jpg', import.meta.url).href] },
])
const hotItems = ref([
  { id: 1, title: '最新校园政策解读' },
  { id: 2, title: '图书馆座位预约新规' },
  { id: 3, title: '食堂新菜品推荐' },
  { id: 4, title: '校园网升级通知' },
  { id: 5, title: '毕业生就业情况调查' }
])
// 分页逻辑，每页 2 个卡片
const newsPages = computed(() => {
  const pages = []
  for (let i = 0; i < newsItems.value.length; i += 2) {
    pages.push(newsItems.value.slice(i, i + 2))
  }
  return pages
})

const goToNews = (id) => {
  router.push({ path: `/home/news` })
}
// 这个函数跳转到AI聊天界面
const gotoAIchat=()=>
{
  router.push({path:`/home/ai`})

}
// 跳转校园论坛
const goTochat = () => {
  router.push({ path: `/home/chat` });
};
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
      <!-- 上部分：轮播图 + 日历 -->
      <section class="top-section">
                 <div class="carousel-box">
           <el-carousel :interval="3000" :arrow="'hover'" height="300px" :loop="true">
             <el-carousel-item v-for="item in carouselItems" :key="item.title">
               <img :src="item.image" class="carousel-image" :alt="item.title" />
               <div class="carousel-caption">{{ item.title }}</div>
             </el-carousel-item>
           </el-carousel>
         </div>
        <div class="calendar-box">
          <el-calendar v-model="value" />
        </div>
      </section>
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

      <!-- 中下部分 -->
      <section class="middle-section">
        <!-- 新闻区域轮播 -->
        <div class="news-section">
          <div class="news-carousel">
            <el-carousel :interval="0" arrow="always" height="500px" :loop="false">
              <el-carousel-item v-for="(page, index) in newsPages" :key="index">
                <div class="news-container">
                  <div
                    class="news-card"
                    v-for="item in page"
                    :key="item.id"
                    @click="goToNews(item.id)"
                  >
                    <img :src="item.images[0]" class="news-image" v-if="item.images.length > 0" />
                    <div class="news-card-title">{{ item.title }}</div>
                    <div class="news-summary">{{ item.summary }}</div>
                  </div>
                </div>
              </el-carousel-item>
            </el-carousel>
          </div>
          <div class="news-description">
            <div class="news-description-title">新闻动态</div>
            <div class="news-description-content">
              <p>校园新闻是学校师生了解校内外重要事件和活动的主要渠道。</p>
              <p>在这里您可以找到最新的校园活动通知、学术讲座信息、招生政策更新等重要内容。</p>
              <p>我们定期更新校园内的各类新闻，确保您不会错过任何重要信息。</p>
              <p>点击新闻卡片可以查看详细内容，了解更多校园动态。</p>
            </div>
            <div class="news-description-footer">
              <button class="more-news-btn" @click="goToNews">查看更多新闻</button>
            </div>
          </div>
        </div>

<div class="title_container">
  <div class="it1">
    <p class="w1">活</p>
    <p class="w2">动</p>
  </div>
<div class="it2">
  <p class="w3">Events</p>
  <p class="w4">信息</p>
  </div>
</div>
   <!-- 这里做AI接入图标和页面跳转 ,一个容器包含两个元素，按行显示，分别为AI头像和名称-->
  

        <!-- 活动和热点信息在一行 -->
        <div class="events-hot-section">
         <!-- 活动展示区域 -->
  <div class="events-container">
    <div
      v-for="event in eventsList"
      :key="event.id"
      class="event-card"
      @click="goToEvent(event)"
    >
      <img :src="event.image" alt="event.title" class="event-image" />
      <div class="event-info">
        <h3 class="event-title">{{ event.title }}</h3>
        <p class="event-summary">{{ event.summary }}</p>
      </div>
    </div>
  </div>

          <aside class="hot-section">
<div class="title_container">
  <div class="it1">
    <p class="w1">校</p>
    <p class="w2">园</p>
  </div>
<div class="it2">
  <p class="w3">Chat</p>
  <p class="w4">论坛</p>
  </div>
</div>
    <div class="hot-items">
      <div 
        class="hot-item" 
        v-for="(item, index) in hotItems" 
        :key="index"
        @click="goTochat"
      >
        <div class="hot-item-content">
          <span class="hot-item-text">{{ item.title }}</span>
        </div>
      </div>
    </div>
  </aside>
        </div>
      </section>
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      © 2025 校智通 | 技术支持：校园导航团队
    </footer>
  </div>
</template>

<style scoped>
.AI-container {
  position: fixed;
  right: 20px;
  top: 50%;

  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 10px;
  border: 2px solid rgba(17, 17, 17, 0.1);
 

}
.AI-icon {
  width: 79px;
  height: 79px;
  border-radius: 50%;
  
  margin-bottom: 5px;
  border:1px solid rgba(8, 7, 7, 0.1);
  
  transition: all 0.3s ease;

}

.AI-icon:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 16px rgba(0, 123, 255, 0.4);
}

.AI-name {
  font-size: 14px;
  font-weight: 600;
  color: #000000;
  text-align: center;
 

}
.AI-icon img {
  width: 100%;
  height: 100%;
}
.news-description {
  width: 350px;
  height: 350px;
  display: flex;
  flex-direction: column;
  padding: 20px;
  background: white;

  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.08);
}

.news-description-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  text-align: center;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}


.news-description-content p {
  font-size: 14px;
  color: #646060;
  text-indent: 2em;
  line-height: 1.5;
  margin-bottom: 20px;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.news-description-footer {
  margin-top: 12px;
  text-align: right;
}

.more-news-btn {
  background: #007aff;
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.more-news-btn:hover {
  background: #0056cc;
  transform: translateY(-1px);
}

/* 页面基础样式 */
.container {
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", "Helvetica Neue", Arial, sans-serif;
  background: #f5f5f7;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  box-sizing: border-box;
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

.title_container {
  display: flex;
  flex-direction: row;
  
}

/* 最新 */
.it1 {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #bf3434;
  color: #fff;
  margin-right: 8px;
  border-radius: 2px;
  width: 50px;
}

/* 最新二字 */
.w1, .w2 {
  font-size: 32px;
  font-family: "STZhongsong", "华文中宋", serif;
}

.it2 {
  display: flex;
  flex-direction: column;
}

/* news */
.w3 {
  font-size: 60px;
  font-family: Arial, Helvetica, sans-serif;
  line-height: 1;
  color: hwb(0 82% 18% / 0.961);
}

/* 消息 */
.w4 {
  font-size: 35px;
  line-height: 1.1;
  color: #000000;
  font-weight: bold;
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
  border-bottom: 1px solid #ec1e25;

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
  width: 85%;
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

/* 轮播 + 日历 */
.top-section {
  display: flex;
  gap: 20px;
  margin-bottom: 25px;
}

.carousel-box {
  flex: 2;
  background: white;
  border-radius: 12px;
  height: 300px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.08);
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
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

/* 自定义轮播图按钮样式 */
:deep(.el-carousel__arrow) {
  width: 70px;
  height: 70px;
  background: rgba(154, 151, 151, 0.517);
 
  font-size: 20px;
  color: #080707;
  
  display: flex;
  align-items: center;
  justify-content: center;
}
:deep(.el-carousel__arrow:hover){
  background: #fff7f7;
}
.calendar-box {
  flex: 1;
  background: rgb(135, 135, 135);
  border-radius: 12px;
  height: 300px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
 
  overflow: hidden;
}

.calendar-box .el-calendar {
  width: 100%;
  height: 100%;
  font-size: 14px;
}

/* 通过深度选择器覆盖 el-calendar 内部样式 */
:deep(.el-calendar) {
  font-size: 12px;
  height: 100%;
  width: 100%;

}

/* 日历头部样式 */
:deep(.el-calendar__header) {
  justify-content: space-between;
  padding: 15px 20px 10px 20px;
  margin-bottom: 15px;
  border-bottom: 1px solid #e4e7ed;
}

/* 日历主体样式 */
:deep(.el-calendar__body) {
  padding: 0 10px 10px 10px;

}

/* 星期标题样式 */
:deep(.el-calendar__body .el-calendar-table thead th) {
  padding: 5px 0;
  font-size: 12px;
  font-weight: 500;
  border-bottom: 1px solid #e4e7ed;
  color: #606266;
}

/* 日期格子样式 */
:deep(.el-calendar__body .el-calendar-day) {
  height: 28px !important;
  width: 100% !important;
  text-align: center;

  border-radius: 4px;
  transition: all 0.2s ease;
  border: 1px solid transparent;
}

:deep(.el-calendar__body .el-calendar-day:hover) {
  background: rgba(0, 122, 255, 0.1);
  color: #007aff;
  border: 1px solid #007aff;
}

/* 今天日期样式 */
:deep(.el-calendar__body .el-calendar-day.is-today) {
  background: #007aff;
  color: white;
  font-weight: 600;
  border: 1px solid #007aff;
}

/* 选中日期样式 */
:deep(.is-selected) {
  background: rgba(0, 122, 255, 0.2) !important;
  color: #007aff !important;
  border-radius: 4px;
  border: 1px solid #007aff;
}

/* 限制表格高度 */
:deep(.el-calendar-table) {
  height: 100%;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

:deep(.el-calendar-table tbody) {
  height: calc(100% - 40px);
}

:deep(.el-calendar-table td) {
  border: 1px solid #f0f0f0;
  padding: 1px;
}

:deep(.el-calendar-table tr) {
  border-bottom: 1px solid #f0f0f0;
}

/* 新闻 + 活动信息 */
.middle-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}



.news-section {
  display: flex;
  flex-direction: row;
  height:400px;
}

.news-carousel {
  flex: 2;
}

.news-container {
  display: flex;
  justify-content: space-between;
  height: 350px;
  padding: 10px;

  gap: 20px;
}

.news-card {
  background: white;
  border-radius: 10px;
  width: 48%;
  height: 350px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding: 15px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.08);
}

.news-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.news-image {
  width: 100%;
  height: 270px;
  object-fit: cover;
  border-radius: 5px;
}

.news-card-title {
  font-size: 16px;
  font-weight: 500;
  text-align: center;
  font-weight: bold;
  margin-top: 10px;
  overflow: hidden;
  height: 40px;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.news-summary {
  font-size: 14px;
  color: #666;
  text-align: left;
  margin-top: 5px;
  overflow: hidden;
  height: 60px;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

/* 活动和校园论坛 */
.events-hot-section {
  display: flex;
  gap: 20px;
}


.events-container {
  display: flex;
  gap: 20px;
  padding: 2px;
  height:500px;

}

.event-card {
  flex: 1;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  flex-direction: column;
  height: 470px;
  border: 1px solid rgba(0, 0, 0, 0.08);
  padding:15px;
  width: 300px;
 
}

.event-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.event-image {
 border-radius:5px;
  width: 100%;
  height: 330px;
  object-fit: cover;
}

.event-info {
  padding: 10px 15px;
  flex: 1;
  
}

.event-title {
  font-size: 16px;
  font-weight: bold;
  margin-top: 10px;
  margin-bottom:10px;
  text-align: center;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.event-summary {
  font-size: 14px;
  color: #666;
  line-height: 1.4;
  text-align:left;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

/* 热点信息 */
.hot-section {
  flex: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 10px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  height: 500px;

  border: 1px solid rgba(0, 0, 0, 0.08);

}


.hot-items {
  margin-top: 8px;
  display: flex;
  flex-direction: column;
  gap: 15px;

  flex: 1;
}

.hot-item {
  padding: 19px 18px;
  border-radius: 8px;
  transition: all 0.2s ease;
  border: 2px solid rgba(0, 0, 0, 0.08);
  background: white;

}

.hot-item:hover {
  background: #f5f5f7;

  border-color: #007aff;
}

.hot-item-content {
  display: flex;
  align-items: center;

}

.hot-item-text {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
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
</style>