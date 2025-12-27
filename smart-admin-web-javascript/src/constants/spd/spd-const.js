/**
 * SPD模块常量定义
 */

// 节点类型枚举
export const NODE_TYPE_ENUM = {
  DEPT: {
    value: 1,
    label: '科室',
    color: 'blue',
  },
  WAREHOUSE: {
    value: 2,
    label: '仓库',
    color: 'green',
  },
};

// 仓库类型枚举
export const WAREHOUSE_TYPE_ENUM = {
  CENTER: {
    value: 1,
    label: '中心库',
    color: 'purple',
  },
  DEPT: {
    value: 2,
    label: '科室库',
    color: 'blue',
  },
  TEMP: {
    value: 3,
    label: '临时库',
    color: 'orange',
  },
};

// 状态枚举
export const STATUS_ENUM = {
  DISABLED: {
    value: 0,
    label: '禁用',
    color: 'red',
  },
  ENABLED: {
    value: 1,
    label: '启用',
    color: 'green',
  },
};

// 申领单状态枚举
export const APPLY_STATUS_ENUM = {
  DRAFT: {
    value: 0,
    label: '草稿',
    color: 'default',
  },
  PENDING: {
    value: 1,
    label: '待审核',
    color: 'orange',
  },
  APPROVED: {
    value: 2,
    label: '已审核',
    color: 'blue',
  },
  DELIVERING: {
    value: 3,
    label: '配送中',
    color: 'cyan',
  },
  COMPLETED: {
    value: 4,
    label: '已完成',
    color: 'green',
  },
  REJECTED: {
    value: 9,
    label: '已拒绝',
    color: 'red',
  },
};

// 采购单状态枚举
export const PURCHASE_STATUS_ENUM = {
  DRAFT: {
    value: 0,
    label: '草稿',
    color: 'default',
  },
  PENDING: {
    value: 1,
    label: '待审核',
    color: 'orange',
  },
  APPROVED: {
    value: 2,
    label: '已审核',
    color: 'blue',
  },
  IN_TRANSIT: {
    value: 3,
    label: '在途',
    color: 'cyan',
  },
  RECEIVED: {
    value: 4,
    label: '已收货',
    color: 'green',
  },
  REJECTED: {
    value: 9,
    label: '已拒绝',
    color: 'red',
  },
};

// 收货单状态枚举
export const RECEIVE_STATUS_ENUM = {
  DRAFT: {
    value: 0,
    label: '草稿',
    color: 'default',
  },
  PENDING: {
    value: 1,
    label: '待验收',
    color: 'orange',
  },
  ACCEPTED: {
    value: 2,
    label: '已验收',
    color: 'green',
  },
  REJECTED: {
    value: 9,
    label: '已拒绝',
    color: 'red',
  },
};

// 耗材状态枚举
export const MATERIAL_STATUS_ENUM = STATUS_ENUM;

// 标签类型枚举
export const LABEL_TYPE_ENUM = {
  BOX: {
    value: 1,
    label: '定数包',
    color: 'blue',
  },
  BULK: {
    value: 2,
    label: '散货',
    color: 'green',
  },
};

// 验收状态枚举
export const ACCEPTANCE_STATUS_ENUM = {
  DRAFT: {
    value: 0,
    label: '草稿',
    color: 'default',
  },
  PENDING: {
    value: 1,
    label: '待验收',
    color: 'orange',
  },
  ACCEPTED: {
    value: 2,
    label: '已验收',
    color: 'green',
  },
  REJECTED: {
    value: 9,
    label: '已拒绝',
    color: 'red',
  },
};

// 配货状态枚举
export const PACKAGE_STATUS_ENUM = {
  DRAFT: {
    value: 0,
    label: '草稿',
    color: 'default',
  },
  PENDING: {
    value: 1,
    label: '待配货',
    color: 'orange',
  },
  PACKAGING: {
    value: 2,
    label: '配货中',
    color: 'blue',
  },
  COMPLETED: {
    value: 3,
    label: '已完成',
    color: 'green',
  },
};

// 退货状态枚举
export const RETURN_STATUS_ENUM = {
  DRAFT: {
    value: 0,
    label: '草稿',
    color: 'default',
  },
  PENDING: {
    value: 1,
    label: '待审核',
    color: 'orange',
  },
  APPROVED: {
    value: 2,
    label: '已审核',
    color: 'blue',
  },
  COMPLETED: {
    value: 3,
    label: '已完成',
    color: 'green',
  },
  REJECTED: {
    value: 9,
    label: '已拒绝',
    color: 'red',
  },
};

// 获取枚举标签
export function getEnumLabel(enumObj, value) {
  for (const key in enumObj) {
    if (enumObj[key].value === value) {
      return enumObj[key].label;
    }
  }
  return '-';
}

// 获取枚举颜色
export function getEnumColor(enumObj, value) {
  for (const key in enumObj) {
    if (enumObj[key].value === value) {
      return enumObj[key].color;
    }
  }
  return 'default';
}

// 转换枚举为选项数组
export function enumToOptions(enumObj) {
  return Object.keys(enumObj).map(key => ({
    value: enumObj[key].value,
    label: enumObj[key].label,
  }));
}

export default {
  NODE_TYPE_ENUM,
  WAREHOUSE_TYPE_ENUM,
  STATUS_ENUM,
  APPLY_STATUS_ENUM,
  PURCHASE_STATUS_ENUM,
  RECEIVE_STATUS_ENUM,
  MATERIAL_STATUS_ENUM,
  LABEL_TYPE_ENUM,
  ACCEPTANCE_STATUS_ENUM,
  PACKAGE_STATUS_ENUM,
  RETURN_STATUS_ENUM,
  getEnumLabel,
  getEnumColor,
  enumToOptions,
};
