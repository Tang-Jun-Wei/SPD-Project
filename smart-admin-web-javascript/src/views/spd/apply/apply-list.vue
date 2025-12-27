<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="申领单号" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.applyNo"
            placeholder="请输入申领单号"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="申领科室" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.applyDeptId"
            placeholder="请选择科室"
            style="width: 200px"
            allowClear
          >
            <!-- 科室选项需要从后端获取 -->
          </a-select>
        </a-form-item>

        <a-form-item label="申领状态" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.applyStatus"
            placeholder="请选择状态"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="1">待审核</a-select-option>
            <a-select-option :value="2">已审核</a-select-option>
            <a-select-option :value="3">已驳回</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="申领时间" class="smart-query-form-item">
          <a-range-picker
            v-model:value="queryTimeRange"
            style="width: 240px"
            :presets="defaultTimeRanges"
          />
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
          <a-button v-privilege="'spd:apply:add'" type="primary" @click="showModal()">
            <template #icon>
              <PlusOutlined />
            </template>
            新增申领单
          </a-button>

          <a-button
            v-privilege="'spd:apply:delete'"
            type="primary"
            danger
            @click="batchDelete"
            :disabled="!hasSelected"
          >
            <template #icon>
              <DeleteOutlined />
            </template>
            批量删除
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
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        size="small"
        :scroll="{ x: 1500 }"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'applyStatus'">
            <a-tag v-if="text === 1" color="orange">待审核</a-tag>
            <a-tag v-else-if="text === 2" color="green">已审核</a-tag>
            <a-tag v-else-if="text === 3" color="red">已驳回</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button
                v-if="record.applyStatus === 1"
                v-privilege="'spd:apply:update'"
                type="link"
                size="small"
                @click="showModal(record)"
              >
                编辑
              </a-button>
              <a-button
                v-if="record.applyStatus === 1"
                v-privilege="'spd:apply:approve'"
                type="link"
                size="small"
                @click="showApproveModal(record)"
              >
                审核
              </a-button>
              <a-button
                v-privilege="'spd:apply:delete'"
                danger
                type="link"
                size="small"
                @click="singleDelete(record)"
              >
                删除
              </a-button>
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

    <!-- 新增/编辑弹窗 -->
    <ApplyFormModal ref="applyFormModal" @reloadList="queryData" />

    <!-- 审核弹窗 -->
    <ApplyApproveModal ref="applyApproveModal" @reloadList="queryData" />

    <!-- 详情弹窗 -->
    <ApplyDetailModal ref="applyDetailModal" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    DeleteOutlined,
  } from '@ant-design/icons-vue';
  import { applyApi } from '/@/api/spd/apply-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import ApplyFormModal from './components/apply-form-modal.vue';
  import ApplyApproveModal from './components/apply-approve-modal.vue';
  import ApplyDetailModal from './components/apply-detail-modal.vue';

  // 查询表单
  const queryForm = reactive({
    applyNo: '',
    applyDeptId: undefined,
    applyStatus: undefined,
    applyTimeStart: undefined,
    applyTimeEnd: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  const queryTimeRange = ref([]);

  // 表格数据
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const selectedRowKeys = ref([]);

  // 表格列定义
  const columns = ref([
    {
      title: '申领单号',
      dataIndex: 'applyNo',
      width: 180,
      fixed: 'left',
    },
    {
      title: '申领科室',
      dataIndex: 'applyDeptName',
      width: 150,
    },
    {
      title: '申领人',
      dataIndex: 'applyUserName',
      width: 100,
    },
    {
      title: '申领时间',
      dataIndex: 'applyTime',
      width: 160,
    },
    {
      title: '申领状态',
      dataIndex: 'applyStatus',
      width: 100,
    },
    {
      title: '审核人',
      dataIndex: 'approveUserName',
      width: 100,
    },
    {
      title: '审核时间',
      dataIndex: 'approveTime',
      width: 160,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: 200,
      ellipsis: true,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      fixed: 'right',
      width: 200,
    },
  ]);

  // 查询数据
  async function queryData() {
    try {
      tableLoading.value = true;
      if (queryTimeRange.value && queryTimeRange.value.length === 2) {
        queryForm.applyTimeStart = queryTimeRange.value[0].format('YYYY-MM-DD HH:mm:ss');
        queryForm.applyTimeEnd = queryTimeRange.value[1].format('YYYY-MM-DD HH:mm:ss');
      } else {
        queryForm.applyTimeStart = undefined;
        queryForm.applyTimeEnd = undefined;
      }

      const res = await applyApi.queryPage(queryForm);
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
    queryForm.applyNo = '';
    queryForm.applyDeptId = undefined;
    queryForm.applyStatus = undefined;
    queryTimeRange.value = [];
    queryForm.pageNum = 1;
    queryData();
  }

  // 选择行
  function onSelectChange(selectedKeys) {
    selectedRowKeys.value = selectedKeys;
  }

  // 是否有选中
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  // 显示表单弹窗
  const applyFormModal = ref();
  function showModal(record) {
    applyFormModal.value.showModal(record);
  }

  // 显示审核弹窗
  const applyApproveModal = ref();
  function showApproveModal(record) {
    applyApproveModal.value.showModal(record);
  }

  // 显示详情弹窗
  const applyDetailModal = ref();
  function showDetail(record) {
    applyDetailModal.value.showModal(record.id);
  }

  // 单个删除
  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该申领单吗？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await applyApi.delete(record.id);
          message.success('删除成功');
          queryData();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
    });
  }

  // 批量删除
  function batchDelete() {
    if (selectedRowKeys.value.length === 0) {
      message.warning('请选择要删除的数据');
      return;
    }

    Modal.confirm({
      title: '确认删除',
      content: `确定要删除选中的 ${selectedRowKeys.value.length} 条数据吗？`,
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          for (const id of selectedRowKeys.value) {
            await applyApi.delete(id);
          }
          message.success('删除成功');
          selectedRowKeys.value = [];
          queryData();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
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
