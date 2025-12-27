/**
 * SPD - 供应商管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const supplierApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/supplier/queryPage', params),
  
  // 新增
  add: (data) => postRequest('/spd/supplier/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/supplier/update', data),
  
  // 删除
  delete: (id) => getRequest(`/spd/supplier/delete/${id}`),
  
  // 详情
  detail: (id) => getRequest(`/spd/supplier/detail/${id}`),
  
  // 更新状态
  updateStatus: (id, status) => postRequest('/spd/supplier/updateStatus', { id, status }),
  
  // 获取所有供应商列表（下拉选项）
  getAll: () => getRequest('/spd/supplier/getAll'),
};
