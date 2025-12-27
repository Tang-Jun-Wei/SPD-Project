/**
 * SPD用户管理API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const spdUserApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/user/queryPage', params),
  
  // 新增用户
  add: (params) => postRequest('/spd/user/add', params),
  
  // 编辑用户
  update: (params) => postRequest('/spd/user/update', params),
  
  // 删除用户
  delete: (id) => getRequest('/spd/user/delete/' + id),
  
  // 重置密码
  resetPassword: (userId) => postRequest('/spd/user/resetPassword', { userId }),
  
  // 获取详情
  getDetail: (id) => getRequest('/spd/user/getDetail/' + id),
};
