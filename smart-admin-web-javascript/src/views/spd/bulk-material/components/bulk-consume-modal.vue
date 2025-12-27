<template>
  <a-modal
    title="散货消耗"
    :width="600"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable="false"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="散货ID">
        <span>{{ bulkInfo.bulkId }}</span>
      </a-form-item>

      <a-form-item label="耗材名称">
        <span>{{ bulkInfo.materialName }}</span>
      </a-form-item>

      <a-form-item label="规格型号">
        <span>{{ bulkInfo.specification }}</span>
      </a-form-item>

      <a-form-item label="批号">
        <span>{{ bulkInfo.batchNo }}</span>
      </a-form-item>

      <a-form-item label="剩余数量">
        <span style="color: #ff4d4f; font-weight: bold;">{{ bulkInfo.remainingNum }} {{ bulkInfo.unit }}</span>
      </a-form-item>

      <a-form-item label="消耗数量" name="consumeNum">
        <a-input-number
          v-model:value="form.consumeNum"
          :min="1"
          :max="bulkInfo.remainingNum"
          :precision="0"
          style="width: 100%"
          placeholder="请输入消耗数量"
        />
        <div style="color: #999; font-size: 12px; margin-top: 4px;">
          最大可消耗：{{ bulkInfo.remainingNum }} {{ bulkInfo.unit }}
        </div>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { bulkMaterialApi } from '/@/api/spd/bulk-material-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const bulkInfo = reactive({
    bulkId: '',
    materialName: '',
    specification: '',
    batchNo: '',
    remainingNum: 0,
    unit: '',
  });

  const form = reactive({
    consumeNum: 1,
  });

  const rules = {
    consumeNum: [
      { required: true, message: '请输入消耗数量', trigger: 'blur' },
      {
        validator: (rule, value) => {
          if (value <= 0) {
            return Promise.reject('消耗数量必须大于0');
          }
          if (value > bulkInfo.remainingNum) {
            return Promise.reject(`消耗数量不能超过剩余数量${bulkInfo.remainingNum}`);
          }
          return Promise.resolve();
        },
        trigger: 'change',
      },
    ],
  };

  function showModal(record) {
    visible.value = true;
    bulkInfo.bulkId = record.bulkId;
    bulkInfo.materialName = record.materialName;
    bulkInfo.specification = record.specification;
    bulkInfo.batchNo = record.batchNo;
    bulkInfo.remainingNum = record.remainingNum;
    bulkInfo.unit = record.unit;
    form.consumeNum = 1;
  }

  async function handleOk() {
    try {
      await formRef.value.validate();

      confirmLoading.value = true;
      SmartLoading.show();

      await bulkMaterialApi.consume(bulkInfo.bulkId, form.consumeNum);

      message.success('消耗成功');
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
