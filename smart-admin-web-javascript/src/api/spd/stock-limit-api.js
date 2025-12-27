import { postRequest, getRequest } from '/@/lib/axios';

export const stockLimitApi = {
  // 分页查询库存安全量配置
  queryPage: (param) => {
    return postRequest('/spd/stockLimit/queryPage', param);
  },

  // 查询详情
  getDetail: (id) => {
    return getRequest(`/spd/stockLimit/detail/${id}`);
  },

  // 新增配置
  add: (param) => {
    return postRequest('/spd/stockLimit/add', param);
  },

  // 更新配置
  update: (param) => {
    return postRequest('/spd/stockLimit/update', param);
  },

  // 删除配置
  delete: (id) => {
    return getRequest(`/spd/stockLimit/delete/${id}`);
  },
};
