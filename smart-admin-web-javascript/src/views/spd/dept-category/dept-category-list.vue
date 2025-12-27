<template>
  <div class="smart-table-container">
    <a-card size="small" :bordered="false">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增分类</a-button>
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
        :defaultExpandAllRows="true"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'categoryStatus'">
            <a-tag v-if="text === 1" color="green">启用</a-tag>
            <a-tag v-else color="default">禁用</a-tag>
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button type="link" size="small" @click="addChild(record)">添加子分类</a-button>
              <a-button v-if="record.categoryStatus === 1" type="link" size="small" @click="updateStatus(record, 0)">禁用</a-button>
              <a-button v-else type="link" size="small" @click="updateStatus(record, 1)">启用</a-button>
              <a-button danger type="link" size="small" @click="deleteCategory(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <DeptCategoryFormModal ref="formModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue';
  import { deptCategoryApi } from '/@/api/spd/dept-category-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import DeptCategoryFormModal from './components/dept-category-form-modal.vue';

  const tableData = ref([]);
  const tableLoading = ref(false);

  const columns = ref([
    { title: '分类名称', dataIndex: 'categoryName', width: 200 },
    { title: '分类编码', dataIndex: 'categoryCode', width: 150 },
    { title: '层级', dataIndex: 'categoryLevel', width: 80 },
    { title: '排序', dataIndex: 'sortOrder', width: 80 },
    { title: '状态', dataIndex: 'categoryStatus', width: 80 },
    { title: '备注', dataIndex: 'remark', width: 200, ellipsis: true },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 280 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await deptCategoryApi.queryTree();
      tableData.value = res.data || [];
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);
  
  const addChild = (record) => {
    formModal.value.showModal({ parentId: record.categoryId, categoryLevel: record.categoryLevel + 1 });
  };

  async function updateStatus(record, status) {
    const loading = SmartLoading.show();
    try {
      await deptCategoryApi.updateStatus({ categoryId: record.categoryId, status });
      message.success('状态更新成功');
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide(loading);
    }
  }

  async function deleteCategory(record) {
    const loading = SmartLoading.show();
    try {
      await deptCategoryApi.delete(record.categoryId);
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
