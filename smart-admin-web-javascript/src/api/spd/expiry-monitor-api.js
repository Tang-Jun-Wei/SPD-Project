import { postRequest, getRequest } from '/@/lib/axios';

export const expiryMonitorApi = {
  // 分页查询库存效期监控数据
  queryPage: (param) => {
    return postRequest('/spd/expiryMonitor/queryPage', param);
  },

  // 查询即将过期的库存（用于预警）
  queryExpiringSoon: (days) => {
    return getRequest(`/spd/expiryMonitor/queryExpiringSoon?days=${days || 30}`);
  },
};
