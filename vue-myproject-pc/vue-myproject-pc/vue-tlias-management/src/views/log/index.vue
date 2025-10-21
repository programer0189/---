<script setup>
import * as echarts from 'echarts'
import { onMounted, ref, nextTick } from 'vue'
import { ElMessage, ElCard } from 'element-plus'
import request from '@/utils/request'

const genderChart = ref(null)
const countChart = ref(null)
const newUserChart = ref(null)

const genderData = ref([])
const countData = ref({})
const newUserData = ref({})

onMounted(async () => {
  await nextTick()

  try {
    // 获取用户性别统计数据
    const genderRes = await request.get('/report/userGenderData')
    if (genderRes.code === 1) {
      genderData.value = genderRes.data
    }

    // 先获取新增用户统计数据（作为备用数据）
    const newUserRes = await request.get('/report/newUserData')
    if (newUserRes.code === 1) {
      newUserData.value = newUserRes.data
    }

    // 获取用户数量统计数据
    try {
      const countRes = await request.get('/report/userCountData')
      if (countRes.code === 1) {
        // 后端返回的字段与前端期望的不完全匹配，需要进行字段映射
        const backendData = countRes.data
        countData.value = {
          todayNewUsers: backendData.todayNewUsers || 0,
          yesterdayNewUsers: backendData.yesterdayNewUsers || 0,
          last7DaysTotal: backendData.last7DaysTotal || 0,
          last30DaysTotal: backendData.last30DaysTotal || 0,
          last90DaysTotal: backendData.last90DaysTotal || 0
        }
      } else {
        // 使用新增用户数据作为备用数据
        if (newUserData.value) {
          countData.value = {
            todayNewUsers: newUserData.value.todayNewUsers || 0,
            yesterdayNewUsers: newUserData.value.yesterdayNewUsers || 0,
            last7DaysTotal: newUserData.value.last7DaysTotal || 0,
            last30DaysTotal: newUserData.value.last30DaysTotal || 0,
            last90DaysTotal: newUserData.value.last90DaysTotal || 0
          }
        }
      }
    } catch (error) {
      // 使用新增用户数据作为备用数据
      if (newUserData.value) {
        countData.value = {
          todayNewUsers: newUserData.value.todayNewUsers || 0,
          yesterdayNewUsers: newUserData.value.yesterdayNewUsers || 0,
          last7DaysTotal: newUserData.value.last7DaysTotal || 0,
          last30DaysTotal: newUserData.value.last30DaysTotal || 0,
          last90DaysTotal: newUserData.value.last90DaysTotal || 0
        }
      }
    }

    // 等待数据设置完成后再初始化图表
    await nextTick()
    
    // 初始化图表
    initCharts()

  } catch (error) {
    // 静默处理错误
  }

  // 响应式调整
  window.addEventListener('resize', () => {
    if (genderChart.value) {
      const chart = echarts.getInstanceByDom(genderChart.value)
      if (chart) chart.resize()
    }
    if (countChart.value) {
      const chart = echarts.getInstanceByDom(countChart.value)
      if (chart) chart.resize()
    }
    if (newUserChart.value) {
      const chart = echarts.getInstanceByDom(newUserChart.value)
      if (chart) chart.resize()
    }
  })
})

// 初始化所有图表
const initCharts = () => {
  initGenderChart()
  initCountChart()
}

// 初始化性别分布饼图
const initGenderChart = () => {
  if (!genderChart.value) return
  
  const chart = echarts.init(genderChart.value)
  
  const option = {
    title: { 
      text: '用户性别分布', 
      left: 'center',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#2c3e50'
      }
    },
    tooltip: { 
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e74c3c',
      borderWidth: 1,
      textStyle: {
        color: '#2c3e50'
      }
    },
    legend: { 
      orient: 'horizontal', 
      bottom: 20,
      textStyle: {
        color: '#64748b',
        fontSize: 12
      }
    },
    series: [
      {
        name: '性别统计',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '45%'],
        data: genderData.value,
        emphasis: {
          itemStyle: {
            shadowBlur: 15,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          }
        },
        label: {
          show: true,
          formatter: '{b}: {c} ({d}%)',
          color: '#2c3e50',
          fontSize: 12
        },
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 3
        }
      }
    ]
  }
  
  chart.setOption(option)
}

