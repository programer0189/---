<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User } from '@element-plus/icons-vue'
import { 
  getPendingPostsApi, 
  approvePostApi, 
  rejectPostApi, 
  deletePostApi,
  getAllPostsWithPageApi,
  getAllPostsCountApi,
  getPendingPostsCountApi
} from '@/api/post8081'

// 帖子列表
const postsList = ref([])
// 分页信息
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})
// 加载状态
const loading = ref(false)

// 所有帖子列表
const allPostsList = ref([])
// 所有帖子分页信息
const allPostsPagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})
// 所有帖子加载状态
const allPostsLoading = ref(false)

// 统计数据
const pendingCount = ref(0)
const allPostsCount = ref(0)
const approvedCount = ref(0)
const rejectedCount = ref(0)

// 显示模式：待审核 / 所有帖子
const displayMode = ref('pending') // 'pending' 或 'all'

// 拒绝对话框
const rejectDialogVisible = ref(false)
const rejectForm = ref({
  postId: null,
  adminId: null,
  reason: ''
})
const rejectFormRef = ref()

// 删除对话框
const deleteDialogVisible = ref(false)
const deleteForm = ref({
  postId: null,
  adminId: null,
  reason: ''
})
const deleteFormRef = ref()



// 加载待审核帖子（分页）
const loadPendingPosts = async () => {
  loading.value = true
  try {
    console.log('开始加载待审核帖子...')
    const result = await getPendingPostsApi(pagination.value.currentPage, pagination.value.pageSize)
    console.log('待审核帖子API返回结果:', result)
    
    if (result.code === 1) {
      // 处理不同的数据结构
      if (result.data && result.data.list) {
        // 分页数据结构
        postsList.value = result.data.list
        pagination.value.total = result.data.total || 0
        console.log('分页数据结构，帖子数量:', postsList.value.length)
      } else if (Array.isArray(result.data)) {
        // 直接数组结构
        postsList.value = result.data
        pagination.value.total = result.data.length
        console.log('数组数据结构，帖子数量:', postsList.value.length)
      } else if (result.data && typeof result.data === 'object') {
        // 其他对象结构
        postsList.value = result.data || []
        pagination.value.total = 0
        console.log('对象数据结构，帖子数量:', postsList.value.length)
      } else {
        // 其他结构
        postsList.value = result.data || []
        pagination.value.total = 0
        console.log('其他数据结构，帖子数量:', postsList.value.length)
      }
      
      // 更新统计数据
      await loadStatistics()
    } else {
      ElMessage.error('加载失败：' + result.msg)
      console.error('API返回错误:', result.msg)
    }
  } catch (error) {
    ElMessage.error('加载失败：' + error.message)
    console.error('加载待审核帖子异常:', error)
  } finally {
    loading.value = false
  }
}

// 加载所有帖子（分页）
const loadAllPosts = async () => {
  allPostsLoading.value = true
  try {
    const result = await getAllPostsWithPageApi(allPostsPagination.value.currentPage, allPostsPagination.value.pageSize)
    if (result.code === 1) {
      // 处理不同的数据结构
      if (result.data && result.data.list) {
        // 分页数据结构
        allPostsList.value = result.data.list
        allPostsPagination.value.total = result.data.total || 0
      } else if (Array.isArray(result.data)) {
        // 直接数组结构
        allPostsList.value = result.data
        allPostsPagination.value.total = result.data.length
      } else {
        // 其他结构
        allPostsList.value = result.data || []
        allPostsPagination.value.total = 0
      }
      // 更新统计数据
      await loadStatistics()
    } else {
      ElMessage.error('加载失败：' + result.msg)
    }
  } catch (error) {
    ElMessage.error('加载失败：' + error.message)
  } finally {
    allPostsLoading.value = false
  }
}

