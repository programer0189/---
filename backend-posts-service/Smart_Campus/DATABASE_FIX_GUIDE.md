# 数据库修复指南 - 添加gender字段

## 问题描述
用户注册时出现数据库错误：
```
Unknown column 'gender' in 'field list'
```

## 问题原因
数据库 `user` 表中缺少 `gender` 字段，但代码中已经使用了这个字段。

## 解决方案

### 方法1: 使用SQL脚本（推荐）

#### 步骤1: 连接到数据库
```bash
mysql -u root -p
```

#### 步骤2: 选择数据库
```sql
USE tlias;
```

#### 步骤3: 检查当前表结构
```sql
DESCRIBE `user`;
```

#### 步骤4: 添加gender字段
```sql
ALTER TABLE `user` ADD COLUMN `gender` VARCHAR(3) COMMENT '性别' AFTER `image`;
```

#### 步骤5: 验证字段添加成功
```sql
DESCRIBE `user`;
```

### 方法2: 使用MySQL Workbench等图形化工具

1. 连接到数据库
2. 选择 `tlias` 数据库
3. 右键点击 `user` 表
4. 选择 "Alter Table"
5. 添加新列：
   - 列名：`gender`
   - 数据类型：`VARCHAR(3)`
   - 注释：`性别`
   - 位置：在 `image` 列之后
6. 应用更改

### 方法3: 使用Navicat等工具

1. 连接到数据库
2. 展开 `tlias` 数据库
3. 右键点击 `user` 表
4. 选择 "设计表"
5. 添加新字段：
   - 字段名：`gender`
   - 类型：`varchar(3)`
   - 注释：`性别`
6. 保存更改

## 完整的SQL脚本

```sql
-- 修复user表，添加gender字段
USE tlias;

-- 检查当前表结构
DESCRIBE `user`;

-- 添加gender字段
ALTER TABLE `user` ADD COLUMN `gender` VARCHAR(3) COMMENT '性别' AFTER `image`;

-- 验证字段是否添加成功
DESCRIBE `user`;

-- 查看最终表结构
SHOW CREATE TABLE `user`;
```

## 预期结果

### 执行前的表结构
```
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int          | NO   | PRI | NULL    | auto_increment |
| username    | varchar(20)  | NO   | UNI | NULL    |                |
| password    | varchar(64)  | NO   |     | NULL    |                |
| phone       | char(11)     | NO   | UNI | NULL    |                |
| role        | varchar(10)  | NO   |     | user    |                |
| image       | varchar(255) | YES  |     | NULL    |                |
| create_time | date         | YES  |     | NULL    |                |
| update_time | date         | YES  |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
```

### 执行后的表结构
```
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int          | NO   | PRI | NULL    | auto_increment |
| username    | varchar(20)  | NO   | UNI | NULL    |                |
| password    | varchar(64)  | NO   |     | NULL    |                |
| phone       | char(11)     | NO   | UNI | NULL    |                |
| role        | varchar(10)  | NO   |     | user    |                |
| image       | varchar(255) | YES  |     | NULL    |                |
| gender      | varchar(3)   | YES  |     | NULL    |                |
| create_time | date         | YES  |     | NULL    |                |
| update_time | date         | YES  |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
```

## 注意事项

### 1. 备份数据
执行ALTER TABLE前，建议先备份数据库：
```bash
mysqldump -u root -p tlias > tlias_backup_$(date +%Y%m%d_%H%M%S).sql
```

### 2. 字段类型选择
- `VARCHAR(3)` 适合存储"男"、"女"、"其他"等性别值
- 如果需要更长的性别描述，可以改为 `VARCHAR(10)`

### 3. 默认值设置
可以为现有用户设置默认性别：
```sql
UPDATE `user` SET `gender` = '男' WHERE `gender` IS NULL;
```

### 4. 约束设置
如果需要限制性别值，可以添加CHECK约束：
```sql
ALTER TABLE `user` ADD CONSTRAINT chk_gender CHECK (gender IN ('男', '女', '其他'));
```

## 验证修复

### 1. 重启应用
确保应用重新加载数据库连接

### 2. 测试用户注册
尝试注册新用户，包含性别字段

### 3. 检查日志
确认没有数据库错误

### 4. 查询验证
```sql
SELECT username, gender FROM `user` WHERE gender IS NOT NULL;
```

## 常见问题

### Q1: 执行ALTER TABLE失败
**可能原因**：
- 权限不足
- 表被锁定
- 语法错误

**解决方案**：
```sql
-- 检查权限
SHOW GRANTS FOR CURRENT_USER();

-- 检查表状态
SHOW TABLE STATUS LIKE 'user';
```

### Q2: 字段已存在
**错误信息**：
```
Duplicate column name 'gender'
```

**解决方案**：
```sql
-- 检查字段是否已存在
SHOW COLUMNS FROM `user` LIKE 'gender';

-- 如果存在，可以删除后重新添加
ALTER TABLE `user` DROP COLUMN `gender`;
ALTER TABLE `user` ADD COLUMN `gender` VARCHAR(3) COMMENT '性别' AFTER `image`;
```

### Q3: 应用仍然报错
**可能原因**：
- 应用未重启
- 数据库连接池缓存
- 代码中的字段映射问题

**解决方案**：
1. 重启Spring Boot应用
2. 检查MyBatis映射文件
3. 验证实体类字段名

## 总结

按照以上步骤操作，应该能够成功添加 `gender` 字段并解决用户注册问题。关键步骤：

1. ✅ 连接到数据库
2. ✅ 执行ALTER TABLE语句
3. ✅ 验证字段添加成功
4. ✅ 重启应用
5. ✅ 测试功能

如果还有问题，请检查数据库连接和权限设置。

