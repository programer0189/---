import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import zhCn from 'element-plus/es/locale/lang/zh-cn';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import BaiduMap from 'vue-baidu-map-3x';
import './assets/main.css';

const app = createApp(App);

app.use(router);
app.use(ElementPlus, { locale: zhCn });
app.use(BaiduMap, {
  ak: 'UBQYpEDwgMq5YzD4l5KcrjVBQvfTQyDg',
  v: '3.0',
  type: 'API',
});

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.mount('#app');