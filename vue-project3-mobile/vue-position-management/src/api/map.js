import request from "@/utils/maprequest.js";

// 查询地图分页数据
export const queryMapPageApi = (name, longitude, latitude, begin, end, page, pageSize) => {
  return request.get(`/maps?name=${name}&longitude=${longitude}&latitude=${latitude}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`);
};

// 添加地图信息
export const addMapApi = (map) => {
  return request.post('/maps', map);
};

// 根据 ID 查询地图信息
export const queryMapInfoApi = (id) => {
  return request.get(`/maps/${id}`);
};

// 更新地图信息
export const updateMapApi = (map) => {
  return request.put('/maps', map);
};

// 删除地图信息（支持多个）
export const deleteMapApi = (ids) => {
  return request.delete(`/maps?ids=${ids}`);
};
