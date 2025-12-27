/**
 * 配货管理API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const packageApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/package/queryPage', params),
  
  // 新增配货单
  add: (params) => postRequest('/spd/package/add', params),
  
  // 编辑配货单
  update: (params) => postRequest('/spd/package/update', params),
  
  // 删除配货单
  delete: (id) => getRequest('/spd/package/delete/' + id),
  
  // 配货审核
  audit: (params) => postRequest('/spd/package/audit', params),
  
  // 获取详情
  getDetail: (id) => getRequest('/spd/package/getDetail/' + id),
};
