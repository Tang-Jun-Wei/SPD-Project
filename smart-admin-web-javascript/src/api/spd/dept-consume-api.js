/**
 * 科室消耗API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const deptConsumeApi = {
  /**
   * 分页查询
   */
  queryPage: (params) => {
    return postRequest('/spd/dept-consume/queryPage', params);
  },

  /**
   * 查询详情
   */
  getDetail: (consumeId) => {
    return getRequest(`/spd/dept-consume/getDetail/${consumeId}`);
  },

  /**
   * 新增消耗
   */
  add: (data) => {
    return postRequest('/spd/dept-consume/add', data);
  },

  /**
   * 反消耗
   */
  reverse: (consumeId, reason) => {
    return postRequest('/spd/dept-consume/reverse', null, { consumeId, reason });
  },

  /**
   * 删除
   */
  delete: (consumeId) => {
    return getRequest(`/spd/dept-consume/delete/${consumeId}`);
  },
};
