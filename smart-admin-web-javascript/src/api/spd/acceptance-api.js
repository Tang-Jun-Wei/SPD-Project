/**
 * 验收管理API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const acceptanceApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/acceptance/queryPage', params),
  
  // 新增验收单
  add: (params) => postRequest('/spd/acceptance/add', params),
  
  // 编辑验收单
  update: (params) => postRequest('/spd/acceptance/update', params),
  
  // 删除验收单
  delete: (id) => getRequest('/spd/acceptance/delete/' + id),
  
  // 验收审核
  audit: (params) => postRequest('/spd/acceptance/audit', params),
  
  // 获取详情
  getDetail: (id) => getRequest('/spd/acceptance/getDetail/' + id),
};
