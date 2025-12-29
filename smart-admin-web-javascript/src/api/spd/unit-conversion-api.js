/**
 * SPD - 单位换算管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const unitConversionApi = {
  // 查询换算关系列表
  queryList: (params) => postRequest('/spd/unit/conversion/queryList', params),
  
  // 新增换算关系
  add: (data) => postRequest('/spd/unit/conversion/add', data),
  
  // 更新换算关系
  update: (data) => postRequest('/spd/unit/conversion/update', data),
  
  // 删除换算关系
  delete: (id) => getRequest(`/spd/unit/conversion/delete/${id}`),
};
