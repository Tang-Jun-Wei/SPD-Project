<template>
  <a-modal
    :title="form.id ? '编辑库存安全量配置' : '新增库存安全量配置'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="耗材" name="materialId">
        <a-select
          v-model:value="form.materialId"
          placeholder="请选择耗材"
          show-search
          :filter-option="filterMaterialOption"
          :disabled="!!form.id"
        >
          <a-select-option v-for="item in materialList" :key="item.materialId" :value="item.materialId">
            {{ item.materialName }} ({{ item.specification }})
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="仓库" name="warehouseId">
        <a-select
          v-model:value="form.warehouseId"
          placeholder="请选择仓库"
          :disabled="!!form.id"
        >
          <a-select-option v-for="item in warehouseList" :key="item.nodeId" :value="item.nodeId">
            {{ item.nodeName }}
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="最小库存量" name="minStock">
        <a-input-number
          v-model:value="form.minStock"
          placeholder="请输入最小库存量"
          :min="0"
          :precision="0"
          style="width: 100%"
        />
      </a-form-item>

      <a-form-item label="预警库存量" name="warningStock">
        <a-input-number
          v-model:value="form.warningStock"
          placeholder="请输入预警库存量（可选）"
          :min="0"
          :precision="0"
          style="width: 100%"
        />
      </a-form-item>

      <a-form-item label="最大库存量" name="maxStock">
        <a-input-number
          v-model:value="form.maxStock"
          placeholder="请输入最大库存量"
          :min="0"
          :precision="0"
          style="width: 100%"
        />
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea
          v-model:value="form.remark"
          placeholder="请输入备注"
          :rows="3"
          :maxlength="500"
          show-count
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { stockLimitApi } from '/@/api/spd/stock-limit-api';
  import { materialApi } from '/@/api/spd/material-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: undefined,
    materialId: undefined,
    warehouseId: undefined,
    minStock: 0,
    warningStock: undefined,
    maxStock: 0,
    remark: '',
  });

  const rules = {
    materialId: [{ required: true, message: '请选择耗材', trigger: 'change' }],
    warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
    minStock: [{ required: true, message: '请输入最小库存量', trigger: 'blur' }],
    maxStock: [{ required: true, message: '请输入最大库存量', trigger: 'blur' }],
  };

  const materialList = ref([]);
  const warehouseList = ref([]);

  // 显示弹窗
  async function showModal(row) {
    visible.value = true;
    resetForm();

    await loadMaterialList();
    await loadWarehouseList();

    if (row) {
      nextTick(() => {
        Object.assign(form, {
          id: row.id,
          materialId: row.materialId,
          warehouseId: row.warehouseId,
          minStock: row.minStock,
          warningStock: row.warningStock,
          maxStock: row.maxStock,
          remark: row.remark,
        });
      });
    }
  }

  // 重置表单
  function resetForm() {
    form.id = undefined;
    form.materialId = undefined;
    form.warehouseId = undefined;
    form.minStock = 0;
    form.warningStock = undefined;
    form.maxStock = 0;
    form.remark = '';
    formRef.value?.clearValidate();
  }

  // 确定
  async function handleOk() {
    try {
      await formRef.value.validateFields();

      // 验证逻辑
      if (form.minStock > form.maxStock) {
        message.error('最小库存量不能大于最大库存量');
        return;
      }
      if (form.warningStock && (form.warningStock < form.minStock || form.warningStock > form.maxStock)) {
        message.error('预警库存量应介于最小库存量和最大库存量之间');
        return;
      }

      confirmLoading.value = true;
      const loading = SmartLoading.show();

      try {
        const api = form.id ? stockLimitApi.update : stockLimitApi.add;
        const res = await api(form);

        if (res.data) {
          message.success(form.id ? '更新成功' : '新增成功');
          visible.value = false;
          emit('reloadList');
        } else {
          message.error(res.msg || '操作失败');
        }
      } finally {
        SmartLoading.hide(loading);
        confirmLoading.value = false;
      }
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  // 取消
  function handleCancel() {
    visible.value = false;
    resetForm();
  }

  // 加载耗材列表
  async function loadMaterialList() {
    try {
      const res = await materialApi.queryAll();
      if (res.data) {
        materialList.value = res.data || [];
      }
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  // 加载仓库列表
  async function loadWarehouseList() {
    try {
      const res = await orgNodeApi.queryByType({ nodeType: 2 });
      if (res.data) {
        warehouseList.value = res.data || [];
      }
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  // 过滤耗材选项
  function filterMaterialOption(input, option) {
    return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped lang="less"></style>
