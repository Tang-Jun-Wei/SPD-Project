<template>
  <a-modal
    :title="'新增消耗'"
    :width="1000"
    :open="visible"
    :maskClosable="false"
    :destroyOnClose="true"
    @cancel="onClose"
    @ok="onSubmit"
    :confirmLoading="loading"
  >
    <a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="科室" name="deptId">
            <a-input v-model:value="formData.deptName" placeholder="请选择科室" readonly />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="消耗类型" name="consumeType">
            <a-select v-model:value="formData.consumeType" placeholder="请选择">
              <a-select-option :value="1">正常消耗</a-select-option>
              <a-select-option :value="2">手术消耗</a-select-option>
              <a-select-option :value="3">急诊消耗</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="消耗日期" name="consumeDate">
            <a-date-picker
              v-model:value="formData.consumeDate"
              show-time
              format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择消耗日期"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="患者姓名">
            <a-input v-model:value="formData.patientName" placeholder="请输入患者姓名" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="患者编号">
            <a-input v-model:value="formData.patientNo" placeholder="请输入患者编号" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备注" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
            <a-textarea v-model:value="formData.remark" placeholder="请输入备注" :rows="2" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>

    <!-- 消耗明细 -->
    <a-divider>消耗明细</a-divider>
    <a-button @click="addDetail" type="primary" size="small" style="margin-bottom: 10px">
      <template #icon><PlusOutlined /></template>
      添加明细
    </a-button>

    <a-table :dataSource="formData.detailList" :columns="detailColumns" rowKey="key" :pagination="false" size="small" bordered>
      <template #bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'materialName'">
          <a-input v-model:value="record.materialName" placeholder="耗材名称" />
        </template>
        <template v-if="column.dataIndex === 'specification'">
          <a-input v-model:value="record.specification" placeholder="规格型号" />
        </template>
        <template v-if="column.dataIndex === 'quantity'">
          <a-input-number v-model:value="record.quantity" :min="0" :precision="2" style="width: 100%" />
        </template>
        <template v-if="column.dataIndex === 'unitPrice'">
          <a-input-number v-model:value="record.unitPrice" :min="0" :precision="2" style="width: 100%" />
        </template>
        <template v-if="column.dataIndex === 'batchNo'">
          <a-input v-model:value="record.batchNo" placeholder="批号" />
        </template>
        <template v-if="column.dataIndex === 'serialNo'">
          <a-input v-model:value="record.serialNo" placeholder="序列号/标签码" />
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-button @click="removeDetail(index)" type="link" danger size="small">删除</a-button>
        </template>
      </template>
    </a-table>
  </a-modal>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { deptConsumeApi } from '/@/api/spd/dept-consume-api';
import { smartSentry } from '/@/lib/smart-sentry';
import dayjs from 'dayjs';

const emit = defineEmits(['reloadList']);

const visible = ref(false);
const loading = ref(false);
const formRef = ref();

// 表单数据
const formData = reactive({
  deptId: null,
  deptName: '',
  consumeType: 1,
  consumeDate: dayjs(),
  patientName: '',
  patientNo: '',
  remark: '',
  detailList: [],
});

// 表单验证规则
const rules = {
  deptId: [{ required: true, message: '请选择科室' }],
  consumeType: [{ required: true, message: '请选择消耗类型' }],
  consumeDate: [{ required: true, message: '请选择消耗日期' }],
};

// 明细表格列
const detailColumns = [
  { title: '耗材名称', dataIndex: 'materialName', width: 150 },
  { title: '规格型号', dataIndex: 'specification', width: 120 },
  { title: '数量', dataIndex: 'quantity', width: 100 },
  { title: '单价', dataIndex: 'unitPrice', width: 100 },
  { title: '批号', dataIndex: 'batchNo', width: 120 },
  { title: '序列号/标签码', dataIndex: 'serialNo', width: 150 },
  { title: '操作', dataIndex: 'action', width: 80, fixed: 'right' },
];

// 显示弹窗
function show() {
  visible.value = true;
  // 默认添加一条明细
  formData.detailList = [createDetailRow()];
}

// 关闭弹窗
function onClose() {
  visible.value = false;
  formRef.value?.resetFields();
  formData.detailList = [];
}

// 创建明细行
let detailKey = 0;
function createDetailRow() {
  return {
    key: ++detailKey,
    materialId: null,
    materialName: '',
    specification: '',
    quantity: 1,
    unitPrice: 0,
    batchNo: '',
    serialNo: '',
  };
}

// 添加明细
function addDetail() {
  formData.detailList.push(createDetailRow());
}

// 删除明细
function removeDetail(index) {
  formData.detailList.splice(index, 1);
}

// 提交表单
async function onSubmit() {
  try {
    await formRef.value?.validate();
    
    if (formData.detailList.length === 0) {
      message.warning('请至少添加一条消耗明细');
      return;
    }

    loading.value = true;
    
    // 计算金额
    const detailList = formData.detailList.map(item => ({
      ...item,
      amount: item.quantity && item.unitPrice ? item.quantity * item.unitPrice : 0,
    }));

    await deptConsumeApi.add({
      ...formData,
      detailList,
    });
    
    message.success('新增成功');
    emit('reloadList');
    onClose();
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
}

defineExpose({
  show,
});
</script>
