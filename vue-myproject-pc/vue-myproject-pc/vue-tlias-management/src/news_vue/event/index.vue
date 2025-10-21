<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="news-detail">
    <h1>春季运动会报名开始</h1>
    <p class="subtitle">一年一度的春季运动会即将开始，现开始接受报名</p>
    <div class="content">
      <img src="@/assets/sports.jpg" alt="春季运动会" class="news-image">
      <h2>运动会详情</h2>
      <p>为增强学生体质，培养体育精神，促进校园体育文化建设，我校将于<strong>2025年春季</strong>举办全校运动会。这是一年一度的体育盛事，是展现青春风采、挑战自我极限的绝佳机会！</p>
      
      <h3>比赛项目</h3>
      <p><strong>径赛项目</strong>：100米、200米、400米、800米、1500米、3000米、4×100米接力、4×400米接力、110米栏、400米栏</p>
      <p><strong>田赛项目</strong>：跳高、跳远、三级跳远、铅球、铁饼、标枪、男子十项全能、女子七项全能</p>
      <p><strong>趣味项目</strong>：拔河、集体跳绳、趣味接力、袋鼠跳、两人三足、背夹球、定向越野、体育知识竞答</p>
      
      <h3>参赛要求</h3>
      <p><strong>参赛资格</strong>：全校在校学生，身体健康，无重大疾病史</p>
      <p><strong>报名限制</strong>：每人最多可报名3个个人项目，2个团体项目</p>
      <p><strong>团队规模</strong>：团体项目每队不超过5人，可跨专业组队</p>
      
      <h3>大赛流程</h3>
      <p><strong>报名阶段</strong>：2025年2月1日-2月15日，通过各院系体育部报名</p>
      <p><strong>预赛阶段</strong>：2025年3月1日-3月5日，各项目预赛，选拔决赛选手</p>
      <p><strong>决赛阶段</strong>：2025年3月15日，各项目决赛，决出最终名次</p>
      
      <h3>奖励设置</h3>
      <p><strong>个人项目</strong>：前三名颁发奖牌和证书，冠军奖金500元，亚军奖金300元，季军奖金200元</p>
      <p><strong>团体项目</strong>：前三名颁发奖杯和证书，冠军奖金1000元，亚军奖金600元，季军奖金400元</p>
      
      <h3>注意事项</h3>
      <p>参赛选手必须携带学生证和身份证，否则不得参赛</p>
      <p>请穿着适合的运动服装和运动鞋，注意安全</p>
      <p>比赛前请充分热身，避免运动损伤</p>
      <p>遵守比赛规则，服从裁判判决</p>
      <p>如遇恶劣天气，比赛将延期举行</p>
      <p>请保持场地整洁，爱护体育设施</p>
      
      <h3>联系方式</h3>
      <p>体育部办公室：010-87654321</p>
      <p>邮箱：sports@university.edu.cn</p>
      <p>地址：体育馆一楼体育部办公室</p>
      <p>办公时间：周一至周五 8:00-17:00</p>
      
      <p class="highlight">挥洒汗水，追逐梦想，让我们在运动场上展现青春风采！</p>
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
  max-height: 400px;
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