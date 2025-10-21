<template>
  <div class="news-detail">
    <h1>期末考试时间安排</h1>
    <p class="subtitle">本学期期末考试将于12月25日开始</p>
    <div class="content">
      <img src="@/assets/class.jpg" alt="期末考试安排" class="news-image">
      <h2>考试安排详情</h2>
      <p>根据学校教学计划安排，2024-2025学年第一学期期末考试将于2024年12月25日开始。为确保考试顺利进行，现将考试安排及相关要求通知如下，请全体师生认真阅读并严格执行。</p>
      
      <h3>考试时间</h3>
      <p><strong>考试开始时间</strong>：2024年12月25日（周三）</p>
      <p><strong>考试结束时间</strong>：2025年1月17日（周五）</p>
      <p><strong>考试周安排</strong>：第17-20周为考试周</p>
      <p><strong>每天考试时间</strong>：上午8:30-10:30，下午14:00-16:00</p>
      
      <h3>考试科目安排</h3>
      <p><strong>公共基础课</strong>：大学英语、高等数学、大学物理、计算机基础</p>
      <p><strong>专业核心课</strong>：各专业核心课程，具体安排见各院系通知</p>
      <p><strong>选修课程</strong>：根据选课情况安排，部分课程采用论文或项目考核</p>
      <p><strong>实践课程</strong>：实验、实习等实践课程在考试周前完成考核</p>
      
      <h3>考试地点</h3>
      <p><strong>主考场</strong>：教学楼A、B、C栋各教室</p>
      <p><strong>特殊考场</strong>：计算机房、语音室、实验室等专业考场</p>
      <p><strong>备用考场</strong>：图书馆报告厅、学生活动中心等</p>
      <p><strong>考场查询</strong>：学生可登录教务系统查询具体考场安排</p>
      
      <h3>考试要求</h3>
      <p><strong>证件要求</strong>：必须携带学生证和身份证，否则不得参加考试</p>
      <p><strong>时间要求</strong>：提前15分钟到达考场，迟到15分钟以上不得入场</p>
      <p><strong>物品要求</strong>：只允许携带必要的文具，手机等电子设备需关机存放</p>
      <p><strong>着装要求</strong>：着装得体，不得穿拖鞋、背心等不雅服装</p>
      
      <h3>考试纪律</h3>
      <p>严格遵守考场纪律，服从监考老师管理</p>
      <p>考试期间不得交头接耳、左顾右盼</p>
      <p>不得抄袭、作弊，一经发现将严肃处理</p>
      <p>考试结束铃响后立即停止答题，不得拖延时间</p>
      <p>考试结束后有序离场，不得在考场附近逗留喧哗</p>
      
      <h3>成绩查询</h3>
      <p><strong>成绩公布</strong>：考试结束后7个工作日内公布成绩</p>
      <p><strong>查询方式</strong>：登录教务系统或关注学校微信公众号</p>
      <p><strong>成绩复议</strong>：对成绩有异议的学生可在成绩公布后3天内申请复议</p>
      <p><strong>补考安排</strong>：不及格课程将在下学期开学前安排补考</p>
      
      <h3>考前准备</h3>
      <p>认真复习各门课程，做好知识梳理和重点掌握</p>
      <p>合理安排作息时间，保证充足的睡眠和休息</p>
      <p>准备必要的考试用品，如2B铅笔、橡皮、计算器等</p>
      <p>熟悉考场位置，提前规划到达路线</p>
      <p>保持良好的心态，以积极的态度面对考试</p>
      
      <h3>特殊情况处理</h3>
      <p><strong>因病缺考</strong>：需提供医院证明，经批准后可申请缓考</p>
      <p><strong>因事缺考</strong>：需提前申请，经批准后方可缓考</p>
      <p><strong>考试冲突</strong>：如有考试时间冲突，请及时联系教务处</p>
      <p><strong>紧急情况</strong>：考试期间如遇紧急情况，可联系监考老师或教务处</p>
      
      <h3>联系方式</h3>
      <p>教务处考试科：010-87654321</p>
      <p>各院系教务办：具体联系方式见各院系通知</p>
      <p>考试监督电话：010-87654322</p>
      <p>紧急联系：010-87654323（考试期间24小时值班）</p>
      
      <p class="highlight">预祝全体同学考试顺利，取得优异成绩！</p>
    </div>
  </div>
</template>

<script setup>
// 组件逻辑
</script>

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
