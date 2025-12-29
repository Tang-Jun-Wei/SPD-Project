<template>
  <div class="smart-table-container">
    <a-card size="small" :bordered="false">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button v-privilege="'spd:unit:conversion:add'" type="primary" @click="showModal()">
            <template #icon><PlusOutlined /></template>
            新增换算关系
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
          <template v-if="column.dataIndex === 'conversion'">
            {{ record.fromUnitName }} → {{ record.toUnitName }} (1:{{ record.conversionRate }})
          </template>

          <template v-if="column.dataIndex === 'status'">
            <a-tag v-if="text === 1" color="green">启用</a-tag>
            <a-tag v-else color="red">停用</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button v-privilege="'spd:unit:conversion:update'" type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button v-privilege="'spd:unit:conversion:delete'" danger type="link" size="small" @click="deleteConversion(record)">删除</a-button>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <ConversionFormModal ref="formModal" @reloadList="queryData" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import { unitConversionApi } from '/@/api/spd/unit-conversion-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import ConversionFormModal from './components/conversion-form-modal.vue';

  const queryForm = reactive({});
  const tableData = ref([]);
  const tableLoading = ref(false);

  const columns = ref([
    { title: '换算关系', dataIndex: 'conversion', width: 300 },
    { title: '换算比率', dataIndex: 'conversionRate', width: 120 },
    { title: '是否双向', dataIndex: 'isReciprocal', width: 100 },
    { title: '状态', dataIndex: 'status', width: 100 },
    { title: '备注', dataIndex: 'remark', width: 200 },
    { title: '创建时间', dataIndex: 'createTime', width: 160 },
    { title: '操作', dataIndex: 'operate', fixed: 'right', width: 150 },
  ]);

  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await unitConversionApi.queryList(queryForm);
      tableData.value = res.data || [];
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  const formModal = ref();
  const showModal = (record) => formModal.value.showModal(record);

  function deleteConversion(record) {
    Modal.confirm({
      title: '确认删除',
      content: `确定删除换算关系"${record.fromUnitName} → ${record.toUnitName}"吗？`,
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await unitConversionApi.delete(record.id);
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
