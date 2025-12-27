import { postRequest, getRequest } from '/@/lib/axios';

export const warehouseUserApi = {
  // 分页查询仓库人员关系
  queryPage: (param) => {
    return postRequest('/spd/warehouseUser/queryPage', param);
  },

  // 查询详情
  getDetail: (id) => {
    return getRequest(`/spd/warehouseUser/detail/${id}`);
  },

  // 新增关系
  add: (param) => {
    return postRequest('/spd/warehouseUser/add', param);
  },

  // 更新关系
  update: (param) => {
    return postRequest('/spd/warehouseUser/update', param);
  },

  // 删除关系
  delete: (id) => {
    return getRequest(`/spd/warehouseUser/delete/${id}`);
  },

  // 更新状态
  updateStatus: (id, status) => {
    return getRequest(`/spd/warehouseUser/updateStatus/${id}/${status}`);
  },

  // 根据仓库ID查询人员列表
  queryByWarehouse: (warehouseId) => {
    return getRequest(`/spd/warehouseUser/queryByWarehouse/${warehouseId}`);
  },

  // 根据用户ID查询仓库列表
  queryByUser: (userId) => {
    return getRequest(`/spd/warehouseUser/queryByUser/${userId}`);
  },
};
