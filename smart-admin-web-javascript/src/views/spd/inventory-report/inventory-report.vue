<template>
  <div class="inventory-report-container">
    <!-- 统计卡片 -->
    <a-row :gutter="16" style="margin-bottom: 16px">
      <a-col :span="6">
        <a-card :bordered="false" :loading="overviewLoading">
          <a-statistic
            title="库存总值"
            :value="overview.totalValue"
            prefix="¥"
            :value-style="{ color: '#3f8600' }"
          >
            <template #suffix>
              <span style="font-size: 14px">元</span>
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" :loading="overviewLoading">
          <a-statistic
            title="库存数量"
            :value="overview.totalQuantity"
            :value-style="{ color: '#1890ff' }"
          >
            <template #suffix>
              <span style="font-size: 14px">件</span>
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" :loading="overviewLoading">
          <a-statistic
            title="库存预警"
            :value="overview.warningCount"
            :value-style="{ color: '#cf1322' }"
          >
            <template #suffix>
              <span style="font-size: 14px">项</span>
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" :loading="overviewLoading">
          <a-statistic
            title="效期预警"
            :value="overview.expiryWarningCount"
            :value-style="{ color: '#faad14' }"
          >
            <template #suffix>
              <span style="font-size: 14px">项</span>
            </template>
          </a-statistic>
        </a-card>
      </a-col>
    </a-row>

    <!-- 查询表单 -->
    <a-card size="small" :bordered="false" style="margin-bottom: 16px">
      <a-form class="smart-query-form" layout="inline">
        <a-row class="smart-query-form-row">
          <a-form-item label="耗材名称" class="smart-query-form-item">
            <a-input
              v-model:value="queryForm.materialName"
              placeholder="请输入耗材名称"
              style="width: 200px"
              allowClear
            />
          </a-form-item>

          <a-form-item label="仓库" class="smart-query-form-item">
            <a-select
              v-model:value="queryForm.warehouseId"
              placeholder="请选择仓库"
              style="width: 200px"
              allowClear
            >
              <!-- 仓库选项 -->
            </a-select>
          </a-form-item>

          <a-form-item label="库存类型" class="smart-query-form-item">
            <a-select
              v-model:value="queryForm.inventoryType"
              placeholder="请选择类型"
              style="width: 200px"
              allowClear
            >
              <a-select-option value="label">定数包</a-select-option>
              <a-select-option value="bulk">散货</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item class="smart-query-form-item smart-margin-left10">
            <a-button-group>
              <a-button type="primary" @click="queryData">
                <template #icon>
                  <SearchOutlined />
                </template>
                查询
              </a-button>
              <a-button @click="resetQuery">
                <template #icon>
                  <ReloadOutlined />
                </template>
                重置
              </a-button>
            </a-button-group>
            <a-button type="primary" @click="exportData" style="margin-left: 10px">
              <template #icon>
                <DownloadOutlined />
              </template>
              导出
            </a-button>
          </a-form-item>
        </a-row>
      </a-form>
    </a-card>

    <!-- Tab 切换 -->
    <a-card size="small" :bordered="false">
      <a-tabs v-model:activeKey="activeTab" @change="handleTabChange">
        <!-- 库存明细 -->
        <a-tab-pane key="inventory" tab="库存明细">
          <a-table
            size="small"
            :scroll="{ x: 1500 }"
            :dataSource="inventoryData"
            :columns="inventoryColumns"
            :loading="tableLoading"
            :pagination="false"
            rowKey="id"
            bordered
          >
            <template #bodyCell="{ text, record, column }">
              <template v-if="column.dataIndex === 'inventoryType'">
                <a-tag v-if="text === 'label'" color="blue">定数包</a-tag>
                <a-tag v-else color="green">散货</a-tag>
              </template>

              <template v-if="column.dataIndex === 'quantity'">
                <span :style="{ 
                  color: text <= 10 ? '#ff4d4f' : text <= 50 ? '#faad14' : 'inherit',
                  fontWeight: text <= 50 ? 'bold' : 'normal'
                }">
                  {{ text }}
                </span>
              </template>
            </template>
          </a-table>

          <div class="smart-query-table-page">
            <a-pagination
              showSizeChanger
              showQuickJumper
              show-less-items
              :pageSizeOptions="PAGE_SIZE_OPTIONS"
              :defaultPageSize="queryForm.pageSize"
              v-model:current="queryForm.pageNum"
              v-model:pageSize="queryForm.pageSize"
              :total="total"
              @change="queryData"
              @showSizeChange="queryData"
              :show-total="(total) => `共${total}条`"
            />
          </div>
        </a-tab-pane>

        <!-- 库存预警 -->
        <a-tab-pane key="warning" tab="库存预警">
          <a-table
            size="small"
            :scroll="{ x: 1500 }"
            :dataSource="warningData"
            :columns="warningColumns"
            :loading="tableLoading"
            :pagination="false"
            rowKey="id"
            bordered
          >
            <template #bodyCell="{ text, record, column }">
              <template v-if="column.dataIndex === 'warningLevel'">
                <a-tag v-if="text === 1" color="red">严重</a-tag>
                <a-tag v-else-if="text === 2" color="orange">警告</a-tag>
                <a-tag v-else color="yellow">提示</a-tag>
              </template>
            </template>
          </a-table>

          <div class="smart-query-table-page">
            <a-pagination
              showSizeChanger
              showQuickJumper
              show-less-items
              :pageSizeOptions="PAGE_SIZE_OPTIONS"
              :defaultPageSize="queryForm.pageSize"
              v-model:current="queryForm.pageNum"
              v-model:pageSize="queryForm.pageSize"
              :total="total"
              @change="queryWarningData"
              @showSizeChange="queryWarningData"
              :show-total="(total) => `共${total}条`"
            />
          </div>
        </a-tab-pane>

        <!-- 效期预警 -->
        <a-tab-pane key="expiry" tab="效期预警">
          <a-table
            size="small"
            :scroll="{ x: 1500 }"
            :dataSource="expiryWarningData"
            :columns="expiryWarningColumns"
            :loading="tableLoading"
            :pagination="false"
            rowKey="id"
            bordered
          >
            <template #bodyCell="{ text, record, column }">
              <template v-if="column.dataIndex === 'daysToExpiry'">
                <span :style="{ 
                  color: text <= 30 ? '#ff4d4f' : text <= 90 ? '#faad14' : 'inherit',
                  fontWeight: text <= 90 ? 'bold' : 'normal'
                }">
                  {{ text }} 天
                </span>
              </template>
            </template>
          </a-table>

          <div class="smart-query-table-page">
            <a-pagination
              showSizeChanger
              showQuickJumper
              show-less-items
              :pageSizeOptions="PAGE_SIZE_OPTIONS"
              :defaultPageSize="queryForm.pageSize"
              v-model:current="queryForm.pageNum"
              v-model:pageSize="queryForm.pageSize"
              :total="total"
              @change="queryExpiryWarningData"
              @showSizeChange="queryExpiryWarningData"
              :show-total="(total) => `共${total}条`"
            />
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    DownloadOutlined,
  } from '@ant-design/icons-vue';
  import { inventoryReportApi } from '/@/api/spd/inventory-report-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';

  const activeTab = ref('inventory');
  const overviewLoading = ref(false);
  const tableLoading = ref(false);

  // 概览数据
  const overview = reactive({
    totalValue: 0,
    totalQuantity: 0,
    warningCount: 0,
    expiryWarningCount: 0,
  });

  // 查询表单
  const queryForm = reactive({
    materialName: '',
    warehouseId: undefined,
    inventoryType: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  // 表格数据
  const inventoryData = ref([]);
  const warningData = ref([]);
  const expiryWarningData = ref([]);
  const total = ref(0);

  // 库存明细列
  const inventoryColumns = ref([
    { title: '耗材名称', dataIndex: 'materialName', width: 200, fixed: 'left' },
    { title: '规格型号', dataIndex: 'specification', width: 150 },
    { title: '批号', dataIndex: 'batchNo', width: 120 },
    { title: '库存类型', dataIndex: 'inventoryType', width: 100 },
    { title: '仓库', dataIndex: 'warehouseName', width: 120 },
    { title: '数量', dataIndex: 'quantity', width: 100 },
    { title: '单位', dataIndex: 'unit', width: 60 },
    { title: '单价', dataIndex: 'unitPrice', width: 100 },
    { title: '总价', dataIndex: 'totalPrice', width: 120 },
    { title: '生产日期', dataIndex: 'productionDate', width: 120 },
    { title: '有效期至', dataIndex: 'expiryDate', width: 120 },
  ]);

  // 库存预警列
  const warningColumns = ref([
    { title: '耗材名称', dataIndex: 'materialName', width: 200, fixed: 'left' },
    { title: '仓库', dataIndex: 'warehouseName', width: 120 },
    { title: '当前库存', dataIndex: 'currentStock', width: 100 },
    { title: '预警阈值', dataIndex: 'warningThreshold', width: 100 },
    { title: '预警级别', dataIndex: 'warningLevel', width: 100 },
    { title: '预警时间', dataIndex: 'warningTime', width: 160 },
  ]);

  // 效期预警列
  const expiryWarningColumns = ref([
    { title: '耗材名称', dataIndex: 'materialName', width: 200, fixed: 'left' },
    { title: '批号', dataIndex: 'batchNo', width: 120 },
    { title: '仓库', dataIndex: 'warehouseName', width: 120 },
    { title: '数量', dataIndex: 'quantity', width: 100 },
    { title: '生产日期', dataIndex: 'productionDate', width: 120 },
    { title: '有效期至', dataIndex: 'expiryDate', width: 120 },
    { title: '距到期', dataIndex: 'daysToExpiry', width: 100 },
  ]);

  // 查询概览
  async function queryOverview() {
    try {
      overviewLoading.value = true;
      const res = await inventoryReportApi.getOverview();
      Object.assign(overview, res.data);
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      overviewLoading.value = false;
    }
  }

  // 查询库存明细
  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await inventoryReportApi.queryInventoryDetail(queryForm);
      inventoryData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // 查询库存预警
  async function queryWarningData() {
    try {
      tableLoading.value = true;
      const res = await inventoryReportApi.queryWarning(queryForm);
      warningData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // 查询效期预警
  async function queryExpiryWarningData() {
    try {
      tableLoading.value = true;
      const res = await inventoryReportApi.queryExpiryWarning(queryForm);
      expiryWarningData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // Tab切换
  function handleTabChange(key) {
    queryForm.pageNum = 1;
    if (key === 'inventory') {
      queryData();
    } else if (key === 'warning') {
      queryWarningData();
    } else if (key === 'expiry') {
      queryExpiryWarningData();
    }
  }

  // 重置查询
  function resetQuery() {
    queryForm.materialName = '';
    queryForm.warehouseId = undefined;
    queryForm.inventoryType = undefined;
    queryForm.pageNum = 1;
    handleTabChange(activeTab.value);
  }

  // 导出数据
  async function exportData() {
    try {
      SmartLoading.show();
      const res = await inventoryReportApi.exportInventory(queryForm);
      
      // 创建下载链接
      const blob = new Blob([res], { type: 'application/vnd.ms-excel' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `库存报表_${new Date().getTime()}.xlsx`;
      link.click();
      window.URL.revokeObjectURL(url);
      
      message.success('导出成功');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  onMounted(() => {
    queryOverview();
    queryData();
  });
</script>

<style scoped lang="less">
  .inventory-report-container {
    padding: 10px;
  }
</style>
