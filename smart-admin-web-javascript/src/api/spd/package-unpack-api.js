/**
 * 定数包拆包API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const packageUnpackApi = {
  queryPage: (params) => postRequest('/spd/package-unpack/queryPage', params),
  getDetail: (unpackId) => getRequest(`/spd/package-unpack/getDetail/${unpackId}`),
  add: (data) => postRequest('/spd/package-unpack/add', data),
  delete: (unpackId) => getRequest(`/spd/package-unpack/delete/${unpackId}`),
};
