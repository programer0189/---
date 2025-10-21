<script setup>
import { ref, onMounted } from 'vue'
import { queryMapPageApi } from '@/api/map.js'
import { addClickApi } from '@/api/click.js'
import { ElMessage } from 'element-plus'

const center = ref({ lng: 108.738822, lat: 34.367593 })
const zoom = ref(18)

const pointList = ref([])
const startId = ref('')
const endId = ref('')

let BMapInstance = null
let mapInstance = null
let walkingRoute = null
let startPoint = null
let endPoint = null

const panelVisible = ref(false)
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
async function fetchPoints(){
  const result = await queryMapPageApi('', '', '', '', '', 1, 200)
  if(result.code===1){
    pointList.value = result.data.rows
  }
}

function recordClick(id){
  addClickApi({locationId:id})
}

function handleSelectStart(id){
  // 确保id是数字类型
  const locationId = Number(id)
  recordClick(locationId)
  const obj = pointList.value.find(p=>p.id===locationId)
  if(obj){
    startPoint = new BMapInstance.Point(obj.longitude, obj.latitude)
  }
}

function handleSelectEnd(id){
  // 确保id是数字类型
  const locationId = Number(id)
  recordClick(locationId)
  const obj = pointList.value.find(p=>p.id===locationId)
  if(obj){
    endPoint = new BMapInstance.Point(obj.longitude, obj.latitude)
  }
}

function buildRoute(){
  if(!startId.value || !endId.value){
    ElMessage.error("请选择起点和终点")
    return
  }
  if(startId.value===endId.value){
    ElMessage.error("起点与终点不能相同")
    return
  }
  const s = pointList.value.find(p=>p.id===startId.value)
  const e = pointList.value.find(p=>p.id===endId.value)
  startPoint = new BMapInstance.Point(s.longitude,s.latitude)
  endPoint   = new BMapInstance.Point(e.longitude,e.latitude)
  mapInstance.clearOverlays()

  const m1 = new BMapInstance.Marker(startPoint)
  m1.setLabel(new BMapInstance.Label('起点'))
  const m2 = new BMapInstance.Marker(endPoint)
  m2.setLabel(new BMapInstance.Label('终点'))
  mapInstance.addOverlay(m1); mapInstance.addOverlay(m2)

  walkingRoute = new BMapInstance.WalkingRoute(mapInstance,{
    renderOptions:{ map:mapInstance,panel:"panel",autoViewport:true }
  })
  walkingRoute.search(startPoint,endPoint)
  panelVisible.value = true
}

function onMapReady({BMap,map}){
  BMapInstance = BMap
  mapInstance = map
  map.disableScrollWheelZoom()

  map.addEventListener('click', (e)=>{
    const pt = e.point

    // 同时存在时归零
    if(startPoint && endPoint){
      startPoint = null
      endPoint   = null
      map.clearOverlays()
      if(walkingRoute) walkingRoute.clearResults()
    }
    if(!startPoint){
      startPoint = pt
      const mk = new BMap.Marker(pt)
      mk.setLabel(new BMap.Label("起点",{offset:new BMap.Size(20,-10)}))
      map.addOverlay(mk)
    }else if(!endPoint){
      if(pt.lng===startPoint.lng&&pt.lat===startPoint.lat){
        ElMessage.error("起点和终点不能相同")
        return
      }
      endPoint = pt
      const mk = new BMap.Marker(pt)
      mk.setLabel(new BMap.Label("终点",{offset:new BMap.Size(20,-10)}))
      map.addOverlay(mk)

      walkingRoute = new BMap.WalkingRoute(map,{
        renderOptions:{ map, panel:'panel', autoViewport:true }
      })
      walkingRoute.search(startPoint,endPoint)
      panelVisible.value = true
    }
  })

  map.addEventListener('rightclick', ()=>{
    map.clearOverlays()
    map.reset()
    startPoint=null;endPoint=null
    if(walkingRoute) 
    walkingRoute.clearResults()
    const panel = document.getElementById('panel')
    if(panel) 
    panel.innerHTML = ''
    panelVisible.value=false
  })
}

