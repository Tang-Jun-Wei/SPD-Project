<template>
  <a-modal
    title="拒收"
    v-model:visible="visible"
    :width="500"
    :maskClosable="false"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="formRef">
      <a-form-item label="收货单号">
        <span>{{ currentRecord.receiveCode }}</span>
      </a-form-item>
      
      <a-form-item label="拒收原因" name="reason">
        <a-textarea v-model:value="form.reason" placeholder="请输入拒收原因" :rows="4" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { deptReceiveApi } from '/@/api/spd/dept-receive-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const formRef = ref();
  const currentRecord = ref({});
  
  const form = reactive({
    reason: '',
  });

  /**
   * 显示弹窗
   */
  function showModal(record) {
    visible.value = true;
    currentRecord.value = record;
    form.reason = '';
  }

  /**
   * 提交拒收
   */
  async function handleSubmit() {
    const loading = SmartLoading.show();
    try {
      await deptReceiveApi.rejectReceive(currentRecord.value.receiveId, form.reason);
      message.success('拒收成功');
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