// 加载统计数据
const loadStatistics = async () => {
  try {
    // 获取统计信息
    const statsResult = await getAllPostsCountApi()
    if (statsResult.code === 1) {
      // 直接使用返回的data对象
      const data = statsResult.data
      pendingCount.value = data.pending || 0
      allPostsCount.value = data.total || 0
      approvedCount.value = data.approved || 0
      rejectedCount.value = data.rejected || 0
      
      console.log('统计数据加载完成:', {
        pending: pendingCount.value,
        total: allPostsCount.value,
        approved: approvedCount.value,
        rejected: rejectedCount.value
      })
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 切换显示模式
const switchDisplayMode = (mode) => {
  displayMode.value = mode
  if (mode === 'pending') {
    pagination.value.currentPage = 1
    loadPendingPosts()
  } else {
    allPostsPagination.value.currentPage = 1
    loadAllPosts()
  }
}

// 获取管理员信息
const adminInfo = ref({
  id: null,
  username: ''
})

// 获取管理员信息
const getAdminInfo = () => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser && loginUser.role === 'admin') {
    adminInfo.value = {
      id: loginUser.id,
      username: loginUser.username
    }
  }
}

// 通过帖子
const approvePost = async (postId) => {
 
    if (!adminInfo.value.id) {
      ElMessage.error('管理员信息获取失败')
      return
    }
    const result = await approvePostApi(postId, adminInfo.value.id)
    if (result.code === 1) {
      ElMessage.success('帖子审核通过')
      // 根据当前显示模式重新加载相应的数据
      if (displayMode.value === 'pending') {
        loadPendingPosts()
      } else {
        loadAllPosts()
      }
    } else {
      ElMessage.error('操作失败：' + result.msg)
    }

}

// 拒绝帖子
const showRejectDialog = (postId) => {
  if (!adminInfo.value.id) {
    ElMessage.error('管理员信息获取失败')
    return
  }
  rejectForm.value = {
    postId: postId,
    adminId: adminInfo.value.id,
    reason: ''
  }
  rejectDialogVisible.value = true
}

const submitReject = async () => {

    const result = await rejectPostApi(
      rejectForm.value.postId, 
      rejectForm.value.adminId, 
      rejectForm.value.reason
    )
    if (result.code === 1) {
      ElMessage.success('帖子已拒绝')
      rejectDialogVisible.value = false
      // 根据当前显示模式重新加载相应的数据
      if (displayMode.value === 'pending') {
        loadPendingPosts()
      } else {
        loadAllPosts()
      }
    } else {
      ElMessage.error('操作失败：' + result.msg)
    }
 
}

// 删除帖子
const showDeleteDialog = (postId) => {
  if (!adminInfo.value.id) {
    ElMessage.error('管理员信息获取失败')
    return
  }
  deleteForm.value = {
    postId: postId,
    adminId: adminInfo.value.id,
    reason: ''
  }
  deleteDialogVisible.value = true
}

const submitDelete = async () => {

    const result = await deletePostApi(
      deleteForm.value.postId, 
      deleteForm.value.adminId, 
      deleteForm.value.reason || null
    )
    if (result.code === 1) {
      ElMessage.success('帖子已删除')
      deleteDialogVisible.value = false
      // 根据当前显示模式重新加载相应的数据
      if (displayMode.value === 'pending') {
        loadPendingPosts()
      } else {
        loadAllPosts()
      }
    } else {
      ElMessage.error('操作失败：' + result.msg)
    }
 
}



// 分页改变
const handleCurrentChange = (page) => {
  if (displayMode.value === 'pending') {
    pagination.value.currentPage = page
    loadPendingPosts()
  } else {
    allPostsPagination.value.currentPage = page
    loadAllPosts()
  }
}

const handleSizeChange = (size) => {
  if (displayMode.value === 'pending') {
    pagination.value.pageSize = size
    pagination.value.currentPage = 1
    loadPendingPosts()
  } else {
    allPostsPagination.value.pageSize = size
    allPostsPagination.value.currentPage = 1
    loadAllPosts()
  }
}

// 所有帖子分页改变
const handleAllPostsCurrentChange = (page) => {
  allPostsPagination.value.currentPage = page
  loadAllPosts()
}

const handleAllPostsSizeChange = (size) => {
  allPostsPagination.value.pageSize = size
  allPostsPagination.value.currentPage = 1
  loadAllPosts()
}

// 获取帖子类型标签样式
const getTypeTag = (type) => {
  const typeMap = {
    1: 'success',   // 我要表白
    2: 'info',      // 代取快递
    3: 'primary',   // 学习交流
    4: 'warning',   // 活动组织
    5: 'danger',    // 失物招领
    6: 'info'       // 闲聊灌水
  }
  return typeMap[type]
}

// 获取帖子类型文本
const getTypeText = (type) => {
  const typeMap = {
    1: '我要表白',
    2: '代取快递',
    3: '学习交流',
    4: '活动组织',
    5: '失物招领',
    6: '闲聊灌水'
  }
  return typeMap[type] 
}

// 获取帖子状态标签样式
const getStatusTag = (status) => {
  const statusMap = {
    0: 'info',      // 待审核
    1: 'success',   // 已通过
    2: 'danger',    // 已拒绝
    3: 'danger'     // 已删除
  }
  return statusMap[status] || 'info' // 默认返回info样式
}

// 获取帖子状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待审核',
    1: '已通过',
    2: '已拒绝',
    3: '已删除'
  }
  return statusMap[status] || `未知状态(${status})` // 显示未知状态和原始值
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = 'https://via.placeholder.com/50x50?text=No+Image' // 替换为默认图片
}

