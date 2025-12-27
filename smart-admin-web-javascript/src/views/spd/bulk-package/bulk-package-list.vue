<template>
  <a-card size="small" :bordered="false" :loading="tableLoading">
    <a-form class="smart-query-form">
      <a-row :gutter="[10, 0]">
        <a-col :span="6">
          <a-form-item label="打包单号">
            <a-input v-model:value="queryForm.packageNo" placeholder="打包单号" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label="耗材名称">
            <a-input v-model:value="queryForm.materialName" placeholder="耗材名称" />
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item>
            <a-button type="primary" @click="queryData">
              <SearchOutlined />
              查询
            </a-button>
            <a-button class="smart-margin-left10" @click="resetQuery">
              <ReloadOutlined />
              重置
            </a-button>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>

    <a-row class="smart-table-btn-block">
      <a-button @click="showAddModal" type="primary" size="small">
        <PlusOutlined />
        新增打包
      </a-button>
    </a-row>

    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="packageId"
      :loading="tableLoading"
      :pagination="false"
      bordered
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <a-tag v-if="record.status === 1" color="blue">待审核</a-tag>
          <a-tag v-else-if="record.status === 2" color="green">已审核</a-tag>
          <a-tag v-else color="red">已驳回</a-tag>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-button @click="showDetail(record.packageId)" size="small" type="link">详情</a-button>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="queryData"
        show-size-changer
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { bulkPackageApi } from '/@/api/spd/bulk-package-api';
import { smartSentry } from '/@/lib/smart-sentry';

const queryForm = reactive({
  packageNo: '',
  materialName: '',
  pageNum: 1,
  pageSize: 10,
});

const tableData = ref([]);
const total = ref(0);
const tableLoading = ref(false);

const columns = [
  { title: '打包单号', dataIndex: 'packageNo', width: 150 },
  { title: '耗材名称', dataIndex: 'materialName', width: 150 },
  { title: '规格型号', dataIndex: 'specification', width: 120 },
  { title: '批号', dataIndex: 'batchNo', width: 100 },
  { title: '定数包数量', dataIndex: 'packageQuantity', width: 100 },
  { title: '单包数量', dataIndex: 'unitQuantity', width: 100 },
  { title: '总消耗数量', dataIndex: 'totalQuantity', width: 100 },
  { title: '状态', dataIndex: 'status', width: 100 },
  { title: '创建人', dataIndex: 'createUserName', width: 100 },
  { title: '创建时间', dataIndex: 'createTime', width: 150 },
  { title: '操作', dataIndex: 'action', fixed: 'right', width: 100 },
];

async function queryData() {
  try {
    tableLoading.value = true;
    const res = await bulkPackageApi.queryPage(queryForm);
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

function resetQuery() {
  Object.assign(queryForm, { packageNo: '', materialName: '', pageNum: 1, pageSize: 10 });
  queryData();
}

function showAddModal() {
  // TODO: 实现新增弹窗
}

function showDetail(packageId) {
  // TODO: 实现详情抽屉
}

onMounted(() => queryData());
</script>
