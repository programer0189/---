<script setup>
import { ref, onMounted } from 'vue'
import { queryMapPageApi } from '@/api/map.js'
import { addClickApi } from '@/api/click.js'
import { ElMessage } from 'element-plus'

const mapList = ref([])
const page = ref(1)
const pageSize = ref(9) // 修改为每页显示9个地点
const total = ref(0)
const loading = ref(false) // 添加加载状态
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
// 前端手动编写介绍文案（索引 = locationId）
const descriptions = {
  1: '东南门是学校重要出入口，临近公交站，学生流量大，周边设施完善。这里不仅是学生日常出入的主要通道，也是连接校园与城市的重要枢纽。',
  2: '艺术楼配备多个展演功能厅，常用于举办艺术展与文艺演出活动。建筑风格独特，内部设施先进，是学校艺术教育的重要基地。',
  3: '3号教学楼主要用于基础课程教学，设施齐全、环境优雅。这里承载着众多学子的求知梦想，是知识传播的重要场所。',
  4: '1号教学楼为校区早期建筑，多用于管理与行政办公。建筑历史悠久，见证了学校的发展历程，具有重要的历史价值。'
}

// 若想自定义图片，可写这里（不写则默认显示 item.image）
const extraImages = {
  1: 'https://myprojrct-javaweb.oss-cn-beijing.aliyuncs.com/2025/07/3a9e4e72-f6c7-49e2-a7ad-31d77b8473fd.jpg',
  2: 'https://myprojrct-javaweb.oss-cn-beijing.aliyuncs.com/2025/07/1bdd6ca9-7c21-49ef-a929-76593fa006f4.png'
}

const visible = ref(false)
const currentInfo = ref({})

// 加载分页
async function loadList() {
  loading.value = true
  try {
    const res = await queryMapPageApi('', '', '', '', '', page.value, pageSize.value)
    if (res.code === 1) {
      mapList.value = res.data.rows
      total.value = res.data.total
    } else {
      ElMessage.error('加载地点列表失败：' + res.msg)
    }
  } catch (error) {
    ElMessage.error('加载地点列表失败，请稍后重试')
    console.error('加载地点列表错误:', error)
  } finally {
    loading.value = false
  }
}

// 查看详情
function handleView(item) {
  // 统计点击 - 确保传递正确的数据类型
  const clickData = {
    locationId: Number(item.id)
  }
  
  // 验证数据类型
  if (isNaN(clickData.locationId)) {
    console.error('无效的locationId:', item.id)
    return
  }
  
  // 添加调试日志
  console.log('发送点击数据:', clickData)
  
  addClickApi(clickData).then((response) => {
    console.log('点击统计成功:', response)
    ElMessage.success('浏览记录已统计')
  }).catch(error => {
    console.error('点击统计失败:', error)
    console.error('请求数据:', clickData)
    // 不显示错误提示，避免影响用户体验
  })

  // 展示内容
  currentInfo.value = {
    name: item.name,
    image: extraImages[item.id] || item.image,
    description: descriptions[item.id] || '暂无详细介绍'
  }
  visible.value = true
}

// 分页变化
function handlePageChange(newPage) {
  page.value = newPage
  loadList()
}

// 页面大小变化
function handleSizeChange(newSize) {
  pageSize.value = newSize
  page.value = 1 // 当页面大小改变时，重置当前页为1
  loadList()
}

onMounted(loadList)
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
    <div class="background">

    </div>
    <!-- 主体内容 -->
    <div class="main">
      <!-- 页面标题区域 -->
      <div class="page-header">

                <div class="title-container">
          <div class="title-icon">
            <i class="map-icon">🗺️</i>
          </div>
          <div class="title-text">
            <h1 class="main-title">校园地图</h1>
            <p class="subtitle">探索校园的每一个角落，发现美好时光</p>
          </div>
        </div>
      </div>


      <!-- 地图内容区域 -->
      <div class="map-content">
        <div class="content-header">
          <h2 class="section-title">校园位置列表</h2>
          <p class="section-desc">点击查看按钮了解每个地点的详细信息</p>
        </div>

        <!-- 位置卡片网格 -->
        <div v-loading="loading" element-loading-text="正在加载地点信息..." class="location-grid">
          <!-- 空状态提示 -->
          <div v-if="!loading && mapList.length === 0" class="empty-state">
            <div class="empty-icon">🗺️</div>
            <div class="empty-text">暂无地点信息</div>
            <div class="empty-desc">请稍后再试或联系管理员</div>
          </div>
          
          <!-- 地点卡片 -->
          <div 
            v-for="item in mapList" 
            :key="item.id" 
            class="location-card"
            @click="handleView(item)"
          >
            <div class="card-image">
              <img 
                :src="extraImages[item.id] || item.image || 'https://via.placeholder.com/300x200?text=校园风光'" 
                :alt="item.name"
                class="location-img"
              />
            </div>
            <div class="card-content">
              <h3 class="location-name">{{ item.name }}</h3>
              <p class="location-desc">{{ descriptions[item.id] || '点击查看详细介绍' }}</p>
            </div>
          </div>
        </div>

        <!-- 分页器 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="page"
            v-model:page-size="pageSize"
            :page-sizes="[9, 18, 27, 36]"
            :total="total"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :pager-count="5"
            prev-text="上一页"
            next-text="下一页"
            :hide-on-single-page="false"
          />
        </div>
      </div>
    </div>

    <!-- 弹窗 -->
    <el-dialog v-model="visible" :title="currentInfo.name" width="700px" center>
      <div>
        <img
          v-if="currentInfo.image"
          :src="currentInfo.image"
          class="dialog-image"
        />
        <div class="dialog-description">
          <p>{{ currentInfo.description }}</p>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" @click="visible=false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 页脚 -->
    <footer class="footer">
      <p>© 2025 校智通. All rights reserved.</p>
    </footer>
  </div>
