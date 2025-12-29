/**
 * SPD - 单位管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const unitApi = {
  // 查询单位列表
  queryList: (params) => postRequest('/spd/unit/queryList', params),
  
  // 查询所有启用的单位(下拉选项)
  queryAll: () => getRequest('/spd/unit/queryAll'),
  
  // 新增单位
  add: (data) => postRequest('/spd/unit/add', data),
  
  // 更新单位
  update: (data) => postRequest('/spd/unit/update', data),
  
  // 删除单位
  delete: (id) => getRequest(`/spd/unit/delete/${id}`),
  
  // 更新状态
  updateStatus: (id, status) => postRequest('/spd/unit/updateStatus', null, { id, status }),
};
