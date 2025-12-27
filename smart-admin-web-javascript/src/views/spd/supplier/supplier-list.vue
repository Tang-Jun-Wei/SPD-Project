<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="供应商名称" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.supplierName"
            placeholder="请输入供应商名称"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="联系人" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.contactPerson"
            placeholder="请输入联系人"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="状态" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.status"
            placeholder="请选择状态"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
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
          <a-button v-privilege="'spd:supplier:add'" type="primary" @click="showModal()">
            <template #icon>
              <PlusOutlined />
            </template>
            新增供应商
          </a-button>

          <a-button
            v-privilege="'spd:supplier:delete'"
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
          <template v-if="column.dataIndex === 'status'">
            <a-tag v-if="text === 1" color="green">启用</a-tag>
            <a-tag v-else color="red">禁用</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button
                v-privilege="'spd:supplier:update'"
                type="link"
                size="small"
                @click="showModal(record)"
              >
                编辑
              </a-button>
              <a-button
                v-privilege="'spd:supplier:updateStatus'"
                type="link"
                size="small"
                @click="updateStatus(record)"
              >
                {{ record.status === 1 ? '禁用' : '启用' }}
              </a-button>
              <a-button
                v-privilege="'spd:supplier:delete'"
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
    <SupplierFormModal ref="supplierFormModal" @reloadList="queryData" />

    <!-- 详情弹窗 -->
    <SupplierDetailDrawer ref="supplierDetailDrawer" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    DeleteOutlined,
  } from '@ant-design/icons-vue';
  import { supplierApi } from '/@/api/spd/supplier-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SupplierFormModal from './components/supplier-form-modal.vue';
  import SupplierDetailDrawer from './components/supplier-detail-drawer.vue';

  // 查询表单
  const queryForm = reactive({
    supplierName: '',
    contactPerson: '',
    status: undefined,
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
      title: '供应商名称',
      dataIndex: 'supplierName',
      width: 200,
      fixed: 'left',
    },
    {
      title: '联系人',
      dataIndex: 'contactPerson',
      width: 100,
    },
    {
      title: '联系电话',
      dataIndex: 'contactPhone',
      width: 120,
    },
    {
      title: '地址',
      dataIndex: 'address',
      width: 200,
      ellipsis: true,
    },
    {
      title: '营业执照号',
      dataIndex: 'businessLicense',
      width: 180,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 160,
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
      const res = await supplierApi.queryPage(queryForm);
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
    queryForm.supplierName = '';
    queryForm.contactPerson = '';
    queryForm.status = undefined;
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
  const supplierFormModal = ref();
  function showModal(record) {
    supplierFormModal.value.showModal(record);
  }

  // 显示详情弹窗
  const supplierDetailDrawer = ref();
  function showDetail(record) {
    supplierDetailDrawer.value.showDrawer(record.id);
  }

  // 更新状态
  function updateStatus(record) {
    const newStatus = record.status === 1 ? 0 : 1;
    const statusText = newStatus === 1 ? '启用' : '禁用';
    
    Modal.confirm({
      title: '确认操作',
      content: `确定要${statusText}该供应商吗？`,
      okText: '确定',
      async onOk() {
        SmartLoading.show();
        try {
          await supplierApi.updateStatus(record.id, newStatus);
          message.success(`${statusText}成功`);
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
      content: '确定要删除该供应商吗？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await supplierApi.delete(record.id);
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
            await supplierApi.delete(id);
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
