/**
 * 操作日志记录工具
 */
import { useUserStore } from '/@/store/modules/system/user';

/**
 * 操作类型枚举
 */
export const OPERATION_TYPE = {
  ADD: 'add',
  EDIT: 'edit',
  DELETE: 'delete',
  QUERY: 'query',
  EXPORT: 'export',
  IMPORT: 'import',
  APPROVE: 'approve',
  REJECT: 'reject',
};

/**
 * 操作模块枚举
 */
export const OPERATION_MODULE = {
  ORG_NODE: 'org_node',
  MATERIAL: 'material',
  BATCH: 'batch',
  SUPPLIER: 'supplier',
  PURCHASE: 'purchase',
  RECEIVE: 'receive',
  APPLY: 'apply',
  LABEL: 'label',
  BULK_MATERIAL: 'bulk_material',
  INVENTORY: 'inventory',
};

/**
 * 操作日志数据结构
 */
class OperationLog {
  constructor(module, type, description, data = null) {
    this.module = module;           // 操作模块
    this.type = type;                // 操作类型
    this.description = description;  // 操作描述
    this.data = data;                // 操作数据
    this.timestamp = new Date();     // 操作时间
    this.userId = null;              // 操作人ID
    this.userName = null;            // 操作人姓名
  }
}

/**
 * 记录操作日志
 * @param {String} module - 操作模块
 * @param {String} type - 操作类型
 * @param {String} description - 操作描述
 * @param {Object} data - 操作数据
 */
export function recordLog(module, type, description, data = null) {
  try {
    const userStore = useUserStore();
    const log = new OperationLog(module, type, description, data);
    
    // 填充用户信息
    if (userStore.user) {
      log.userId = userStore.user.userId;
      log.userName = userStore.user.userName;
    }

    // 开发环境打印日志
    if (import.meta.env.DEV) {
      console.log('[操作日志]', {
        时间: log.timestamp.toLocaleString(),
        模块: log.module,
        类型: log.type,
        描述: log.description,
        操作人: log.userName,
        数据: log.data,
      });
    }

    // TODO: 发送到后端记录（可选）
    // await operationLogApi.record(log);

    return log;
  } catch (error) {
    console.error('记录操作日志失败：', error);
  }
}

/**
 * 便捷方法：记录新增操作
 */
export function logAdd(module, description, data) {
  return recordLog(module, OPERATION_TYPE.ADD, description, data);
}

/**
 * 便捷方法：记录编辑操作
 */
export function logEdit(module, description, data) {
  return recordLog(module, OPERATION_TYPE.EDIT, description, data);
}

/**
 * 便捷方法：记录删除操作
 */
export function logDelete(module, description, data) {
  return recordLog(module, OPERATION_TYPE.DELETE, description, data);
}

/**
 * 便捷方法：记录查询操作
 */
export function logQuery(module, description, data) {
  return recordLog(module, OPERATION_TYPE.QUERY, description, data);
}

/**
 * 便捷方法：记录导出操作
 */
export function logExport(module, description, data) {
  return recordLog(module, OPERATION_TYPE.EXPORT, description, data);
}

/**
 * 便捷方法：记录审核操作
 */
export function logApprove(module, description, data) {
  return recordLog(module, OPERATION_TYPE.APPROVE, description, data);
}

export default {
  OPERATION_TYPE,
  OPERATION_MODULE,
  recordLog,
  logAdd,
  logEdit,
  logDelete,
  logQuery,
  logExport,
  logApprove,
};
