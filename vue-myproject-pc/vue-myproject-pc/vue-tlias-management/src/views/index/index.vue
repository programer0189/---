<script setup>

</script>

<template>
  <div class="logo-container">
    <img src="../../assets/index.png" alt="校智通" class="logo-image" />
  </div>
</template>

<style scoped>
.logo-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.logo-image {
  max-width: 300px;
  max-height: 300px;
  width: auto;
  height: auto;
  object-fit: contain;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  /* 去掉白边 - 设置透明背景 */
  background: transparent;
  /* 如果图片本身有白边，可以使用混合模式 */
  mix-blend-mode: multiply;
  /* 或者使用滤镜去除白色 */
  filter: contrast(1.2) brightness(1.1);
}

.logo-image:hover {
  transform: scale(1.05);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .logo-container {
    padding: 10px;
  }
  
  .logo-image {
    max-width: 250px;
    max-height: 250px;
  }
}

@media (max-width: 480px) {
  .logo-image {
    max-width: 200px;
    max-height: 200px;
  }
}
</style>