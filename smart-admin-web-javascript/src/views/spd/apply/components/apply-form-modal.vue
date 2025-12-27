<template>
  <a-modal
    :title="form.id ? '编辑申领单' : '新增申领单'"
    :width="1000"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable="false"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="申领科室" name="applyDeptId">
            <a-select v-model:value="form.applyDeptId" placeholder="请选择科室">
              <!-- 科室选项 -->
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="备注" name="remark">
            <a-input v-model:value="form.remark" placeholder="请输入备注" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-divider>申领明细</a-divider>

      <div style="margin-bottom: 16px">
        <a-button type="primary" @click="addDetail">
          <template #icon>
            <PlusOutlined />
          </template>
          添加明细
        </a-button>
      </div>

      <a-table
        :columns="detailColumns"
        :data-source="form.detailList"
        :pagination="false"
        rowKey="tempId"
        size="small"
        bordered
      >
        <template #bodyCell="{ text, record, column, index }">
          <template v-if="column.dataIndex === 'materialId'">
            <a-select
              v-model:value="record.materialId"
              placeholder="请选择耗材"
              style="width: 100%"
              show-search
              :filter-option="filterMaterial"
            >
              <!-- 耗材选项 -->
            </a-select>
          </template>

          <template v-if="column.dataIndex === 'batchId'">
            <a-select v-model:value="record.batchId" placeholder="请选择批号" style="width: 100%">
              <!-- 批号选项 -->
            </a-select>
          </template>

          <template v-if="column.dataIndex === 'applyQuantity'">
            <a-input-number
              v-model:value="record.applyQuantity"
              :min="1"
              :precision="0"
              style="width: 100%"
              placeholder="请输入数量"
            />
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <a-button danger type="link" size="small" @click="removeDetail(index)">删除</a-button>
          </template>
        </template>
      </a-table>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { applyApi } from '/@/api/spd/apply-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();

  const form = reactive({
    id: undefined,
    applyDeptId: undefined,
    remark: '',
    detailList: [],
  });

  const rules = {
    applyDeptId: [{ required: true, message: '请选择申领科室', trigger: 'change' }],
  };

  const detailColumns = [
    {
      title: '耗材名称',
      dataIndex: 'materialId',
      width: 200,
    },
    {
      title: '批号',
      dataIndex: 'batchId',
      width: 150,
    },
    {
      title: '申领数量',
      dataIndex: 'applyQuantity',
      width: 120,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 80,
    },
  ];

  let tempIdCounter = 0;

  function showModal(record) {
    visible.value = true;
    if (record) {
      form.id = record.id;
      form.applyDeptId = record.applyDeptId;
      form.remark = record.remark;
      // 加载明细数据
      loadDetail(record.id);
    } else {
      resetForm();
    }
  }

  async function loadDetail(id) {
    try {
      const res = await applyApi.getDetail(id);
      form.detailList = res.data.detailList.map(item => ({
        ...item,
        tempId: tempIdCounter++,
      }));
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  function addDetail() {
    form.detailList.push({
      tempId: tempIdCounter++,
      materialId: undefined,
      batchId: undefined,
      applyQuantity: 1,
    });
  }

  function removeDetail(index) {
    form.detailList.splice(index, 1);
  }

  async function handleOk() {
    try {
      await formRef.value.validate();

      if (form.detailList.length === 0) {
        message.warning('请至少添加一条申领明细');
        return;
      }

      confirmLoading.value = true;
      SmartLoading.show();

      const apiMethod = form.id ? applyApi.update : applyApi.add;
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
    form.applyDeptId = undefined;
    form.remark = '';
    form.detailList = [];
    tempIdCounter = 0;
    nextTick(() => {
      formRef.value?.clearValidate();
    });
  }

  function filterMaterial(input, option) {
    return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  }

  defineExpose({
    showModal,
  });
</script>
