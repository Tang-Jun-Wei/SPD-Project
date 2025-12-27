<template>
  <a-modal
    title="审核申领单"
    :width="600"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable="false"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="申领单号">
        <span>{{ applyInfo.applyNo }}</span>
      </a-form-item>

      <a-form-item label="申领科室">
        <span>{{ applyInfo.applyDeptName }}</span>
      </a-form-item>

      <a-form-item label="申领人">
        <span>{{ applyInfo.applyUserName }}</span>
      </a-form-item>

      <a-form-item label="审核结果" name="approveStatus">
        <a-radio-group v-model:value="form.approveStatus">
          <a-radio :value="2">通过</a-radio>
          <a-radio :value="3">驳回</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="审核意见" name="approveRemark">
        <a-textarea
          v-model:value="form.approveRemark"
          placeholder="请输入审核意见"
          :rows="4"
          :maxlength="200"
          show-count
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { applyApi } from '/@/api/spd/apply-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const applyInfo = reactive({
    id: undefined,
    applyNo: '',
    applyDeptName: '',
    applyUserName: '',
  });

  const form = reactive({
    approveStatus: 2,
    approveRemark: '',
  });

  const rules = {
    approveStatus: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
  };

  function showModal(record) {
    visible.value = true;
    applyInfo.id = record.id;
    applyInfo.applyNo = record.applyNo;
    applyInfo.applyDeptName = record.applyDeptName;
    applyInfo.applyUserName = record.applyUserName;
    form.approveStatus = 2;
    form.approveRemark = '';
  }

  async function handleOk() {
    try {
      await formRef.value.validate();

      confirmLoading.value = true;
      SmartLoading.show();

      await applyApi.approve(applyInfo.id, form.approveStatus, form.approveRemark);

      message.success('审核成功');
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
    nextTick(() => {
      formRef.value?.clearValidate();
    });
  }

  defineExpose({
    showModal,
  });
</script>
