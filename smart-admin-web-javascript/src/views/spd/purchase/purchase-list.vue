<template>
  <div class="smart-table-container">
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="采购单号" class="smart-query-form-item">
          <a-input v-model:value="queryForm.purchaseNo" placeholder="采购单号" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="供应商" class="smart-query-form-item">
          <a-select v-model:value="queryForm.supplierId" placeholder="请选择" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select v-model:value="queryForm.purchaseStatus" placeholder="请选择" style="width:150px" allowClear>
            <a-select-option :value="1">待审核</a-select-option>
            <a-select-option :value="2">已审核</a-select-option>
            <a-select-option :value="3">已驳回</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button-group>
            <a-button type="primary" @click="queryData"><SearchOutlined />查询</a-button>
            <a-button @click="resetQuery"><ReloadOutlined />重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增</a-button>
          <a-button type="primary" danger @click="batchDelete" :disabled="!hasSelected"><DeleteOutlined />批量删除</a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData"><ReloadOutlined /></a-button>
        </div>
      </a-row>

      <a-table
        :row-selection="{ selectedRowKeys, onChange: onSelectChange }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'purchaseStatus'">
            <a-tag v-if="text === 1" color="orange">待审核</a-tag>
            <a-tag v-else-if="text === 2" color="green">已审核</a-tag>
            <a-tag v-else-if="text === 3" color="red">已驳回</a-tag>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button v-if="record.purchaseStatus === 1" type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button v-if="record.purchaseStatus === 1" type="link" size="small" @click="showApproveModal(record)">审核</a-button>
              <a-button danger type="link" size="small" @click="singleDelete(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          :show-total="t => `共${t}条`"
        />
      </div>
    </a-card>

    <PurchaseFormModal ref="formModal" @reloadList="queryData" />
    <PurchaseApproveModal ref="approveModal" @reloadList="queryData" />
    <PurchaseDetailModal ref="detailModal" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { purchaseApi } from '/@/api/spd/purchase-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import PurchaseFormModal from './components/purchase-form-modal.vue';
  import PurchaseApproveModal from './components/purchase-approve-modal.vue';
  import PurchaseDetailModal from './components/purchase-detail-modal.vue';

  const queryForm = reactive({ purchaseNo: '', supplierId: undefined, purchaseStatus: undefined, pageNum: 1, pageSize: 10 });
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const selectedRowKeys = ref([]);

  const columns = ref([
    { title: '采购单号', dataIndex: 'purchaseNo', width: 180, fixed: 'left' },
    { title: '供应商', dataIndex: 'supplierName', width: 150 },
    { title: '采购人', dataIndex: 'purchaseUserName', width: 100 },
    { title: '采购时间', dataIndex: 'purchaseTime', width: 160 },
    { title: '状态', dataIndex: 'purchaseStatus', width: 100 },
    { title: '总金额', dataIndex: 'totalAmount', width: 120 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 200 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await purchaseApi.queryPage(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  function resetQuery() {
    Object.assign(queryForm, { purchaseNo: '', supplierId: undefined, purchaseStatus: undefined, pageNum: 1 });
    queryData();
  }

  const onSelectChange = (keys) => (selectedRowKeys.value = keys);
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  const approveModal = ref();
  const showApproveModal = (record) => approveModal.value.showModal(record);

  const detailModal = ref();
  const showDetail = (record) => detailModal.value.showModal(record.id);

  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: '确定删除该采购单吗？',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await purchaseApi.delete(record.id);
          message.success('删除成功');
          queryData();
        } finally {
          SmartLoading.hide();
        }
      },
    });
  }

  function batchDelete() {
    if (!selectedRowKeys.value.length) return message.warning('请选择要删除的数据');
    Modal.confirm({
      title: '确认删除',
      content: `确定删除选中的 ${selectedRowKeys.value.length} 条数据吗？`,
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          for (const id of selectedRowKeys.value) await purchaseApi.delete(id);
          message.success('删除成功');
          selectedRowKeys.value = [];
          queryData();
        } finally {
          SmartLoading.hide();
        }
      },
    });
  }

  onMounted(queryData);
</script>

<style scoped lang="less">
  .smart-table-container {
    padding: 10px;
  }
</style>
