import request8081 from '@/utils/request8081'

// 分页查询所有帖子
export const getAllPostsWithPageApi = (page, pageSize) => {
  return request8081.get(`/posts/admin/all/page?page=${page}&pageSize=${pageSize}`)
}

// 获取所有帖子数量
export const getAllPostsCountApi = () => {
  return request8081.get('/posts/admin/statistics')
}

// 通过帖子审核
export const approvePostApi = (postId, adminId) => {
  return request8081.put(`/posts/admin/${postId}/approve?adminId=${adminId}`)
}

// 拒绝帖子审核
export const rejectPostApi = (postId, adminId, reason) => {
  return request8081.put(`/posts/admin/${postId}/reject?adminId=${adminId}&reason=${reason || ''}`)
}

// 删除帖子
export const deletePostApi = (postId, adminId, reason) => {
  return request8081.delete(`/posts/admin/${postId}?adminId=${adminId}&reason=${reason || ''}`)
}

// 获取帖子详情
export const getPostDetailApi = (postId) => {
  return request8081.get(`/posts/${postId}`)
}

// 获取待审核帖子
export const getPendingPostsApi = (page, pageSize) => {
  return request8081.get(`/posts/admin/pending/page?page=${page}&pageSize=${pageSize}`)
}

// 获取待审核帖子数量
export const getPendingPostsCountApi = () => {
  return request8081.get('/posts/admin/pending')
}

// 测试接口
export const testConnectionApi = () => {
  return request8081.get('/posts/test')
}
