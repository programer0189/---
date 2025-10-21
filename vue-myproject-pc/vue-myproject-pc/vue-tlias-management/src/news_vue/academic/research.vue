<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="news-detail">
    <h1>学术研究：量子计算新突破</h1>
    <p class="subtitle">我校科研团队在量子计算领域取得重要突破</p>
    <div class="content">
  
      <h2>研究背景</h2>
      <p>　　量子计算是未来计算技术的重要发展方向，我校计算机科学与技术学院联合物理学院，在量子算法和量子纠错方面取得了重要进展。</p>
      <h3>主要成果</h3>
      <p>　　<b>量子算法优化</b>：提出了新的量子搜索算法，搜索效率提升30%</p>
      <p>　　<b>量子纠错技术</b>：开发了基于表面码的量子纠错方案</p>
      <p>　　<b>量子模拟器</b>：构建了小型量子模拟器，支持10个量子比特</p>
      <h3>技术特点</h3>
      <p>　　1. 算法复杂度从O(√N)降低到O(√N/log N)</p>
      <p>　　2. 纠错成功率从85%提升到95%</p>
      <p>　　3. 量子比特相干时间延长至毫秒级</p>
      <h3>应用前景</h3>
      <p>　　该技术可应用于密码学、药物研发、材料科学等领域，具有重要的理论价值和实际应用前景。</p>
      <p>　　联系方式：计算机学院 | 电话：123-456-7892</p>
    </div>
  </div>
</template>

<style scoped>
.news-detail h1 {
  font-size: 28px;
  color: #000;
  margin-bottom: 10px;
  text-align: center;
}

.subtitle {
  font-size: 18px;
  color: #000;
  margin-bottom: 15px;
  text-align: center;
}

.publish-time {
  font-size: 14px;
  color: #999;
  margin-bottom: 20px;
  text-align: center;
  font-family: "SimSun", "宋体", serif;
}

.content {
  line-height: 1.8;
}

.content h2 {
  font-size: 22px;
  color: #000;
  margin-top: 20px;
  margin-bottom: 10px;
  text-align: center;
}

.content h3 {
  font-size: 18px;
  color: #000;
  margin-top: 15px;
  margin-bottom: 10px;
  text-align: center;
}

.content p {
  font-size: 16px;
  color: #000;
  margin-bottom: 15px;
  text-indent: 2em;
}

.news-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  margin-bottom: 20px;
}

.highlight {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  text-align: center;
  margin-top: 20px;
}

strong {
  color: #000;
}
</style>
