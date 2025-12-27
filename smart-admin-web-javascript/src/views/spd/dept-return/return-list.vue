<template>
  <div class="smart-table-container">
    <a-card size="small" :bordered="false">
      <a-form class="smart-query-form" layout="inline">
        <a-form-item label="退库单号">
          <a-input v-model:value="queryForm.returnCode" placeholder="请输入退库单号" style="width: 200px" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="queryForm.returnStatus" placeholder="请选择状态" style="width: 150px" allowClear>
            <a-select-option :value="1">待审核</a-select-option>
            <a-select-option :value="2">已通过</a-select-option>
            <a-select-option :value="3">已驳回</a-select-option>
            <a-select-option :value="4">已完成</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="退库类型">
          <a-select v-model:value="queryForm.returnType" placeholder="请选择退库类型" style="width: 150px" allowClear>
            <a-select-option :value="1">正常退库</a-select-option>
            <a-select-option :value="2">过期退库</a-select-option>
            <a-select-option :value="3">损坏退库</a-select-option>
            <a-select-option :value="4">其他</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="queryData"><SearchOutlined />查询</a-button>
          <a-button @click="resetQuery"><ReloadOutlined />重置</a-button>
        </a-form-item>
      </a-form>

      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增退库单</a-button>
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
          <template v-if="column.dataIndex === 'returnStatus'">
            <a-tag v-if="text === 1" color="blue">待审核</a-tag>
            <a-tag v-else-if="text === 2" color="green">已通过</a-tag>
            <a-tag v-else-if="text === 3" color="red">已驳回</a-tag>
            <a-tag v-else-if="text === 4" color="purple">已完成</a-tag>
          </template>
          <template v-if="column.dataIndex === 'returnType'">
            <span v-if="text === 1">正常退库</span>
            <span v-else-if="text === 2">过期退库</span>
            <span v-else-if="text === 3">损坏退库</span>
            <span v-else-if="text === 4">其他</span>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button v-if="record.returnStatus === 1" type="link" size="small" @click="showAudit(record, 2)">通过</a-button>
              <a-button v-if="record.returnStatus === 1" danger type="link" size="small" @click="showAudit(record, 3)">驳回</a-button>
              <a-button v-if="record.returnStatus === 1" danger type="link" size="small" @click="deleteReturn(record)">删除</a-button>
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

    <ReturnFormModal ref="formModal" @reloadList="queryData" />
    <ReturnDetailDrawer ref="detailDrawer" />
    <ReturnAuditModal ref="auditModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined, ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue';
  import { deptReturnApi } from '/@/api/spd/dept-return-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import ReturnFormModal from './components/return-form-modal.vue';
  import ReturnDetailDrawer from './components/return-detail-drawer.vue';
  import ReturnAuditModal from './components/return-audit-modal.vue';

  const queryForm = reactive({
    returnCode: '',
    returnStatus: undefined,
    returnType: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  const columns = ref([
    { title: '退库单号', dataIndex: 'returnCode', width: 180 },
    { title: '退库科室', dataIndex: 'fromNodeName', width: 150 },
    { title: '接收仓库', dataIndex: 'toNodeName', width: 150 },
    { title: '退库类型', dataIndex: 'returnType', width: 100 },
    { title: '状态', dataIndex: 'returnStatus', width: 100 },
    { title: '申请人', dataIndex: 'applyUserName', width: 120 },
    { title: '申请时间', dataIndex: 'applyTime', width: 160 },
    { title: '审核人', dataIndex: 'auditUserName', width: 120 },
    { title: '审核时间', dataIndex: 'auditTime', width: 160 },
    { title: '备注', dataIndex: 'remark', width: 200, ellipsis: true },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 250 },
  ]);

  /**
   * 查询数据
   */
  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await deptReturnApi.queryPage(queryForm);
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
      returnCode: '',
      returnStatus: undefined,
      returnType: undefined,
      pageNum: 1,
      pageSize: 10,
    });
    queryData();
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  const detailDrawer = ref();
  const showDetail = (record) => detailDrawer.value.showDrawer(record.returnId);

  const auditModal = ref();
  const showAudit = (record, status) => auditModal.value.showModal(record, status);

  /**
   * 删除退库单
   */
  async function deleteReturn(record) {
    const loading = SmartLoading.show();
    try {
      await deptReturnApi.delete(record.returnId);
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