function resetRoute(){
  startId.value = ''
  endId.value   = ''
  startPoint    = null
  endPoint      = null
  if(mapInstance){
    mapInstance.clearOverlays()
    mapInstance.reset()
  }
  if(walkingRoute){
    walkingRoute.clearResults()
  }
  document.getElementById('panel').innerHTML=''
  panelVisible.value = false
  ElMessage.success("已重置")
}

onMounted(fetchPoints)
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
            <i class="nav-icon">🧭</i>
          </div>
          <div class="title-text">
            <h1 class="main-title">校园智能导航</h1>
            <p class="subtitle">智能规划路线，轻松到达目的地</p>
          </div>
        </div>
      </div>

  
        <!-- 导航工具区域 -->
        <div class="nav-tools">
          <div class="tools-container">
            <div class="tool-item">
              <label class="tool-label">起点</label>
              <el-select 
                v-model="startId" 
                placeholder="请选择起点" 
                @change="handleSelectStart" 
                class="tool-select"
              >
                <el-option 
                  v-for="p in pointList" 
                  :key="p.id" 
                  :label="p.name" 
                  :value="p.id" 
                />
              </el-select>
            </div>
            
            <div class="tool-item">
              <label class="tool-label">终点</label>
              <el-select 
                v-model="endId" 
                placeholder="请选择终点" 
                @change="handleSelectEnd" 
                class="tool-select"
              >
                <el-option 
                  v-for="p in pointList" 
                  :key="p.id" 
                  :label="p.name" 
                  :value="p.id" 
                />
              </el-select>
            </div>
            <p class="notice">
              点击规划路线，点击重置按钮即可重置 鼠标左键可地图选点 右键即可清除
            </p>
            <div class="tool-buttons">
              <el-button type="primary" @click="buildRoute" class="route-btn">
                <i class="el-icon-location"></i>
                规划路线
              </el-button>
              <el-button type="warning" @click="resetRoute" class="reset-btn">
                <i class="el-icon-refresh"></i>
                重置
              </el-button>
            </div>
          </div>
        </div>

        <!-- 地图容器 -->
        <div class="map-container">
          <baidu-map
            class="map"
            :center="center"
            :zoom="zoom"
            ak="UBQYpEDwgMq5YzD4l5KcrjVBQvfTQyDg"
            v="3.0"
            type="API"
            :scroll-wheel-zoom="true"
            @ready="onMapReady"
          />
          <div id="panel" v-show="panelVisible" class="route-panel"></div>
        </div>
     
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
.background {
  background-image: url('../../assets/navigate.jpg');

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
  position: relative;
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

  margin-top:-80px;
}

/* 页面标题区域 */
.page-header {
  text-align: center;
  margin-bottom: 10px;
}

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

.nav-icon {
  font-style: normal;
}

.title-text {
  text-align: center;
}

.main-title {
  font-size: 36px;
  font-weight: 800;
  color: #1e293b;
  margin: 0 0 10px 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.subtitle {
  font-size: 18px;
  color: #64748b;
  margin: 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}




/* 导航工具区域 */
.nav-tools {
  background: white;
border-radius: 10px;
  padding: 30px;
  border-bottom: 1px solid rgba(8, 8, 8, 0.733);
}

.tools-container {
  display: flex;
  align-items: center;
  gap: 80px;
 
}
.tool-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.tool-label {
  font-size: 20px;
  font-weight: 600;
  color: #374151;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.tool-select {
  width: 200px;
  
  
}

.tool-buttons {
  display: flex;
  gap: 15px;
  margin-left: auto;
}

/* 提示信息样式 */
.notice {
  font-size: 19px;
  color: #000000;
  background: #f8fafc;
  padding: 8px 8px;
  border-radius: 8px;
  border-left: 4px solid #5a75a1;
  margin: 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
  line-height: 1.5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.route-btn, .reset-btn {
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.route-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.3);
}

.reset-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(245, 158, 11, 0.3);
}

/* 地图容器 */
.map-container {
  position: relative;
  width: 100%;
  height: 700px;
  border-radius: 5px;



  border: 1px solid rgba(0, 0, 0, 0.06);


}

.map {
  width: 100%;
  height: 100%;
}

.route-panel {
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
  max-height: 300px;
  padding: 20px;
  overflow: auto;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  font-size: 14px;
  border-left: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 0 15px 0 0;
  box-shadow: -4px 0 20px rgba(0, 0, 0, 0.1);
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
