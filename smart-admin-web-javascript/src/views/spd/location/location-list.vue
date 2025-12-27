<template>
  <div class="smart-table-container">
    <a-card size="small" :bordered="false">
      <a-form class="smart-query-form" layout="inline">
        <a-form-item label="货位编码">
          <a-input v-model:value="queryForm.locationCode" placeholder="请输入货位编码" style="width: 200px" />
        </a-form-item>
        <a-form-item label="货位名称">
          <a-input v-model:value="queryForm.locationName" placeholder="请输入货位名称" style="width: 200px" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="queryForm.locationStatus" placeholder="请选择状态" style="width: 120px" allowClear>
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="queryData"><SearchOutlined />查询</a-button>
          <a-button @click="resetQuery"><ReloadOutlined />重置</a-button>
        </a-form-item>
      </a-form>

      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增货位</a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData"><ReloadOutlined />刷新</a-button>
        </div>
      </a-row>

      <a-table
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'locationStatus'">
            <a-tag v-if="text === 1" color="green">启用</a-tag>
            <a-tag v-else color="default">禁用</a-tag>
          </template>
          <template v-if="column.dataIndex === 'locationType'">
            <span v-if="text === 1">常温</span>
            <span v-if="text === 2">阴凉</span>
            <span v-if="text === 3">冷藏</span>
            <span v-if="text === 4">冷冻</span>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button v-if="record.locationStatus === 1" type="link" size="small" @click="updateStatus(record, 0)">禁用</a-button>
              <a-button v-else type="link" size="small" @click="updateStatus(record, 1)">启用</a-button>
              <a-button danger type="link" size="small" @click="deleteLocation(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          :show-total="(total) => `共 ${total} 条`"
          :show-size-changer="true"
          @change="queryData"
        />
      </div>
    </a-card>

    <LocationFormModal ref="formModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined, ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue';
  import { locationApi } from '/@/api/spd/location-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import LocationFormModal from './components/location-form-modal.vue';

  const queryForm = reactive({
    locationCode: '',
    locationName: '',
    locationStatus: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  const columns = ref([
    { title: '货位编码', dataIndex: 'locationCode', width: 150 },
    { title: '货位名称', dataIndex: 'locationName', width: 200 },
    { title: '关联节点', dataIndex: 'nodeName', width: 150 },
    { title: '货位层级', dataIndex: 'locationLevel', width: 100 },
    { title: '货位类型', dataIndex: 'locationType', width: 100 },
    { title: '最大容量', dataIndex: 'maxCapacity', width: 100 },
    { title: '当前存量', dataIndex: 'currentQuantity', width: 100 },
    { title: '状态', dataIndex: 'locationStatus', width: 80 },
    { title: '备注', dataIndex: 'remark', width: 200, ellipsis: true },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 200 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await locationApi.queryPage(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  function resetQuery() {
    Object.assign(queryForm, {
      locationCode: '',
      locationName: '',
      locationStatus: undefined,
      pageNum: 1,
      pageSize: 10,
    });
    queryData();
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  async function updateStatus(record, status) {
    const loading = SmartLoading.show();
    try {
      await locationApi.updateStatus({ locationId: record.locationId, status });
      message.success('状态更新成功');
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide(loading);
    }
  }

  async function deleteLocation(record) {
    const loading = SmartLoading.show();
    try {
      await locationApi.delete(record.locationId);
      message.success('删除成功');
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide(loading);
    }
  }

  onMounted(() => {
    queryData();
  });
</script>

<style scoped lang="less">
  .smart-table-container {
    padding: 10px;
  }
</style>