// 初始化用户数量统计柱状图
const initCountChart = () => {
  if (!countChart.value) return
  
  const chart = echarts.init(countChart.value)
  
  // 使用数据库实际返回的有效数据字段
  const countLabels = ['今日新增', '昨日新增', '近7天新增', '近30天新增', '近90天新增']
  const countValues = [
    countData.value.todayNewUsers || 0,
    countData.value.yesterdayNewUsers || 0,
    countData.value.last7DaysTotal || 0,
    countData.value.last30DaysTotal || 0,
    countData.value.last90DaysTotal || 0
  ]

  const option = {
    title: { 
      text: '用户数量统计', 
      left: 'center',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#2c3e50'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#3498db',
      borderWidth: 1,
      textStyle: {
        color: '#2c3e50'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: countLabels,
      axisLabel: {
        rotate: 45,
        fontSize: 12,
        color: '#64748b'
      },
      axisLine: {
        lineStyle: {
          color: '#e2e8f0'
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '用户数量',
      nameTextStyle: {
        color: '#64748b',
        fontSize: 12
      },
      axisLabel: {
        color: '#64748b'
      },
      axisLine: {
        lineStyle: {
          color: '#e2e8f0'
        }
      },
      splitLine: {
        lineStyle: {
          color: '#f1f5f9'
        }
      }
    },
    series: [
      {
        name: '用户数量',
        data: countValues,
        type: 'bar',
        barWidth: '50%',
        itemStyle: {
          color: function(params) {
            const colors = [
              new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#3498db' },
                { offset: 1, color: '#2980b9' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#2ecc71' },
                { offset: 1, color: '#27ae60' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#f39c12' },
                { offset: 1, color: '#e67e22' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#e74c3c' },
                { offset: 1, color: '#c0392b' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#9b59b6' },
                { offset: 1, color: '#8e44ad' }
              ])
            ]
            return colors[params.dataIndex]
          },
          borderRadius: [6, 6, 0, 0]
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          }
        },
        label: {
          show: true,
          position: 'top',
          formatter: '{c}',
          color: '#2c3e50',
          fontSize: 12,
          fontWeight: 'bold'
        }
      }
    ]
  }
  
  chart.setOption(option)
}

</script>

<template>
  <div class="user-statistics-container">
    <div class="page-header">
      <h1 class="main-title">用户信息统计</h1>
      <p class="subtitle">全面了解用户数据分布和增长趋势</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon">🆕</div>
          <div class="stat-info">
            <div class="stat-number">{{ countData.todayNewUsers || 0 }}</div>
            <div class="stat-label">今日新增</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon">📅</div>
          <div class="stat-info">
            <div class="stat-number">{{ countData.yesterdayNewUsers || 0 }}</div>
            <div class="stat-label">昨日新增</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon">🗓️</div>
          <div class="stat-info">
            <div class="stat-number">{{ countData.last7DaysTotal || 0 }}</div>
            <div class="stat-label">近7天新增</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon">📊</div>
          <div class="stat-info">
            <div class="stat-number">{{ countData.last30DaysTotal || 0 }}</div>
            <div class="stat-label">近30天新增</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <el-card class="chart-card">
        <div ref="genderChart" class="chart-box"></div>
      </el-card>
      
      <el-card class="chart-card">
        <div ref="countChart" class="chart-box"></div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.user-statistics-container {
  padding: 30px;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.main-title {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin: 0 0 15px 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

.subtitle {
  font-size: 18px;
  color: #64748b;
  margin: 0;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", sans-serif;
}

/* 统计卡片样式 */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  border: none;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  background: white;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.stat-icon {
  font-size: 40px;
  opacity: 0.9;
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

/* 图表区域样式 */
.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 30px;
}

.chart-card {
  border: none;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  background: white;
}

.chart-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.chart-box {
  width: 100%;
  height: 400px;
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .charts-container {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .user-statistics-container {
    padding: 20px;
  }
  
  .stats-cards {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 15px;
  }
  
  .charts-container {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .main-title {
    font-size: 24px;
  }
  
  .subtitle {
    font-size: 16px;
  }
  
  .chart-box {
    height: 350px;
  }
  
  .stat-content {
    padding: 15px;
  }
  
  .stat-icon {
    font-size: 32px;
  }
  
  .stat-number {
    font-size: 20px;
  }
}
</style>
