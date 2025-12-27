<template>
  <a-modal
    :title="statusText"
    v-model:visible="visible"
    :width="500"
    :maskClosable="false"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="formRef">
      <a-form-item label="调拨单号">
        <span>{{ currentRecord.allocationCode }}</span>
      </a-form-item>
      
      <a-form-item label="审核意见" name="auditOpinion">
        <a-textarea v-model:value="form.auditOpinion" placeholder="请输入审核意见" :rows="4" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { allocationApi } from '/@/api/spd/allocation-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const formRef = ref();
  const currentRecord = ref({});
  const auditStatus = ref(2);
  
  const form = reactive({
    auditOpinion: '',
  });

  const statusText = computed(() => {
    return auditStatus.value === 2 ? '审核通过' : '审核驳回';
  });

  /**
   * 显示弹窗
   */
  function showModal(record, status) {
    visible.value = true;
    currentRecord.value = record;
    auditStatus.value = status;
    form.auditOpinion = '';
  }

  /**
   * 提交审核
   */
  async function handleSubmit() {
    const loading = SmartLoading.show();
    try {
      await allocationApi.audit({
        allocationId: currentRecord.value.allocationId,
        auditStatus: auditStatus.value,
        auditOpinion: form.auditOpinion,
      });
      message.success('审核成功');
      handleCancel();
      emit('reloadList');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide(loading);
    }
  }

  /**
   * 取消弹窗
   */
  function handleCancel() {
    visible.value = false;
    formRef.value?.resetFields();
  }

  defineExpose({ showModal });
</script>
