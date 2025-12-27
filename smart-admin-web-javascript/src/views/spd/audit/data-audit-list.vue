<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="审核类型" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.auditType"
            placeholder="请选择审核类型"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="1">供应商</a-select-option>
            <a-select-option :value="2">耗材</a-select-option>
            <a-select-option :value="3">生产厂家</a-select-option>
            <a-select-option :value="4">调价</a-select-option>
            <a-select-option :value="5">收费状态</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="目标名称" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.targetName"
            placeholder="请输入目标名称"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="审核状态" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.auditStatus"
            placeholder="请选择审核状态"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="0">待审核</a-select-option>
            <a-select-option :value="1">已通过</a-select-option>
            <a-select-option :value="2">已拒绝</a-select-option>
            <a-select-option :value="3">已撤销</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="queryData">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery">
              <template #icon>
                <ReloadOutlined />
              </template>
              重置
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <!-- 操作按钮和表格 -->
    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" @click="showSubmitModal()">
            <template #icon>
              <PlusOutlined />
            </template>
            提交审核
          </a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData">
            <template #icon>
              <ReloadOutlined />
            </template>
          </a-button>
        </div>
      </a-row>

      <!-- 表格 -->
      <a-table
        size="small"
        :scroll="{ x: 1800 }"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'auditType'">
            <a-tag color="blue">{{ record.auditTypeName }}</a-tag>
          </template>

          <template v-if="column.dataIndex === 'actionType'">
            <a-tag :color="record.actionType === 1 ? 'green' : 'orange'">
              {{ record.actionTypeName }}
            </a-tag>
          </template>

          <template v-if="column.dataIndex === 'auditStatus'">
            <a-tag v-if="record.auditStatus === 0" color="gold">待审核</a-tag>
            <a-tag v-else-if="record.auditStatus === 1" color="green">已通过</a-tag>
            <a-tag v-else-if="record.auditStatus === 2" color="red">已拒绝</a-tag>
            <a-tag v-else color="gray">已撤销</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              
              <template v-if="record.auditStatus === 0">
                <a-button
                  v-privilege="'spd:audit:process'"
                  type="link"
                  size="small"
                  @click="processAudit(record, 1)"
                >
                  通过
                </a-button>
                <a-button
                  v-privilege="'spd:audit:process'"
                  danger
                  type="link"
                  size="small"
                  @click="processAudit(record, 2)"
                >
                  拒绝
                </a-button>
                <a-button
                  type="link"
                  size="small"
                  @click="cancelAudit(record)"
                >
                  撤销
                </a-button>
              </template>
            </div>
          </template>
        </template>
      </a-table>

      <!-- 分页 -->
      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          @showSizeChange="queryData"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>

    <!-- 提交审核弹窗 -->
    <DataAuditSubmitModal ref="submitModal" @reloadList="queryData" />

    <!-- 详情抽屉 -->
    <DataAuditDetailDrawer ref="detailDrawer" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
  } from '@ant-design/icons-vue';
  import { dataAuditApi } from '/@/api/spd/data-audit-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import DataAuditSubmitModal from './components/data-audit-submit-modal.vue';
  import DataAuditDetailDrawer from './components/data-audit-detail-drawer.vue';

  // 查询表单
  const queryForm = reactive({
    auditType: undefined,
    targetName: '',
    auditStatus: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  // 表格数据
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  // 表格列定义
  const columns = ref([
    {
      title: '审核业务ID',
      dataIndex: 'auditId',
      width: 180,
      fixed: 'left',
    },
    {
      title: '审核类型',
      dataIndex: 'auditType',
      width: 100,
    },
    {
      title: '目标名称',
      dataIndex: 'targetName',
      width: 200,
    },
    {
      title: '操作类型',
      dataIndex: 'actionType',
      width: 100,
    },
    {
      title: '审核状态',
      dataIndex: 'auditStatus',
      width: 100,
    },
    {
      title: '提交理由',
      dataIndex: 'submitReason',
      width: 200,
      ellipsis: true,
    },
    {
      title: '审核意见',
      dataIndex: 'auditOpinion',
      width: 200,
      ellipsis: true,
    },
    {
      title: '提交时间',
      dataIndex: 'submitTime',
      width: 160,
    },
    {
      title: '审核时间',
      dataIndex: 'auditTime',
      width: 160,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      fixed: 'right',
      width: 220,
    },
  ]);

  // 查询数据
  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await dataAuditApi.queryPage(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // 重置查询
  function resetQuery() {
    queryForm.auditType = undefined;
    queryForm.targetName = '';
    queryForm.auditStatus = undefined;
    queryForm.pageNum = 1;
    queryData();
  }

  // 显示提交审核弹窗
  const submitModal = ref();
  function showSubmitModal() {
    submitModal.value.showModal();
  }

  // 显示详情抽屉
  const detailDrawer = ref();
  function showDetail(record) {
    detailDrawer.value.showDrawer(record.id);
  }

  // 处理审核
  async function processAudit(record, status) {
    const statusText = status === 1 ? '通过' : '拒绝';
    
    SmartLoading.show();
    try {
      await dataAuditApi.process({
        id: record.id,
        auditStatus: status,
        auditOpinion: '',
      });
      message.success(`审核${statusText}成功`);
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  // 撤销审核
  async function cancelAudit(record) {
    SmartLoading.show();
    try {
      await dataAuditApi.cancel(record.id);
      message.success('撤销成功');
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
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
