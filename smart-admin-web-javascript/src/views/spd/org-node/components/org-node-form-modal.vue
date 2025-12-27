<template>
  <a-modal
    :title="form.nodeId ? '编辑节点' : '新增节点'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="节点名称" name="nodeName">
        <a-input v-model:value="form.nodeName" placeholder="请输入节点名称" />
      </a-form-item>
      <a-form-item label="节点类型" name="nodeType">
        <a-select v-model:value="form.nodeType" placeholder="请选择节点类型">
          <a-select-option :value="1">科室</a-select-option>
          <a-select-option :value="2">仓库</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="上级节点" name="parentNodeId">
        <a-select v-model:value="form.parentNodeId" placeholder="请选择上级节点" allowClear>
          <a-select-option v-for="node in parentNodeOptions" :key="node.nodeId" :value="node.nodeId">
            {{ node.nodeName }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="负责人" name="responsiblePerson">
        <a-input v-model:value="form.responsiblePerson" placeholder="请输入负责人姓名" />
      </a-form-item>
      <a-form-item label="联系电话" name="contactPhone">
        <a-input v-model:value="form.contactPhone" placeholder="请输入联系电话" />
      </a-form-item>
      <a-form-item label="地址" name="address">
        <a-textarea v-model:value="form.address" placeholder="请输入地址" :rows="2" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="2" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const parentNodeOptions = ref([]);

  const form = reactive({
    nodeId: undefined,
    nodeName: '',
    nodeType: undefined,
    parentNodeId: undefined,
    responsiblePerson: '',
    contactPhone: '',
    address: '',
    remark: '',
  });

  const rules = {
    nodeName: [{ required: true, message: '请输入节点名称', trigger: 'blur' }],
    nodeType: [{ required: true, message: '请选择节点类型', trigger: 'change' }],
  };

  async function showModal(record) {
    visible.value = true;
    await loadParentNodes();
    if (record) {
      Object.assign(form, record);
    } else {
      resetForm();
    }
  }

  async function loadParentNodes() {
    try {
      const res = await orgNodeApi.queryPage({ pageNum: 1, pageSize: 100 });
      parentNodeOptions.value = res.data.list || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  function handleCancel() {
    visible.value = false;
    resetForm();
  }

  async function handleOk() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;
      
      if (form.nodeId) {
        await orgNodeApi.update(form);
        message.success('编辑成功');
      } else {
        await orgNodeApi.add(form);
        message.success('新增成功');
      }
      
      visible.value = false;
      emit('reloadList');
      resetForm();
    } catch (error) {
      if (error.errorFields) return;
      smartSentry.captureError(error);
    } finally {
      confirmLoading.value = false;
    }
  }

  function resetForm() {
    Object.assign(form, {
      nodeId: undefined,
      nodeName: '',
      nodeType: undefined,
      parentNodeId: undefined,
      responsiblePerson: '',
      contactPhone: '',
      address: '',
      remark: '',
    });
    formRef.value?.resetFields();
  }

  defineExpose({ showModal });
</script>
