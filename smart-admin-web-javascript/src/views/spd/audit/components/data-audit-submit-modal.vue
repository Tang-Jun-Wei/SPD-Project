<template>
  <a-modal
    title="提交审核申请"
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
          <a-form-item label="审核类型" name="auditType">
            <a-select v-model:value="form.auditType" placeholder="请选择审核类型">
              <a-select-option :value="1">供应商</a-select-option>
              <a-select-option :value="2">耗材</a-select-option>
              <a-select-option :value="3">生产厂家</a-select-option>
              <a-select-option :value="4">调价</a-select-option>
              <a-select-option :value="5">收费状态</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="操作类型" name="actionType">
            <a-radio-group v-model:value="form.actionType">
              <a-radio :value="1">新增</a-radio>
              <a-radio :value="2">修改</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="目标业务ID" name="targetId">
            <a-input v-model:value="form.targetId" placeholder="请输入目标业务ID" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="目标名称" name="targetName">
            <a-input v-model:value="form.targetName" placeholder="请输入目标名称" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="提交理由" name="submitReason" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-textarea
              v-model:value="form.submitReason"
              placeholder="请输入提交理由"
              :rows="3"
              :maxlength="500"
              show-count
            />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="修改前数据" name="oldData" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-textarea
              v-model:value="form.oldData"
              placeholder="修改前数据（JSON格式，新增时可为空）"
              :rows="4"
            />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="新数据" name="newData" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-textarea
              v-model:value="form.newData"
              placeholder="新数据或修改后数据（JSON格式）"
              :rows="4"
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
  import { dataAuditApi } from '/@/api/spd/data-audit-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    auditType: undefined,
    actionType: 1,
    targetId: '',
    targetName: '',
    submitReason: '',
    oldData: '',
    newData: '',
  });

  const rules = {
    auditType: [{ required: true, message: '请选择审核类型', trigger: 'change' }],
    actionType: [{ required: true, message: '请选择操作类型', trigger: 'change' }],
    targetId: [{ required: true, message: '请输入目标业务ID', trigger: 'blur' }],
    submitReason: [{ required: true, message: '请输入提交理由', trigger: 'blur' }],
    newData: [{ required: true, message: '请输入新数据', trigger: 'blur' }],
  };

  function showModal() {
    visible.value = true;
    resetForm();
  }

  async function handleOk() {
    try {
      await formRef.value.validate();

      confirmLoading.value = true;
      SmartLoading.show();

      await dataAuditApi.submit(form);

      message.success('提交审核申请成功');
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
    form.auditType = undefined;
    form.actionType = 1;
    form.targetId = '';
    form.targetName = '';
    form.submitReason = '';
    form.oldData = '';
    form.newData = '';
    nextTick(() => {
      formRef.value?.clearValidate();
    });
  }

  defineExpose({
    showModal,
  });
</script>
