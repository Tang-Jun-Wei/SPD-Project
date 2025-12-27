<template>
  <div class="smart-table-container">
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="耗材名称" class="smart-query-form-item">
          <a-input v-model:value="queryForm.materialName" placeholder="耗材名称" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="规格型号" class="smart-query-form-item">
          <a-input v-model:value="queryForm.spec" placeholder="规格型号" style="width:200px" allowClear />
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
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增耗材</a-button>
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
        rowKey="materialId"
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

    <MaterialFormModal ref="formModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import { materialApi } from '/@/api/spd/material-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import MaterialFormModal from './components/material-form-modal.vue';

  const queryForm = reactive({ materialName: '', spec: '', pageNum: 1, pageSize: 10 });
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  const columns = ref([
    { title: '耗材ID', dataIndex: 'materialId', width: 120 },
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '耗材编码', dataIndex: 'materialCode', width: 120 },
    { title: '规格型号', dataIndex: 'specification', width: 150 },
    { title: '单位', dataIndex: 'unit', width: 80 },
    { title: '生产厂家', dataIndex: 'manufacturer', width: 150 },
    { title: '供应商', dataIndex: 'supplierName', width: 150 },
    { title: '单价（元）', dataIndex: 'unitPrice', width: 100 },
    { title: '状态', dataIndex: 'materialStatus', width: 80 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 150 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await materialApi.queryPage(queryForm);
      tableData.value = res.data.list || [];
      total.value = res.data.total || 0;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  function resetQuery() {
    Object.assign(queryForm, { materialName: '', spec: '', pageNum: 1 });
    queryData();
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: `确定删除耗材"${record.materialName}"吗？`,
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await materialApi.delete(record.materialId);
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
