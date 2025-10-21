-- 更新user表，添加性别字段
ALTER TABLE `user` ADD COLUMN `gender` VARCHAR(3) COMMENT '性别' AFTER `image`;

-- 更新现有用户的性别字段（可选，根据实际需求设置默认值）
-- UPDATE `user` SET `gender` = '男' WHERE `gender` IS NULL;

-- 验证表结构
DESCRIBE `user`;
