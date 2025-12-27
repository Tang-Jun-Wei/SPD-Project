/**
 * SPD - 耗材管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const materialApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/material/queryPage', params),
  
  // 新增
  add: (data) => postRequest('/spd/material/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/material/update', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/material/delete/${id}`),
  
  // 查询详情
  detail: (id) => getRequest(`/spd/material/get/${id}`),
};
