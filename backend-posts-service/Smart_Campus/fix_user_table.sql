-- 修复user表，添加gender字段
-- 执行前请先备份数据库

-- 1. 检查表结构
DESCRIBE `user`;

-- 2. 添加gender字段
ALTER TABLE `user` ADD COLUMN `gender` VARCHAR(3) COMMENT '性别' AFTER `image`;

-- 3. 验证字段是否添加成功
DESCRIBE `user`;

-- 4. 可选：为现有用户设置默认性别值
-- UPDATE `user` SET `gender` = '男' WHERE `gender` IS NULL;

-- 5. 查看最终表结构
SHOW CREATE TABLE `user`;

