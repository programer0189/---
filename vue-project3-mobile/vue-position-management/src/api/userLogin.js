import request from '@/utils/request'
export const UserloginApi=(data)=>
{
    return request.post('/user/login',data)
}