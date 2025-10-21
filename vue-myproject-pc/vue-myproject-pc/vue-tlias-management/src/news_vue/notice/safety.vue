<template>
  <div class="news-detail">
    <h1>校园安全注意事项</h1>
    <p class="subtitle">为确保校园安全，请师生注意以下安全事项</p>
    <div class="content">
      <img src="@/assets/greener.jpg" alt="校园安全" class="news-image">
      <h2>安全注意事项详情</h2>
      <p>为保障全体师生的人身和财产安全，维护良好的校园秩序，学校特制定以下安全注意事项。请全体师生认真阅读并严格遵守，共同营造安全、和谐的校园环境。</p>
      
      <h3>人身安全</h3>
      <p><strong>出行安全</strong>：夜间出行请结伴而行，避免单独行动</p>
      <p><strong>交通安全</strong>：遵守交通规则，校内限速20公里/小时</p>
      <p><strong>运动安全</strong>：参加体育活动前做好热身，避免运动损伤</p>
      <p><strong>饮食安全</strong>：注意饮食卫生，避免食用过期或变质食品</p>
      
      <h3>财产安全</h3>
      <p><strong>贵重物品</strong>：贵重物品请随身携带，不要随意放置</p>
      <p><strong>宿舍安全</strong>：离开宿舍时锁好门窗，贵重物品放入保险柜</p>
      <p><strong>财物保管</strong>：在公共场所注意保管好随身物品</p>
      <p><strong>防范诈骗</strong>：提高警惕，防范各类电信诈骗和网络诈骗</p>
      
      <h3>消防安全</h3>
      <p><strong>用电安全</strong>：不使用大功率电器，不私拉乱接电线</p>
      <p><strong>明火管理</strong>：宿舍内严禁使用明火，不吸烟、不点蜡烛</p>
      <p><strong>消防设施</strong>：爱护消防设施，不得随意移动或损坏</p>
      <p><strong>应急疏散</strong>：熟悉安全出口位置，掌握应急疏散路线</p>
      
      <h3>网络安全</h3>
      <p><strong>个人信息</strong>：保护个人隐私，不随意泄露个人信息</p>
      <p><strong>网络交友</strong>：谨慎网络交友，避免上当受骗</p>
      <p><strong>网络言论</strong>：文明上网，不传播虚假信息和不当言论</p>
      <p><strong>网络游戏</strong>：适度游戏，避免沉迷影响学习和生活</p>
      
      <h3>实验室安全</h3>
      <p><strong>操作规程</strong>：严格遵守实验室操作规程，不得违规操作</p>
      <p><strong>防护措施</strong>：按要求佩戴防护用品，注意实验安全</p>
      <p><strong>危险品管理</strong>：危险化学品严格管理，不得私自取用</p>
      <p><strong>应急处理</strong>：掌握实验室应急处理程序，遇险及时报告</p>
      
      <h3>食品安全</h3>
      <p><strong>食堂就餐</strong>：选择正规食堂就餐，注意食品卫生</p>
      <p><strong>外卖选择</strong>：选择有资质的正规外卖商家</p>
      <p><strong>食品储存</strong>：注意食品保质期，避免食用过期食品</p>
      <p><strong>过敏防范</strong>：有食物过敏史的学生要特别注意</p>
      
      <h3>心理健康</h3>
      <p><strong>情绪管理</strong>：学会调节情绪，保持积极乐观的心态</p>
      <p><strong>人际交往</strong>：建立良好的人际关系，学会沟通交流</p>
      <p><strong>压力调节</strong>：学会缓解学习压力，保持心理健康</p>
      <p><strong>求助意识</strong>：遇到心理问题及时寻求帮助</p>
      
      <h3>应急处理</h3>
      <p><strong>紧急情况</strong>：遇到紧急情况保持冷静，及时报警求助</p>
      <p><strong>报警电话</strong>：校园报警电话110，火警电话119</p>
      <p><strong>医疗急救</strong>：校医院急救电话120，24小时值班</p>
      <p><strong>信息报告</strong>：及时向辅导员或相关部门报告异常情况</p>
      
      <h3>联系方式</h3>
      <p>保卫处：010-87654321（24小时值班）</p>
      <p>校医院：010-87654322（24小时值班）</p>
      <p>心理咨询中心：010-87654323（工作日8:00-17:00）</p>
      <p>学生处：010-87654324（工作日8:00-17:00）</p>
      <p>紧急情况：110（报警）、120（急救）、119（火警）</p>
      
      <p class="highlight">安全无小事，责任重于山！让我们共同维护校园安全！</p>
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