// 图片预览相关
const imagePreviewVisible = ref(false)
const imagePreviewList = ref([])
const imagePreviewIndex = ref(0)

// 显示图片预览
const showImagePreview = (imageUrl, imageList, index = 0) => {
  if (imageList && imageList.length > 0) {
    imagePreviewList.value = imageList
    imagePreviewIndex.value = index
    imagePreviewVisible.value = true
  } else if (imageUrl) {
    imagePreviewList.value = [imageUrl]
    imagePreviewIndex.value = 0
    imagePreviewVisible.value = true
  }
}

// 页面加载时获取数据
onMounted(() => {
  getAdminInfo() // 先获取管理员信息
  loadPendingPosts() // 默认加载待审核帖子
  loadAllPosts() // 加载所有帖子
  loadStatistics() // 加载统计数据
})
</script>

<template>
  <div class="forum-management">
    <div class="header">
      <div class="header-content">
        <div class="header-left">
          <h2>论坛管理</h2>
          <p>{{ displayMode === 'pending' ? '管理待审核的帖子内容' : '管理所有帖子内容' }}</p>
        </div>
        <div class="header-right">
          <el-tag type="success" size="large">
            <el-icon><User /></el-icon>
            管理员：{{ adminInfo.username }}
          </el-tag>
        </div>
      </div>
      
      <!-- 统计信息 -->
      <div class="statistics-container">
        <div class="statistics-header">
          <h3>数据统计</h3>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ pendingCount }}</div>
                <div class="stat-label">待审核帖子</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ allPostsCount }}</div>
                <div class="stat-label">所有帖子</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ approvedCount }}</div>
                <div class="stat-label">已通过帖子</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ rejectedCount }}</div>
                <div class="stat-label">已拒绝帖子</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <!-- 显示模式切换 -->
      <div class="mode-switch">
        <el-radio-group v-model="displayMode" @change="switchDisplayMode">
          <el-radio-button label="pending">待审核帖子</el-radio-button>
          <el-radio-button label="all">所有帖子</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 帖子列表表格 -->
    <div class="table-container" v-if="displayMode === 'pending'">
      <el-table 
        :data="postsList" 
        :loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="content" label="内容" min-width="200"/>
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)">
              {{ getTypeText(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column prop="username" label="发布者" width="120" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button 
              type="success" 
              size="small" 
              @click="approvePost(scope.row.id)"
            >
              通过
            </el-button>
            <el-button 
              type="warning" 
              size="small" 
              @click="showRejectDialog(scope.row.id)"
            >
              拒绝
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="showDeleteDialog(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>

    <!-- 所有帖子列表表格 -->
    <div class="table-container" v-if="displayMode === 'all'">
      <el-table 
        :data="allPostsList" 
        :loading="allPostsLoading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="content" label="内容" min-width="200"/>
        <el-table-column prop="image" label="图片" width="120">
          <template #default="scope">
            <div v-if="scope.row.image && scope.row.image.trim()">
              <!-- 处理多图片URL，用逗号分隔 -->
              <template v-if="scope.row.image.includes(',')">
                <div class="image-container">
                  <el-image
                    v-for="(imgUrl, index) in scope.row.image.split(',').filter(url => url.trim())"
                    :key="index"
                    :src="imgUrl.trim()"
                    fit="cover"
                    style="width: 50px; height: 50px; border-radius: 4px; margin: 2px; cursor: pointer;"
                    @error="handleImageError"
                    @click="showImagePreview(imgUrl.trim(), scope.row.image.split(',').filter(url => url.trim()), index)"
                  />
                </div>
              </template>
              <!-- 单张图片 -->
              <template v-else>
                <el-image
                  :src="scope.row.image.trim()"
                  fit="cover"
                  style="width: 60px; height: 60px; border-radius: 4px; cursor: pointer;"
                  @error="handleImageError"
                  @click="showImagePreview(scope.row.image.trim())"
                />
              </template>
            </div>
            <span v-else style="color: #999;">无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)">
              {{ getTypeText(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="judgeStatus" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.judgeStatus)">
              {{ getStatusText(scope.row.judgeStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column prop="username" label="发布者" width="120" />
                 <el-table-column label="操作" width="250">
           <template #default="scope">
             <!-- 待审核状态的操作 -->
             <template v-if="scope.row.judgeStatus === 0">
               <el-button 
                 type="success" 
                 size="small" 
                 @click="approvePost(scope.row.id)"
               >
                 通过
               </el-button>
               <el-button 
                 type="warning" 
                 size="small" 
                 @click="showRejectDialog(scope.row.id)"
               >
                 拒绝
               </el-button>
               <el-button 
                 type="danger" 
                 size="small" 
                 @click="showDeleteDialog(scope.row.id)"
               >
                 删除
               </el-button>
             </template>
             
             <!-- 已通过状态的操作 -->
             <template v-if="scope.row.judgeStatus === 1">
               <el-button 
                 type="warning" 
                 size="small" 
                 @click="showRejectDialog(scope.row.id)"
               >
                 再拒绝
               </el-button>
               <el-button 
                 type="danger" 
                 size="small" 
                 @click="showDeleteDialog(scope.row.id)"
               >
                 删除
               </el-button>
             </template>
             
             <!-- 已拒绝状态的操作 -->
             <template v-if="scope.row.judgeStatus === 2">
               <el-button 
                 type="success" 
                 size="small" 
                 @click="approvePost(scope.row.id)"
               >
                 再通过
               </el-button>
               <el-button 
                 type="danger" 
                 size="small" 
                 @click="showDeleteDialog(scope.row.id)"
               >
                 删除
               </el-button>
             </template>
             
             <!-- 已删除状态的操作 -->
             <template v-if="scope.row.judgeStatus === 3">
               <el-button 
                 type="success" 
                 size="small" 
                 @click="approvePost(scope.row.id)"
               >
                 再通过
               </el-button>
             </template>
           </template>
         </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="allPostsPagination.currentPage"
          v-model:page-size="allPostsPagination.pageSize"
          :page-sizes="[10]"
          :total="allPostsPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handleAllPostsCurrentChange"
          @size-change="handleAllPostsSizeChange"
        />
      </div>
    </div>

    <!-- 拒绝对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="拒绝帖子"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="rejectForm" ref="rejectFormRef" label-width="80px">
        <el-form-item label="拒绝原因">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因（非必填）"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="warning" @click="submitReject">确认拒绝</el-button>
        </span>
      </template>
    </el-dialog>



    <!-- 删除对话框 -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="删除帖子"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="deleteForm" ref="deleteFormRef" label-width="80px">
        <el-form-item label="删除原因">
          <el-input
            v-model="deleteForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入删除原因（非必填）"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="submitDelete">确认删除</el-button>
        </span>
      </template>
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

<style scoped>
/* 全局图片预览器样式 - 确保在最上层 */
:global(.el-image-viewer__wrapper) {
  z-index: 99999 !important;
  position: fixed !important;
}

:global(.el-image-viewer__mask) {
  z-index: 99998 !important;
  position: fixed !important;
}

.forum-management {
  padding: 20px;
}

.header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 24px;
}

.header-left p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.header-right .el-tag {
  font-size: 14px;
  padding: 8px 16px;
}

.statistics-container {
  margin-bottom: 20px;
}

.statistics-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.statistics-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.stat-card {
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stat-card:hover {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.2);
}

.stat-content {
  padding: 20px 0;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.mode-switch {
  margin-top: 10px;
  display: flex;
  justify-content: center;
}

.table-container {
  padding: 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.debug-info {
  margin-top: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 表格样式优化 */
.el-table {
  border-radius: 8px;
  overflow:hidden;
  position: relative;
  z-index: 1;
}

.el-table th {
  background-color: #fafafa;
  color: #333;
  font-weight: 600;
}

.el-table td {
  padding: 12px 0;
}

/* 图片预览样式优化 */
.image-container {
  display: flex;
  flex-wrap: wrap;
  gap: 2px;
}

/* 确保图片预览在最上层 */
:deep(.el-image-viewer__wrapper) {
  z-index: 99999 !important;
  position: fixed !important;
}

:deep(.el-image-viewer__mask) {
  z-index: 99998 !important;
  position: fixed !important;
}

:deep(.el-image-viewer__img) {
  z-index: 99999 !important;
  position: relative !important;
}

:deep(.el-image-viewer__btn) {
  z-index: 100000 !important;
  position: relative !important;
}

:deep(.el-image-viewer__close) {
  z-index: 100000 !important;
  position: relative !important;
}

:deep(.el-image-viewer__actions) {
  z-index: 100000 !important;
  position: relative !important;
}

:deep(.el-image-viewer__prev) {
  z-index: 100000 !important;
  position: relative !important;
}

:deep(.el-image-viewer__next) {
  z-index: 100000 !important;
  position: relative !important;
}

/* 图片样式优化 */
.el-image {
  border: 1px solid #ebeef5;
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
}

.el-image:hover {
  border-color: #409eff;
  transform: scale(1.05);
}

/* 图片预览组件样式 - 确保在最上层 */
:deep(.el-image-viewer) {
  z-index: 99999 !important;
}

:deep(.el-image-viewer__wrapper) {
  z-index: 99999 !important;
  position: fixed !important;
}

:deep(.el-image-viewer__mask) {
  z-index: 99998 !important;
  position: fixed !important;
}

:deep(.el-image-viewer__img) {
  z-index: 99999 !important;
}

:deep(.el-image-viewer__btn) {
  z-index: 100000 !important;
}

:deep(.el-image-viewer__close) {
  z-index: 100000 !important;
}

:deep(.el-image-viewer__actions) {
  z-index: 100000 !important;
}

:deep(.el-image-viewer__prev) {
  z-index: 100000 !important;
}

:deep(.el-image-viewer__next) {
  z-index: 100000 !important;
}
</style>