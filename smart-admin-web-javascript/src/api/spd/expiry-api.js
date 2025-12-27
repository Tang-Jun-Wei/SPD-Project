/**
 * 库存效期API
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const expiryApi = {
  /**
   * 查询效期统计
   */
  getStatistics: () => {
    return getRequest('/spd/expiry/statistics');
  },

  /**
   * 分页查询效期预警列表
   */
  queryWarningPage: (params) => {
    return postRequest('/spd/expiry/queryWarningPage', params);
  },
};
