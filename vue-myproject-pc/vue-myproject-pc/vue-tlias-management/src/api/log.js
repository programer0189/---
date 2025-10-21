import request from '@/utils/request.js'

// 分页查询日志
export const queryLogPage = (page, pageSize) => {
  return request.get(`/logs?page=${page}&pageSize=${pageSize}`)
}
