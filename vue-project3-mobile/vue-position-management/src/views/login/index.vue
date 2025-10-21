<script setup>
import { ref, onMounted } from 'vue'
import { UserloginApi } from '@/api/userLogin.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '@/utils/request.js'
import { userAPI } from '@/api/chat.js'
import { Plus } from '@element-plus/icons-vue'
import { isLoggedIn, setUserAuth } from '@/utils/auth.js'

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
    // 使用新的认证工具函数设置用户信息
    setUserAuth(result.data)
    
    // 登录成功后直接跳转到首页
    router.push('/app/home')
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

// 检查用户是否已登录
onMounted(() => {
  // 使用新的认证工具函数检查登录状态
  if (isLoggedIn()) {
    // 用户已登录，直接跳转到首页
    router.push('/app/home')
  }
})
</script>

<template>
  <div class="login-container">
 

      
      <!-- 右侧登录表单 -->
      <div class="login-form">
        <div class="form-header">
          <h1 class="title">校智通</h1>
          <p class="subtitle">智慧校园，连接你我</p>
        </div>

        <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form-content">
          <div class="form-group">
            <label class="form-label">用户名</label>
            <el-input
              v-model="loginForm.username"
              type="text"
              placeholder="请输入用户名"
              class="form-input"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">密码</label>
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              class="form-input"
              size="large"
              show-password
            />
          </div>
        </el-form>

        <div class="form-actions">
          <button @click="login" class="login-btn">登录</button>
          
          <div class="form-links">
            <a href="javascript:void(0)" @click="register" class="link-btn">注册账号</a>
            <span class="divider">|</span>
            <a href="javascript:void(0)" @click="forgotPassword" class="link-btn">忘记密码</a>
          </div>
        </div>
      </div>
   
  </div>

  <!-- 注册对话框 -->
  <el-dialog v-model="showRegister" title="注册" width="400" class="register-dialog">
    <template #header>
      <div class="dialog-header register-header">注册账号</div>
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
        <el-button type="primary" @click="submitRegister" class="submit-btn">注册</el-button>
        <el-button @click="showRegister = false" class="cancel-btn">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 忘记密码对话框 -->
  <el-dialog v-model="showForgot" title="忘记密码" width="400" class="forgot-dialog">
    <template #header>
      <div class="dialog-header forgot-header">重置密码</div>
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
        <el-button type="primary" @click="submitForgotPassword" class="submit-btn">提交</el-button>
        <el-button @click="showForgot = false" class="cancel-btn">取消</el-button>
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
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 登录框 */
.login-box {
  display: flex;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(255, 255, 255, 0.3);
  overflow: hidden;
  width: 1000px;
  height: 600px;
  border: 1px solid #e0e0e0;
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
  filter: grayscale(30%);
}

.carousel-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:white;
  z-index: 1;
}

/* 右侧登录表单 */
.login-form {
  flex: 1;
  padding: 60px 50px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #ffffff;
}

.form-header {
  text-align: center;
  margin-bottom: 50px;
}

.title {
  font-size: 36px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 12px 0;
  letter-spacing: 2px;
}

.subtitle {
  font-size: 16px;
  color: #666666;
  margin: 0;
  font-weight: 400;
}

.login-form-content {
  margin-bottom: 40px;
}

.form-group {
  margin-bottom: 30px;
}

