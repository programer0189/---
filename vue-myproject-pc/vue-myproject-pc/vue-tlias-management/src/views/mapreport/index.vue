
<script setup>
import * as echarts from 'echarts';
import { ref, onMounted, nextTick } from 'vue';
import { ElMessage, ElCard, ElButton, ElSelect, ElOption } from 'element-plus';
import { getTopClickCount } from '@/api/clickCount.js';

const clickChart = ref(null);
const pieChart = ref(null);
const lineChart = ref(null);
const clickNames = ref([]);
const clickValues = ref([]);
const chartType = ref('bar'); // 图表类型选择

onMounted(async () => {
  await nextTick(); // 确保 DOM 渲染完成

  // 获取数据
  const res = await getTopClickCount();
  if (res.code !== 1) {
    ElMessage.error(res.msg);
    return;
  }

  // 封装数据
  clickNames.value = res.data.map(item => item.locationName);
  clickValues.value = res.data.map(item => item.clickCount);

  console.log('clickNames:', clickNames.value);
  console.log('clickValues:', clickValues.value);

  // 初始化所有图表
  initCharts();

  // 添加 resize 监听
  window.addEventListener('resize', () => {
    if (clickChart.value) {
      const chart = echarts.getInstanceByDom(clickChart.value);
      if (chart) chart.resize();
    }
    if (pieChart.value) {
      const chart = echarts.getInstanceByDom(pieChart.value);
      if (chart) chart.resize();
    }
    if (lineChart.value) {
      const chart = echarts.getInstanceByDom(lineChart.value);
      if (chart) chart.resize();
    }
  });
});

// 初始化所有图表
const initCharts = () => {
  initBarChart();
  initPieChart();
  initLineChart();
};

// 初始化柱状图
const initBarChart = () => {
  if (!clickChart.value) return;
  
  const chart = echarts.init(clickChart.value);
  
  const option = {
    title: { 
      text: '地点点击量统计', 
      left: 'center',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#2c3e50'
      }
    },
    tooltip: { 
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderColor: '#3b82f6',
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
      data: clickNames.value,
      axisLabel: {
        rotate: 45,
        interval: 0,
        color: '#64748b',
        fontSize: 12
      },
      axisLine: {
        lineStyle: {
          color: '#e2e8f0'
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '点击量',
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
        data: clickValues.value,
        type: 'bar',
        barWidth: '40%',
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#3b82f6' },
            { offset: 1, color: '#1d4ed8' }
          ]),
          borderRadius: [4, 4, 0, 0]
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#60a5fa' },
              { offset: 1, color: '#3b82f6' }
            ])
          }
        }
      }
    ]
  };
  
  chart.setOption(option);
};

// 初始化饼图
const initPieChart = () => {
  if (!pieChart.value) return;
  
  const chart = echarts.init(pieChart.value);
  
  const option = {
    title: { 
      text: '点击量占比分布', 
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#2c3e50'
      }
    },
    tooltip: { 
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      textStyle: {
        color: '#64748b'
      }
    },
    series: [
      {
        name: '点击量',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['60%', '50%'],
        data: clickNames.value.map((name, index) => ({
          name: name,
          value: clickValues.value[index]
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2
        }
      }
    ]
  };
  
  chart.setOption(option);
};


</script>

<template>
  <div class="click-count-container">
    <div class="page-header">
      <h1 class="main-title">地点点击量统计</h1>
      <p class="subtitle">分析校园各地点的人气热度</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon">📊</div>
          <div class="stat-info">
            <div class="stat-number">{{ clickValues.reduce((a, b) => a + b, 0) }}</div>
            <div class="stat-label">总点击量</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon">🏆</div>
          <div class="stat-info">
            <div class="stat-number">{{ clickNames[0] || '暂无' }}</div>
            <div class="stat-label">最热门地点</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon">📈</div>
          <div class="stat-info">
            <div class="stat-number">{{ Math.max(...clickValues) || 0 }}</div>
            <div class="stat-label">最高点击量</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <!-- 柱状图 -->
      <el-card class="chart-card">
        <div ref="clickChart" class="chart-box"></div>
      </el-card>
      
      <!-- 饼图 -->
      <el-card class="chart-card">
        <div ref="pieChart" class="chart-box"></div>
      </el-card>
      
 
    </div>
  </div>
</template>

<style scoped>
.click-count-container {
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
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  border: none;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 10px;
}

.stat-icon {
  font-size: 48px;
  opacity: 0.8;
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
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
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
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
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.chart-box {
  width: 100%;
  height: 350px;
  padding: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .click-count-container {
    padding: 20px;
  }
  
  .stats-cards {
    grid-template-columns: 1fr;
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
    height: 300px;
  }
}
</style>
