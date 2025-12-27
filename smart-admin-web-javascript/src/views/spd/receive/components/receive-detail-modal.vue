<template>
  <a-drawer title="收货单详情" :width="900" :visible="visible" @close="handleClose" :bodyStyle="{ paddingBottom: '80px' }">
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="收货单号">{{ detail.receiveNo }}</a-descriptions-item>
        <a-descriptions-item label="采购单号">{{ detail.purchaseNo }}</a-descriptions-item>
        <a-descriptions-item label="供应商">{{ detail.supplierName }}</a-descriptions-item>
        <a-descriptions-item label="收货人">{{ detail.receiveUserName }}</a-descriptions-item>
        <a-descriptions-item label="收货时间">{{ detail.receiveTime }}</a-descriptions-item>
        <a-descriptions-item label="仓库">{{ detail.warehouseName }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>
      <a-divider>收货明细</a-divider>
      <a-table :columns="detailColumns" :data-source="detail.detailList" :pagination="false" rowKey="id" size="small" bordered />
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { receiveApi } from '/@/api/spd/receive-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);
  const detail = reactive({ receiveNo: '', purchaseNo: '', supplierName: '', receiveUserName: '', receiveTime: '', warehouseName: '', remark: '', detailList: [] });
  const detailColumns = [
    { title: '序号', width: 60, customRender: ({ index }) => index + 1 },
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '规格型号', dataIndex: 'specification', width: 150 },
    { title: '采购数量', dataIndex: 'purchaseQuantity', width: 100 },
    { title: '收货数量', dataIndex: 'receiveQuantity', width: 100 },
  ];

  async function showModal(id) {
    visible.value = true;
    loading.value = true;
    try {
      const res = await receiveApi.getDetail(id);
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
