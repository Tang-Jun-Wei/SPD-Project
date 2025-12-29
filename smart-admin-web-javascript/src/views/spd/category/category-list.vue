<template>
  <div class="smart-table-container">
    <!-- 操作按钮 -->
    <a-card size="small" :bordered="false">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button v-privilege="'spd:category:add'" type="primary" @click="showModal()">
            <template #icon><PlusOutlined /></template>
            新增分类
          </a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData">
            <template #icon><ReloadOutlined /></template>
          </a-button>
        </div>
      </a-row>

      <!-- 树形表格 -->
      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        :defaultExpandAllRows="true"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'categoryName'">
            <span :style="{ paddingLeft: (record.level - 1) * 20 + 'px' }">
              {{ text }}
            </span>
          </template>

          <template v-if="column.dataIndex === 'status'">
            <a-tag v-if="text === 1" color="green">启用</a-tag>
            <a-tag v-else color="red">停用</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button
                v-privilege="'spd:category:add'"
                v-if="record.level < 3"
                type="link"
                size="small"
                @click="showModal(null, record.id)"
              >
                添加子分类
              </a-button>
              <a-button
                v-privilege="'spd:category:update'"
                type="link"
                size="small"
                @click="showModal(record)"
              >
                编辑
              </a-button>
              <a-button
                v-privilege="'spd:category:updateStatus'"
                type="link"
                size="small"
                @click="updateStatus(record)"
              >
                {{ record.status === 1 ? '停用' : '启用' }}
              </a-button>
              <a-button
                v-privilege="'spd:category:delete'"
                danger
                type="link"
                size="small"
                @click="deleteCategory(record)"
              >
                删除
              </a-button>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <CategoryFormModal ref="formModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue';
  import { categoryApi } from '/@/api/spd/category-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import CategoryFormModal from './components/category-form-modal.vue';

  const tableData = ref([]);
  const tableLoading = ref(false);

  const columns = ref([
    { title: '分类名称', dataIndex: 'categoryName', width: 250 },
    { title: '分类编码', dataIndex: 'categoryCode', width: 150 },
    { title: '层级', dataIndex: 'level', width: 80 },
    { title: '排序', dataIndex: 'sort', width: 80 },
    { title: '状态', dataIndex: 'status', width: 100 },
    { title: '备注', dataIndex: 'remark', width: 200 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 280 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await categoryApi.queryTree();
      tableData.value = flattenTree(res.data || []);
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // 将树形结构展平为列表(保持层级关系)
  function flattenTree(tree, result = []) {
    tree.forEach(node => {
      const { children, ...nodeData } = node;
      result.push(nodeData);
      if (children && children.length > 0) {
        flattenTree(children, result);
      }
    });
    return result;
  }

  const formModal = ref();
  const showModal = (record, parentId) => formModal.value.showModal(record, parentId);

  function updateStatus(record) {
    const newStatus = record.status === 1 ? 0 : 1;
    SmartLoading.show();
    categoryApi.updateStatus(record.id, newStatus)
      .then(() => {
        message.success('状态更新成功');
        queryData();
      })
      .finally(() => SmartLoading.hide());
  }

  function deleteCategory(record) {
    Modal.confirm({
      title: '确认删除',
      content: `确定删除分类"${record.categoryName}"吗？`,
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await categoryApi.delete(record.id);
          message.success('删除成功');
          queryData();
        } finally {
          SmartLoading.hide();
        }
      },
    });
  }

  onMounted(queryData);
</script>

<style scoped lang="less">
  .smart-table-container {
    padding: 10px;
  }
</style>
