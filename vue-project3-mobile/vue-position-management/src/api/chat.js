import request from '@/utils/request'

// 帖子相关API
export const postAPI = {
  // 根据类型获取帖子列表
  getPostsByType: (type) => {
    return request.get(`/posts/type/${type}`)
  },
  
  // 根据ID获取帖子
  getPostById: (postId) => {
    return request.get(`/posts/${postId}`)
  },
  
  // 添加帖子
  addPost: (post) => {
    return request.post('/posts', post)
  },
  
  // 更新帖子
  updatePost: (postId, post) => {
    return request.put(`/posts/${postId}`, post)
  },
  
  // 删除帖子
  deletePost: (postId, userId) => {
    return request.delete(`/posts/${postId}?userId=${userId}`)
  },
  
  // 获取用户帖子数量
  getUserPostCount: (userId) => {
    return request.get(`/posts/user/${userId}/count`)
  },
  
  // 获取类型帖子数量
  getTypePostCount: (type) => {
    return request.get(`/posts/type/${type}/count`)
  },
  
  // 获取用户所有帖子
  getUserPosts: (userId) => {
    return request.get(`/posts/user/${userId}`)
  }
}

// 评论相关API
export const commentAPI = {
  // 根据帖子ID获取评论
  getCommentsByPostId: (postId) => {
    return request.get(`/comments?postId=${postId}`)
  },
  
  // 添加评论
  addComment: (comment) => {
    return request.post('/comments', comment)
  },
  
  // 更新评论
  updateComment: (commentId, comment) => {
    return request.put(`/comments/${commentId}`, comment)
  },
  
  // 删除评论
  deleteComment: (commentId, userId) => {
    return request.delete(`/comments/${commentId}?userId=${userId}`)
  },
  
  // 获取用户评论数量
  getUserCommentCount: (userId) => {
    return request.get(`/comments/user/${userId}/count`)
  },
  
  // 获取帖子评论数量
  getPostCommentCount: (postId) => {
    return request.get(`/comments/post/${postId}/count`)
  }
}

// 点赞相关API
export const likeAPI = {
  // 点赞帖子
  likePost: (postId, userId) => {
    return request.post('/likes', { postId, userId })
  },
  
  // 取消点赞
  unlikePost: (postId, userId) => {
    return request.delete(`/likes/${postId}?userId=${userId}`)
  },
  
  // 检查用户是否点赞
  checkUserLike: (postId, userId) => {
    return request.get(`/likes/check?postId=${postId}&userId=${userId}`)
  },
  
  // 获取帖子点赞数量
  getPostLikeCount: (postId) => {
    return request.get(`/likes/post/${postId}/count`)
  },
  
  // 获取用户发出的点赞总数（用户给其他帖子的点赞数）
  getUserLikeCount: (userId) => {
    return request.get(`/likes/user/${userId}/count`)
  },
  
  // 获取用户获得的点赞总数（用户发布的帖子被点赞的总数）
  getUserReceivedLikeCount: (userId) => {
    return request.get(`/likes/user/${userId}/received-count`)
  }
}

// 用户相关API
export const userAPI = {
  // 根据用户ID获取用户信息
  getUserInfo: (userId) => {
    return request.get(`/users/${userId}`)
  },
  
  // 更新用户信息
  updateUserInfo: (userId, userData) => {
    return request.put(`/users/${userId}`, userData)
  },
  
  // 根据用户名查询用户
  getUserByUsername: (username) => {
    return request.get(`/users/username/${username}`)
  },
  
  // 根据手机号查询用户
  getUserByPhone: (phone) => {
    return request.get(`/users/phone/${phone}`)
  },
  
  // 获取用户帖子统计信息
  getUserPostStats: (userId) => {
    return request.get(`/posts/user/${userId}/stats`)
  },
  
  // 删除用户账号
  deleteUser: (userId) => {
    return request.delete(`/users/${userId}`)
  }
}

