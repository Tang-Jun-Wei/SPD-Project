<template>
  <a-drawer :title="'消耗单详情'" :width="1000" :open="visible" @close="onClose" :destroyOnClose="true">
    <a-spin :spinning="loading">
      <a-descriptions :column="2" bordered size="small">
        <a-descriptions-item label="消耗单号">{{ detail.consumeNo }}</a-descriptions-item>
        <a-descriptions-item label="科室">{{ detail.deptName }}</a-descriptions-item>
        <a-descriptions-item label="消耗类型">
          <a-tag v-if="detail.consumeType === 1" color="blue">正常消耗</a-tag>
          <a-tag v-else-if="detail.consumeType === 2" color="green">手术消耗</a-tag>
          <a-tag v-else-if="detail.consumeType === 3" color="red">急诊消耗</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="消耗日期">{{ detail.consumeDate }}</a-descriptions-item>
        <a-descriptions-item label="消耗人">{{ detail.consumeUserName }}</a-descriptions-item>
        <a-descriptions-item label="患者姓名">{{ detail.patientName || '-' }}</a-descriptions-item>
        <a-descriptions-item label="患者编号">{{ detail.patientNo || '-' }}</a-descriptions-item>
        <a-descriptions-item label="消耗总数量">{{ detail.totalQuantity }}</a-descriptions-item>
        <a-descriptions-item label="消耗总金额">{{ detail.totalAmount }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag v-if="detail.status === 1" color="green">正常</a-tag>
          <a-tag v-else-if="detail.status === 2" color="orange">已反消耗</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="创建人">{{ detail.createUserName }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>

      <!-- 消耗明细 -->
      <a-divider>消耗明细</a-divider>
      <a-table :dataSource="detail.detailList" :columns="detailColumns" rowKey="detailId" :pagination="false" size="small" bordered>
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'isReversed'">
            <a-tag v-if="record.isReversed === 0" color="green">正常</a-tag>
            <a-tag v-else-if="record.isReversed === 1" color="orange">已反消耗</a-tag>
          </template>
        </template>
      </a-table>
    </a-spin>
  </a-drawer>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { deptConsumeApi } from '/@/api/spd/dept-consume-api';
import { smartSentry } from '/@/lib/smart-sentry';

const visible = ref(false);
const loading = ref(false);
const detail = reactive({
  detailList: [],
});

// 明细表格列
const detailColumns = [
  { title: '耗材编码', dataIndex: 'materialCode', width: 120 },
  { title: '耗材名称', dataIndex: 'materialName', width: 150 },
  { title: '规格型号', dataIndex: 'specification', width: 120 },
  { title: '单位', dataIndex: 'unit', width: 80 },
  { title: '批号', dataIndex: 'batchNo', width: 100 },
  { title: '序列号/标签码', dataIndex: 'serialNo', width: 150 },
  { title: '效期', dataIndex: 'expiryDate', width: 100 },
  { title: '数量', dataIndex: 'quantity', width: 80 },
  { title: '单价', dataIndex: 'unitPrice', width: 80 },
  { title: '金额', dataIndex: 'amount', width: 80 },
  { title: '状态', dataIndex: 'isReversed', width: 100 },
];

// 显示抽屉
async function show(consumeId) {
  visible.value = true;
  loading.value = true;
  try {
    const res = await deptConsumeApi.getDetail(consumeId);
    if (res.data) {
      Object.assign(detail, res.data);
    }
  } catch (error) {
    smartSentry.captureError(error);
  } finally {
    loading.value = false;
  }
}

// 关闭抽屉
function onClose() {
  visible.value = false;
  Object.assign(detail, { detailList: [] });
}

defineExpose({
  show,
});
</script>
