import { postRequest, getRequest } from '/@/lib/axios';

export const deptReceiveApi = {
  /**
   * 分页查询科室收货单
   */
  queryPage: (params) => {
    return postRequest('/spd/dept-receive/queryPage', params);
  },

  /**
   * 查询科室收货单详情
   */
  getDetail: (receiveId) => {
    return getRequest(`/spd/dept-receive/getDetail/${receiveId}`);
  },

  /**
   * 确认收货
   */
  confirmReceive: (receiveId) => {
    return postRequest('/spd/dept-receive/confirm', null, { receiveId });
  },

  /**
   * 拒收
   */
  rejectReceive: (receiveId, reason) => {
    return postRequest('/spd/dept-receive/reject', null, { receiveId, reason });
  },
};
