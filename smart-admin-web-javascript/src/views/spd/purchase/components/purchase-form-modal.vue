<template>
  <a-modal :title="form.id ? '编辑采购单' : '新增采购单'" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" :maskClosable="false">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="供应商" name="supplierId">
            <a-select v-model:value="form.supplierId" placeholder="请选择供应商" show-search>
              <!-- 供应商选项 -->
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="备注" name="remark">
            <a-input v-model:value="form.remark" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-divider>采购明细</a-divider>
      <div style="margin-bottom:16px">
        <a-button type="primary" @click="addDetail"><PlusOutlined />添加明细</a-button>
      </div>
      <a-table :columns="detailColumns" :data-source="form.detailList" :pagination="false" rowKey="tempId" size="small" bordered>
        <template #bodyCell="{ record, column, index }">
          <template v-if="column.dataIndex === 'materialId'">
            <a-select v-model:value="record.materialId" placeholder="请选择耗材" style="width:100%" show-search />
          </template>
          <template v-if="column.dataIndex === 'quantity'">
            <a-input-number v-model:value="record.quantity" :min="1" :precision="0" style="width:100%" />
          </template>
          <template v-if="column.dataIndex === 'unitPrice'">
            <a-input-number v-model:value="record.unitPrice" :min="0" :precision="2" style="width:100%" />
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <a-button danger type="link" size="small" @click="removeDetail(index)">删除</a-button>
          </template>
        </template>
      </a-table>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { purchaseApi } from '/@/api/spd/purchase-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);
  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const form = reactive({ id: undefined, supplierId: undefined, remark: '', detailList: [] });
  const rules = { supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }] };
  const detailColumns = [
    { title: '耗材', dataIndex: 'materialId', width: 200 },
    { title: '数量', dataIndex: 'quantity', width: 100 },
    { title: '单价', dataIndex: 'unitPrice', width: 100 },
    { title: '操作', dataIndex: 'operate', width: 80 },
  ];
  let tempIdCounter = 0;

  function showModal(record) {
    visible.value = true;
    if (record) {
      form.id = record.id;
      form.supplierId = record.supplierId;
      form.remark = record.remark;
      loadDetail(record.id);
    } else {
      resetForm();
    }
  }

  async function loadDetail(id) {
    try {
      const res = await purchaseApi.getDetail(id);
      form.detailList = res.data.detailList.map(item => ({ ...item, tempId: tempIdCounter++ }));
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  function addDetail() {
    form.detailList.push({ tempId: tempIdCounter++, materialId: undefined, quantity: 1, unitPrice: 0 });
  }

  function removeDetail(index) {
    form.detailList.splice(index, 1);
  }

  async function handleOk() {
    try {
      await formRef.value.validate();
      if (!form.detailList.length) return message.warning('请至少添加一条采购明细');
      confirmLoading.value = true;
      SmartLoading.show();
      await (form.id ? purchaseApi.update : purchaseApi.add)(form);
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
    Object.assign(form, { id: undefined, supplierId: undefined, remark: '', detailList: [] });
    tempIdCounter = 0;
    nextTick(() => formRef.value?.clearValidate());
  }

  defineExpose({ showModal });
</script>
