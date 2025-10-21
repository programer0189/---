# Smart Campus - 智能校园管理系统

## 📝 项目简介
Smart Campus是一个基于Spring Boot的智能校园管理系统，集成了校园信息管理、社交论坛、导航等功能。该项目旨在为校园师生提供便捷的信息化服务平台。

## ✨ 主要功能
- **用户管理**：用户注册、登录、权限管理
- **部门管理**：部门信息的增删改查
- **员工管理**：员工信息的增删改查
- **社交平台**：
  - 表白墙
  - 代取快递
  - 学习交流
  - 活动组织
  - 失物招领
  - 闲聊灌水
- **互动功能**：帖子发布、评论、点赞
- **文件上传**：支持阿里云OSS文件存储
- **日志管理**：系统操作日志记录
- **JWT认证**：安全的token认证机制

## 🛠️ 技术栈
- **后端框架**：Spring Boot 3.4.7
- **ORM框架**：MyBatis 3.5.17
- **数据库**：MySQL 8.0
- **构建工具**：Maven 3.9.4
- **Java版本**：JDK 17
- **安全认证**：JWT (JSON Web Token)
- **分页插件**：PageHelper 5.3.3
- **对象存储**：阿里云OSS 3.17.4
- **其他工具**：Lombok, HikariCP

## 📋 运行要求
- Java 17+
- Maven 3.6+
- MySQL 8.0+

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/programer0189/Smart-Campus.git
cd Smart-Campus
```

### 2. 配置数据库
在MySQL中创建数据库：
```sql
CREATE DATABASE tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tlias
    username: root
    password: your_password
```

### 3. 编译项目
```bash
mvn clean compile
```

### 4. 运行项目
```bash
mvn spring-boot:run
```

或者打包后运行：
```bash
mvn clean package
java -jar target/Smart_Campus-0.0.1-SNAPSHOT.jar
```

### 5. 访问项目
项目启动成功后，访问：`http://localhost:8080`

## 📁 项目结构
```
Smart_Campus/
├── src/
│   ├── main/
│   │   ├── java/com/webproject/
│   │   │   ├── Smart_CampusApplication.java    # 主启动类
│   │   │   ├── controller/                     # 控制器层
│   │   │   ├── service/                        # 服务层
│   │   │   │   └── impl/                       # 服务实现
│   │   │   ├── mapper/                         # 数据访问层
│   │   │   ├── pojo/                           # 实体类
│   │   │   ├── filter/                         # 过滤器
│   │   │   ├── Exception/                      # 异常处理
│   │   │   └── utils/                          # 工具类
│   │   └── resources/
│   │       ├── application.yml                 # 配置文件
│   │       ├── logback.xml                     # 日志配置
│   │       ├── com/webproject/mapper/          # MyBatis映射文件
│   │       └── static/                         # 静态资源
│   └── test/                                   # 测试代码
└── pom.xml                                     # Maven配置文件
```

## 📊 数据库表设计
主要数据表：
- `user` - 用户表
- `dept` - 部门表
- `emp` - 员工表
- `posts` - 帖子表
- `comments` - 评论表
- `likes` - 点赞表
- `sys_log` - 系统日志表

## 🔐 API接口
项目采用RESTful API设计，主要接口包括：
- `/login` - 用户登录
- `/users/**` - 用户管理
- `/depts/**` - 部门管理
- `/emps/**` - 员工管理
- `/posts/**` - 帖子管理
- `/comments/**` - 评论管理
- `/likes/**` - 点赞管理

详细API文档请参考项目中的API文档文件。

## ⚠️ 常见问题

### Maven无法识别项目
1. 检查 `pom.xml` 文件是否正确
2. 确保主类名与 `pom.xml` 中的配置一致
3. 检查项目结构是否符合Maven标准

### 项目无法启动
1. 检查数据库连接配置
2. 确保MySQL服务正在运行
3. 检查端口8080是否被占用
4. 确保Java版本为17+

### 数据库连接失败
1. 检查MySQL服务是否启动
2. 验证数据库用户名和密码
3. 确认数据库已创建
4. 检查MySQL端口（默认3306）

## 📝 开发历史
- **v1.0.0** (2025-08)：项目重命名完成，从 tlias-web-management 更名为 Smart_Campus
- 更新了主类名称为 Smart_CampusApplication
- 更新了Maven配置中的项目信息
- 完善了社交平台功能模块

## 🤝 贡献
欢迎提交Issue和Pull Request！

## 📄 许可证
本项目采用 MIT 许可证

## 👥 联系方式
- GitHub: [@programer0189](https://github.com/programer0189)
- 项目地址: [Smart-Campus](https://github.com/programer0189/Smart-Campus)

## 🙏 致谢
感谢所有为这个项目做出贡献的开发者！

