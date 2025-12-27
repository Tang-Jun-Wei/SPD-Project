<template>
  <a-modal
    :title="form.id ? '编辑仓库人员关系' : '新增仓库人员关系'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
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

      <a-form-item label="用户" name="userId">
        <a-select
          v-model:value="form.userId"
          placeholder="请选择用户"
          show-search
          :filter-option="filterUserOption"
          :disabled="!!form.id"
        >
          <a-select-option v-for="item in userList" :key="item.employeeId" :value="item.employeeId">
            {{ item.actualName }} ({{ item.userName }})
          </a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="角色类型" name="roleType">
        <a-select v-model:value="form.roleType" placeholder="请选择角色类型">
          <a-select-option :value="1">仓库管理员</a-select-option>
          <a-select-option :value="2">普通库管员</a-select-option>
          <a-select-option :value="3">拣货员</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="状态" name="status">
        <a-radio-group v-model:value="form.status">
          <a-radio :value="1">启用</a-radio>
          <a-radio :value="0">禁用</a-radio>
        </a-radio-group>
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
  import { warehouseUserApi } from '/@/api/spd/warehouse-user-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { employeeApi } from '/@/api/system/employee-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: undefined,
    warehouseId: undefined,
    userId: undefined,
    roleType: 1,
    status: 1,
    remark: '',
  });

  const rules = {
    warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
    userId: [{ required: true, message: '请选择用户', trigger: 'change' }],
    roleType: [{ required: true, message: '请选择角色类型', trigger: 'change' }],
  };

  const warehouseList = ref([]);
  const userList = ref([]);

  // 显示弹窗
  async function showModal(row) {
    visible.value = true;
    resetForm();

    await loadWarehouseList();
    await loadUserList();

    if (row) {
      nextTick(() => {
        Object.assign(form, {
          id: row.id,
          warehouseId: row.warehouseId,
          userId: row.userId,
          roleType: row.roleType,
          status: row.status,
          remark: row.remark,
        });
      });
    }
  }

  // 重置表单
  function resetForm() {
    form.id = undefined;
    form.warehouseId = undefined;
    form.userId = undefined;
    form.roleType = 1;
    form.status = 1;
    form.remark = '';
    formRef.value?.clearValidate();
  }

  // 确定
  async function handleOk() {
    try {
      await formRef.value.validateFields();

      confirmLoading.value = true;
      const loading = SmartLoading.show();

      try {
        const api = form.id ? warehouseUserApi.update : warehouseUserApi.add;
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

  // 加载用户列表
  async function loadUserList() {
    try {
      const res = await employeeApi.queryAll();
      if (res.data) {
        userList.value = res.data || [];
      }
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  // 过滤用户选项
  function filterUserOption(input, option) {
    return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped lang="less"></style>
