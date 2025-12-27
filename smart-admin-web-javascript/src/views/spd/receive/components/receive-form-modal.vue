<template>
  <a-modal :title="form.id ? '编辑收货单' : '新增收货单'" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" :maskClosable="false">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="采购单" name="purchaseId">
            <a-select v-model:value="form.purchaseId" placeholder="请选择采购单" show-search />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="仓库" name="warehouseId">
            <a-select v-model:value="form.warehouseId" placeholder="请选择仓库" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备注" name="remark" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-divider>收货明细</a-divider>
      <a-table :columns="detailColumns" :data-source="form.detailList" :pagination="false" rowKey="tempId" size="small" bordered>
        <template #bodyCell="{ record, column }">
          <template v-if="column.dataIndex === 'receiveQuantity'">
            <a-input-number v-model:value="record.receiveQuantity" :min="1" :precision="0" style="width:100%" />
          </template>
        </template>
      </a-table>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { receiveApi } from '/@/api/spd/receive-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);
  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const form = reactive({ id: undefined, purchaseId: undefined, warehouseId: undefined, remark: '', detailList: [] });
  const rules = {
    purchaseId: [{ required: true, message: '请选择采购单', trigger: 'change' }],
    warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
  };
  const detailColumns = [
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '采购数量', dataIndex: 'purchaseQuantity', width: 100 },
    { title: '收货数量', dataIndex: 'receiveQuantity', width: 120 },
  ];

  function showModal(record) {
    visible.value = true;
    if (record) {
      form.id = record.id;
      form.purchaseId = record.purchaseId;
      form.warehouseId = record.warehouseId;
      form.remark = record.remark;
    } else {
      resetForm();
    }
  }

  async function handleOk() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      SmartLoading.show();
      await (form.id ? receiveApi.update : receiveApi.add)(form);
      message.success(form.id ? '更新成功' : '新增成功');
      visible.value = false;
      emit('reloadList');
    } catch (error) {
      if (!error.errorFields) smartSentry.captureError(error);
    } finally {
      confirmLoading.value = false;
      SmartLoading.hide();
    }
  }

  function handleCancel() {
    visible.value = false;
    resetForm();
  }

  function resetForm() {
    Object.assign(form, { id: undefined, purchaseId: undefined, warehouseId: undefined, remark: '', detailList: [] });
    nextTick(() => formRef.value?.clearValidate());
  }

  defineExpose({ showModal });
</script>
