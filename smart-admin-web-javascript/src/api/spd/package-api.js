import { postRequest, getRequest } from '/@/lib/axios';

export const packageApi = {
  // 分页查询打包单
  queryPage: (params) => {
    return postRequest('/spd/package/queryPage', params);
  },

  // 获取打包单详情
  getDetail: (packageId) => {
    return getRequest(`/spd/package/getDetail/${packageId}`);
  },

  // 新增打包单
  add: (data) => {
    return postRequest('/spd/package/add', data);
  },

  // 更新打包单
  update: (data) => {
    return postRequest('/spd/package/update', data);
  },

  // 删除打包单
  delete: (packageId) => {
    return getRequest(`/spd/package/delete/${packageId}`);
  },

  // 更新打包单状态
  updateStatus: (data) => {
    return postRequest('/spd/package/updateStatus', data);
  },
};
