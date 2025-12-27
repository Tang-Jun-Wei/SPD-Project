import { postRequest, getRequest } from '/@/lib/axios';

export const locationApi = {
  // 分页查询货位
  queryPage: (params) => {
    return postRequest('/spd/location/queryPage', params);
  },

  // 新增货位
  add: (data) => {
    return postRequest('/spd/location/add', data);
  },

  // 更新货位
  update: (data) => {
    return postRequest('/spd/location/update', data);
  },

  // 删除货位
  delete: (locationId) => {
    return getRequest(`/spd/location/delete/${locationId}`);
  },

  // 更新状态
  updateStatus: (data) => {
    return postRequest('/spd/location/updateStatus', data);
  },
};
