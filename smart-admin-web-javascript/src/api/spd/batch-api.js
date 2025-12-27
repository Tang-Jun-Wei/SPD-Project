/**
 * SPD - 批号管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const batchApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/batch/query', params),
  
  // 查询详情
  detail: (id) => getRequest(`/spd/batch/get/${id}`),
  
  // 新增
  add: (data) => postRequest('/spd/batch/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/batch/update', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/batch/delete/${id}`),
};
