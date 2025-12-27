/**
 * SPD - 收货单 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const receiveApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/receive/queryPage', params),
  
  // 新增
  add: (data) => postRequest('/spd/receive/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/receive/update', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/receive/delete/${id}`),
  
  // 详情
  detail: (id) => getRequest(`/spd/receive/detail/${id}`),
};
