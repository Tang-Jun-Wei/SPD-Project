<template>
  <a-drawer title="采购单详情" :width="900" :visible="visible" @close="handleClose" :bodyStyle="{ paddingBottom: '80px' }">
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="采购单号">{{ detail.purchaseNo }}</a-descriptions-item>
        <a-descriptions-item label="采购状态">
          <a-tag v-if="detail.purchaseStatus === 1" color="orange">待审核</a-tag>
          <a-tag v-else-if="detail.purchaseStatus === 2" color="green">已审核</a-tag>
          <a-tag v-else-if="detail.purchaseStatus === 3" color="red">已驳回</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="供应商">{{ detail.supplierName }}</a-descriptions-item>
        <a-descriptions-item label="采购人">{{ detail.purchaseUserName }}</a-descriptions-item>
        <a-descriptions-item label="采购时间">{{ detail.purchaseTime }}</a-descriptions-item>
        <a-descriptions-item label="审核人">{{ detail.approveUserName || '-' }}</a-descriptions-item>
        <a-descriptions-item label="审核时间">{{ detail.approveTime || '-' }}</a-descriptions-item>
        <a-descriptions-item label="总金额">{{ detail.totalAmount }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>
      <a-divider>采购明细</a-divider>
      <a-table :columns="detailColumns" :data-source="detail.detailList" :pagination="false" rowKey="id" size="small" bordered />
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { purchaseApi } from '/@/api/spd/purchase-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);
  const detail = reactive({ purchaseNo: '', purchaseStatus: undefined, supplierName: '', purchaseUserName: '', purchaseTime: '', approveUserName: '', approveTime: '', totalAmount: 0, remark: '', detailList: [] });
  const detailColumns = [
    { title: '序号', width: 60, customRender: ({ index }) => index + 1 },
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '规格型号', dataIndex: 'specification', width: 150 },
    { title: '数量', dataIndex: 'quantity', width: 100 },
    { title: '单价', dataIndex: 'unitPrice', width: 100 },
    { title: '金额', dataIndex: 'amount', width: 100 },
  ];

  async function showModal(id) {
    visible.value = true;
    loading.value = true;
    try {
      const res = await purchaseApi.getDetail(id);
      Object.assign(detail, res.data);
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      loading.value = false;
    }
  }

  function handleClose() {
    visible.value = false;
  }

  defineExpose({ showModal });
</script>
