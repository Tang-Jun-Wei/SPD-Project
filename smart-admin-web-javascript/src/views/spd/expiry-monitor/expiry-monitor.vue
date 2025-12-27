<template>
  <a-card size="small" :bordered="false" :loading="loading">
    <!-- 统计卡片 -->
    <a-row :gutter="16" style="margin-bottom: 20px">
      <a-col :span="6">
        <a-card size="small" class="stat-card stat-card-expired">
          <a-statistic title="已过期" :value="statistics.expiredCount || 0" suffix="项">
            <template #prefix>
              <CloseCircleOutlined style="color: #ff4d4f" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card size="small" class="stat-card stat-card-soon">
          <a-statistic title="即将过期（30天内）" :value="statistics.soonExpireCount || 0" suffix="项">
            <template #prefix>
              <WarningOutlined style="color: #ff7a45" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card size="small" class="stat-card stat-card-near">
          <a-statistic title="临期（60天内）" :value="statistics.nearExpireCount || 0" suffix="项">
            <template #prefix>
              <ExclamationCircleOutlined style="color: #faad14" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card size="small" class="stat-card stat-card-normal">
          <a-statistic title="正常" :value="statistics.normalCount || 0" suffix="项">
            <template #prefix>
              <CheckCircleOutlined style="color: #52c41a" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
    </a-row>

    <!-- 查询条件 -->
    <a-form :model="queryForm" layout="inline" class="smart-query-form">
      <a-form-item label="耗材名称">
        <a-input v-model:value="queryForm.materialName" placeholder="请输入耗材名称" style="width: 200px" />
      </a-form-item>
      <a-form-item label="批号">
        <a-input v-model:value="queryForm.batchNo" placeholder="请输入批号" style="width: 150px" />
      </a-form-item>
      <a-form-item label="预警级别">
        <a-select v-model:value="queryForm.warningLevel" placeholder="请选择" style="width: 150px" allowClear>
          <a-select-option :value="4">已过期</a-select-option>
          <a-select-option :value="3">即将过期</a-select-option>
          <a-select-option :value="2">临期</a-select-option>
          <a-select-option :value="1">正常</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="queryData">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button class="smart-margin-left10" @click="resetQuery">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-form>

    <!-- 数据表格 -->
    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="stockId"
      :loading="tableLoading"
      :pagination="false"
      bordered
      style="margin-top: 10px"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'warningLevel'">
          <a-tag v-if="record.warningLevel === 4" color="red">已过期</a-tag>
          <a-tag v-else-if="record.warningLevel === 3" color="orange">即将过期</a-tag>
          <a-tag v-else-if="record.warningLevel === 2" color="gold">临期</a-tag>
          <a-tag v-else color="green">正常</a-tag>
        </template>
        <template v-if="column.dataIndex === 'remainDays'">
          <span :style="{ color: record.remainDays <= 0 ? '#ff4d4f' : record.remainDays <= 30 ? '#ff7a45' : '#666' }">
            {{ record.remainDays }}天
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
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import {
  SearchOutlined,
  ReloadOutlined,
  CloseCircleOutlined,
  WarningOutlined,
  ExclamationCircleOutlined,
  CheckCircleOutlined,
} from '@ant-design/icons-vue';
import { expiryApi } from '/@/api/spd/expiry-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { smartSentry } from '/@/lib/smart-sentry';

// 加载状态
const loading = ref(false);
const tableLoading = ref(false);

// 统计数据
const statistics = reactive({
  expiredCount: 0,
  soonExpireCount: 0,
  nearExpireCount: 0,
  normalCount: 0,
  totalCount: 0,
});

// 查询表单
const queryForm = reactive({
  materialName: '',
  batchNo: '',
  warningLevel: undefined,
  pageNum: 1,
  pageSize: 10,
});

// 表格数据
const tableData = ref([]);
const total = ref(0);

// 表格列定义
const columns = ref([
  {
    title: '耗材编码',
    dataIndex: 'materialCode',
    width: 120,
  },
  {
    title: '耗材名称',
    dataIndex: 'materialName',
    width: 150,
  },
  {
    title: '规格型号',
    dataIndex: 'specification',
    width: 120,
  },
  {
    title: '批号',
    dataIndex: 'batchNo',
    width: 100,
  },
  {
    title: '效期',
    dataIndex: 'expiryDate',
    width: 100,
  },
  {
    title: '剩余天数',
    dataIndex: 'remainDays',
    width: 100,
  },
  {
    title: '预警级别',
    dataIndex: 'warningLevel',
    width: 100,
  },
  {
    title: '库存数量',
    dataIndex: 'quantity',
    width: 100,
  },
  {
    title: '仓库',
    dataIndex: 'warehouseName',
    width: 120,
  },
  {
    title: '货位',
    dataIndex: 'locationName',
    width: 120,
  },
  {
    title: '生产厂家',
    dataIndex: 'manufacturer',
    width: 150,
  },
]);

// 查询统计
async function queryStatistics() {
  try {
    loading.value = true;
    const res = await expiryApi.getStatistics();
    if (res.data) {
      Object.assign(statistics, res.data);
    }
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
}

// 查询列表数据
async function queryData() {
  try {
    tableLoading.value = true;
    const res = await expiryApi.queryWarningPage(queryForm);
    if (res.data) {
      tableData.value = res.data.list;
      total.value = res.data.total;
    }
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    tableLoading.value = false;
  }
}

// 重置查询
function resetQuery() {
  Object.assign(queryForm, {
    materialName: '',
    batchNo: '',
    warningLevel: undefined,
    pageNum: 1,
    pageSize: 10,
  });
  queryData();
}

onMounted(() => {
  queryStatistics();
  queryData();
});
</script>

<style scoped lang="less">
.stat-card {
  border-radius: 4px;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  }

  :deep(.ant-statistic-title) {
    font-size: 14px;
    color: #666;
  }

  :deep(.ant-statistic-content) {
    font-size: 24px;
    font-weight: bold;
  }
}

.stat-card-expired {
  border-left: 4px solid #ff4d4f;
}

.stat-card-soon {
  border-left: 4px solid #ff7a45;
}

.stat-card-near {
  border-left: 4px solid #faad14;
}

.stat-card-normal {
  border-left: 4px solid #52c41a;
}
</style>
