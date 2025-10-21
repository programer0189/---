<script setup>
import { ref } from 'vue'
import { UserloginApi } from '@/api/userLogin.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '@/utils/request.js'
import { userAPI } from '@/api/chat.js'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()

// 表单数据
const loginForm = ref({ username: '', password: '' })
const loginFormRef = ref()
const registerForm = ref({
  username: '',
  password: '',
  phone: '',
  image: '',
  gender: ''
})
const forgotForm = ref({
  username: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单引用
const registerFormRef = ref()
const forgotFormRef = ref()

// 对话框显示状态
const showRegister = ref(false)
const showForgot = ref(false)

// 表单验证规则
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 1, max: 20, message: '用户名长度在 1 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  image: [{ required: true, message: '请上传头像', trigger: 'change' }]
}

const forgotRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== forgotForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 轮播图数据
const carouselImages = ref([
  { url: new URL('@/assets/login1.jpg', import.meta.url).href },
  { url: new URL('@/assets/login2.jpg', import.meta.url).href },
  { url: new URL('@/assets/login3.jpg', import.meta.url).href },
  { url: new URL('@/assets/login4.jpg', import.meta.url).href },
  { url: new URL('@/assets/login5.jpg', import.meta.url).href },
  { url: new URL('@/assets/login6.jpg', import.meta.url).href }
])

// 上传配置
const uploadUrl = '/api/upload'

// 登录
const login = async () => {
  if (!loginFormRef.value) return
  
  const valid = await loginFormRef.value.validate()
  if (!valid) return
  
  const result = await UserloginApi(loginForm.value)
  if (result.code === 1) {
    ElMessage.success('登录成功')
    localStorage.setItem('loginUser', JSON.stringify(result.data))
    
    if (result.data.role === 'admin') {
      router.push('/admin/index')
    } else {
      router.push('/home')
    }
  } else {
    ElMessage.error(result.msg || '登录失败')
  }
}

// 注册
const register = () => {
  showRegister.value = true
  registerForm.value = { username: '', password: '', phone: '', image: '', gender: '' }
  // 清除表单验证提示
  if (registerFormRef.value) {
    registerFormRef.value.resetFields()
  }
}

const submitRegister = async () => {
  // 检查用户名
  
  const userCheck = await userAPI.getUserByUsername(registerForm.value.username)
  if (userCheck.code === 1) {
    ElMessage.error('用户名已存在')
    return
  }
  
  // 检查手机号
  if (registerForm.value.phone) {
    const phoneCheck = await userAPI.getUserByPhone(registerForm.value.phone)
    if (phoneCheck.code === 1) {
      ElMessage.error('手机号已存在')
      return
    }
  }
  
  // 执行注册
  const response = await request.post('/users', {
    username: registerForm.value.username,
    password: registerForm.value.password,
    phone: registerForm.value.phone,
    image: registerForm.value.image || '',
    gender: registerForm.value.gender,
    role: 'user'
  })
  
  if (response.code === 1) {
    ElMessage.success('注册成功！')
    showRegister.value = false
    registerForm.value = { username: '', password: '', phone: '', image: '', gender: '' }
  } else {
    ElMessage.error(response.msg || '注册失败')
  }
}

// 忘记密码
const forgotPassword = () => {
  showForgot.value = true
  forgotForm.value = { username: '', newPassword: '', confirmPassword: '' }
  // 清除表单验证提示
  if (forgotFormRef.value) {
    forgotFormRef.value.resetFields()
  }
}

const submitForgotPassword = async () => {
  // 查询用户名
  const userCheck = await userAPI.getUserByUsername(forgotForm.value.username)
  if (userCheck.code !== 1) {
    ElMessage.error('用户名不存在')
    return
  }
  
  // 更新密码
  const updateResult = await userAPI.updateUserInfo(userCheck.data.id, {
    password: forgotForm.value.newPassword
  })
  
  if (updateResult.code === 1) {
    ElMessage.success('密码修改成功！')
    showForgot.value = false
    forgotForm.value = { username: '', newPassword: '', confirmPassword: '' }
  } else {
    ElMessage.error(updateResult.msg || '密码修改失败')
  }
}

// 头像上传
const handleAvatarSuccess = (response) => {
  if (response.code === 1) {
    registerForm.value.image = response.data
    ElMessage.success('头像上传成功！')
  } else {
    ElMessage.error('头像上传失败：'+"头像大小不能超过2MB")
  }
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isSmall = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('头像只能是 JPG/PNG 格式!')
  }
  if (!isSmall) {
    ElMessage.error('头像大小不能超过 2MB!')
  }
  return isImage && isSmall
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 左侧轮播图 -->
      <div class="carousel-section">
        <el-carousel 
          height="100%" 
          class="login-carousel" 
          :interval="2000"
          :indicator-position="'none'"  
          :arrow="false"              
        >
          <el-carousel-item v-for="item in carouselImages" :key="item.url">
            <img :src="item.url" alt="" class="carousel-image" />
          </el-carousel-item>
        </el-carousel>
      </div>
      
      <!-- 右侧登录表单 -->
      <div class="login-form">
        <h2 class="title">校智通</h2>

        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
          <div class="form-group">
            <label>用户名</label>
            <el-input
              v-model="loginForm.username"
              type="text"
              placeholder="请输入用户名"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label>密码</label>
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              class="form-input"
            />
          </div>
        </el-form>

        <div class="options">
          <a href="javascript:void(0)" @click="register" class="register-btn">还没有账号？点击注册</a>
          <a href="javascript:void(0)" @click="forgotPassword" class="forgot-link">忘记密码?</a>
        </div>

        <button @click="login" class="login-btn">登录</button>
      </div>
    </div>
  </div>

  <!-- 注册对话框 -->
  <el-dialog v-model="showRegister" title="注册" width="500" class="register-dialog">
    <template #header>
      <div class="dialog-header register-header">注册</div>
    </template>
    <el-form :model="registerForm" :rules="registerRules" label-width="80px" ref="registerFormRef">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="registerForm.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="registerForm.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="registerForm.gender" placeholder="请选择性别" style="width: 100%">
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="头像" prop="image">
        <el-upload
          class="avatar-uploader"
          :action="uploadUrl"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="registerForm.image" :src="registerForm.image" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitRegister">注册</el-button>
        <el-button @click="showRegister = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 忘记密码对话框 -->
  <el-dialog v-model="showForgot" title="忘记密码" width="400" class="forgot-dialog">
    <template #header>
      <div class="dialog-header forgot-header">忘记密码</div>
    </template>
    <el-form :model="forgotForm" :rules="forgotRules" label-width="80px" ref="forgotFormRef">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="forgotForm.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="forgotForm.newPassword" type="password" placeholder="请输入新密码" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="forgotForm.confirmPassword" type="password" placeholder="请确认密码" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForgotPassword">提交</el-button>
        <el-button @click="showForgot = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<style scoped>
/* 整体容器 */
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}

