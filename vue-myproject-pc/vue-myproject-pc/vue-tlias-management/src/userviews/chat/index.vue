<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

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
        <span class="username">欢迎 {{ loginName }}</span>
        <button class="logout-btn" @click="logout">退出</button>
      </div>
    </header>

    <!-- 主要内容 -->
    <div class="main">
      <el-container>
        <!-- 左侧分类 -->
        <el-aside width="200px" class="aside">
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
          <el-menu
            router
            class="nav-menu"
            :default-active="$route.path"
          >
            <el-menu-item index="/home/chat/confession">
              <span>我要表白</span>
            </el-menu-item>
            <el-menu-item index="/home/chat/express">
              <span>代取快递</span>
            </el-menu-item>
            <el-menu-item index="/home/chat/study">
              <span>学习交流</span>
            </el-menu-item>
            <el-menu-item index="/home/chat/activity">
              <span>活动组织</span>
            </el-menu-item>
            <el-menu-item index="/home/chat/lost">
              <span>失物招领</span>
            </el-menu-item>
            <el-menu-item index="/home/chat/chat">
              <span>闲聊灌水</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <!-- 主视图区 -->
        <el-main class="main-container">
          <router-view></router-view>
        </el-main>
      </el-container>
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2025 校智通 | 技术支持：校园导航团队</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f7;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", "Helvetica Neue", Arial, sans-serif;
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
  color: #fff;
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

/* 主要内容样式 */
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
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
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

.aside {
  background-color: transparent;
 

  height: auto;
}

.nav-menu {
  height: 680px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 5px;
  padding: 10px 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
}/* 侧边栏样式修改 */
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


.main-container {
  height: 720px;
  overflow: auto;
  background-color: transparent;
  padding: 20px;
}

/* 页脚样式 */
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

.footer-content p {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}
</style>