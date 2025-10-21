import request from "@/utils/request.js";


//查询员工列表数据
export const queryPageApi=(name,gender,begin,end,page,pageSize)=>
{
    return request.get(`/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
}
export const addApi=(emp)=>
{
  return request.post('/emps',emp);

}
 export const queryInfoApi=(id)=>
{
    return request.get(`/emps/${id}`)
}
export const updateApi=(emp)=>
{
    return request.put('/emps',emp);
}
export const deleteApi=(ids)=>
{
    return request.delete(`/emps?ids=${ids}`)
}