import request from '@/utils/request.js'

export function getTopClickCount() {
  return request.get('/report/locationClick')
}
