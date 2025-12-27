/**
 * SPD - 库存报表 API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const inventoryApi = {
  // 库存汇总查询
  querySummary: (params) => postRequest('/spd/inventory/querySummary', params),
  
  // 标签库存查询
  queryLabel: (params) => postRequest('/spd/inventory/queryLabel', params),
  
  // 散货库存查询
  queryBulk: (params) => postRequest('/spd/inventory/queryBulk', params),
  
  // 导出库存报表
  export: (params) => postRequest('/spd/inventory/export', params, { responseType: 'blob' }),
};
