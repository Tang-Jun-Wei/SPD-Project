/**
 * SPD - 耗材分类管理 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const categoryApi = {
  // 查询分类树
  queryTree: () => getRequest('/spd/category/queryTree'),
  
  // 查询所有启用的分类(下拉选项)
  queryAllEnabled: () => getRequest('/spd/category/queryAllEnabled'),
  
  // 新增分类
  add: (data) => postRequest('/spd/category/add', data),
  
  // 更新分类
  update: (data) => postRequest('/spd/category/update', data),
  
  // 删除分类
  delete: (id) => getRequest(`/spd/category/delete/${id}`),
  
  // 更新状态
  updateStatus: (id, status) => postRequest('/spd/category/updateStatus', null, { id, status }),
};
