<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="单位名称" class="smart-query-form-item">
          <a-input v-model:value="queryForm.unitName" placeholder="请输入单位名称" style="width:200px" allowClear />
        </a-form-item>
        <a-form-item label="单位类型" class="smart-query-form-item">
          <a-select v-model:value="queryForm.unitType" placeholder="请选择单位类型" style="width:200px" allowClear>
            <a-select-option :value="1">基本单位</a-select-option>
            <a-select-option :value="2">辅助单位</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select v-model:value="queryForm.status" placeholder="请选择状态" style="width:200px" allowClear>
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">停用</a-select-option>
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

    <!-- 操作按钮和表格 -->
    <a-card size="small" :bordered="false">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button v-privilege="'spd:unit:add'" type="primary" @click="showModal()">
            <template #icon><PlusOutlined /></template>
            新增单位
          </a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData"><template #icon><ReloadOutlined /></template></a-button>
        </div>
      </a-row>

      <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'unitType'">
            <a-tag v-if="text === 1" color="blue">基本单位</a-tag>
            <a-tag v-else color="purple">辅助单位</a-tag>
          </template>

          <template v-if="column.dataIndex === 'isDefault'">
            <a-tag v-if="text === 1" color="green">是</a-tag>
            <span v-else>否</span>
          </template>

          <template v-if="column.dataIndex === 'status'">
            <a-tag v-if="text === 1" color="green">启用</a-tag>
            <a-tag v-else color="red">停用</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button v-privilege="'spd:unit:update'" type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button v-privilege="'spd:unit:updateStatus'" type="link" size="small" @click="updateStatus(record)">
                {{ record.status === 1 ? '停用' : '启用' }}
              </a-button>
              <a-button v-privilege="'spd:unit:delete'" danger type="link" size="small" @click="deleteUnit(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <UnitFormModal ref="formModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import { unitApi } from '/@/api/spd/unit-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import UnitFormModal from './components/unit-form-modal.vue';

  const queryForm = reactive({ unitName: '', unitType: undefined, status: undefined });
  const tableData = ref([]);
  const tableLoading = ref(false);

  const columns = ref([
    { title: '单位编码', dataIndex: 'unitCode', width: 120 },
    { title: '单位名称', dataIndex: 'unitName', width: 150 },
    { title: '单位简称', dataIndex: 'unitAbbr', width: 100 },
    { title: '单位类型', dataIndex: 'unitType', width: 120 },
    { title: '是否默认', dataIndex: 'isDefault', width: 100 },
    { title: '排序', dataIndex: 'sort', width: 80 },
    { title: '状态', dataIndex: 'status', width: 100 },
    { title: '备注', dataIndex: 'remark', width: 200 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 200 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await unitApi.queryList(queryForm);
      tableData.value = res.data || [];
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  function resetQuery() {
    Object.assign(queryForm, { unitName: '', unitType: undefined, status: undefined });
    queryData();
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  function updateStatus(record) {
    const newStatus = record.status === 1 ? 0 : 1;
    SmartLoading.show();
    unitApi.updateStatus(record.id, newStatus)
      .then(() => {
        message.success('状态更新成功');
        queryData();
      })
      .finally(() => SmartLoading.hide());
  }

  function deleteUnit(record) {
    Modal.confirm({
      title: '确认删除',
      content: `确定删除单位"${record.unitName}"吗？`,
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await unitApi.delete(record.id);
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
