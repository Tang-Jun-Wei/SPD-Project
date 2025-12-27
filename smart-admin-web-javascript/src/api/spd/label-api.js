/**
 * SPD - 标签管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const labelApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/label/queryPage', params),
  
  // 新增
  add: (data) => postRequest('/spd/label/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/label/update', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/label/delete/${id}`),
  
  // 详情
  detail: (id) => getRequest(`/spd/label/detail/${id}`),
  
  // 打印标签
  print: (id) => getRequest(`/spd/label/print/${id}`),
};
