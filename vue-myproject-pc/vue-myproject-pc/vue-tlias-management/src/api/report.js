
import request from "@/utils/request.js"

// 获取性别统计数据
export function getEmpGenderData() {
  return request.get("/report/empGenderData")
}

// 获取岗位统计数据
export function getEmpJobData() {
  return request.get("/report/empJobData")
}

// 获取帖子统计数据
export function getPostData() {
  return request.get("/report/postData")
}

// 获取用户性别统计数据
export function getUserGenderData() {
  return request.get("/report/userGenderData")
}

// 获取用户数量统计数据
export function getUserCountData() {
  return request.get("/report/userCountData")
}
