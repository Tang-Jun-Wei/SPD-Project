/**
 * 消耗管理API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const consumeApi = {
  // 分页查询
  queryPage: (params) => postRequest('/spd/consume/queryPage', params),
  
  // 新增消耗记录
  add: (params) => postRequest('/spd/consume/add', params),
  
  // 获取详情
  getDetail: (id) => getRequest('/spd/consume/getDetail/' + id),
  
  // 统计分析
  statistics: (params) => postRequest('/spd/consume/statistics', params),
};
