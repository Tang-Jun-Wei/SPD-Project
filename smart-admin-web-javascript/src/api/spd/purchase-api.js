/**
 * SPD - 采购单 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const purchaseApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/purchase/queryPage', params),
  
  // 新增
  add: (data) => postRequest('/spd/purchase/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/purchase/update', data),
  
  // 审核
  approve: (data) => postRequest('/spd/purchase/approve', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/purchase/delete/${id}`),
  
  // 详情
  detail: (id) => getRequest(`/spd/purchase/detail/${id}`),
};
