<template>
  <a-modal
    :title="form.labelCode ? '编辑标签' : '新增标签'"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable="false"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="耗材" name="materialId">
            <a-select
              v-model:value="form.materialId"
              placeholder="请选择耗材"
              show-search
              :filter-option="filterMaterial"
              @change="handleMaterialChange"
            >
              <!-- 耗材选项需要从后端获取 -->
            </a-select>
          </a-form-item>
        </a-col>
        
        <a-col :span="12">
          <a-form-item label="批号" name="batchId">
            <a-select v-model:value="form.batchId" placeholder="请选择批号">
              <!-- 批号选项需要从后端获取 -->
            </a-select>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="数量" name="quantity">
            <a-input-number
              v-model:value="form.quantity"
              :min="1"
              :precision="0"
              style="width: 100%"
              placeholder="请输入数量"
            />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="仓库" name="warehouseId">
            <a-select v-model:value="form.warehouseId" placeholder="请选择仓库">
              <!-- 仓库选项需要从后端获取 -->
            </a-select>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="备注" name="remark" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-textarea
              v-model:value="form.remark"
              placeholder="请输入备注"
              :rows="3"
              :maxlength="200"
              show-count
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { labelApi } from '/@/api/spd/label-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    labelCode: undefined,
    materialId: undefined,
    batchId: undefined,
    quantity: 1,
    warehouseId: undefined,
    remark: '',
  });

  const rules = {
    materialId: [{ required: true, message: '请选择耗材', trigger: 'change' }],
    batchId: [{ required: true, message: '请选择批号', trigger: 'change' }],
    quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
    warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
  };

  function showModal(record) {
    visible.value = true;
    if (record) {
      form.labelCode = record.labelCode;
      form.materialId = record.materialId;
      form.batchId = record.batchId;
      form.quantity = record.quantity;
      form.warehouseId = record.warehouseId;
      form.remark = record.remark;
    } else {
      resetForm();
    }
  }

  function handleMaterialChange(value) {
    form.batchId = undefined;
  }

  async function handleOk() {
    try {
      await formRef.value.validate();

      confirmLoading.value = true;
      SmartLoading.show();

      const apiMethod = form.labelCode ? labelApi.update : labelApi.add;
      await apiMethod(form);

      message.success(form.labelCode ? '更新成功' : '新增成功');
      visible.value = false;
      emit('reloadList');
    } catch (error) {
      if (error.errorFields) {
        return;
      }
      smartSentry.captureError(error);
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
    form.labelCode = undefined;
    form.materialId = undefined;
    form.batchId = undefined;
    form.quantity = 1;
    form.warehouseId = undefined;
    form.remark = '';
    nextTick(() => {
      formRef.value?.clearValidate();
    });
  }

  function filterMaterial(input, option) {
    return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  }

  defineExpose({
    showModal,
  });
</script>
