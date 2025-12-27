import { postRequest, getRequest } from '/@/lib/axios';

export const deptCategoryApi = {
  // 查询科室分类树
  queryTree: () => {
    return getRequest('/spd/deptCategory/queryTree');
  },

  // 查询所有科室分类
  queryAll: () => {
    return getRequest('/spd/deptCategory/queryAll');
  },

  // 新增科室分类
  add: (data) => {
    return postRequest('/spd/deptCategory/add', data);
  },

  // 更新科室分类
  update: (data) => {
    return postRequest('/spd/deptCategory/update', data);
  },

  // 删除科室分类
  delete: (categoryId) => {
    return getRequest(`/spd/deptCategory/delete/${categoryId}`);
  },

  // 更新状态
  updateStatus: (data) => {
    return postRequest('/spd/deptCategory/updateStatus', data);
  },
};
