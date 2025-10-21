import request from '@/utils/request'

// 获取待审核的帖子（管理员使用）
export const getPendingchatApi = () => {
    return request.get('/posts/admin/pending')
}

// 审核功能 - 通过帖子
export const approveChatsApi = (postId, adminId) => {
    return request.put(`/posts/admin/${postId}/approve?adminId=${adminId}`)
}

// 审核功能 - 拒绝帖子
export const rejectChatsApi = (postId, adminId, reason) => {
    return request.put(`/posts/admin/${postId}/delete?adminId=${adminId}&reason=${encodeURIComponent(reason)}`)
}

// 审核功能 - 删除帖子（逻辑删除，设置状态为删除）
export const deletePostByAdminApi = (postId, adminId, reason = null) => {
    const url = reason 
        ? `/posts/admin/${postId}?adminId=${adminId}&reason=${encodeURIComponent(reason)}`
        : `/posts/admin/${postId}?adminId=${adminId}`
    return request.delete(url)
}

// 获取各状态帖子统计（管理员使用）
export const getPostStatisticsApi = () => {
    return request.get('/posts/admin/statistics')
}

// 获取待审核帖子分页数据
export const getPendingPostsWithPageApi = (page = 1, pageSize = 10) => {
    return request.get(`/posts/admin/pending/page?page=${page}&pageSize=${pageSize}`)
}

// 获取所有帖子（管理员使用）
export const getAllPostsApi = (page = 1, pageSize = 10, type = null, status = null, keyword = null) => {
    let url = `/posts/admin/all?page=${page}&pageSize=${pageSize}`
    if (type) url += `&type=${type}`
    if (status) url += `&status=${status}`
    if (keyword) url += `&keyword=${encodeURIComponent(keyword)}`
    return request.get(url)
}

// 获取所有帖子（不分页，用于导出等）
export const getAllPostsWithoutPageApi = (type = null, status = null) => {
    let url = '/posts/admin/all/list'
    if (type) url += `?type=${type}`
    if (status) url += `${type ? '&' : '?'}status=${status}`
    return request.get(url)
}

// 更新帖子（管理员使用）
export const updatePostByAdminApi = (postId, postData) => {
    return request.put(`/posts/admin/${postId}`, postData)
}
