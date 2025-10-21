import request from '@/utils/request'

export const addClickApi = (data) => {
  return request.post('/click', data)
}