</template>

<style scoped>

.title_container {
  display: flex;
  flex-direction: row;
  align-items: left;
  
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
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 50%, #dee2e6 100%);
  display: flex;
  flex-direction: column;
}
.background {
  background-image: url('../../assets/mapbg.jpg');

  background-size: cover;


  height: 400px;
 

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
  width: 90%;
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

  padding: 40px;
  box-sizing: border-box;
  margin-top: -80px;
}

/* 页面标题区域 */
.page-header {
  text-align: center;
  margin-bottom: 30px;
}
/* 校园地图标题区域 */
.title-container {
  display: flex;

  align-items: center;
  justify-content: center;
  gap: 25px;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.08);

}

.title-icon {
  font-size: 60px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border-radius: 50%;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.3);
}

.map-icon {
  font-style: normal;
}

.title-text {
  text-align: center;
}

.main-title {
  font-size: 36px;
  font-weight: 800;
  color: #1e293b;
  margin: 0 0 15px 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.subtitle {
  font-size: 18px;
  color: #64748b;
  margin: 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}
  
/* 地图内容区域 */
.map-content {
  background: white;
  border-radius: 10px;
  padding: 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.08);
}
/* 校园位置列表标题区域 */
.content-header {
  text-align: center;
  margin-bottom: 25px;
}
/* 校园位置列表标题区域 */
.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 15px 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}
/* 点击按钮描述信息 */
.section-desc {
  font-size: 16px;
  color: #64748b;
  margin: 8px;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

/* 位置卡片网格 */
.location-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 固定每行3列 */
  gap: 25px;
  margin-bottom: 48px;
}

/* 响应式网格布局 */
@media (max-width: 1200px) {
  .location-grid {
    grid-template-columns: repeat(2, 1fr); /* 中等屏幕每行2列 */
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .location-grid {
    grid-template-columns: 1fr; /* 小屏幕每行1列 */
    gap: 15px;
  }
}

.location-card {
  background: white;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.08);
  transition: all 0.2s ease;

}
.location-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
  
  align-items: center;
  justify-content: center;
}

.location-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;

}

.location-card:hover .location-img {
  transform: scale(0.99);
}





.card-content {
  padding: 20px;
  transition: background-color 0.3s ease;
}

.location-card:hover .card-content {
  background-color: #f8f9fa;
}

.location-name {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 15px 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.location-desc {
  font-size: 14px;
  color: #64748b;
  line-height: 1.6;
  margin: 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
  display: -webkit-box;

  overflow: hidden;
}

/* 空状态样式 */
.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 20px;
  opacity: 0.6;
}

.empty-text {
  font-size: 18px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 10px;
}

.empty-desc {
  font-size: 14px;
  color: #6b7280;
}

/* 分页器 */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding: 20px 0;
}

/* 分页器中文样式 */
:deep(.el-pagination) {
  --el-pagination-font-size: 14px;
  --el-pagination-bg-color: white;
  --el-pagination-text-color: #606266;
  --el-pagination-border-radius: 4px;
  --el-pagination-button-color: #606266;
  --el-pagination-button-bg-color: white;
  --el-pagination-button-border: 1px solid #dcdfe6;
  --el-pagination-button-hover-color: #409eff;
  --el-pagination-button-hover-bg-color: #ecf5ff;
  --el-pagination-button-hover-border-color: #409eff;
  --el-pagination-button-disabled-color: #c0c4cc;
  --el-pagination-button-disabled-bg-color: #f5f7fa;
  --el-pagination-button-disabled-border-color: #e4e7ed;
}

:deep(.el-pagination .el-pagination__total) {
  color: #606266;
  font-weight: 500;
}

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  font-weight: 500;
}

:deep(.el-pagination .el-pager li) {
  font-weight: 500;
  border-radius: 4px;
  margin: 0 2px;
}

:deep(.el-pagination .el-pager li.is-active) {
  background-color: #409eff;
  color: white;
  border-color: #409eff;
}

/* 响应式分页器 */
@media (max-width: 768px) {
  .pagination-container {
    margin-top: 20px;
    padding: 15px 0;
  }
  
  :deep(.el-pagination) {
    --el-pagination-font-size: 12px;
  }
  
  :deep(.el-pagination .el-pagination__sizes) {
    margin-right: 10px;
  }
}

/* 弹窗样式 */
.dialog-image {
  width: 100%;
  max-height: 400px;

  border-radius: 5px;
  margin-bottom: 20px;
}

.dialog-description {
  text-align: left;
  padding: 20px;
  background: #f8f9fa;

  border-left: 2px solid #040404;
}

.dialog-description p {
  font-size: 16px;
  line-height: 1.8;
  color: #374151;
  margin: 8px;
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
