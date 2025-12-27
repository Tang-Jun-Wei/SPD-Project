<template>
  <div class="smart-table-container">
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="批号" class="smart-query-form-item">
          <a-input v-model:value="queryForm.batchNo" placeholder="批号" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="耗材名称" class="smart-query-form-item">
          <a-input v-model:value="queryForm.materialName" placeholder="耗材名称" style="width:200px" allowClear />
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
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增批号</a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData"><ReloadOutlined /></a-button>
        </div>
      </a-row>

      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="batchId"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showModal(record)">编辑</a-button>
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

    <BatchFormModal ref="formModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import { batchApi } from '/@/api/spd/batch-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import BatchFormModal from './components/batch-form-modal.vue';

  const queryForm = reactive({ batchNo: '', materialName: '', pageNum: 1, pageSize: 10 });
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  const columns = ref([
    { title: '批号', dataIndex: 'batchNo', width: 150 },
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '规格', dataIndex: 'specification', width: 150 },
    { title: '有效期', dataIndex: 'validDate', width: 120 },
    { title: '供应商', dataIndex: 'supplierName', width: 150 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 150 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await batchApi.queryPage(queryForm);
      tableData.value = res.data.list || [];
      total.value = res.data.total || 0;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  function resetQuery() {
    Object.assign(queryForm, { batchNo: '', materialName: '', pageNum: 1 });
    queryData();
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: `确定删除批号"${record.batchNo}"吗？`,
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await batchApi.delete(record.batchId);
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
