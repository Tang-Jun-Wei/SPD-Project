<template>
  <a-modal
    :title="form.id ? '编辑单位' : '新增单位'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="单位编码" name="unitCode">
        <a-input v-model:value="form.unitCode" placeholder="请输入单位编码(可选)" />
      </a-form-item>

      <a-form-item label="单位名称" name="unitName" :rules="[{ required: true, message: '请输入单位名称' }]">
        <a-input v-model:value="form.unitName" placeholder="请输入单位名称" />
      </a-form-item>

      <a-form-item label="单位简称" name="unitAbbr">
        <a-input v-model:value="form.unitAbbr" placeholder="请输入单位简称" />
      </a-form-item>

      <a-form-item label="单位类型" name="unitType">
        <a-radio-group v-model:value="form.unitType">
          <a-radio :value="1">基本单位</a-radio>
          <a-radio :value="2">辅助单位</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="是否默认" name="isDefault">
        <a-radio-group v-model:value="form.isDefault">
          <a-radio :value="1">是</a-radio>
          <a-radio :value="0">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="排序号" name="sort">
        <a-input-number v-model:value="form.sort" :min="0" placeholder="数字越小越靠前" style="width:100%" />
      </a-form-item>

      <a-form-item label="状态" name="status">
        <a-radio-group v-model:value="form.status">
          <a-radio :value="1">启用</a-radio>
          <a-radio :value="0">停用</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="3" placeholder="请输入备注" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { unitApi } from '/@/api/spd/unit-api';

  const emits = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: null,
    unitCode: '',
    unitName: '',
    unitAbbr: '',
    unitType: 1,
    isDefault: 0,
    sort: 0,
    status: 1,
    remark: '',
  });

  async function showModal(record) {
    visible.value = true;
    await nextTick();
    formRef.value?.resetFields();

    if (record) {
      Object.assign(form, {
        id: record.id,
        unitCode: record.unitCode,
        unitName: record.unitName,
        unitAbbr: record.unitAbbr,
        unitType: record.unitType,
        isDefault: record.isDefault,
        sort: record.sort,
        status: record.status,
        remark: record.remark,
      });
    } else {
      Object.assign(form, {
        id: null,
        unitCode: '',
        unitName: '',
        unitAbbr: '',
        unitType: 1,
        isDefault: 0,
        sort: 0,
        status: 1,
        remark: '',
      });
    }
  }

  async function handleSubmit() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;

      const apiMethod = form.id ? unitApi.update : unitApi.add;
      await apiMethod(form);

      message.success(form.id ? '修改成功' : '新增成功');
      visible.value = false;
      emits('reloadList');
    } catch (error) {
      console.error('提交失败', error);
    } finally {
      confirmLoading.value = false;
    }
  }

  function handleCancel() {
    visible.value = false;
  }

  defineExpose({ showModal });
</script>
