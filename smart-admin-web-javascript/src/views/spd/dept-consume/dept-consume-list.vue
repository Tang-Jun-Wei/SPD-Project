<template>
  <a-card size="small" :bordered="false" :loading="tableLoading">
    <!-- 查询条件 -->
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row" :gutter="[10, 0]">
        <a-col :span="6">
          <a-form-item label="消耗单号" class="smart-query-form-item">
            <a-input v-model:value="queryForm.consumeNo" placeholder="消耗单号" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="消耗类型" class="smart-query-form-item">
            <a-select v-model:value="queryForm.consumeType" placeholder="请选择" allowClear>
              <a-select-option :value="1">正常消耗</a-select-option>
              <a-select-option :value="2">手术消耗</a-select-option>
              <a-select-option :value="3">急诊消耗</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="状态" class="smart-query-form-item">
            <a-select v-model:value="queryForm.status" placeholder="请选择" allowClear>
              <a-select-option :value="1">正常</a-select-option>
              <a-select-option :value="2">已反消耗</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="患者姓名" class="smart-query-form-item">
            <a-input v-model:value="queryForm.patientName" placeholder="患者姓名" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="消耗时间" class="smart-query-form-item">
            <a-range-picker
              v-model:value="dateRange"
              :show-time="{ format: 'HH:mm:ss' }"
              format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
              @change="onDateChange"
            />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item class="smart-query-form-item">
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
        </a-col>
      </a-row>
    </a-form>

    <!-- 表格操作按钮 -->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="showConsumeModal" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新增消耗
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
      </div>
    </a-row>

    <!-- 数据表格 -->
    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="consumeId"
      :loading="tableLoading"
      :pagination="false"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'consumeType'">
          <a-tag v-if="record.consumeType === 1" color="blue">正常消耗</a-tag>
          <a-tag v-else-if="record.consumeType === 2" color="green">手术消耗</a-tag>
          <a-tag v-else-if="record.consumeType === 3" color="red">急诊消耗</a-tag>
        </template>
        <template v-if="column.dataIndex === 'status'">
          <a-tag v-if="record.status === 1" color="green">正常</a-tag>
          <a-tag v-else-if="record.status === 2" color="orange">已反消耗</a-tag>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-space>
            <a-button @click="showDetail(record.consumeId)" size="small" type="link">详情</a-button>
            <a-button @click="showReverseModal(record)" v-if="record.status === 1" size="small" type="link" danger>反消耗</a-button>
            <a-button @click="deleteConsume(record.consumeId)" size="small" type="link" danger>删除</a-button>
          </a-space>
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

  <!-- 消耗表单弹窗 -->
  <ConsumeFormModal ref="consumeFormModalRef" @reloadList="queryData" />

  <!-- 详情抽屉 -->
  <DetailDrawer ref="detailDrawerRef" />

  <!-- 反消耗弹窗 -->
  <ReverseModal ref="reverseModalRef" @reloadList="queryData" />
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { deptConsumeApi } from '/@/api/spd/dept-consume-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { smartSentry } from '/@/lib/smart-sentry';
import TableOperator from '/@/components/framework/table-operator/index.vue';
import ConsumeFormModal from './components/consume-form-modal.vue';
import DetailDrawer from './components/detail-drawer.vue';
import ReverseModal from './components/reverse-modal.vue';

// 查询表单
const queryForm = reactive({
  consumeNo: '',
  consumeType: undefined,
  status: undefined,
  patientName: '',
  startTime: null,
  endTime: null,
  pageNum: 1,
  pageSize: 10,
});

// 日期范围
const dateRange = ref([]);

// 日期范围变化
const onDateChange = (dates) => {
  if (dates && dates.length === 2) {
    queryForm.startTime = dates[0];
    queryForm.endTime = dates[1];
  } else {
    queryForm.startTime = null;
    queryForm.endTime = null;
  }
};

// 表格数据
const tableData = ref([]);
const total = ref(0);
const tableLoading = ref(false);

// 表格列定义
const columns = ref([
  {
    title: '消耗单号',
    dataIndex: 'consumeNo',
    width: 150,
  },
  {
    title: '科室',
    dataIndex: 'deptName',
    width: 120,
  },
  {
    title: '消耗类型',
    dataIndex: 'consumeType',
    width: 100,
  },
  {
    title: '消耗日期',
    dataIndex: 'consumeDate',
    width: 150,
  },
  {
    title: '消耗人',
    dataIndex: 'consumeUserName',
    width: 100,
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
  },
  {
    title: '患者编号',
    dataIndex: 'patientNo',
    width: 120,
  },
  {
    title: '消耗总数量',
    dataIndex: 'totalQuantity',
    width: 100,
  },
  {
    title: '消耗总金额',
    dataIndex: 'totalAmount',
    width: 100,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150,
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 150,
  },
]);

// 查询数据
async function queryData() {
  try {
    tableLoading.value = true;
    const res = await deptConsumeApi.queryPage(queryForm);
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
    consumeNo: '',
    consumeType: undefined,
    status: undefined,
    patientName: '',
    startTime: null,
    endTime: null,
    pageNum: 1,
    pageSize: 10,
  });
  dateRange.value = [];
  queryData();
}

// 消耗表单弹窗
const consumeFormModalRef = ref();
function showConsumeModal() {
  consumeFormModalRef.value.show();
}

// 详情抽屉
const detailDrawerRef = ref();
function showDetail(consumeId) {
  detailDrawerRef.value.show(consumeId);
}

// 反消耗弹窗
const reverseModalRef = ref();
function showReverseModal(record) {
  reverseModalRef.value.show(record);
}

// 删除
function deleteConsume(consumeId) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除该消耗单吗？',
    okText: '确定',
    okType: 'danger',
    async onOk() {
      try {
        await deptConsumeApi.delete(consumeId);
        message.success('删除成功');
        queryData();
      } catch (error) {
        smartSentry.captureError(error);
      }
    },
  });
}

onMounted(() => {
  queryData();
});
</script>

<style scoped lang="less"></style>
