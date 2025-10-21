<script setup>
import { ref, onMounted } from 'vue'

const username = ref('')

onMounted(() => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))
  username.value = loginUser?.username || ''
})
</script>

<template>
  <div class="home-wrap">
    <h2>欢迎回来，{{ username }}！</h2>
    <p>这里是普通用户首页</p>
  </div>
</template>

<style scoped>
.home-wrap {
  padding: 40px;
  background-color: #f6f6f6;
  min-height: 400px;
}
</style>
