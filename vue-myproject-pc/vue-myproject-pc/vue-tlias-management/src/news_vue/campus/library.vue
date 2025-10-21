<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="news-detail">
    <h1>图书馆新增电子资源</h1>
    <p class="subtitle">为提升学生学习体验，图书馆新增多种电子资源</p>
    <div class="content">
      <img src="@/assets/library.jpg" alt="图书馆电子资源" class="news-image">
      <h2>提升学习体验，丰富知识资源</h2>
      <p>为满足师生日益增长的学术研究需求，提升学习体验，我校图书馆经过精心筛选和谈判，新增了多种高质量的电子资源，为广大师生提供更加便捷、丰富的学术资源服务。</p>
      
      <h3>新增数据库资源</h3>
      <p><strong>学术期刊数据库</strong>：中国知网（CNKI）全库、万方数据库、维普期刊数据库、ScienceDirect（爱思唯尔）、IEEE Xplore数字图书馆</p>
      <p><strong>电子图书资源</strong>：超星数字图书馆、读秀学术搜索、Springer电子图书、Wiley在线图书、ProQuest电子书</p>
      <p><strong>专业数据库</strong>：Web of Science核心合集、Scopus文摘数据库、EI工程索引、CSSCI中文社会科学引文索引、中国科学引文数据库</p>
      
      <h3>特色功能服务</h3>
      <p><strong>统一检索平台</strong>：提供跨库检索功能，一次检索即可获得多个数据库的检索结果，大大提高检索效率。</p>
      <p><strong>移动端访问</strong>：支持手机、平板等移动设备访问，随时随地获取学术资源。</p>
      <p><strong>文献传递服务</strong>：对于本馆未收藏的文献，提供文献传递服务，满足特殊研究需求。</p>
      
      <h3>使用指南</h3>
      <p><strong>访问方式</strong>：校园网内可直接访问，校外可通过VPN访问，支持移动端访问</p>
      <p><strong>检索技巧</strong>：使用高级检索功能，利用主题词表，结合多个数据库，保存检索策略</p>
      
      <h3>培训安排</h3>
      <p>为帮助师生更好地使用新增电子资源，图书馆将举办系列培训讲座：</p>
      <p><strong>8月20日 14:00-16:00</strong>：学术数据库使用技巧</p>
      <p><strong>8月25日 14:00-16:00</strong>：文献检索与分析方法</p>
      <p><strong>8月30日 14:00-16:00</strong>：移动端资源使用指南</p>
      
      <h3>联系方式</h3>
      <p>如有使用问题或建议，请联系图书馆信息咨询部：</p>
      <p>咨询电话：010-87654321</p>
      <p>邮箱：library@university.edu.cn</p>
      <p>在线咨询：图书馆官网"在线咨询"栏目</p>
      <p>现场咨询：图书馆一楼信息咨询台</p>
      
      <p class="highlight">让知识触手可及，让学习更加高效！</p>
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