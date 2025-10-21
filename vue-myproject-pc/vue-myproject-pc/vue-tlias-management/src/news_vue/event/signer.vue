<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="news-detail">
    <h1>校园歌手大赛海选</h1>
    <p class="subtitle">展现青春风采的舞台，校园歌手大赛海选开始</p>
    <div class="content">
      <img src="@/assets/signer.jpg" alt="校园歌手大赛" class="news-image">
      <h2>大赛详情</h2>
      <p>展现青春风采的舞台，校园歌手大赛海选正式开始！这是一个让音乐梦想起航的舞台，这是一个展现才华的绝佳机会。无论你是流行、摇滚、民谣还是古典音乐的爱好者，这里都有属于你的舞台！</p>
      
      <h3>比赛形式</h3>
      <p><b>海选阶段</b>：参赛者需准备1-2首歌曲，清唱或伴奏均可，时长控制在3-5分钟。评委将根据音准、节奏、表现力等方面进行评分。</p>
      <p><b>复赛阶段</b>：通过海选的选手进入复赛，需要准备完整的伴奏版本，评委将综合评估选手的综合实力。</p>
      <p><b>决赛阶段</b>：最终10名选手进入决赛，进行现场演唱，决出最终名次。决赛将邀请专业音乐人担任评委。</p>
      
      <h3>参赛要求</h3>
      <p><b>参赛资格</b>：全校在校学生，热爱音乐，有一定演唱基础</p>
      <p><b>曲目要求</b>：歌曲内容健康向上，时长适中，风格不限</p>
      <p><b>报名方式</b>：线上报名或现场报名，需提交个人简介和演唱视频</p>
      
      <h3>比赛时间安排</h3>
      <p><b>海选报名</b>：8月20日 - 9月5日，提交报名表和演唱视频</p>
      <p><b>海选比赛</b>：9月10日 - 9月15日，分批进行海选，选拔优秀选手</p>
      <p><b>复赛</b>：9月20日 - 9月25日，复赛选手进行第二轮比拼</p>
      <p><b>决赛</b>：10月1日，决赛暨颁奖典礼</p>
      
      <h3>评分标准</h3>
      <p><b>音准节奏 (40%)</b>：音高准确度、节奏稳定性</p>
      <p><b>演唱技巧 (30%)</b>：气息控制、发声技巧、情感表达</p>
      <p><b>舞台表现 (20%)</b>：台风、互动、舞台魅力</p>
      <p><b>创新创意 (10%)</b>：曲目选择、编曲改编、个人特色</p>
      
      <h3>奖励设置</h3>
      <p><b>冠军</b>：奖金3000元 + 奖杯 + 证书</p>
      <p><b>亚军</b>：奖金2000元 + 奖杯 + 证书</p>
      <p><b>季军</b>：奖金1000元 + 奖杯 + 证书</p>
      <p><b>优秀奖</b>：证书 + 纪念品</p>
      
      <h3>特别福利</h3>
      <p>优秀选手有机会参与校园文艺演出，获得专业音乐人指导机会，优秀表演将在校园电视台播出，优先参与各类校园文化活动。</p>
      
      <h3>报名方式</h3>
      <p><b>线上报名</b>：访问学校官网"校园活动"栏目，填写报名表并上传演唱视频</p>
      <p><b>现场报名</b>：到团委办公室现场报名，需携带学生证</p>
      <p><b>微信报名</b>：关注"校团委"公众号，点击"歌手大赛"进行报名</p>
      
      <h3>联系方式</h3>
      <p>团委办公室：010-87654321</p>
      <p>邮箱：tuanwei@university.edu.cn</p>
      <p>地址：学生活动中心二楼团委办公室</p>
      <p>办公时间：周一至周五 8:00-17:00</p>
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