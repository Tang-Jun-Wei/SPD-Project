<template>
  <div class="smart-table-container">
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="节点名称" class="smart-query-form-item">
          <a-input v-model:value="queryForm.nodeName" placeholder="节点名称" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="节点类型" class="smart-query-form-item">
          <a-select v-model:value="queryForm.nodeType" placeholder="请选择" style="width:150px" allowClear>
            <a-select-option :value="1">科室</a-select-option>
            <a-select-option :value="2">仓库</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select v-model:value="queryForm.nodeStatus" placeholder="请选择" style="width:150px" allowClear>
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button-group>
            <a-button type="primary" @click="queryData"><SearchOutlined />查询</a-button>
            <a-button @click="resetQuery"><ReloadOutlined />重置</a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button type="primary" @click="showModal()"><PlusOutlined />新增节点</a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData"><ReloadOutlined /></a-button>
        </div>
      </a-row>

      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="nodeId"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'nodeType'">
            <a-tag v-if="text === 1" color="blue">科室</a-tag>
            <a-tag v-else-if="text === 2" color="green">仓库</a-tag>
          </template>
          <template v-if="column.dataIndex === 'nodeStatus'">
            <a-switch 
              :checked="text === 1" 
              @change="(checked) => updateStatus(record, checked)"
              checked-children="启用" 
              un-checked-children="禁用" 
            />
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button danger type="link" size="small" @click="singleDelete(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          :show-total="t => `共${t}条`"
        />
      </div>
    </a-card>

    <OrgNodeFormModal ref="formModal" @reloadList="queryData" />
    <OrgNodeDetailModal ref="detailModal" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { NODE_TYPE_ENUM, WAREHOUSE_TYPE_ENUM, STATUS_ENUM, getEnumLabel } from '/@/constants/spd/spd-const';
  import OrgNodeFormModal from './components/org-node-form-modal.vue';
  import OrgNodeDetailModal from './components/org-node-detail-modal.vue';

  const queryForm = reactive({ 
    nodeName: '', 
    nodeType: undefined, 
    nodeStatus: undefined, 
    pageNum: 1, 
    pageSize: 10 
  });
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  const columns = ref([
    { title: '节点ID', dataIndex: 'nodeId', width: 150, fixed: 'left' },
    { title: '节点名称', dataIndex: 'nodeName', width: 150 },
    { title: '节点类型', dataIndex: 'nodeTypeName', width: 100 },
    { title: '仓库类型', dataIndex: 'warehouseTypeName', width: 100 },
    { title: '上级节点', dataIndex: 'parentNodeName', width: 150 },
    { title: '状态', dataIndex: 'status', width: 100 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 200 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await orgNodeApi.queryPage(queryForm);
      tableData.value = res.data.list || [];
      total.value = res.data.total || 0;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  function resetQuery() {
    Object.assign(queryForm, { 
      nodeName: '', 
      nodeType: undefined, 
      nodeStatus: undefined, 
      pageNum: 1 
    });
    queryData();
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  const detailModal = ref();
  const showDetail = (record) => detailModal.value.showModal(record.nodeId);

  async function updateStatus(record, checked) {
    try {
      SmartLoading.show();
      const status = checked ? 1 : 0;
      if (record.nodeType === 1) {
        await orgNodeApi.updateDeptStatus(record.nodeId, status);
      } else if (record.nodeType === 2) {
        await orgNodeApi.updateWarehouseStatus(record.nodeId, status);
      }
      message.success('状态更新成功');
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: `确定删除节点"${record.nodeName}"吗？`,
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await orgNodeApi.delete(record.nodeId);
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

  onMounted(queryData);
</script>

<style scoped lang="less">
  .smart-table-container {
    padding: 10px;
  }
</style>
