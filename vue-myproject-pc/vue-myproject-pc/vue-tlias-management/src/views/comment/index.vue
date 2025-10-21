<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, ChatDotRound } from '@element-plus/icons-vue'
import { 
  getAllCommentsWithPageApi, 
  getAllCommentsCountApi,
  deleteCommentApi,
  updateCommentApi
} from '@/api/comment'

// 评论列表
const commentsList = ref([])
// 分页信息
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})
// 加载状态
const loading = ref(false)

// 编辑对话框
const editDialogVisible = ref(false)
const editForm = ref({
  id: null,
  content: ''
})
const editFormRef = ref()

// 统计信息
const stats = ref({
  totalComments: 0
})

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

// 加载评论列表（分页）
const loadComments = async () => {
  loading.value = true
  try {
    const result = await getAllCommentsWithPageApi(pagination.value.currentPage, pagination.value.pageSize)
    if (result.code === 1) {
      commentsList.value = result.data.list || result.data
      pagination.value.total = result.data.total || 0
    } else {
      ElMessage.error('加载失败：' + result.msg)
    }
  } catch (error) {
    ElMessage.error('加载失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 加载统计信息
const loadStats = async () => {
  try {
    const result = await getAllCommentsCountApi()
    if (result.code === 1) {
      stats.value.totalComments = result.data || 0
    }
  } catch (error) {
    console.error('加载统计信息失败：', error)
  }
}

// 编辑评论
const showEditDialog = (comment) => {
  editForm.value = {
    id: comment.id,
    content: comment.content
  }
  editDialogVisible.value = true
}

const submitEdit = async () => {
  try {
    const result = await updateCommentApi(editForm.value.id, {
      content: editForm.value.content
    })
    if (result.code === 1) {
      ElMessage.success('评论更新成功')
      editDialogVisible.value = false
      loadComments() // 重新加载数据
    } else {
      ElMessage.error('更新失败：' + result.msg)
    }
  } catch (error) {
    ElMessage.error('更新失败：' + error.message)
  }
}

// 删除评论
const deleteComment = async (commentId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const result = await deleteCommentApi(commentId, adminInfo.value.id)
    if (result.code === 1) {
      ElMessage.success('评论删除成功')
      loadComments() // 重新加载数据
      loadStats() // 重新加载统计信息
    } else {
      ElMessage.error('删除失败：' + result.msg)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 分页改变
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  loadComments()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  loadComments()
}

// 页面加载时获取数据
onMounted(() => {
  getAdminInfo() // 先获取管理员信息
  loadComments() // 加载评论列表
  loadStats() // 加载统计信息
})
</script>

<template>
  <div class="comment-management">
    <div class="header">
      <div class="header-content">
        <div class="header-left">
          <h2>评论管理</h2>
          <p>管理系统用户评论内容</p>
        </div>
        <div class="header-right">
          <el-tag type="success" size="large">
            <el-icon><User /></el-icon>
            管理员：{{ adminInfo.username }}
          </el-tag>
        </div>
      </div>
      
      <!-- 统计信息 -->
      <div class="stats-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="stats-card">
              <div class="stats-content">
                <el-icon class="stats-icon"><ChatDotRound /></el-icon>
                <div class="stats-info">
                  <div class="stats-number">{{ stats.totalComments }}</div>
                  <div class="stats-label">总评论数</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 评论列表表格 -->
    <div class="table-container">
      <el-table 
        :data="commentsList" 
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="content" label="评论内容" min-width="300" />
        <el-table-column prop="username" label="评论者" width="120" />
        <el-table-column prop="postId" label="帖子ID" width="100" />
        <el-table-column prop="createTime" label="评论时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="showEditDialog(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="deleteComment(scope.row.id)"
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
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑评论"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="editForm" ref="editFormRef" label-width="80px">
        <el-form-item label="评论内容">
          <el-input
            v-model="editForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入评论内容"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.comment-management {
  padding: 20px;
}

.header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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

.stats-container {
  margin-bottom: 20px;
}

.stats-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stats-icon {
  font-size: 48px;
  color: #409eff;
  margin-right: 20px;
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  line-height: 1;
}

.stats-label {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}

.table-container {
  padding: 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 表格样式优化 */
.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-table th {
  background-color: #fafafa;
  color: #333;
  font-weight: 600;
}

.el-table td {
  padding: 12px 0;
}
</style>
