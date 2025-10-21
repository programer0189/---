<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="news-detail">
    <h1>校园网升级通知</h1>
    <p class="subtitle">校园网将进行全面升级，提升网络速度和稳定性</p>
    <div class="content">
    
      <p>　　为提升校园网络服务质量，学校将于近期对校园网进行全面升级改造，包括网络设备更新、带宽扩容和网络安全加固。</p>
      <h3>升级内容</h3>
      <p>　　<b>网络设备</b>：更换核心交换机和路由器，提升网络处理能力</p>
      <p>　　<b>带宽扩容</b>：主干网络带宽从10G提升至100G</p>
      <p>　　<b>无线覆盖</b>：增加WiFi6设备，提升无线网络性能</p>
      <p>　　<b>安全加固</b>：升级防火墙和入侵检测系统</p>
      <h3>升级时间</h3>
      <p>　　升级时间：<b>2025年2月1日-2月3日</b></p>
      <p>　　升级期间可能出现网络中断，请师生提前做好准备</p>
      <h3>升级后效果</h3>
      <p>　　网络速度提升10倍，支持4K视频流畅播放</p>
      <p>　　无线网络覆盖更全面，信号更稳定</p>
      <p>　　网络安全防护能力大幅提升</p>
      <p>　　联系方式：网络中心 | 电话：123-456-7899</p>
    </div>
  </div>
</template>

<style scoped>
.news-detail h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
  text-align: center;
  font-family: "SimSun", "宋体", serif;
}

.subtitle {
  font-size: 18px;
  color: #666;
  margin-bottom: 15px;
  text-align: center;
  font-family: "SimSun", "宋体", serif;
}

.content {
  line-height: 1.8;
  font-family: "SimSun", "宋体", serif;
}

.content h2 {
  font-size: 22px;
  color: #444;
  margin-top: 20px;
  margin-bottom: 10px;
  text-align: center;
  font-family: "SimSun", "宋体", serif;
}

.content h3 {
  font-size: 18px;
  color: #555;
  margin-top: 15px;
  margin-bottom: 10px;
  text-align: center;
  font-family: "SimSun", "宋体", serif;
}

.content p {
  font-size: 16px;
  color: #444;
  margin-bottom: 15px;
  text-indent: 2em;
  font-family: "SimSun", "宋体", serif;
}

.news-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  margin-bottom: 20px;
}
</style>
