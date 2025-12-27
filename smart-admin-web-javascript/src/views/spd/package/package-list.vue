<template>
  <div class="smart-table-container">
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="打包单号" class="smart-query-form-item">
          <a-input v-model:value="queryForm.packageCode" placeholder="打包单号" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="仓库" class="smart-query-form-item">
          <a-select v-model:value="queryForm.warehouseId" placeholder="请选择" style="width:200px" allowClear>
            <a-select-option v-for="item in warehouseOptions" :key="item.nodeId" :value="item.nodeId">{{ item.nodeName }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select v-model:value="queryForm.packageStatus" placeholder="请选择" style="width:150px" allowClear>
            <a-select-option :value="0">待打包</a-select-option>
            <a-select-option :value="1">已打包</a-select-option>
            <a-select-option :value="2">已作废</a-select-option>
            <a-select-option :value="3">待上架</a-select-option>
            <a-select-option :value="4">已上架</a-select-option>
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
          <template v-if="column.dataIndex === 'packageStatus'">
            <a-tag v-if="text === 0" color="blue">待打包</a-tag>
            <a-tag v-else-if="text === 1" color="green">已打包</a-tag>
            <a-tag v-else-if="text === 2" color="red">已作废</a-tag>
            <a-tag v-else-if="text === 3" color="orange">待上架</a-tag>
            <a-tag v-else-if="text === 4" color="purple">已上架</a-tag>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button v-if="record.packageStatus === 0 || record.packageStatus === 3" type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button v-if="record.packageStatus === 0" type="link" size="small" @click="updateStatus(record, 1)">确认打包</a-button>
              <a-button v-if="record.packageStatus === 3" type="link" size="small" @click="updateStatus(record, 4)">确认上架</a-button>
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

    <PackageFormModal ref="formModal" @reloadList="queryData" />
    <PackageDetailDrawer ref="detailDrawer" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { packageApi } from '/@/api/spd/package-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import PackageFormModal from './components/package-form-modal.vue';
  import PackageDetailDrawer from './components/package-detail-drawer.vue';

  const queryForm = reactive({ packageCode: '', warehouseId: undefined, packageStatus: undefined, pageNum: 1, pageSize: 10 });
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const selectedRowKeys = ref([]);
  const warehouseOptions = ref([]);

  const columns = ref([
    { title: '打包单号', dataIndex: 'packageCode', width: 180, fixed: 'left' },
    { title: '业务ID', dataIndex: 'packageId', width: 160 },
    { title: '仓库', dataIndex: 'warehouseName', width: 150 },
    { title: '打包状态', dataIndex: 'packageStatus', width: 100 },
    { title: '创建人', dataIndex: 'createUserName', width: 100 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '备注', dataIndex: 'remark', width: 200, ellipsis: true },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 260 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await packageApi.queryPage(queryForm);
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
    Object.assign(queryForm, { packageCode: '', warehouseId: undefined, packageStatus: undefined, pageNum: 1 });
    queryData();
  }

  const onSelectChange = (keys) => (selectedRowKeys.value = keys);
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  const detailDrawer = ref();
  const showDetail = (record) => detailDrawer.value.showDrawer(record.packageId);

  async function updateStatus(record, status) {
    const loading = SmartLoading.show();
    try {
      await packageApi.updateStatus({ packageId: record.packageId, packageStatus: status });
      message.success('状态更新成功');
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide(loading);
    }
  }

  async function singleDelete(record) {
    const loading = SmartLoading.show();
    try {
      await packageApi.delete(record.packageId);
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
        await packageApi.delete(record.packageId);
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
