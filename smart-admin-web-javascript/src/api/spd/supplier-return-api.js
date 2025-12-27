import { postRequest, getRequest } from '/@/lib/axios';

export const supplierReturnApi = {
  /**
   * 分页查询退供应商单
   */
  queryPage: (params) => {
    return postRequest('/spd/supplier-return/queryPage', params);
  },

  /**
   * 查询退供应商单详情
   */
  getDetail: (supplierReturnId) => {
    return getRequest(`/spd/supplier-return/getDetail/${supplierReturnId}`);
  },

  /**
   * 新增退供应商单
   */
  add: (data) => {
    return postRequest('/spd/supplier-return/add', data);
  },

  /**
   * 审核退供应商单
   */
  audit: (supplierReturnId, auditStatus, auditOpinion) => {
    return postRequest('/spd/supplier-return/audit', null, {
      supplierReturnId,
      auditStatus,
      auditOpinion,
    });
  },

  /**
   * 删除退供应商单
   */
  delete: (supplierReturnId) => {
    return getRequest(`/spd/supplier-return/delete/${supplierReturnId}`);
  },
};
