<template>
  <a-modal
    title="新增退货单"
    v-model:visible="visible"
    :width="900"
    :maskClosable="false"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="formRef">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="供应商ID" name="supplierId" :rules="[{ required: true, message: '请输入供应商ID' }]">
            <a-input v-model:value="form.supplierId" placeholder="请输入供应商ID" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="退货类型" name="returnType" :rules="[{ required: true, message: '请选择退货类型' }]">
            <a-select v-model:value="form.returnType" placeholder="请选择退货类型">
              <a-select-option :value="1">质量问题</a-select-option>
              <a-select-option :value="2">过期退货</a-select-option>
              <a-select-option :value="3">订单错误</a-select-option>
              <a-select-option :value="4">其他</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      
      <a-form-item label="备注" name="remark" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="2" />
      </a-form-item>

      <a-divider orientation="left">退货明细</a-divider>

      <a-table :dataSource="form.detailList" :columns="detailColumns" :pagination="false" rowKey="index" size="small" bordered>
        <template #bodyCell="{ text, record, column, index }">
          <template v-if="column.dataIndex === 'materialId'">
            <a-input v-model:value="record.materialId" placeholder="耗材ID" />
          </template>
          <template v-if="column.dataIndex === 'batchId'">
            <a-input v-model:value="record.batchId" placeholder="批号ID" />
          </template>
          <template v-if="column.dataIndex === 'returnQuantity'">
            <a-input-number v-model:value="record.returnQuantity" :min="1" style="width: 100%" />
          </template>
          <template v-if="column.dataIndex === 'returnReason'">
            <a-input v-model:value="record.returnReason" placeholder="退货原因" />
          </template>
          <template v-if="column.dataIndex === 'operate'">
            <a-button danger type="link" size="small" @click="removeDetail(index)">删除</a-button>
          </template>
        </template>
      </a-table>

      <a-button type="dashed" block @click="addDetail" style="margin-top: 10px"><PlusOutlined />添加明细</a-button>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { supplierReturnApi } from '/@/api/spd/supplier-return-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const formRef = ref();
  
  const form = reactive({
    supplierId: '',
    returnType: undefined,
    remark: '',
    detailList: [],
  });

  const detailColumns = ref([
    { title: '耗材ID', dataIndex: 'materialId', width: 180 },
    { title: '批号ID', dataIndex: 'batchId', width: 180 },
    { title: '退货数量', dataIndex: 'returnQuantity', width: 100 },
    { title: '退货原因', dataIndex: 'returnReason', width: 200 },
    { title: '操作', dataIndex: 'operate', width: 80 },
  ]);

  /**
   * 显示弹窗
   */
  function showModal() {
    visible.value = true;
    form.supplierId = '';
    form.returnType = undefined;
    form.remark = '';
    form.detailList = [];
    nextTick(() => formRef.value?.clearValidate());
  }

  /**
   * 添加明细行
   */
  function addDetail() {
    form.detailList.push({
      index: Date.now(),
      materialId: '',
      batchId: '',
      returnQuantity: 1,
      returnReason: '',
    });
  }

  /**
   * 删除明细行
   */
  function removeDetail(index) {
    form.detailList.splice(index, 1);
  }

  /**
   * 提交表单
   */
  async function handleSubmit() {
    try {
      await formRef.value.validate();
      
      if (form.detailList.length === 0) {
        message.warning('请至少添加一条退货明细');
        return;
      }
      
      const loading = SmartLoading.show();
      try {
        await supplierReturnApi.add(form);
        message.success('新增成功');
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

  /**
   * 取消弹窗
   */
  function handleCancel() {
    visible.value = false;
    formRef.value?.resetFields();
  }

  defineExpose({ showModal });
</script>
