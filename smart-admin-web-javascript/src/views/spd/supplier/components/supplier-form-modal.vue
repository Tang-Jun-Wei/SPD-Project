<template>
  <a-modal
    :title="form.id ? '编辑供应商' : '新增供应商'"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable="false"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="供应商名称" name="supplierName">
            <a-input v-model:value="form.supplierName" placeholder="请输入供应商名称" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="联系人" name="contactPerson">
            <a-input v-model:value="form.contactPerson" placeholder="请输入联系人" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="联系电话" name="contactPhone">
            <a-input v-model:value="form.contactPhone" placeholder="请输入联系电话" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="邮箱" name="email">
            <a-input v-model:value="form.email" placeholder="请输入邮箱" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="地址" name="address" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-input v-model:value="form.address" placeholder="请输入地址" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="营业执照号" name="businessLicense">
            <a-input v-model:value="form.businessLicense" placeholder="请输入营业执照号" />
          </a-form-item>
        </a-col>

        <a-col :span="12">
          <a-form-item label="状态" name="status">
            <a-radio-group v-model:value="form.status">
              <a-radio :value="1">启用</a-radio>
              <a-radio :value="0">禁用</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="备注" name="remark" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-textarea
              v-model:value="form.remark"
              placeholder="请输入备注"
              :rows="4"
              :maxlength="500"
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
  import { supplierApi } from '/@/api/spd/supplier-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: undefined,
    supplierName: '',
    contactPerson: '',
    contactPhone: '',
    email: '',
    address: '',
    businessLicense: '',
    status: 1,
    remark: '',
  });

  const rules = {
    supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
    contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
    contactPhone: [
      { required: true, message: '请输入联系电话', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
    ],
    email: [
      { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ],
  };

  function showModal(record) {
    visible.value = true;
    if (record) {
      Object.assign(form, record);
    } else {
      resetForm();
    }
  }

  async function handleOk() {
    try {
      await formRef.value.validate();

      confirmLoading.value = true;
      SmartLoading.show();

      const apiMethod = form.id ? supplierApi.update : supplierApi.add;
      await apiMethod(form);

      message.success(form.id ? '更新成功' : '新增成功');
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
    form.id = undefined;
    form.supplierName = '';
    form.contactPerson = '';
    form.contactPhone = '';
    form.email = '';
    form.address = '';
    form.businessLicense = '';
    form.status = 1;
    form.remark = '';
    nextTick(() => {
      formRef.value?.clearValidate();
    });
  }

  defineExpose({
    showModal,
  });
</script>
