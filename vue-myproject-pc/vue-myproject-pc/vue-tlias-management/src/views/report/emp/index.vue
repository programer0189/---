<script setup>
import * as echarts from 'echarts'
import { onMounted, ref, nextTick } from 'vue'
import { getEmpGenderData, getEmpJobData } from '@/api/report.js'

const genderChart = ref(null)
const jobChart = ref(null)

const genderData = ref([])
const jobLabels = ref([])
const jobValues = ref([])

onMounted(async () => {
  await nextTick()

  const genderMyChart = echarts.init(genderChart.value)
  const jobMyChart = echarts.init(jobChart.value)

  const genderRes = await getEmpGenderData()
  const jobRes = await getEmpJobData()

  genderData.value = genderRes.data 
  jobLabels.value = jobRes.data.jobList 
  jobValues.value = jobRes.data.dataList 

  genderMyChart.setOption({
    title: { text: '员工性别分布', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { orient: 'horizontal', bottom: 10 },
    series: [
      {
        name: '性别统计',
        type: 'pie',
        radius: '50%',
        data: genderData.value,
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

  jobMyChart.setOption({
    title: { text: '员工岗位分布', left: 'center' },
    tooltip: {},
    xAxis: {
      type: 'category',
      data: jobLabels.value
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: jobValues.value,
        type: 'bar'
      }
    ]
  })

  window.addEventListener('resize', () => {
    genderMyChart.resize()
    jobMyChart.resize()
  })
})
</script>

<template>
  <div>
    <h2>教职工信息统计</h2>
    <div class="charts-container">
      <div ref="genderChart" class="chart"></div>
      <div ref="jobChart" class="chart"></div>
    </div>
  </div>
</template>

<style scoped>
.charts-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: nowrap; /* 关键：不允许换行 */
  width: 100%;
  margin-top: 100px;
}

.chart {
  width: 48%;   /* 两张图加起来 <100% */
  height: 400px;
}

h2
{
  text-align: center;
}
</style>
