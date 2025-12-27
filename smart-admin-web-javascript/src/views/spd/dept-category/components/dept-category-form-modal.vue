<template>
  <a-modal
    :title="isEdit ? '编辑科室分类' : '新增科室分类'"
    v-model:visible="visible"
    :width="600"
    :maskClosable="false"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="formRef">
      <a-form-item label="上级分类" name="parentId">
        <a-tree-select
          v-model:value="form.parentId"
          :tree-data="categoryTreeData"
          placeholder="请选择上级分类(不选则为根节点)"
          :fieldNames="{ children: 'children', label: 'categoryName', value: 'categoryId' }"
          allowClear
          treeDefaultExpandAll
        />
      </a-form-item>
      
      <a-form-item label="分类名称" name="categoryName" :rules="[{ required: true, message: '请输入分类名称' }]">
        <a-input v-model:value="form.categoryName" placeholder="请输入分类名称" />
      </a-form-item>
      
      <a-form-item label="分类编码" name="categoryCode">
        <a-input v-model:value="form.categoryCode" placeholder="请输入分类编码" />
      </a-form-item>
      
      <a-form-item label="排序号" name="sortOrder">
        <a-input-number v-model:value="form.sortOrder" :min="0" style="width: 100%" />
      </a-form-item>
      
      <a-form-item label="状态" name="categoryStatus">
        <a-radio-group v-model:value="form.categoryStatus">
          <a-radio :value="1">启用</a-radio>
          <a-radio :value="0">禁用</a-radio>
        </a-radio-group>
      </a-form-item>
      
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { deptCategoryApi } from '/@/api/spd/dept-category-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const isEdit = ref(false);
  const formRef = ref();
  const categoryTreeData = ref([]);
  
  const form = reactive({
    categoryId: '',
    categoryName: '',
    categoryCode: '',
    parentId: undefined,
    categoryLevel: undefined,
    sortOrder: 0,
    categoryStatus: 1,
    remark: '',
  });

  async function showModal(record) {
    visible.value = true;
    isEdit.value = !!record?.categoryId;
    
    await loadCategoryTree();
    
    if (record) {
      Object.assign(form, {
        categoryId: record.categoryId || '',
        categoryName: record.categoryName || '',
        categoryCode: record.categoryCode || '',
        parentId: record.parentId,
        categoryLevel: record.categoryLevel,
        sortOrder: record.sortOrder ?? 0,
        categoryStatus: record.categoryStatus ?? 1,
        remark: record.remark || '',
      });
    } else {
      Object.assign(form, {
        categoryId: '',
        categoryName: '',
        categoryCode: '',
        parentId: undefined,
        categoryLevel: undefined,
        sortOrder: 0,
        categoryStatus: 1,
        remark: '',
      });
    }
    
    nextTick(() => formRef.value?.clearValidate());
  }

  async function loadCategoryTree() {
    try {
      const res = await deptCategoryApi.queryTree();
      categoryTreeData.value = res.data || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  async function handleSubmit() {
    try {
      await formRef.value.validate();
      
      const loading = SmartLoading.show();
      try {
        if (isEdit.value) {
          await deptCategoryApi.update(form);
          message.success('修改成功');
        } else {
          await deptCategoryApi.add(form);
          message.success('新增成功');
        }
        handleCancel();
        emit('reloadList');
      } catch (error) {
        smartSentry.captureError(error);
      } finally {
        SmartLoading.hide(loading);
      }
    } catch (error) {
      console.error('表单验证失败:', error);
    }
  }

  function handleCancel() {
    visible.value = false;
    formRef.value?.resetFields();
  }

  defineExpose({ showModal });
</script>
