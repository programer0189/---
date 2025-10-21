-- 创建用户表
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `phone` char(11) NOT NULL COMMENT '手机号',
  `role` varchar(10) NOT NULL DEFAULT 'user' COMMENT '角色：admin 或 user',
  `image` varchar(255) DEFAULT NULL COMMENT '头像',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `update_time` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 插入测试数据
INSERT INTO `user` (`username`, `password`, `phone`, `role`, `image`, `create_time`, `update_time`) VALUES
('admin', '123456', '13800138000', 'admin', 'https://example.com/admin.jpg', CURDATE(), CURDATE()),
('zhangsan', '123456', '13800138001', 'user', 'https://example.com/zhangsan.jpg', CURDATE(), CURDATE()),
('lisi', '123456', '13800138002', 'user', 'https://example.com/lisi.jpg', CURDATE(), CURDATE()),
('wangwu', '123456', '13800138003', 'user', 'https://example.com/wangwu.jpg', CURDATE(), CURDATE());
