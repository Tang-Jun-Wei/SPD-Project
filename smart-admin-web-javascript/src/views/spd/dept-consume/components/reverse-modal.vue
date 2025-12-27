<template>
  <a-modal
    title="反消耗"
    :width="500"
    :open="visible"
    :maskClosable="false"
    @cancel="onClose"
    @ok="onSubmit"
    :confirmLoading="loading"
  >
    <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="消耗单号">
        <span>{{ record.consumeNo }}</span>
      </a-form-item>
      <a-form-item label="科室">
        <span>{{ record.deptName }}</span>
      </a-form-item>
      <a-form-item label="消耗总数量">
        <span>{{ record.totalQuantity }}</span>
      </a-form-item>
      <a-form-item label="反消耗原因">
        <a-textarea v-model:value="reason" placeholder="请输入反消耗原因" :rows="4" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { deptConsumeApi } from '/@/api/spd/dept-consume-api';
import { smartSentry } from '/@/lib/smart-sentry';

const emit = defineEmits(['reloadList']);

const visible = ref(false);
const loading = ref(false);
const record = reactive({});
const reason = ref('');

// 显示弹窗
function show(data) {
  visible.value = true;
  Object.assign(record, data);
  reason.value = '';
}

// 关闭弹窗
function onClose() {
  visible.value = false;
  Object.assign(record, {});
  reason.value = '';
}

// 提交
async function onSubmit() {
  try {
    loading.value = true;
    await deptConsumeApi.reverse(record.consumeId, reason.value);
    message.success('反消耗成功');
    emit('reloadList');
    onClose();
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
}

defineExpose({
  show,
});
</script>