.form-label {
  display: block;
  font-size: 14px;
  color: #333333;
  margin-bottom: 12px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.form-input {
  width: 100%;
}

.form-input :deep(.el-input__wrapper) {
  background: #f8f9fa;
  border: 2px solid #e9ecef;
  border-radius: 12px;
  box-shadow: none;
  transition: all 0.3s ease;
}

.form-input :deep(.el-input__wrapper:hover) {
  border-color: #666666;
}

.form-input :deep(.el-input__wrapper.is-focus) {
  border-color: #1a1a1a;
  background: #ffffff;
  box-shadow: 0 0 0 3px rgba(26, 26, 26, 0.1);
}

.form-input :deep(.el-input__inner) {
  color: #333333;
  font-size: 16px;
  padding: 16px 20px;
}

.form-input :deep(.el-input__inner::placeholder) {
  color: #999999;
}

/* 表单操作区域 */
.form-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.login-btn {
  width: 100%;
  padding: 18px;
  background: linear-gradient(135deg, #1a1a1a 0%, #333333 100%);
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 18px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.login-btn:hover {
  background: linear-gradient(135deg, #000000 0%, #1a1a1a 100%);
  transform: translateY(-2px);
  box-shadow: 0 12px 35px rgba(26, 26, 26, 0.3);
}

.form-links {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.link-btn {
  color: #666666;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  padding: 8px 12px;
  border-radius: 6px;
}

.link-btn:hover {
  color: #1a1a1a;
  background: #f8f9fa;
}

.divider {
  color: #e0e0e0;
  font-weight: 300;
}

/* 对话框样式 */
.register-dialog,
.forgot-dialog {
  border-radius: 16px;
}

.dialog-header {
  color: #1a1a1a;
  font-weight: 700;
  font-size: 24px;
  padding: 20px;
  text-align: center;
  background: #f8f9fa;
  border-radius: 16px 16px 0 0;
  border-bottom: 1px solid #e9ecef;
}

.register-header {
  color: #1a1a1a;
}

.forgot-header {
  color: #1a1a1a;
}

/* 头像上传 */
.avatar-uploader {
  text-align: center;
}

.avatar-uploader .el-upload {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.avatar-uploader .el-upload:hover {
  border-color: #1a1a1a;
  background: #f8f9fa;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #999999;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
  border-radius: 12px;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  gap: 15px;
  justify-content: center;
  align-items: center;
}

/* 按钮样式 */
.submit-btn {
  background: #1a1a1a;
  border-color: #1a1a1a;
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 600;
  min-width: 100px;
  height: 44px;
  flex: 1;
  max-width: 120px;
}

.submit-btn:hover {
  background: #000000;
  border-color: #000000;
}

.cancel-btn {
  background: #f8f9fa;
  border-color: #e9ecef;
  color: #666666;
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 600;
  min-width: 100px;
  height: 44px;
  flex: 1;
  max-width: 120px;
}

.cancel-btn:hover {
  background: #e9ecef;
  border-color: #dee2e6;
  color: #333333;
}

/* 轮播图隐藏箭头和指示器 */
:deep(.el-carousel__arrow) {
  display: none !important;
}

:deep(.el-carousel__indicators) {
  display: none !important;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .login-box {
    width: 90%;
    max-width: 900px;
    height: 550px;
  }
  
  .login-form {
    padding: 40px 30px;
  }
  
  .title {
    font-size: 32px;
  }
}

@media (max-width: 768px) {
  .login-box {
    flex-direction: column;
    height: auto;
    width: 95%;
    max-width: 500px;
  }
  
  .carousel-section {
    height: 200px;
  }
  
  .login-form {
    padding: 40px 30px;
  }
  
  .title {
    font-size: 28px;
  }
  
  .form-links {
    flex-direction: column;
    gap: 15px;
  }
  
  .divider {
    display: none;
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 20px;
  }
  
  .login-box {
    width: 100%;
    border-radius: 16px;
  }
  
  .login-form {
    padding: 30px 20px;
  }
  
  .title {
    font-size: 24px;
  }
  
  .subtitle {
    font-size: 14px;
  }
  
  /* 移动端按钮样式优化 */
  .button-group {
    flex-direction: column;
    gap: 12px;
  }
  
  .submit-btn,
  .cancel-btn {
    width: 100%;
    max-width: none;
    min-width: auto;
    height: 48px;
    font-size: 16px;
  }
  
  /* 移动端对话框宽度调整 */
  .register-dialog,
  .forgot-dialog {
    width: 95% !important;
    margin: 20px auto;
  }
}
</style>
