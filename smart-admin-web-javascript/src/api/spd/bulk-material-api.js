/**
 * SPD - 散货管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const bulkMaterialApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/bulkMaterial/queryPage', params),
  
  // 新增
  add: (data) => postRequest('/spd/bulkMaterial/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/bulkMaterial/update', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/bulkMaterial/delete/${id}`),
  
  // 详情
  detail: (id) => getRequest(`/spd/bulkMaterial/detail/${id}`),
  
  // 领用
  use: (data) => postRequest('/spd/bulkMaterial/use', data),
};
