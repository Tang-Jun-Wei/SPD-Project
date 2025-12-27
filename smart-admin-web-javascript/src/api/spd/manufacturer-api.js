import { postRequest, getRequest } from '/@/lib/axios';

export const manufacturerApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/spd/manufacturer/queryPage', param);
  },

  // 查询详情
  getDetail: (id) => {
    return getRequest(`/spd/manufacturer/detail/${id}`);
  },

  // 查询全部(下拉选项)
  queryAll: () => {
    return getRequest('/spd/manufacturer/queryAll');
  },

  // 新增
  add: (param) => {
    return postRequest('/spd/manufacturer/add', param);
  },

  // 更新
  update: (param) => {
    return postRequest('/spd/manufacturer/update', param);
  },

  // 删除
  delete: (id) => {
    return getRequest(`/spd/manufacturer/delete/${id}`);
  },

  // 更新状态
  updateStatus: (id, status) => {
    return postRequest('/spd/manufacturer/updateStatus', null, { id, status });
  },
};
