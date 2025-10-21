<template>
  <div style="display: flex; width: 1100px; height: 600px;">
    <!-- 地图区域 -->
    <div style="width: 800px; height: 600px;">
      <baidu-map
        class="map"
        :center="center"
        :zoom="zoom"
        ak="UBQYpEDwgMq5YzD4l5KcrjVBQvfTQyDg"
        v="3.0"
        type="API"
        :scroll-wheel-zoom="true"
        @ready="onMapReady"
      />
    </div>

    <!-- 路线规划结果面板 -->
    <div id="panel" v-show="panelVisible"></div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';

const center = ref({ lng: 108.738822, lat: 34.367593 });
const zoom = ref(18);

const startPoint = ref(null);
const endPoint = ref(null);
const panelVisible = ref(false);

let walkingRoute = null;
let BMapInstance = null;
let mapInstance = null;

// 判断两个点是否相同（经纬度一致）
const isSamePoint = (p1, p2) => 
{
  return p1 && p2 && p1.lng === p2.lng && p1.lat === p2.lat;
};

const onMapReady = ({ BMap, map }) => {
  BMapInstance = BMap;
  mapInstance = map;

  map.disableScrollWheelZoom(); // 禁用滚轮缩放

  map.addEventListener('click', (e) => {
    const point = e.point;
    console.log('点击地图坐标：', point.lng, point.lat);

    // 清除旧路径与标记
    if (walkingRoute) {
      walkingRoute.clearResults();
    }
    map.clearOverlays();

    // 新一轮导航，重置状态
    if (startPoint.value && endPoint.value) {
      startPoint.value = null;
      endPoint.value = null;
    }

    // 设置起点或终点
    if (!startPoint.value) 
    {
      startPoint.value = point;
      const marker = new BMap.Marker(point);
      marker.setLabel(new BMap.Label("起点", { offset: new BMap.Size(20, -10) }));
      map.addOverlay(marker);
    } else if (!endPoint.value) {
      // 判断是否与起点重复
      if (isSamePoint(startPoint.value, point)) 
      {
        ElMessage.error('起点与终点不能相同');
        return;
      }

      endPoint.value = point;
      const marker = new BMap.Marker(point);
      marker.setLabel(new BMap.Label("终点", { offset: new BMap.Size(20, -10) }));
      map.addOverlay(marker);

      // 初始化路径规划
      walkingRoute = new BMap.WalkingRoute(map, {
        renderOptions: {
          map: map,
          panel: "panel",
          autoViewport: true
        }
      });

      // 开始搜索路径
      walkingRoute.search(startPoint.value, endPoint.value);

      walkingRoute.setSearchCompleteCallback(() => {
        if (walkingRoute.getStatus() === 0) {
          ElMessage.success("规划成功");
          panelVisible.value = true; // 显示面板
        } else {
          ElMessage.error('规划失败，状态码：' + walkingRoute.getStatus());
        }
      });
    }
  });

  // 右键清除地图与结果面板
  map.addEventListener('rightclick', () => {
    map.clearOverlays();
    map.reset();
    const panel = document.getElementById('panel');
    if (panel) panel.innerHTML = '';
    panelVisible.value = false; // 隐藏面板
  });
};
</script>

<style scoped>
.map {
  width: 800px;
  height: 600px;

}

#panel {
  width: 300px;
  max-height: 200px;
  padding: 10px;
  overflow: auto;
  background: #f5f5f5;
  font-size: 14px;
  border-left: 1px solid #ccc;
}

</style>
