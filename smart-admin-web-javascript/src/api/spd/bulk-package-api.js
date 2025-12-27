/**
 * 散货打包API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const bulkPackageApi = {
  queryPage: (params) => postRequest('/spd/bulk-package/queryPage', params),
  getDetail: (packageId) => getRequest(`/spd/bulk-package/getDetail/${packageId}`),
  add: (data) => postRequest('/spd/bulk-package/add', data),
  delete: (packageId) => getRequest(`/spd/bulk-package/delete/${packageId}`),
};
