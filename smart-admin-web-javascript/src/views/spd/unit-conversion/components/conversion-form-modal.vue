<template>
  <a-modal
    :title="form.id ? '编辑换算关系' : '新增换算关系'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="源单位" name="fromUnitId" :rules="[{ required: true, message: '请选择源单位' }]">
        <a-select v-model:value="form.fromUnitId" placeholder="请选择源单位" allow-clear>
          <a-select-option v-for="unit in unitList" :key="unit.id" :value="unit.id">
            {{ unit.unitName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="目标单位" name="toUnitId" :rules="[{ required: true, message: '请选择目标单位' }]">
        <a-select v-model:value="form.toUnitId" placeholder="请选择目标单位" allow-clear>
          <a-select-option v-for="unit in unitList" :key="unit.id" :value="unit.id">
            {{ unit.unitName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="换算比率" name="conversionRate" :rules="[{ required: true, message: '请输入换算比率' }]">
        <a-input-number v-model:value="form.conversionRate" :min="0.000001" :precision="6" placeholder="1源单位=N目标单位" style="width:100%" />
      </a-form-item>

      <a-form-item label="是否双向换算" name="isReciprocal">
        <a-radio-group v-model:value="form.isReciprocal">
          <a-radio :value="1">是</a-radio>
          <a-radio :value="0">否</a-radio>
        </a-radio-group>
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
  import { ref, reactive, nextTick, onMounted } from 'vue';
  import { message } from 'ant-design-vue';
  import { unitConversionApi } from '/@/api/spd/unit-conversion-api';
  import { unitApi } from '/@/api/spd/unit-api';

  const emits = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const unitList = ref([]);

  const form = reactive({
    id: null,
    fromUnitId: undefined,
    toUnitId: undefined,
    conversionRate: 1,
    isReciprocal: 1,
    status: 1,
    remark: '',
  });

  async function loadUnitList() {
    const res = await unitApi.queryAll();
    unitList.value = res.data || [];
  }

  async function showModal(record) {
    visible.value = true;
    await nextTick();
    formRef.value?.resetFields();
    await loadUnitList();

    if (record) {
      Object.assign(form, {
        id: record.id,
        fromUnitId: record.fromUnitId,
        toUnitId: record.toUnitId,
        conversionRate: record.conversionRate,
        isReciprocal: record.isReciprocal,
        status: record.status,
        remark: record.remark,
      });
    } else {
      Object.assign(form, {
        id: null,
        fromUnitId: undefined,
        toUnitId: undefined,
        conversionRate: 1,
        isReciprocal: 1,
        status: 1,
        remark: '',
      });
    }
  }

  async function handleSubmit() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;

      const apiMethod = form.id ? unitConversionApi.update : unitConversionApi.add;
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
