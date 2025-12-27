<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="标签码" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.labelCode"
            placeholder="请输入标签码"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="耗材名称" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.materialName"
            placeholder="请输入耗材名称"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="标签状态" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.labelStatus"
            placeholder="请选择状态"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="0">已生成</a-select-option>
            <a-select-option :value="1">已入库</a-select-option>
            <a-select-option :value="2">已领用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="仓库" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.warehouseId"
            placeholder="请选择仓库"
            style="width: 200px"
            allowClear
          >
            <!-- 仓库选项需要从后端获取 -->
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
          <a-button v-privilege="'spd:label:add'" type="primary" @click="showModal()">
            <template #icon>
              <PlusOutlined />
            </template>
            新增标签
          </a-button>

          <a-button
            v-privilege="'spd:label:use'"
            type="primary"
            @click="batchUse"
            :disabled="!hasSelected"
          >
            <template #icon>
              <CheckOutlined />
            </template>
            批量领用
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
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
          getCheckboxProps: (record) => ({
            disabled: record.labelStatus !== 1,
          }),
        }"
        size="small"
        :scroll="{ x: 1500 }"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="labelCode"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'labelStatus'">
            <a-tag v-if="text === 0" color="default">已生成</a-tag>
            <a-tag v-else-if="text === 1" color="green">已入库</a-tag>
            <a-tag v-else-if="text === 2" color="orange">已领用</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button
                v-if="record.labelStatus === 1"
                v-privilege="'spd:label:use'"
                type="link"
                size="small"
                @click="singleUse(record)"
              >
                领用
              </a-button>
              <a-button
                v-privilege="'spd:label:delete'"
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
    <LabelFormModal ref="labelFormModal" @reloadList="queryData" />

    <!-- 详情弹窗 -->
    <LabelDetailDrawer ref="labelDetailDrawer" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    CheckOutlined,
  } from '@ant-design/icons-vue';
  import { labelApi } from '/@/api/spd/label-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import LabelFormModal from './components/label-form-modal.vue';
  import LabelDetailDrawer from './components/label-detail-drawer.vue';

  // 查询表单
  const queryForm = reactive({
    labelCode: '',
    materialName: '',
    labelStatus: undefined,
    warehouseId: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  // 表格数据
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const selectedRowKeys = ref([]);

  // 表格列定义
  const columns = ref([
    {
      title: '标签码',
      dataIndex: 'labelCode',
      width: 160,
      fixed: 'left',
    },
    {
      title: '耗材名称',
      dataIndex: 'materialName',
      width: 200,
    },
    {
      title: '规格型号',
      dataIndex: 'specification',
      width: 150,
    },
    {
      title: '批号',
      dataIndex: 'batchNo',
      width: 120,
    },
    {
      title: '数量',
      dataIndex: 'quantity',
      width: 80,
    },
    {
      title: '单位',
      dataIndex: 'unit',
      width: 60,
    },
    {
      title: '仓库',
      dataIndex: 'warehouseName',
      width: 120,
    },
    {
      title: '标签状态',
      dataIndex: 'labelStatus',
      width: 100,
    },
    {
      title: '生成时间',
      dataIndex: 'createTime',
      width: 160,
    },
    {
      title: '领用时间',
      dataIndex: 'useTime',
      width: 160,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      fixed: 'right',
      width: 150,
    },
  ]);

  // 查询数据
  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await labelApi.queryPage(queryForm);
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
    queryForm.labelCode = '';
    queryForm.materialName = '';
    queryForm.labelStatus = undefined;
    queryForm.warehouseId = undefined;
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
  const labelFormModal = ref();
  function showModal(record) {
    labelFormModal.value.showModal(record);
  }

  // 显示详情弹窗
  const labelDetailDrawer = ref();
  function showDetail(record) {
    labelDetailDrawer.value.showDrawer(record.labelCode);
  }

  // 单个领用
  function singleUse(record) {
    Modal.confirm({
      title: '确认领用',
      content: `确定要领用标签 ${record.labelCode} 吗？`,
      okText: '确定',
      async onOk() {
        SmartLoading.show();
        try {
          await labelApi.use(record.labelCode);
          message.success('领用成功');
          queryData();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
    });
  }

  // 批量领用
  function batchUse() {
    if (selectedRowKeys.value.length === 0) {
      message.warning('请选择要领用的标签');
      return;
    }

    Modal.confirm({
      title: '确认领用',
      content: `确定要领用选中的 ${selectedRowKeys.value.length} 个标签吗？`,
      okText: '确定',
      async onOk() {
        SmartLoading.show();
        try {
          for (const labelCode of selectedRowKeys.value) {
            await labelApi.use(labelCode);
          }
          message.success('领用成功');
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

  // 单个删除
  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该标签吗？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await labelApi.delete(record.labelCode);
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

  onMounted(() => {
    queryData();
  });
</script>

<style scoped lang="less">
  .smart-table-container {
    padding: 10px;
  }
</style>
