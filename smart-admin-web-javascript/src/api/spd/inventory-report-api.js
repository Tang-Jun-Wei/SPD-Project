import { postRequest, getRequest } from '/@/lib/axios';

export const inventoryReportApi = {
  // 库存统计概览
  getOverview: () => {
    return getRequest('/spd/inventoryReport/overview');
  },

  // 库存明细列表
  queryInventoryDetail: (params) => {
    return postRequest('/spd/inventoryReport/queryDetail', params);
  },

  // 库存预警列表
  queryWarning: (params) => {
    return postRequest('/spd/inventoryReport/queryWarning', params);
  },

  // 效期预警列表
  queryExpiryWarning: (params) => {
    return postRequest('/spd/inventoryReport/queryExpiryWarning', params);
  },

  // 导出库存报表
  exportInventory: (params) => {
    return postRequest('/spd/inventoryReport/export', params, { responseType: 'blob' });
  },
};
