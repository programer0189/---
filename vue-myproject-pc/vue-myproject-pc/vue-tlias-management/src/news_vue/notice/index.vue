<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="news-detail">
    <h1>寒假放假安排通知</h1>
    <p class="subtitle">根据学校安排，寒假将于1月20日开始</p>
    <div class="content">
     
      <p>　　根据学校教学安排和节假日管理规定，现将2025年寒假放假安排通知如下：</p>
      <h3>放假时间</h3>
      <p>　　<b>放假开始</b>：2025年1月20日（周一）</p>
      <p>　　<b>返校时间</b>：2025年2月24日（周一）</p>
      <p>　　<b>放假天数</b>：共计35天</p>
      <h3>注意事项</h3>
      <p>　　1. 离校前请关闭宿舍电源，锁好门窗</p>
      <p>　　2. 贵重物品请随身携带，不要留在宿舍</p>
      <p>　　3. 离校前请到宿管处登记离校时间</p>
      <p>　　4. 返校时请携带学生证和身份证</p>
      <h3>安全提醒</h3>
      <p>　　假期期间请注意人身和财产安全，遵守交通规则，避免参与危险活动。</p>
      <p>　　联系方式：学生处 | 电话：123-456-7891</p>
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

.publish-time {
  font-size: 14px;
  color: #999;
  margin-bottom: 20px;
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