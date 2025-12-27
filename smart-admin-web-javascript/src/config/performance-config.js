/**
 * 前端性能优化配置
 */

// 分页配置
export const PAGINATION_CONFIG = {
  // 默认每页显示条数
  DEFAULT_PAGE_SIZE: 10,
  // 可选的每页显示条数
  PAGE_SIZE_OPTIONS: ['10', '20', '50', '100'],
  // 最大每页显示条数
  MAX_PAGE_SIZE: 100,
};

// 缓存配置
export const CACHE_CONFIG = {
  // 字典缓存时长（毫秒）
  DICT_CACHE_DURATION: 30 * 60 * 1000, // 30分钟
  // 用户信息缓存时长
  USER_CACHE_DURATION: 60 * 60 * 1000, // 1小时
  // 下拉选项缓存时长
  OPTIONS_CACHE_DURATION: 15 * 60 * 1000, // 15分钟
};

// 懒加载配置
export const LAZY_LOAD_CONFIG = {
  // 图片懒加载阈值（像素）
  IMAGE_THRESHOLD: 200,
  // 列表虚拟滚动启用阈值（条数）
  VIRTUAL_SCROLL_THRESHOLD: 100,
};

// 防抖节流配置
export const DEBOUNCE_CONFIG = {
  // 搜索输入防抖延迟（毫秒）
  SEARCH_DELAY: 300,
  // 窗口resize节流延迟
  RESIZE_DELAY: 200,
  // 滚动事件节流延迟
  SCROLL_DELAY: 100,
};

// 请求优化配置
export const REQUEST_CONFIG = {
  // 请求超时时间（毫秒）
  TIMEOUT: 30000,
  // 并发请求最大数量
  MAX_CONCURRENT: 6,
  // 重试次数
  MAX_RETRY: 2,
};

export default {
  PAGINATION_CONFIG,
  CACHE_CONFIG,
  LAZY_LOAD_CONFIG,
  DEBOUNCE_CONFIG,
  REQUEST_CONFIG,
};
