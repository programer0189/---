import request8081 from '@/utils/request8081'

// 分页查询所有用户的评论
export const getAllCommentsWithPageApi = (page, pageSize) => {
  return request8081.get(`/comments/admin/all/page?page=${page}&pageSize=${pageSize}`)
}

// 获取所有评论数量
export const getAllCommentsCountApi = () => {
  return request8081.get('/comments/admin/count')
}

// 删除评论
export const deleteCommentApi = (commentId, userId) => {
  return request8081.delete(`/comments/${commentId}?userId=${userId}`)
}

// 更新评论
export const updateCommentApi = (commentId, comment) => {
  return request8081.put(`/comments/${commentId}`, comment)
}

// 获取帖子评论数量
export const getPostCommentCountApi = (postId) => {
  return request8081.get(`/comments/post/${postId}/count`)
}

// 获取用户评论数量
export const getUserCommentCountApi = (userId) => {
  return request8081.get(`/comments/user/${userId}/count`)
}
