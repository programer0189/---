Smart Campus - 校智通

 项目简介
校智通是一个完整的智能校园管理系统，包含前后端分离架构，提供PC端和移动端访问，集成了校园信息管理、社交论坛、导航、帖子管理等功能。


```
Smart_Campus/
├── README.md                          # 项目总说明文档
├── application.yml.example            # 配置文件示例
│
├── src/                               # 主后端服务（端口：9090）
│   ├── main/
│   │   ├── java/com/webproject/
│   │   │   ├── Smart_CampusApplication.java    # 主启动类
│   │   │   ├── controller/             # 控制器层
│   │   │   ├── service/                # 服务层
│   │   │   ├── mapper/                 # 数据访问层
│   │   │   ├── pojo/                   # 实体类
│   │   │   ├── filter/                 # 过滤器
│   │   │   ├── Exception/              # 异常处理
│   │   │   └── utils/                  # 工具类
│   │   └── resources/
│   │       ├── application.yml         # 配置文件
│   │       ├── mapper/                 # MyBatis映射文件
│   │       └── logback.xml             # 日志配置
│   └── test/                           # 测试代码
│
├── backend-posts-service/             # 帖子管理服务（端口：8082）
│   └── Smart_Campus/
│       ├── src/                        # 源代码
│       ├── pom.xml                     # Maven配置
│       └── *.md                        # 各种API文档
│
├── vue-myproject-pc/                  # PC端前端项目（端口：98）
│   └── vue-myproject-pc/
│       └── vue-tlias-management/
│           ├── src/                    # 源代码
│           │   ├── views/              # 管理后台页面
│           │   ├── userviews/          # 用户前台页面
│           │   │   ├── home/           # 首页
│           │   │   ├── chat/           # 社交论坛
│           │   │   ├── navigate/       # 校园导航
│           │   │   ├── news/           # 新闻公告
│           │   │   └── mapmessage/     # 地图信息
│           │   ├── news_vue/           # 新闻分类页面
│           │   ├── api/                # API接口
│           │   ├── router/             # 路由配置
│           │   ├── utils/              # 工具函数
│           │   └── assets/             # 静态资源
│           ├── public/                 # 公共资源
│           ├── package.json            # 项目依赖
│           └── vite.config.js          # Vite配置
│
└── vue-project3-mobile/               # 移动端前端项目（端口：99）
    └── vue-position-management/
        ├── src/                        # 源代码
        │   ├── views/                  # 页面组件
        │   │   ├── home/               # 首页
        │   │   ├── login/              # 登录
        │   │   ├── layout/             # 布局
        │   │   ├── userInfo/           # 用户信息
        │   │   ├── userPosts/          # 用户帖子
        │   │   ├── confession/         # 表白墙
        │   │   ├── express/            # 代取快递
        │   │   ├── study/              # 学习交流
        │   │   ├── activity/           # 活动组织
        │   │   ├── lost/               # 失物招领
        │   │   └── chat/               # 闲聊灌水
        │   ├── api/                    # API接口
        │   ├── router/                 # 路由配置
        │   ├── utils/                  # 工具函数
        │   └── assets/                 # 静态资源
        ├── public/                     # 公共资源
        ├── package.json                # 项目依赖
        └── vite.config.js              # Vite配置
```

## ✨ 主要功能

### 管理后台（PC端）
- **用户管理**：用户注册、登录、权限管理
- **部门管理**：部门信息的增删改查
- **员工管理**：员工信息的增删改查
- **帖子管理**：社交平台帖子的审核与管理
- **评论管理**：评论内容的审核与管理
- **数据统计**：各类数据报表和图表展示
- **日志管理**：系统操作日志记录与查询
- **文件上传**：支持阿里云OSS文件存储

### 用户前台（PC端 + 移动端）
- **首页展示**：校园新闻、公告、活动展示
- **校园导航**：集成百度地图API，校园地点导航
- **新闻公告**：
  - 通知公告（考试、假期、网络、安全）
  - 校园生活（食堂、图书馆、社团）
  - 学术活动（讲座、科研）
  - 活动赛事（竞赛、报名）
- **社交论坛**：
  - 表白墙
  - 代取快递
  - 学习交流
  - 活动组织
  - 失物招领
  - 闲聊灌水
- **互动功能**：帖子发布、评论、点赞
- **个人中心**：用户信息管理、我的帖子

## 🛠️ 技术栈

### 后端技术
- **框架**：Spring Boot 3.4.7
- **ORM**：MyBatis 3.5.17
- **数据库**：MySQL 8.0
- **构建工具**：Maven 3.9.4
- **Java版本**：JDK 17
- **安全认证**：JWT (JSON Web Token)
- **分页插件**：PageHelper 5.3.3
- **对象存储**：阿里云OSS 3.17.4
- **其他工具**：Lombok, HikariCP

### 前端技术
- **框架**：Vue.js 3.x
- **构建工具**：Vite
- **UI组件库**：Element Plus
- **状态管理**：Pinia / Vuex
- **路由**：Vue Router
- **HTTP客户端**：Axios
- **地图服务**：百度地图API

## 📋 运行要求
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+
- npm 或 yarn

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/programer0189/Smart-Campus.git
cd Smart_Campus
```

### 2. 配置数据库
在MySQL中创建数据库：
```sql
CREATE DATABASE tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改后端配置文件（复制 `application.yml.example` 为 `application.yml`）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tlias
    username: root
    password: your_password_here  # 修改为你的MySQL密码

aliyun:
  oss:
    access-key-id: your_access_key_id_here  # 修改为你的阿里云AccessKey
    access-key-secret: your_access_key_secret_here
    bucket-name: your_bucket_name_here
```

### 3. 启动后端服务

#### 主后端服务（端口：9090）
```bash
cd Smart_Campus
mvn clean install
mvn spring-boot:run
```

#### 帖子管理服务（端口：8082）
```bash
cd backend-posts-service/Smart_Campus
mvn clean install
mvn spring-boot:run
```

### 4. 启动前端项目

PC端前端（端口：98/自动）
```bash
cd vue-myproject-pc/vue-myproject-pc/vue-tlias-management
npm install
npm run dev
```

 移动端前端（端口：99/自动）
```bash
cd vue-project3-mobile/vue-position-management
npm install
npm run dev
```

 访问地址

开发环境：
- PC端前台：`http://localhost:5173`（或Vite分配的端口）
- 移动端：`http://localhost:5174`（或Vite分配的端口）
- 主后端API：`http://localhost:9090`
- 帖子服务API：`http://localhost:8082`

生产环境（Nginx部署）：
- PC端：`http://your-server-ip:98`
- 移动端：`http://your-server-ip:99`


本项目中的敏感配置信息（如数据库密码、阿里云密钥等）已从代码中移除
