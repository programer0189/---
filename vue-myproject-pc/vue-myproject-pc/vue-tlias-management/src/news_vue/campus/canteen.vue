<script setup>
// 组件逻辑
</script>

<template>
  <div class="news-detail">
    <h1>食堂新菜品推荐</h1>
    <p class="subtitle">食堂推出多款新菜品，丰富师生用餐选择</p>
    <div class="content">
      <img src="@/assets/sports.jpg" alt="食堂新菜品" class="news-image">
      <h2>新菜品详情</h2>
      <p>为提升师生用餐体验，丰富食堂菜品种类，我校食堂经过精心研发和试菜，现推出多款新菜品。这些菜品既保持了传统美食的风味，又融入了现代烹饪理念，为师生提供更加多样化的用餐选择。</p>
      
      <h3>新菜品介绍</h3>
      <p><strong>川菜系列</strong>：麻婆豆腐、宫保鸡丁、水煮鱼、回锅肉、辣子鸡、夫妻肺片</p>
      <p><strong>粤菜系列</strong>：白切鸡、烧鹅、叉烧肉、清蒸鲈鱼、白灼虾、蒜蓉粉丝蒸扇贝</p>
      <p><strong>湘菜系列</strong>：剁椒鱼头、农家小炒肉、干锅牛蛙、臭豆腐、口味虾、酸辣土豆丝</p>
      <p><strong>鲁菜系列</strong>：糖醋里脊、九转大肠、德州扒鸡、葱烧海参、油爆双脆、锅塌豆腐</p>
      
      <h3>特色菜品</h3>
      <p><strong>创新融合菜</strong>：中西合璧的创意菜品，如黑椒牛排配意面、芝士焗饭、泰式咖喱鸡</p>
      <p><strong>健康轻食</strong>：低脂低糖的健康菜品，如蒸蛋羹、清炒时蔬、白灼菜心、蒸鱼片</p>
      <p><strong>季节限定</strong>：根据时令推出的特色菜品，如春季野菜、夏季凉菜、秋季滋补汤</p>
      
      <h3>价格信息</h3>
      <p><strong>素菜类</strong>：8-15元/份，新鲜时蔬，营养丰富</p>
      <p><strong>荤菜类</strong>：15-25元/份，精选食材，口感鲜美</p>
      <p><strong>特色菜</strong>：20-35元/份，创新工艺，独特风味</p>
      <p><strong>汤品类</strong>：5-12元/份，营养美味，暖胃暖心</p>
      
      <h3>供应时间</h3>
      <p><strong>早餐</strong>：6:30-9:00，提供包子、油条、豆浆、粥品等传统早餐</p>
      <p><strong>午餐</strong>：11:30-13:30，提供各类炒菜、汤品、米饭、面食</p>
      <p><strong>晚餐</strong>：17:30-19:30，提供晚餐菜品，种类丰富</p>
      <p><strong>夜宵</strong>：20:00-22:00，提供小炒、烧烤、夜宵小吃</p>
      
      <h3>用餐环境</h3>
      <p>食堂环境整洁明亮，空调恒温，座位舒适</p>
      <p>提供免费WiFi，方便师生用餐时上网</p>
      <p>设有专门的清真窗口，满足不同饮食需求</p>
      <p>支持校园卡和移动支付，方便快捷</p>
      
      <h3>营养搭配</h3>
      <p>每道菜品都经过营养师精心搭配，确保营养均衡</p>
      <p>提供多种蔬菜搭配，满足维生素和膳食纤维需求</p>
      <p>肉类菜品选用优质食材，蛋白质含量丰富</p>
      <p>汤品营养丰富，补充身体所需的各种微量元素</p>
      
      <h3>反馈渠道</h3>
      <p>师生可通过食堂意见箱反馈用餐体验</p>
      <p>支持线上反馈，扫描二维码即可评价</p>
      <p>定期召开师生座谈会，听取用餐建议</p>
      <p>根据反馈及时调整菜品口味和种类</p>
      
      <h3>联系方式</h3>
      <p>食堂管理办公室：010-87654321</p>
      <p>邮箱：canteen@university.edu.cn</p>
      <p>地址：学生食堂一楼管理办公室</p>
      <p>办公时间：周一至周日 6:00-22:00</p>
      
      <p class="highlight">品味美食，享受生活！欢迎师生前来品尝新菜品！</p>
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
