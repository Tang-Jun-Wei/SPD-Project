<template>
  <a-modal
    :title="form.id ? '编辑分类' : '新增分类'"
    :open="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    width="600px"
  >
    <a-form ref="formRef" :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="分类名称" name="categoryName" :rules="[{ required: true, message: '请输入分类名称' }]">
        <a-input v-model:value="form.categoryName" placeholder="请输入分类名称" />
      </a-form-item>

      <a-form-item label="分类编码" name="categoryCode">
        <a-input v-model:value="form.categoryCode" placeholder="请输入分类编码(可选)" />
      </a-form-item>

      <a-form-item label="父分类" name="parentId" :rules="[{ required: true, message: '请选择父分类' }]">
        <a-tree-select
          v-model:value="form.parentId"
          :tree-data="categoryTree"
          :fieldNames="{ label: 'categoryName', value: 'id', children: 'children' }"
          placeholder="请选择父分类(选择顶级则为一级分类)"
          allow-clear
          tree-default-expand-all
        >
          <template #title="{ categoryName }">
            {{ categoryName }}
          </template>
        </a-tree-select>
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
  import { categoryApi } from '/@/api/spd/category-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';

  const emits = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const categoryTree = ref([]);

  const form = reactive({
    id: null,
    categoryName: '',
    categoryCode: '',
    parentId: 0,
    sort: 0,
    status: 1,
    remark: '',
  });

  async function showModal(record, parentId) {
    visible.value = true;
    await nextTick();
    formRef.value?.resetFields();

    // 加载分类树
    await loadCategoryTree();

    if (record) {
      // 编辑模式
      Object.assign(form, {
        id: record.id,
        categoryName: record.categoryName,
        categoryCode: record.categoryCode,
        parentId: record.parentId,
        sort: record.sort,
        status: record.status,
        remark: record.remark,
      });
    } else {
      // 新增模式
      Object.assign(form, {
        id: null,
        categoryName: '',
        categoryCode: '',
        parentId: parentId || 0,
        sort: 0,
        status: 1,
        remark: '',
      });
    }
  }

  async function loadCategoryTree() {
    try {
      const res = await categoryApi.queryTree();
      categoryTree.value = [
        { id: 0, categoryName: '顶级分类', children: res.data || [] }
      ];
    } catch (error) {
      console.error('加载分类树失败', error);
    }
  }

  async function handleSubmit() {
    try {
      await formRef.value.validate();
      confirmLoading.value = true;

      const apiMethod = form.id ? categoryApi.update : categoryApi.add;
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
