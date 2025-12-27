import { postRequest, getRequest } from '/@/lib/axios';

export const deptReturnApi = {
  /**
   * 分页查询退库单
   */
  queryPage: (params) => {
    return postRequest('/spd/return/queryPage', params);
  },

  /**
   * 查询退库单详情
   */
  getDetail: (returnId) => {
    return getRequest(`/spd/return/getDetail/${returnId}`);
  },

  /**
   * 新增退库单
   */
  add: (data) => {
    return postRequest('/spd/return/add', data);
  },

  /**
   * 审核退库单
   */
  audit: (data) => {
    return postRequest('/spd/return/audit', data);
  },

  /**
   * 删除退库单
   */
  delete: (returnId) => {
    return getRequest(`/spd/return/delete/${returnId}`);
  },
};
