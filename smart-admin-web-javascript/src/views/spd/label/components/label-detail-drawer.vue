<template>
  <a-drawer
    title="标签详情"
    :width="800"
    :visible="visible"
    @close="handleClose"
    :bodyStyle="{ paddingBottom: '80px' }"
  >
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="标签码">{{ detail.labelCode }}</a-descriptions-item>
        <a-descriptions-item label="标签状态">
          <a-tag v-if="detail.labelStatus === 0" color="default">已生成</a-tag>
          <a-tag v-else-if="detail.labelStatus === 1" color="green">已入库</a-tag>
          <a-tag v-else-if="detail.labelStatus === 2" color="orange">已领用</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="耗材名称">{{ detail.materialName }}</a-descriptions-item>
        <a-descriptions-item label="规格型号">{{ detail.specification }}</a-descriptions-item>
        <a-descriptions-item label="批号">{{ detail.batchNo }}</a-descriptions-item>
        <a-descriptions-item label="数量">{{ detail.quantity }} {{ detail.unit }}</a-descriptions-item>
        <a-descriptions-item label="仓库">{{ detail.warehouseName }}</a-descriptions-item>
        <a-descriptions-item label="生产日期">{{ detail.productionDate || '-' }}</a-descriptions-item>
        <a-descriptions-item label="有效期至">{{ detail.expiryDate || '-' }}</a-descriptions-item>
        <a-descriptions-item label="生成时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="入库时间">{{ detail.inboundTime || '-' }}</a-descriptions-item>
        <a-descriptions-item label="领用时间">{{ detail.useTime || '-' }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { labelApi } from '/@/api/spd/label-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);

  const detail = reactive({
    labelCode: '',
    labelStatus: undefined,
    materialName: '',
    specification: '',
    batchNo: '',
    quantity: 0,
    unit: '',
    warehouseName: '',
    productionDate: '',
    expiryDate: '',
    createTime: '',
    inboundTime: '',
    useTime: '',
    remark: '',
  });

  async function showDrawer(labelCode) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await labelApi.getDetail(labelCode);
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

  defineExpose({
    showDrawer,
  });
</script>
