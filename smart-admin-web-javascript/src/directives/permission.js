/**
 * 权限指令 v-permission
 * 用于控制按钮、元素的显示隐藏
 * 
 * 使用示例：
 * <a-button v-permission="'spd:material:add'">新增</a-button>
 * <a-button v-permission="['spd:material:edit', 'spd:material:delete']">编辑</a-button>
 */

import { useUserStore } from '/@/store/modules/system/user';

/**
 * 检查是否有权限
 * @param {String|Array} value - 权限标识
 * @returns {Boolean}
 */
function checkPermission(value) {
  if (!value) {
    return true;
  }

  const userStore = useUserStore();
  const permissions = userStore.permissions || [];

  // 超级管理员拥有所有权限
  if (permissions.includes('*:*:*')) {
    return true;
  }

  // 单个权限检查
  if (typeof value === 'string') {
    return permissions.includes(value);
  }

  // 多个权限检查（满足其中一个即可）
  if (Array.isArray(value) && value.length > 0) {
    return value.some(permission => permissions.includes(permission));
  }

  return false;
}

/**
 * 权限指令
 */
export default {
  // Vue 3 mounted钩子
  mounted(el, binding) {
    const { value } = binding;

    if (!checkPermission(value)) {
      // 没有权限则移除元素
      el.parentNode && el.parentNode.removeChild(el);
    }
  },
};

/**
 * 导出权限检查函数（供JS代码使用）
 */
export { checkPermission };
