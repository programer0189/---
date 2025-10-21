# 校园社交平台 (Campus Social Platform)

[![Vue.js](https://img.shields.io/badge/Vue.js-3.2.38-4FC08D?style=flat-square&logo=vue.js)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-2.4.4-409EFF?style=flat-square)](https://element-plus.org/)
[![Vite](https://img.shields.io/badge/Vite-3.0.9-646CFF?style=flat-square&logo=vite)](https://vitejs.dev/)
[![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)](LICENSE)

一个基于 Vue 3 和 Element Plus 构建的移动端校园社交平台，为校园生活提供表白墙、失物招领、快递代取、学习交流等多功能服务。

## ✨ 功能特性

- 🏠 **首页** - 校园风光轮播，各模块最新动态展示
- 💬 **表白墙** - 发布和浏览校园表白信息
- 🎯 **活动发布** - 校园活动信息发布与参与
- 📦 **快递代取** - 发布和接单校园快递代取服务
- 📚 **学习交流** - 学习资源分享，学习小组组建
- 🔍 **失物招领** - 发布丢失物品和拾到物品信息
- 💭 **交流墙** - 校园生活话题讨论与交流
- 👤 **个人中心** - 用户信息管理，个人帖子管理
- 🗺️ **地图定位** - 集成百度地图，支持位置标记
- 🔐 **用户认证** - 完整的登录注册系统

## 🛠️ 技术栈

- **前端框架**: Vue 3.2.38
- **构建工具**: Vite 3.0.9
- **UI 组件库**: Element Plus 2.4.4
- **路由管理**: Vue Router 4.1.5
- **HTTP 客户端**: Axios 1.7.2
- **地图服务**: Vue Baidu Map 3x 1.0.40
- **代码规范**: ESLint + Prettier

## 📦 项目结构

```
vue-position-management/
├── src/
│   ├── api/              # API 接口定义
│   │   ├── chat.js       # 交流墙相关接口
│   │   ├── login.js      # 登录相关接口
│   │   ├── map.js        # 地图相关接口
│   │   └── ...
│   ├── assets/           # 静态资源
│   ├── components/       # 公共组件
│   ├── router/           # 路由配置
│   │   └── index.js
│   ├── utils/            # 工具函数
│   │   ├── auth.js       # 认证工具
│   │   ├── request.js    # 请求封装
│   │   └── ...
│   ├── views/            # 页面视图
│   │   ├── home/         # 首页
│   │   ├── confession/   # 表白墙
│   │   ├── activity/     # 活动发布
│   │   ├── express/      # 快递代取
│   │   ├── study/        # 学习交流
│   │   ├── lost/         # 失物招领
│   │   ├── chat/         # 交流墙
│   │   ├── userInfo/     # 个人中心
│   │   ├── login/        # 登录注册
│   │   └── layout/       # 布局组件
│   ├── App.vue           # 根组件
│   └── main.js           # 入口文件
├── public/               # 公共资源
├── dist/                 # 打包输出目录
├── vite.config.js        # Vite 配置
├── package.json          # 项目依赖
└── README.md             # 项目说明
```

## 🚀 快速开始

### 环境要求

- Node.js >= 14.0.0
- npm >= 6.0.0 或 yarn >= 1.22.0

### 安装依赖

```bash
# 进入项目目录
cd vue-position-management

# 安装依赖
npm install
```

### 开发环境运行

```bash
npm run dev
```

项目将运行在 `http://localhost:5173`

### 生产环境构建

```bash
npm run build
```

构建产物将输出到 `dist` 目录

### 预览生产构建

```bash
npm run preview
```

## 🔧 配置说明

### API 代理配置

在 `vite.config.js` 中配置后端 API 代理：

```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://your-backend-server:port',
      secure: false,
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, ''),
    }
  }
}
```

### 百度地图配置

需要在百度地图开放平台申请 AK，并在相关组件中配置。

## 📱 主要页面

### 首页
- 校园风光轮播展示
- 各功能模块最新动态
- 快速导航入口

### 表白墙
- 发布匿名/实名表白
- 浏览所有表白信息
- 点赞和评论功能

### 失物招领
- 发布丢失物品信息
- 发布拾到物品信息
- 地图定位功能

### 快递代取
- 发布代取需求
- 接单代取服务
- 位置信息展示

### 学习交流
- 学习资源分享
- 学习小组组建
- 学习经验交流

## 🔐 用户认证

项目实现了完整的用户认证系统：

- 用户注册和登录
- Token 认证机制
- 路由守卫保护
- 自动登录状态检查

## 📝 开发规范

### 代码检查

```bash
npm run lint
```

### 提交规范

建议使用语义化提交信息：

- `feat`: 新功能
- `fix`: 修复问题
- `docs`: 文档修改
- `style`: 代码格式修改
- `refactor`: 代码重构
- `test`: 测试用例修改
- `chore`: 其他修改

## 🤝 贡献指南

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👨‍💻 作者

**programer0189**

- Email: lzimu0189@gmail.com
- GitHub: [@programer0189](https://github.com/programer0189)

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

## 📞 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 📧 Email: lzimu0189@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/programer0189/vue-project3-mobile/issues)

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！

