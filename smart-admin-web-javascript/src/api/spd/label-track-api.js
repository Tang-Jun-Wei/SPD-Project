/**
 * 标签追溯API
 */
import { postRequest } from '/@/lib/axios';

export const labelTrackApi = {
  /**
   * 查询标签追溯信息
   */
  queryTrack: (data) => {
    return postRequest('/spd/label-track/query', data);
  },
};
