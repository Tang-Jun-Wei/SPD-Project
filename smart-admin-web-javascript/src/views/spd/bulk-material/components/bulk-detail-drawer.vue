<template>
  <a-drawer
    title="散货详情"
    :width="800"
    :visible="visible"
    @close="handleClose"
    :bodyStyle="{ paddingBottom: '80px' }"
  >
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="散货ID">{{ detail.bulkId }}</a-descriptions-item>
        <a-descriptions-item label="耗材名称">{{ detail.materialName }}</a-descriptions-item>
        <a-descriptions-item label="规格型号">{{ detail.specification }}</a-descriptions-item>
        <a-descriptions-item label="批号">{{ detail.batchNo }}</a-descriptions-item>
        <a-descriptions-item label="总数量">
          <span style="font-weight: bold;">{{ detail.totalNum }} {{ detail.unit }}</span>
        </a-descriptions-item>
        <a-descriptions-item label="已用数量">
          <span style="color: #faad14;">{{ detail.usedNum }} {{ detail.unit }}</span>
        </a-descriptions-item>
        <a-descriptions-item label="剩余数量">
          <span 
            :style="{ 
              color: detail.remainingNum <= 10 ? '#ff4d4f' : detail.remainingNum <= 50 ? '#faad14' : '#52c41a',
              fontWeight: 'bold'
            }"
          >
            {{ detail.remainingNum }} {{ detail.unit }}
          </span>
        </a-descriptions-item>
        <a-descriptions-item label="使用率">
          <a-progress
            :percent="Math.round((detail.usedNum / detail.totalNum) * 100)"
            :status="detail.remainingNum === 0 ? 'exception' : 'active'"
          />
        </a-descriptions-item>
        <a-descriptions-item label="仓库">{{ detail.warehouseName }}</a-descriptions-item>
        <a-descriptions-item label="生产日期">{{ detail.productionDate || '-' }}</a-descriptions-item>
        <a-descriptions-item label="有效期至">{{ detail.expiryDate || '-' }}</a-descriptions-item>
        <a-descriptions-item label="入库时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>

      <a-divider>消耗记录</a-divider>

      <a-timeline>
        <a-timeline-item v-for="(record, index) in detail.consumeRecords" :key="index" color="blue">
          <p>消耗数量：<span style="color: #ff4d4f; font-weight: bold;">{{ record.consumeNum }} {{ detail.unit }}</span></p>
          <p>操作人：{{ record.operatorName }}</p>
          <p>操作时间：{{ record.operateTime }}</p>
          <p v-if="record.remark">备注：{{ record.remark }}</p>
        </a-timeline-item>
        <a-timeline-item v-if="!detail.consumeRecords || detail.consumeRecords.length === 0" color="gray">
          暂无消耗记录
        </a-timeline-item>
      </a-timeline>
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { bulkMaterialApi } from '/@/api/spd/bulk-material-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);

  const detail = reactive({
    bulkId: '',
    materialName: '',
    specification: '',
    batchNo: '',
    totalNum: 0,
    usedNum: 0,
    remainingNum: 0,
    unit: '',
    warehouseName: '',
    productionDate: '',
    expiryDate: '',
    createTime: '',
    remark: '',
    consumeRecords: [],
  });

  async function showDrawer(bulkId) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await bulkMaterialApi.getDetail(bulkId);
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
