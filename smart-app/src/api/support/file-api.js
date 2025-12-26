/**
 * 系统更新日志 api 封装
 *
 * @Author:    卓大
 */
import { uploadRequest } from '@/lib/smart-request';

export const fileApi = {
  upload: (file, folder) => {
    return uploadRequest(file, folder);
  },
};
