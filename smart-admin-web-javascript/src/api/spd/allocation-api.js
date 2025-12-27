import { postRequest, getRequest } from '/@/lib/axios';

export const allocationApi = {
  /**
   * 分页查询调拨单
   */
  queryPage: (params) => {
    return postRequest('/spd/allocation/queryPage', params);
  },

  /**
   * 查询调拨单详情
   */
  getDetail: (allocationId) => {
    return getRequest(`/spd/allocation/getDetail/${allocationId}`);
  },

  /**
   * 新增调拨单
   */
  add: (data) => {
    return postRequest('/spd/allocation/add', data);
  },

  /**
   * 审核调拨单
   */
  audit: (data) => {
    return postRequest('/spd/allocation/audit', data);
  },

  /**
   * 删除调拨单
   */
  delete: (allocationId) => {
    return getRequest(`/spd/allocation/delete/${allocationId}`);
  },
};
