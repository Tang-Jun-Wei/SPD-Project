/**
 * SPD - 组织节点管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const orgNodeApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/orgNode/query', params),
  
  // 查询详情
  detail: (nodeId) => getRequest(`/spd/orgNode/get/${nodeId}`),
  
  // 新增
  add: (data) => postRequest('/spd/orgNode/add', data),
  
  // 更新
  update: (data) => postRequest('/spd/orgNode/update', data),
  
  // 删除
  delete: (nodeId) => getRequest(`/spd/orgNode/delete/${nodeId}`),
  
  // 查询科室下拉列表
  queryDeptList: () => getRequest('/spd/orgNode/deptList'),
  
  // 查询仓库下拉列表
  queryWarehouseList: () => getRequest('/spd/orgNode/warehouseList'),
  
  // 更新仓库状态
  updateWarehouseStatus: (nodeId, status) => getRequest(`/spd/orgNode/updateWarehouseStatus/${nodeId}/${status}`),
  
  // 更新科室状态
  updateDeptStatus: (nodeId, status) => getRequest(`/spd/orgNode/updateDeptStatus/${nodeId}/${status}`),
};
