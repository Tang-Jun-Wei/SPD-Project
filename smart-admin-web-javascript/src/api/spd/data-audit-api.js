import { postRequest, getRequest } from '/@/lib/axios';

export const dataAuditApi = {
  // 分页查询
  queryPage: (param) => {
    return postRequest('/spd/dataAudit/queryPage', param);
  },

  // 查询详情
  getDetail: (id) => {
    return getRequest(`/spd/dataAudit/detail/${id}`);
  },

  // 提交审核
  submit: (param) => {
    return postRequest('/spd/dataAudit/submit', param);
  },

  // 处理审核
  process: (param) => {
    return postRequest('/spd/dataAudit/process', param);
  },

  // 撤销审核
  cancel: (id) => {
    return getRequest(`/spd/dataAudit/cancel/${id}`);
  },
};
