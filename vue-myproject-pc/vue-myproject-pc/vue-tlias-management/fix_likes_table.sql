-- 修复likes表结构，确保不能重复点赞
-- 1. 删除现有的likes表（如果存在）
DROP TABLE IF EXISTS likes;

-- 2. 重新创建likes表，使用复合主键防止重复点赞
CREATE TABLE likes (
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- 3. 添加唯一约束确保一个用户只能对一个帖子点赞一次
-- 注意：复合主键已经确保了这一点，但为了明确性，可以添加注释

-- 4. 如果之前有重复数据，可以先清理（可选）
-- DELETE l1 FROM likes l1
-- INNER JOIN likes l2 
-- WHERE l1.post_id = l2.post_id 
-- AND l1.user_id = l2.user_id 
-- AND l1.create_time > l2.create_time;

-- 5. 验证表结构
DESCRIBE likes;
