<template>
  <div class="smart-table-container">
    <a-card size="small" :bordered="false">
      <a-form class="smart-query-form" layout="inline">
        <a-form-item label="退货单号">
          <a-input v-model:value="queryForm.returnCode" placeholder="请输入退货单号" style="width: 200px" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="queryForm.returnStatus" placeholder="请选择状态" style="width: 150px" allowClear>
            <a-select-option :value="1">待审核</a-select-option>
            <a-select-option :value="2">已通过</a-select-option>
            <a-select-option :value="3">已驳回</a-select-option>
            <a-select-option :value="4">已完成</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="退货类型">
          <a-select v-model:value="queryForm.returnType" placeholder="请选择退货类型" style="width: 150px" allowClear>
            <a-select-option :value="1">质量问题</a-select-option>
            <a-select-option :value="2">过期退货</a-select-option>
            <a-select-option :value="3">订单错误</a-select-option>
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
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增退货单</a-button>
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
            <span v-if="text === 1">质量问题</span>
            <span v-else-if="text === 2">过期退货</span>
            <span v-else-if="text === 3">订单错误</span>
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

    <SupplierReturnFormModal ref="formModal" @reloadList="queryData" />
    <SupplierReturnDetailDrawer ref="detailDrawer" />
    <SupplierReturnAuditModal ref="auditModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { PlusOutlined, ReloadOutlined, SearchOutlined } from '@ant-design/icons-vue';
  import { supplierReturnApi } from '/@/api/spd/supplier-return-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SupplierReturnFormModal from './components/supplier-return-form-modal.vue';
  import SupplierReturnDetailDrawer from './components/supplier-return-detail-drawer.vue';
  import SupplierReturnAuditModal from './components/supplier-return-audit-modal.vue';

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
    { title: '退货单号', dataIndex: 'returnCode', width: 180 },
    { title: '供应商名称', dataIndex: 'supplierName', width: 200 },
    { title: '退货类型', dataIndex: 'returnType', width: 100 },
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
      const res = await supplierReturnApi.queryPage(queryForm);
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
  const showDetail = (record) => detailDrawer.value.showDrawer(record.supplierReturnId);

  const auditModal = ref();
  const showAudit = (record, status) => auditModal.value.showModal(record, status);

  /**
   * 删除退货单
   */
  async function deleteReturn(record) {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该退货单吗？',
      onOk: async () => {
        const loading = SmartLoading.show();
        try {
          await supplierReturnApi.delete(record.supplierReturnId);
          message.success('删除成功');
          queryData();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide(loading);
        }
      },
    });
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
