/*
 * SPD耗材管理模块路由
 */

const Layout = () => import('/@/layout/index.vue');

export const spdRouters = [
  {
    path: '/spd',
    component: Layout,
    children: [
      // 申领单管理
      {
        path: 'apply',
        name: 'SpdApply',
        component: () => import('/@/views/spd/apply/apply-list.vue'),
        meta: {
          title: '申领单管理',
          hideInMenu: false,
        },
      },
      // 采购单管理
      {
        path: 'purchase',
        name: 'SpdPurchase',
        component: () => import('/@/views/spd/purchase/purchase-list.vue'),
        meta: {
          title: '采购单管理',
          hideInMenu: false,
        },
      },
      // 收货单管理
      {
        path: 'receive',
        name: 'SpdReceive',
        component: () => import('/@/views/spd/receive/receive-list.vue'),
        meta: {
          title: '收货单管理',
          hideInMenu: false,
        },
      },
      // 验收单管理
      {
        path: 'acceptance',
        name: 'SpdAcceptance',
        component: () => import('/@/views/spd/acceptance/acceptance-list.vue'),
        meta: {
          title: '验收单管理',
          hideInMenu: false,
        },
      },
      // 打包单管理
      {
        path: 'package',
        name: 'SpdPackage',
        component: () => import('/@/views/spd/package/package-list.vue'),
        meta: {
          title: '打包单管理',
          hideInMenu: false,
        },
      },
      // 标签管理（定数包）
      {
        path: 'label',
        name: 'SpdLabel',
        component: () => import('/@/views/spd/label/label-list.vue'),
        meta: {
          title: '标签管理',
          hideInMenu: false,
        },
      },
      // 散货管理
      {
        path: 'bulk-material',
        name: 'SpdBulkMaterial',
        component: () => import('/@/views/spd/bulk-material/bulk-material-list.vue'),
        meta: {
          title: '散货管理',
          hideInMenu: false,
        },
      },
      // 供应商管理
      {
        path: 'supplier',
        name: 'SpdSupplier',
        component: () => import('/@/views/spd/supplier/supplier-list.vue'),
        meta: {
          title: '供应商管理',
          hideInMenu: false,
        },
      },
      // 生产厂家管理
      {
        path: 'manufacturer',
        name: 'SpdManufacturer',
        component: () => import('/@/views/spd/manufacturer/manufacturer-list.vue'),
        meta: {
          title: '生产厂家管理',
          hideInMenu: false,
        },
      },
      // 基础资料审核
      {
        path: 'data-audit',
        name: 'SpdDataAudit',
        component: () => import('/@/views/spd/audit/data-audit-list.vue'),
        meta: {
          title: '基础资料审核',
          hideInMenu: false,
        },
      },
      // 库存安全量维护
      {
        path: 'stock-limit',
        name: 'SpdStockLimit',
        component: () => import('/@/views/spd/stock/stock-limit-list.vue'),
        meta: {
          title: '库存安全量维护',
          hideInMenu: false,
        },
      },
      // 库存效期监控
      {
        path: 'expiry-monitor',
        name: 'SpdExpiryMonitor',
        component: () => import('/@/views/spd/expiry/expiry-monitor.vue'),
        meta: {
          title: '库存效期监控',
          hideInMenu: false,
        },
      },
      // 仓库人员对应关系
      {
        path: 'warehouse-user',
        name: 'SpdWarehouseUser',
        component: () => import('/@/views/spd/warehouse-user/warehouse-user-list.vue'),
        meta: {
          title: '仓库人员对应关系',
          hideInMenu: false,
        },
      },
      // 科室分类管理
      {
        path: 'dept-category',
        name: 'SpdDeptCategory',
        component: () => import('/@/views/spd/dept-category/dept-category-list.vue'),
        meta: {
          title: '科室分类管理',
          hideInMenu: false,
        },
      },
      // 库存统计报表
      {
        path: 'inventory-report',
        name: 'SpdInventoryReport',
        component: () => import('/@/views/spd/inventory-report/inventory-report.vue'),
        meta: {
          title: '库存统计报表',
          hideInMenu: false,
        },
      },
    ],
  },
];
