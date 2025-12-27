<template>
  <div class="smart-table-container">
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="收货单号" class="smart-query-form-item">
          <a-input v-model:value="queryForm.receiveNo" placeholder="收货单号" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="供应商" class="smart-query-form-item">
          <a-select v-model:value="queryForm.supplierId" placeholder="请选择" style="width:200px" allowClear />
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
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData"><ReloadOutlined /></a-button>
        </div>
      </a-row>

      <a-table size="small" :dataSource="tableData" :columns="columns" :loading="tableLoading" :pagination="false" rowKey="id" bordered>
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button danger type="link" size="small" @click="singleDelete(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination showSizeChanger :pageSizeOptions="PAGE_SIZE_OPTIONS" v-model:current="queryForm.pageNum" v-model:pageSize="queryForm.pageSize" :total="total" @change="queryData" :show-total="t => `共${t}条`" />
      </div>
    </a-card>

    <ReceiveFormModal ref="formModal" @reloadList="queryData" />
    <ReceiveDetailModal ref="detailModal" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import { receiveApi } from '/@/api/spd/receive-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import ReceiveFormModal from './components/receive-form-modal.vue';
  import ReceiveDetailModal from './components/receive-detail-modal.vue';

  const queryForm = reactive({ receiveNo: '', supplierId: undefined, pageNum: 1, pageSize: 10 });
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const columns = ref([
    { title: '收货单号', dataIndex: 'receiveNo', width: 180, fixed: 'left' },
    { title: '采购单号', dataIndex: 'purchaseNo', width: 180 },
    { title: '供应商', dataIndex: 'supplierName', width: 150 },
    { title: '收货人', dataIndex: 'receiveUserName', width: 100 },
    { title: '收货时间', dataIndex: 'receiveTime', width: 160 },
    { title: '仓库', dataIndex: 'warehouseName', width: 120 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 180 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await receiveApi.queryPage(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  function resetQuery() {
    Object.assign(queryForm, { receiveNo: '', supplierId: undefined, pageNum: 1 });
    queryData();
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);
  const detailModal = ref();
  const showDetail = (record) => detailModal.value.showModal(record.id);

  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: '确定删除该收货单吗？',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await receiveApi.delete(record.id);
          message.success('删除成功');
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
