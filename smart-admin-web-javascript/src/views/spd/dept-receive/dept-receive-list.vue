<template>
  <div class="smart-table-container">
    <a-card size="small" :bordered="false">
      <a-form class="smart-query-form" layout="inline">
        <a-form-item label="收货单号">
          <a-input v-model:value="queryForm.receiveCode" placeholder="请输入收货单号" style="width: 200px" />
        </a-form-item>
        <a-form-item label="收货状态">
          <a-select v-model:value="queryForm.receiveStatus" placeholder="请选择状态" style="width: 150px" allowClear>
            <a-select-option :value="1">待收货</a-select-option>
            <a-select-option :value="2">已收货</a-select-option>
            <a-select-option :value="3">部分拒收</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="queryData"><SearchOutlined />查询</a-button>
          <a-button @click="resetQuery"><ReloadOutlined />重置</a-button>
        </a-form-item>
      </a-form>

      <a-row class="smart-table-btn-block">
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
          <template v-if="column.dataIndex === 'receiveStatus'">
            <a-tag v-if="text === 1" color="blue">待收货</a-tag>
            <a-tag v-else-if="text === 2" color="green">已收货</a-tag>
            <a-tag v-else-if="text === 3" color="orange">部分拒收</a-tag>
          </template>
          <template v-if="column.dataIndex === 'receiveType'">
            <span v-if="text === 1">正常收货</span>
            <span v-else-if="text === 2">紧急收货</span>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button v-if="record.receiveStatus === 1" type="link" size="small" @click="confirmReceive(record)">确认收货</a-button>
              <a-button v-if="record.receiveStatus === 1" danger type="link" size="small" @click="showRejectModal(record)">拒收</a-button>
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

    <DeptReceiveDetailDrawer ref="detailDrawer" />
    <DeptReceiveRejectModal ref="rejectModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import { ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue';
  import { deptReceiveApi } from '/@/api/spd/dept-receive-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import DeptReceiveDetailDrawer from './components/dept-receive-detail-drawer.vue';
  import DeptReceiveRejectModal from './components/dept-receive-reject-modal.vue';

  const queryForm = reactive({
    receiveCode: '',
    receiveStatus: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  const columns = ref([
    { title: '收货单号', dataIndex: 'receiveCode', width: 180 },
    { title: '科室名称', dataIndex: 'deptName', width: 150 },
    { title: '仓库名称', dataIndex: 'warehouseName', width: 150 },
    { title: '收货类型', dataIndex: 'receiveType', width: 100 },
    { title: '收货状态', dataIndex: 'receiveStatus', width: 100 },
    { title: '总数量', dataIndex: 'totalQuantity', width: 80 },
    { title: '已收数量', dataIndex: 'receivedQuantity', width: 80 },
    { title: '拒收数量', dataIndex: 'rejectedQuantity', width: 80 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '收货人', dataIndex: 'receiveBy', width: 100 },
    { title: '收货时间', dataIndex: 'receiveTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 200 },
  ]);

  /**
   * 查询数据
   */
  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await deptReceiveApi.queryPage(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  /**
   * 重置查询条件
   */
  function resetQuery() {
    Object.assign(queryForm, {
      receiveCode: '',
      receiveStatus: undefined,
      pageNum: 1,
      pageSize: 10,
    });
    queryData();
  }

  const detailDrawer = ref();
  const showDetail = (record) => detailDrawer.value.showDrawer(record.receiveId);

  const rejectModal = ref();
  const showRejectModal = (record) => rejectModal.value.showModal(record);

  /**
   * 确认收货
   */
  async function confirmReceive(record) {
    const loading = SmartLoading.show();
    try {
      await deptReceiveApi.confirmReceive(record.receiveId);
      message.success('收货成功');
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
