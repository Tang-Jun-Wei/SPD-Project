<template>
  <a-modal
    :title="form.batchId ? '编辑批号' : '新增批号'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="批号" name="batchNo">
        <a-input v-model:value="form.batchNo" placeholder="请输入批号" />
      </a-form-item>
      <a-form-item label="耗材" name="materialId">
        <a-select v-model:value="form.materialId" placeholder="请选择耗材" show-search :filter-option="filterOption">
          <a-select-option v-for="item in materialOptions" :key="item.materialId" :value="item.materialId">
            {{ item.materialName }} - {{ item.spec }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="有效期" name="validDate">
        <a-date-picker v-model:value="form.validDate" placeholder="请选择有效期" style="width: 100%" />
      </a-form-item>
      <a-form-item label="供应商" name="supplierId">
        <a-select v-model:value="form.supplierId" placeholder="请选择供应商" show-search :filter-option="filterOption">
          <a-select-option v-for="item in supplierOptions" :key="item.id" :value="item.id">
            {{ item.supplierName }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import { batchApi } from '/@/api/spd/batch-api';
  import { materialApi } from '/@/api/spd/material-api';
  import { supplierApi } from '/@/api/spd/supplier-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const materialOptions = ref([]);
  const supplierOptions = ref([]);

  const form = reactive({
    batchId: undefined,
    batchNo: '',
    materialId: undefined,
    validDate: null,
    supplierId: undefined,
    remark: '',
  });

  const rules = {
    batchNo: [{ required: true, message: '请输入批号', trigger: 'blur' }],
    materialId: [{ required: true, message: '请选择耗材', trigger: 'change' }],
    validDate: [{ required: true, message: '请选择有效期', trigger: 'change' }],
  };

  function showModal(record) {
    visible.value = true;
    loadOptions();
    if (record) {
      Object.assign(form, record);
    } else {
      resetForm();
    }
  }

  async function loadOptions() {
    try {
      const [materialRes, supplierRes] = await Promise.all([
        materialApi.queryPage({ pageNum: 1, pageSize: 1000 }),
        supplierApi.queryPage({ pageNum: 1, pageSize: 1000 })
      ]);
      materialOptions.value = materialRes.data.list || [];
      supplierOptions.value = supplierRes.data.list || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  const filterOption = (input, option) => {
    return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  };

  function handleCancel() {
    visible.value = false;
    resetForm();
  }

  async function handleOk() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      
      if (form.batchId) {
        await batchApi.update(form);
        message.success('编辑成功');
      } else {
        await batchApi.add(form);
        message.success('新增成功');
      }
      
      visible.value = false;
      emit('reloadList');
      resetForm();
    } catch (error) {
      if (error.errorFields) return;
      smartSentry.captureError(error);
    } finally {
      confirmLoading.value = false;
    }
  }

  function resetForm() {
    Object.assign(form, {
      batchId: undefined,
      batchNo: '',
      materialId: undefined,
      validDate: null,
      supplierId: undefined,
      remark: '',
    });
    formRef.value?.resetFields();
  }

  defineExpose({ showModal });
</script>
