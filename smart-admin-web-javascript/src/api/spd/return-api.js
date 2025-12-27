/**
 * 退货管理API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const returnApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/return/queryPage', params),
  
  // 新增退货单
  add: (params) => postRequest('/spd/return/add', params),
  
  // 编辑退货单
  update: (params) => postRequest('/spd/return/update', params),
  
  // 删除退货单
  delete: (id) => getRequest('/spd/return/delete/' + id),
  
  // 退货审核
  audit: (params) => postRequest('/spd/return/audit', params),
  
  // 获取详情
  getDetail: (id) => getRequest('/spd/return/getDetail/' + id),
};
