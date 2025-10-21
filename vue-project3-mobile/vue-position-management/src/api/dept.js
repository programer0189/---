import request from "@/utils/request.js";


//查询部门信息
export const queryAll=()=>
{
  return request.get('/depts');

}

//新增
export const addApi=(dept)=>
{
  return request.post('/depts',dept);

}

//根据id查询函数
export const queryAllById=(id)=>
{
  return request.get(`/depts/${id}`);

}
export const updateApi=(dept)=>
{
  return request.put('/depts',dept);

}

export const  deleteApi=(id)=>
{
  return request.delete(`/depts?id=${id}`);

}

