<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 用户列表
const usersList = ref([])
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
  username: '',
  role: ''
})
const editFormRef = ref()

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

// 加载用户列表（分页）
const loadUsers = async () => {
  loading.value = true
  try {
    const result = await request.get('/users/page', {
      params: {
        page: pagination.value.currentPage,
        pageSize: pagination.value.pageSize
      }
    })
    if (result.code === 1) {
      usersList.value = result.data.list || result.data
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

// 编辑用户
const showEditDialog = (user) => {
  editForm.value = {
    id: user.id,
    username: user.username,
    role: user.role
  }
  editDialogVisible.value = true
}

const submitEdit = async () => {
  try {
    const result = await request.put(`/users/${editForm.value.id}`, {
      username: editForm.value.username,
      role: editForm.value.role
    })
    if (result.code === 1) {
      ElMessage.success('用户信息更新成功')
      editDialogVisible.value = false
      loadUsers() // 重新加载数据
    } else {
      ElMessage.error('更新失败：' + result.msg)
    }
  } catch (error) {
    ElMessage.error('更新失败：' + error.message)
  }
}

// 删除用户
const deleteUser = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要删除这个用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const result = await request.delete(`/users/${userId}`)
    if (result.code === 1) {
      ElMessage.success('用户删除成功')
      loadUsers() // 重新加载数据
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
  loadUsers()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  loadUsers()
}

// 获取角色标签样式
const getRoleTag = (role) => {
  return role === 'admin' ? 'success' : 'info'
}

// 获取角色文本
const getRoleText = (role) => {
  return role === 'admin' ? '管理员' : '普通用户'
}

// 页面加载时获取数据
onMounted(() => {
  getAdminInfo() // 先获取管理员信息
  loadUsers()
})
</script>

<template>
  <div class="user-management">
    <div class="header">
      <div class="header-content">
        <div class="header-left">
          <h2>用户管理</h2>
          <p>管理系统用户信息</p>
        </div>
        <div class="header-right">
          <el-tag type="success" size="large">
            <el-icon><User /></el-icon>
            管理员：{{ adminInfo.username }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 用户列表表格 -->
    <div class="table-container">
      <el-table 
        :data="usersList" 
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            <el-tag :type="getRoleTag(scope.row.role)">
              {{ getRoleText(scope.row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
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
              @click="deleteUser(scope.row.id)"
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
      title="编辑用户"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="editForm" ref="editFormRef" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" disabled />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="editForm.role" placeholder="请选择角色">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
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
.user-management {
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