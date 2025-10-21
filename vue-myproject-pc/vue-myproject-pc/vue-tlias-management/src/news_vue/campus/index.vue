<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="news-detail">
    <h1>校园活动通知</h1>
    <p class="subtitle">8月15日东南门广场举办绿色校园主题活动，包括植树和讲座</p>
    <div class="content">
      <img src="@/assets/gate.jpg" alt="校园活动" class="news-image">
      <h2>绿色校园主题活动</h2>
      <p>为响应国家环保号召，建设美丽校园，我校将于<strong>8月15日</strong>在东南门广场举办"绿色校园，从我做起"主题环保活动。</p>
      
      <h3>活动内容</h3>
      <p><strong>植树活动</strong>：师生共同参与校园绿化，种植各类树木和花卉</p>
      <p><strong>环保讲座</strong>：邀请环保专家进行专题讲座，普及环保知识</p>
      <p><strong>垃圾分类展示</strong>：现场演示正确的垃圾分类方法</p>
      <p><strong>环保手工制作</strong>：利用废旧物品制作环保工艺品</p>
      
      <h3>活动安排</h3>
      <p><strong>09:00-09:30</strong>：活动开幕式</p>
      <p><strong>09:30-11:00</strong>：植树活动</p>
      <p><strong>11:00-12:00</strong>：环保讲座</p>
      <p><strong>14:00-16:00</strong>：垃圾分类展示与互动</p>
      <p><strong>16:00-17:00</strong>：环保手工制作</p>
      
      <h3>参与方式</h3>
      <p>欢迎全校师生踊跃参与！请提前在班级群或部门群报名，活动当天请携带身份证件，穿着舒适服装，自备水杯。</p>
      
      <h3>注意事项</h3>
      <p>请遵守活动现场秩序，听从工作人员指挥</p>
      <p>植树活动请穿着运动鞋，注意安全</p>
      <p>如遇恶劣天气，活动将延期举行</p>
      <p>活动结束后请保持场地整洁</p>
      
      <h3>联系方式</h3>
      <p>如有疑问，请联系学生处环保活动组：</p>
      <p>电话：010-12345678</p>
      <p>邮箱：green@university.edu.cn</p>
      
      <p class="highlight">让我们携手共建绿色校园，为地球环保贡献一份力量！</p>
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