<template>
  <div class="smart-table-container">
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="验收单号" class="smart-query-form-item">
          <a-input v-model:value="queryForm.acceptanceNo" placeholder="验收单号" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="仓库" class="smart-query-form-item">
          <a-select v-model:value="queryForm.warehouseId" placeholder="请选择" style="width:200px" allowClear>
            <a-select-option v-for="item in warehouseOptions" :key="item.nodeId" :value="item.nodeId">{{ item.nodeName }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select v-model:value="queryForm.acceptanceStatus" placeholder="请选择" style="width:150px" allowClear>
            <a-select-option :value="0">草稿</a-select-option>
            <a-select-option :value="1">待验收</a-select-option>
            <a-select-option :value="2">已验收</a-select-option>
            <a-select-option :value="9">已拒绝</a-select-option>
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
          <template v-if="column.dataIndex === 'acceptanceStatus'">
            <a-tag v-if="text === 0" color="default">草稿</a-tag>
            <a-tag v-else-if="text === 1" color="orange">待验收</a-tag>
            <a-tag v-else-if="text === 2" color="green">已验收</a-tag>
            <a-tag v-else-if="text === 9" color="red">已拒绝</a-tag>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button v-if="record.acceptanceStatus === 0 || record.acceptanceStatus === 1" type="link" size="small" @click="showModal(record)">编辑</a-button>
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

    <AcceptanceFormModal ref="formModal" @reloadList="queryData" />
    <AcceptanceDetailDrawer ref="detailDrawer" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { acceptanceApi } from '/@/api/spd/acceptance-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import AcceptanceFormModal from './components/acceptance-form-modal.vue';
  import AcceptanceDetailDrawer from './components/acceptance-detail-drawer.vue';

  const queryForm = reactive({ acceptanceNo: '', warehouseId: undefined, acceptanceStatus: undefined, pageNum: 1, pageSize: 10 });
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const selectedRowKeys = ref([]);
  const warehouseOptions = ref([]);

  const columns = ref([
    { title: '验收单号', dataIndex: 'acceptanceNo', width: 180, fixed: 'left' },
    { title: '采购单号', dataIndex: 'purchaseNo', width: 160 },
    { title: '仓库', dataIndex: 'warehouseName', width: 150 },
    { title: '验收人', dataIndex: 'acceptanceByName', width: 100 },
    { title: '验收日期', dataIndex: 'acceptanceDate', width: 160 },
    { title: '状态', dataIndex: 'acceptanceStatus', width: 100 },
    { title: '总金额', dataIndex: 'totalAmount', width: 120 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 180 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await acceptanceApi.queryPage(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  async function loadWarehouseOptions() {
    try {
      const res = await orgNodeApi.queryWarehouseList();
      warehouseOptions.value = res.data || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  function resetQuery() {
    Object.assign(queryForm, { acceptanceNo: '', warehouseId: undefined, acceptanceStatus: undefined, pageNum: 1 });
    queryData();
  }

  const onSelectChange = (keys) => (selectedRowKeys.value = keys);
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  const detailDrawer = ref();
  const showDetail = (record) => detailDrawer.value.showDrawer(record.acceptanceId);

  async function singleDelete(record) {
    const loading = SmartLoading.show();
    try {
      await acceptanceApi.delete(record.acceptanceId);
      message.success('删除成功');
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide(loading);
    }
  }

  async function batchDelete() {
    if (!selectedRowKeys.value.length) return message.warning('请选择要删除的数据');
    const loading = SmartLoading.show();
    try {
      const records = tableData.value.filter(item => selectedRowKeys.value.includes(item.id));
      for (const record of records) {
        await acceptanceApi.delete(record.acceptanceId);
      }
      message.success('删除成功');
      selectedRowKeys.value = [];
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide(loading);
    }
  }

  onMounted(() => {
    loadWarehouseOptions();
    queryData();
  });
</script>

<style scoped lang="less">
  .smart-table-container {
    padding: 10px;
  }
</style>
