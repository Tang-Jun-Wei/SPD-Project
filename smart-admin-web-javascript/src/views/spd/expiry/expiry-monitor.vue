<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
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
            <a-select-option v-for="item in warehouseList" :key="item.nodeId" :value="item.nodeId">
              {{ item.nodeName }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="库存类型" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.stockType"
            placeholder="请选择库存类型"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="1">定数包</a-select-option>
            <a-select-option :value="2">散货</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="预警级别" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.warningLevel"
            placeholder="请选择预警级别"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="4">已过期</a-select-option>
            <a-select-option :value="1">30天内到期</a-select-option>
            <a-select-option :value="2">60天内到期</a-select-option>
            <a-select-option :value="3">90天内到期</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="批号" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.batchNo"
            placeholder="请输入批号"
            style="width: 200px"
            allowClear
          />
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
        </a-form-item>
      </a-row>
    </a-form>

    <!-- 统计卡片 -->
    <a-row :gutter="16" style="margin-bottom: 16px">
      <a-col :span="6">
        <a-card size="small">
          <a-statistic
            title="已过期"
            :value="statistics.expired"
            :value-style="{ color: '#cf1322' }"
          >
            <template #prefix>
              <WarningOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card size="small">
          <a-statistic
            title="30天内到期"
            :value="statistics.days30"
            :value-style="{ color: '#fa8c16' }"
          >
            <template #prefix>
              <ClockCircleOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card size="small">
          <a-statistic
            title="60天内到期"
            :value="statistics.days60"
            :value-style="{ color: '#faad14' }"
          />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card size="small">
          <a-statistic
            title="90天内到期"
            :value="statistics.days90"
            :value-style="{ color: '#52c41a' }"
          />
        </a-card>
      </a-col>
    </a-row>

    <!-- 操作按钮和表格 -->
    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-setting-block">
          <a-button @click="queryData">
            <template #icon>
              <ReloadOutlined />
            </template>
          </a-button>
        </div>
      </a-row>

      <!-- 表格 -->
      <a-table
        size="small"
        :scroll="{ x: 2000 }"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="labelId"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'stockType'">
            <a-tag v-if="record.stockType === 1" color="blue">定数包</a-tag>
            <a-tag v-else color="cyan">散货</a-tag>
          </template>

          <template v-if="column.dataIndex === 'warningLevel'">
            <a-tag v-if="record.warningLevel === 4" color="red">已过期</a-tag>
            <a-tag v-else-if="record.warningLevel === 1" color="orange">30天内到期</a-tag>
            <a-tag v-else-if="record.warningLevel === 2" color="gold">60天内到期</a-tag>
            <a-tag v-else-if="record.warningLevel === 3" color="green">90天内到期</a-tag>
          </template>

          <template v-if="column.dataIndex === 'remainingDays'">
            <span :style="{ color: record.remainingDays < 0 ? '#cf1322' : record.remainingDays <= 30 ? '#fa8c16' : '#000' }">
              {{ record.remainingDays < 0 ? '已过期' : `${record.remainingDays}天` }}
            </span>
          </template>
        </template>
      </a-table>

      <!-- 分页 -->
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
    </a-card>
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    WarningOutlined,
    ClockCircleOutlined,
  } from '@ant-design/icons-vue';
  import { expiryMonitorApi } from '/@/api/spd/expiry-monitor-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';

  // 查询表单
  const queryForm = reactive({
    materialName: '',
    warehouseId: undefined,
    stockType: undefined,
    warningLevel: undefined,
    batchNo: '',
    pageNum: 1,
    pageSize: 10,
  });

  // 表格数据
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const warehouseList = ref([]);

  // 统计数据
  const statistics = reactive({
    expired: 0,
    days30: 0,
    days60: 0,
    days90: 0,
  });

  // 表格列定义
  const columns = ref([
    {
      title: '库存类型',
      dataIndex: 'stockType',
      width: 100,
      align: 'center',
    },
    {
      title: '耗材名称',
      dataIndex: 'materialName',
      width: 200,
      ellipsis: true,
    },
    {
      title: '规格型号',
      dataIndex: 'specification',
      width: 150,
      ellipsis: true,
    },
    {
      title: '仓库',
      dataIndex: 'warehouseName',
      width: 120,
    },
    {
      title: '批号',
      dataIndex: 'batchNo',
      width: 150,
    },
    {
      title: '生产日期',
      dataIndex: 'productionDate',
      width: 120,
    },
    {
      title: '有效期至',
      dataIndex: 'expiryDate',
      width: 120,
    },
    {
      title: '剩余天数',
      dataIndex: 'remainingDays',
      width: 120,
      align: 'center',
    },
    {
      title: '预警级别',
      dataIndex: 'warningLevel',
      width: 120,
      align: 'center',
    },
    {
      title: '库存数量',
      dataIndex: 'stockQuantity',
      width: 100,
      align: 'center',
    },
    {
      title: '供应商',
      dataIndex: 'supplierName',
      width: 150,
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 180,
    },
  ]);

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      const res = await expiryMonitorApi.queryPage(queryForm);
      if (res.data) {
        tableData.value = res.data.list || [];
        total.value = res.data.total || 0;
        
        // 统计预警级别
        const data = res.data.list || [];
        statistics.expired = data.filter(item => item.warningLevel === 4).length;
        statistics.days30 = data.filter(item => item.warningLevel === 1).length;
        statistics.days60 = data.filter(item => item.warningLevel === 2).length;
        statistics.days90 = data.filter(item => item.warningLevel === 3).length;
      }
    } catch (error) {
      smartSentry.captureError(error);
      message.error('查询失败');
    } finally {
      tableLoading.value = false;
    }
  }

  // 重置查询
  function resetQuery() {
    queryForm.materialName = '';
    queryForm.warehouseId = undefined;
    queryForm.stockType = undefined;
    queryForm.warningLevel = undefined;
    queryForm.batchNo = '';
    queryForm.pageNum = 1;
    queryData();
  }

  // 加载仓库列表
  async function loadWarehouseList() {
    try {
      const res = await orgNodeApi.queryByType({ nodeType: 2 });
      if (res.data) {
        warehouseList.value = res.data || [];
      }
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  onMounted(() => {
    queryData();
    loadWarehouseList();
  });
</script>

<style scoped lang="less"></style>
