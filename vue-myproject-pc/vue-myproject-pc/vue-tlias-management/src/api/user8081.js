import request8081 from '@/utils/request8081'

// 分页查询所有用户
export const getAllUsersWithPageApi = (page, pageSize) => {
  return request8081.get(`/users/page?page=${page}&pageSize=${pageSize}`)
}

// 获取用户统计信息
export const getUserStatsApi = () => {
  return request8081.get('/users')
}

// 更新用户信息
export const updateUserApi = (userId, userData) => {
  return request8081.put(`/users/admin/${userId}`, userData)
}

// 删除用户
export const deleteUserApi = (userId, adminId, reason) => {
  return request8081.delete(`/users/admin/${userId}`, {
    data: { adminId, reason }
  })
}

// 获取用户详情
export const getUserDetailApi = (userId) => {
  return request8081.get(`/users/admin/${userId}`)
}

// 测试接口
export const testUserConnectionApi = () => {
  return request8081.get('/users/test')
}
