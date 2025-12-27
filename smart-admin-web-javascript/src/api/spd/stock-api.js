/**
 * 库存管理API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const stockApi = {
  // 分页查询库存
  queryPage: (params) => postRequest('/spd/stock/queryPage', params),
  
  // 库存预警
  getWarningList: (params) => postRequest('/spd/stock/warningList', params),
  
  // 库存统计
  statistics: (params) => postRequest('/spd/stock/statistics', params),
  
  // 库存盘点
  inventory: (params) => postRequest('/spd/stock/inventory', params),
  
  // 获取详情
  getDetail: (id) => getRequest('/spd/stock/getDetail/' + id),
};