/* 登录框 */
.login-box {
  display: flex;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 950px;
  height: 550px;
}

/* 左侧轮播图 */
.carousel-section {
  flex: 1;
  background: #f8f9fa;
  overflow: hidden;
}

.login-carousel {
  width: 100%;
  height: 100%;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

/* 右侧登录表单 */
.login-form {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #000000;
  text-align: center;
  margin-bottom: 30px;
  font-family: 'Microsoft YaHei', sans-serif;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  color: #000000;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-input {
  width: 100%;
}





.options {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.forgot-link {
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
}

.login-btn {
  width: 100%;
  padding: 14px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-bottom: 15px;
}

.login-btn:hover {
  background: #5a6fd8;
}

.register-btn {
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
}

.register-btn:hover {
  color: #5a6fd8;
}

/* 对话框样式 */
.register-dialog,
.forgot-dialog {
  border-radius: 8px;
}

.dialog-header {
  color: white;
  font-weight: 600;
  font-size: 25px;
  padding: 20px;
  border-radius: 8px 8px 0 0;
  text-align: center;
}

.register-header {

color: #000000;
}

.forgot-header {
  color: #000000;
  text-align: center;
  font-size: 25px;
}

/* 头像上传 */
.avatar-uploader {
  text-align: center;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}


.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  border-radius: 10px;
  border: 1px dashed #d9d9d9;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
  border-radius: 6px;
}

/* 轮播图隐藏箭头和指示器 */
:deep(.el-carousel__arrow) {
  display: none !important;
}


</style>