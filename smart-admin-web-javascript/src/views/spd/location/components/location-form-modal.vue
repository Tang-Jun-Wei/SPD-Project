<template>
  <a-modal
    :title="isEdit ? '编辑货位' : '新增货位'"
    v-model:visible="visible"
    :width="600"
    :maskClosable="false"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="formRef">
      <a-form-item label="货位编码" name="locationCode" :rules="[{ required: true, message: '请输入货位编码' }]">
        <a-input v-model:value="form.locationCode" placeholder="请输入货位编码（如:A-01-01）" />
      </a-form-item>
      
      <a-form-item label="货位名称" name="locationName" :rules="[{ required: true, message: '请输入货位名称' }]">
        <a-input v-model:value="form.locationName" placeholder="请输入货位名称" />
      </a-form-item>
      
      <a-form-item label="关联节点" name="nodeId" :rules="[{ required: true, message: '请选择关联节点' }]">
        <a-input v-model:value="form.nodeId" placeholder="请输入节点ID" />
      </a-form-item>
      
      <a-form-item label="货位层级" name="locationLevel">
        <a-select v-model:value="form.locationLevel" placeholder="请选择货位层级">
          <a-select-option :value="1">区</a-select-option>
          <a-select-option :value="2">架</a-select-option>
          <a-select-option :value="3">层</a-select-option>
          <a-select-option :value="4">位</a-select-option>
        </a-select>
      </a-form-item>
      
      <a-form-item label="货位类型" name="locationType">
        <a-select v-model:value="form.locationType" placeholder="请选择货位类型">
          <a-select-option :value="1">常温</a-select-option>
          <a-select-option :value="2">阴凉</a-select-option>
          <a-select-option :value="3">冷藏</a-select-option>
          <a-select-option :value="4">冷冻</a-select-option>
        </a-select>
      </a-form-item>
      
      <a-form-item label="最大容量" name="maxCapacity">
        <a-input-number v-model:value="form.maxCapacity" :min="0" style="width: 100%" placeholder="请输入最大容量" />
      </a-form-item>
      
      <a-form-item label="状态" name="locationStatus">
        <a-radio-group v-model:value="form.locationStatus">
          <a-radio :value="1">启用</a-radio>
          <a-radio :value="0">禁用</a-radio>
        </a-radio-group>
      </a-form-item>
      
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { locationApi } from '/@/api/spd/location-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const isEdit = ref(false);
  const formRef = ref();
  
  const form = reactive({
    locationId: '',
    locationCode: '',
    locationName: '',
    nodeId: '',
    locationLevel: undefined,
    locationType: undefined,
    maxCapacity: undefined,
    locationStatus: 1,
    remark: '',
  });

  async function showModal(record) {
    visible.value = true;
    isEdit.value = !!record?.locationId;
    
    if (record) {
      Object.assign(form, {
        locationId: record.locationId || '',
        locationCode: record.locationCode || '',
        locationName: record.locationName || '',
        nodeId: record.nodeId || '',
        locationLevel: record.locationLevel,
        locationType: record.locationType,
        maxCapacity: record.maxCapacity,
        locationStatus: record.locationStatus ?? 1,
        remark: record.remark || '',
      });
    } else {
      Object.assign(form, {
        locationId: '',
        locationCode: '',
        locationName: '',
        nodeId: '',
        locationLevel: undefined,
        locationType: undefined,
        maxCapacity: undefined,
        locationStatus: 1,
        remark: '',
      });
    }
    
    nextTick(() => formRef.value?.clearValidate());
  }

  async function handleSubmit() {
    try {
      await formRef.value.validate();
      
      const loading = SmartLoading.show();
      try {
        if (isEdit.value) {
          await locationApi.update(form);
          message.success('修改成功');
        } else {
          await locationApi.add(form);
          message.success('新增成功');
        }
        handleCancel();
        emit('reloadList');
      } catch (error) {
        smartSentry.captureError(error);
      } finally {
        SmartLoading.hide(loading);
      }
    } catch (error) {
      console.error('表单验证失败:', error);
    }
  }

  function handleCancel() {
    visible.value = false;
    formRef.value?.resetFields();
  }

  defineExpose({ showModal });
</script>
