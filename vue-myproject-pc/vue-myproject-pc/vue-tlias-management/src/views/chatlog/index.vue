<script setup>
import * as echarts from 'echarts'
import { onMounted, ref, nextTick } from 'vue'
import { getPostData } from '@/api/report'

const postStatusChart = ref(null)
const postTypeChart = ref(null)

const postData = ref({})

onMounted(async () => {
  await nextTick()

  const statusMyChart = echarts.init(postStatusChart.value)
  const typeMyChart = echarts.init(postTypeChart.value)

  // 获取帖子统计数据
  const postRes = await getPostData()
  if (postRes.code === 1) {
    postData.value = postRes.data
  }

  // 帖子状态分布饼图
  const statusData = [
    { name: '待审核', value: postData.value.pendingPosts || 0 },
    { name: '已通过', value: postData.value.approvedPosts || 0 },
    { name: '已拒绝', value: postData.value.rejectedPosts || 0 },
    { name: '已删除', value: postData.value.deletedPosts || 0 }
  ]

  statusMyChart.setOption({
    title: { text: '帖子状态分布', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { orient: 'horizontal', bottom: 10 },
    series: [
      {
        name: '状态统计',
        type: 'pie',
        radius: '50%',
        data: statusData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })

  // 帖子类型分布柱状图
  const typeData = [
    { name: '表白', value: postData.value.confessionPosts || 0 },
    { name: '吐槽', value: postData.value.expressPosts || 0 },
    { name: '学习', value: postData.value.studyPosts || 0 },
    { name: '活动', value: postData.value.activityPosts || 0 },
    { name: '失物招领', value: postData.value.lostFoundPosts || 0 },
    { name: '聊天', value: postData.value.chatPosts || 0 }
  ]

  typeMyChart.setOption({
    title: { text: '帖子类型分布', left: 'center' },
    tooltip: {},
    xAxis: {
      type: 'category',
      data: typeData.map(item => item.name)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: typeData.map(item => item.value),
        type: 'bar'
      }
    ]
  })

  window.addEventListener('resize', () => {
    statusMyChart.resize()
    typeMyChart.resize()
  })
})
</script>

<template>
  <div>
    <h2>帖子数据管理</h2>
    
    <!-- 帖子数量统计表格 -->
    <div class="count-table-container">
 
      <div class="count-table">
        <div 
          v-for="item in [
            { label: '总帖子数', value: postData.totalPosts || 0},
            { label: '今日新增', value: postData.todayNewPosts || 0 },
            { label: '本周新增', value: postData.weekNewPosts || 0},
            { label: '本月新增', value: postData.monthNewPosts || 0 }
          ]" 
          :key="item.label" 
          class="count-item"
          :style="{ borderLeftColor: item.color }"
        >
          <div class="count-label">{{ item.label }}</div>
          <div class="count-value">{{ item.value }}</div>
        </div>
      </div>
    </div>

    <div class="charts-container">
      <div ref="postStatusChart" class="chart"></div>
      <div ref="postTypeChart" class="chart"></div>
    </div>
  </div>
</template>

<style scoped>
.charts-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: nowrap;
  width: 100%;
  margin-top: 100px;
}

.chart {
  width: 48%;
  height: 400px;
}

h2 {
  text-align: center;
}

.count-table-container {

  border-radius: 8px;

  padding: 20px;
  margin-bottom: 30px;
  box-sizing: border-box;
}

.count-table-container h3 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 25px;
}

.count-table {
  display: flex;
  justify-content: space-around;
  gap: 20px;
}

.count-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #a93f3f;
  transition: all 0.3s ease;
  min-width: 120px;
}

.count-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.count-label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
  margin-bottom: 10px;
  text-align: center;
}

.count-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}
</style>