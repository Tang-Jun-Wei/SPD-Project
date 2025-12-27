/**
 * SPD - 申领单 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const applyApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/apply/queryPage', params),
  
  // 新增
  add: (data) => postRequest('/spd/apply/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/apply/update', data),
  
  // 审核
  approve: (data) => postRequest('/spd/apply/approve', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/apply/delete/${id}`),
  
  // 详情
  detail: (id) => getRequest(`/spd/apply/detail/${id}`),
};
