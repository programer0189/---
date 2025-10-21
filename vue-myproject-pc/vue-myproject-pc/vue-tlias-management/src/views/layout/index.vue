<script setup>
// 这个文件是后台管理端的页面
import { ElMessage } from 'element-plus'
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  EditPen, 
  SwitchButton, 
  Promotion, 
  Menu, 
  Comment, 
  ChatDotRound, 
  Tools, 
  HelpFilled, 
  Avatar, 
  InfoFilled, 
  Histogram, 
  Share, 
  Document,
  Cellphone,
  Picture,
  User
} from '@element-plus/icons-vue'

// 登录用户信息
const loginName = ref('')
const router = useRouter()
const route = useRoute()

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

// 折叠菜单
const isCollapse = ref(false)
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const asideWidth = computed(() => {
  return isCollapse.value ? '64px' : '200px'
})
const collapseIcon = computed(() => {
  return isCollapse.value ? 'Expand' : 'Fold'
})
</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <span class="title">校智通系统</span>
        <span class="right_tool">
          <a href="javascript:void(0)">
            <el-icon><EditPen /></el-icon> 修改密码 &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;
          </a>
          <a href="javascript:void(0)" @click="logout">
            <el-icon><SwitchButton /></el-icon> 退出登录[{{ loginName }}]
          </a>
        </span>
      </el-header>

      <el-container>
        <!-- 左侧菜单 -->
        <el-aside :width="asideWidth" class="aside">
          <!-- 折叠按钮 -->
          <div class="collapse-toggle" @click="toggleCollapse">
            <el-icon>
              <component :is="collapseIcon" />
            </el-icon>
          </div>

          <el-menu
            router
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
            :default-active="route.path"
          >
            <!-- 首页 -->
            <el-menu-item index="/admin/index">
              <el-icon><Promotion /></el-icon>
              <span>首页</span>
            </el-menu-item>

            <!-- 我在校园 -->
            <el-sub-menu index="/manage">
              <template #title>
                <el-icon><Menu /></el-icon>
                <span>我在校园</span>
              </template>
              <el-menu-item index="/admin/news">
               <el-icon><Comment /></el-icon>论坛管理
              </el-menu-item>
           
            </el-sub-menu>

            <!-- 校园管理 -->
            <el-sub-menu index="/system">
              <template #title>
                <el-icon><Tools /></el-icon>
                <span>校园管理</span>
              </template>
              <el-menu-item index="/admin/dept">
                <el-icon><HelpFilled /></el-icon>部门管理
              </el-menu-item>
              <el-menu-item index="/admin/emp">
                <el-icon><Avatar /></el-icon>教职工管理
              </el-menu-item>
               <el-menu-item index="/admin/user">
                <el-icon><InfoFilled /></el-icon>用户管理
              </el-menu-item>
            </el-sub-menu>

            <!-- 数在校园 -->
            <el-sub-menu index="/report">
              <template #title>
                <el-icon><Histogram /></el-icon>
                <span>数在校园</span>
              </template>
              <el-menu-item index="/admin/empReport">
                <el-icon><InfoFilled /></el-icon>员工统计
              </el-menu-item>
              <el-menu-item index="/admin/mapReport">
                <el-icon><Share /></el-icon>浏览统计
              </el-menu-item>
              <el-menu-item index="/admin/log">
                <el-icon><Document /></el-icon>用户统计
              </el-menu-item>
              <el-menu-item index="/admin/chatlog">
                 <el-icon><Document /></el-icon>论坛统计
                </el-menu-item>
            </el-sub-menu>

            <!-- 移动端管理 -->
            <el-sub-menu index="/mobile">
              <template #title>
                <el-icon><Cellphone /></el-icon>
                <span>移动端管理</span>
              </template>
              <el-menu-item index="/admin/mobile/posts">
                <el-icon><Picture /></el-icon>帖子管理(新)
              </el-menu-item>
              <el-menu-item index="/admin/mobile/comments">
                <el-icon><ChatDotRound /></el-icon>评论管理
              </el-menu-item>
              <el-menu-item index="/admin/mobile/users">
                <el-icon><User /></el-icon>用户管理(新)
              </el-menu-item>
            </el-sub-menu>

      
          </el-menu>
        </el-aside>

        <!-- 主视图区 -->
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background-image: linear-gradient(to right, #00547d, #007fa4, #00aaa0, #00d072, #a8eb12);
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
}

.title {
  color: white;
  font-size: 40px;
  font-family: 楷体;
  font-weight: bold;
}

.right_tool {
  color: white;
  font-size: 15px;
}

a {
  color: white;
  text-decoration: none;
  margin-left: 10px;
}

.aside {
  border-right: 1px solid #ccc;
  transition: width 0.3s ease;
  min-height: 100vh;
  overflow: hidden;
}

.collapse-toggle {
  text-align: center;
  padding: 10px 0;
  color: #666;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 100%;
  min-height: 100%;
}
</style>
