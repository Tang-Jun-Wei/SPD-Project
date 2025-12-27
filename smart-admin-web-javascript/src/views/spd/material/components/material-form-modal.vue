<template>
  <a-modal
    :title="form.materialId ? '编辑耗材' : '新增耗材'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    width="700px"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="耗材名称" name="materialName">
        <a-input v-model:value="form.materialName" placeholder="请输入耗材名称" />
      </a-form-item>
      <a-form-item label="耗材编码" name="materialCode">
        <a-input v-model:value="form.materialCode" placeholder="请输入耗材编码" />
      </a-form-item>
      <a-form-item label="规格型号" name="specification">
        <a-input v-model:value="form.specification" placeholder="请输入规格型号" />
      </a-form-item>
      <a-form-item label="单位" name="unit">
        <a-input v-model:value="form.unit" placeholder="请输入单位（如：盒、支、个）" />
      </a-form-item>
      <a-form-item label="生产厂家" name="manufacturer">
        <a-input v-model:value="form.manufacturer" placeholder="请输入生产厂家" />
      </a-form-item>
      <a-form-item label="供应商" name="supplierId">
        <a-select v-model:value="form.supplierId" placeholder="请选择供应商" @change="handleSupplierChange" allowClear>
          <a-select-option v-for="item in supplierOptions" :key="item.id" :value="item.id">
            {{ item.supplierName }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="单价" name="unitPrice">
        <a-input-number v-model:value="form.unitPrice" :min="0" :precision="2" placeholder="请输入单价" style="width: 100%" />
      </a-form-item>
      <a-form-item label="是否集采" name="isGroupBuy">
        <a-radio-group v-model:value="form.isGroupBuy">
          <a-radio :value="0">否</a-radio>
          <a-radio :value="1">是</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="UDI编码" name="udiCode">
        <a-input v-model:value="form.udiCode" placeholder="请输入UDI编码" />
      </a-form-item>
      <a-form-item label="注册证号" name="registrationNo">
        <a-input v-model:value="form.registrationNo" placeholder="请输入注册证号" />
      </a-form-item>
      <a-form-item label="注册证有效期" name="registrationExpiry">
        <a-date-picker v-model:value="form.registrationExpiry" placeholder="请选择有效期" style="width: 100%" />
      </a-form-item>
      <a-form-item label="耗材分类" name="category">
        <a-input v-model:value="form.category" placeholder="请输入耗材分类" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { materialApi } from '/@/api/spd/material-api';
  import { supplierApi } from '/@/api/spd/supplier-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const supplierOptions = ref([]);

  const form = reactive({
    materialId: undefined,
    materialName: '',
    materialCode: '',
    specification: '',
    unit: '',
    manufacturer: '',
    supplierId: undefined,
    supplierName: '',
    unitPrice: undefined,
    isGroupBuy: 0,
    udiCode: '',
    registrationNo: '',
    registrationExpiry: null,
    category: '',
    materialStatus: 1,
    remark: '',
  });

  const rules = {
    materialName: [{ required: true, message: '请输入耗材名称', trigger: 'blur' }],
    specification: [{ required: true, message: '请输入规格型号', trigger: 'blur' }],
    unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
    manufacturer: [{ required: true, message: '请输入生产厂家', trigger: 'blur' }],
    unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }],
  };

  function showModal(record) {
    visible.value = true;
    loadSuppliers();
    if (record) {
      Object.assign(form, record);
    } else {
      resetForm();
    }
  }

  async function loadSuppliers() {
    try {
      const res = await supplierApi.queryPage({ pageNum: 1, pageSize: 1000 });
      supplierOptions.value = res.data.list || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  function handleSupplierChange(supplierId) {
    const supplier = supplierOptions.value.find(s => s.id === supplierId);
    if (supplier) {
      form.supplierName = supplier.supplierName;
    }
  }

  function handleCancel() {
    visible.value = false;
    resetForm();
  }

  async function handleOk() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      
      if (form.materialId) {
        await materialApi.update(form);
        message.success('编辑成功');
      } else {
        await materialApi.add(form);
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
      materialId: undefined,
      materialName: '',
      materialCode: '',
      specification: '',
      unit: '',
      manufacturer: '',
      supplierId: undefined,
      supplierName: '',
      unitPrice: undefined,
      isGroupBuy: 0,
      udiCode: '',
      registrationNo: '',
      registrationExpiry: null,
      category: '',
      materialStatus: 1,
      remark: '',
    });
    formRef.value?.resetFields();
  }

  defineExpose({ showModal });
</script>
