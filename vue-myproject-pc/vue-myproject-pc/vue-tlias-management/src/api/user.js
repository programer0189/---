import request from '@/utils/request'

 export const userInfoAPI={
    getUserInfo:(userId)=>{
      return request.get(`/users/${userId}`)
    },
    
  
 }