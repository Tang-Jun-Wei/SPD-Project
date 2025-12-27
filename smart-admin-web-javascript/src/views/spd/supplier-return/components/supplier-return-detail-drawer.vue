<template>
  <a-drawer
    title="退货单详情"
    v-model:visible="visible"
    width="800"
    :maskClosable="false"
  >
    <a-descriptions title="基本信息" :column="2" bordered>
      <a-descriptions-item label="退货单号">{{ detail.returnCode }}</a-descriptions-item>
      <a-descriptions-item label="状态">
        <a-tag v-if="detail.returnStatus === 1" color="blue">待审核</a-tag>
        <a-tag v-else-if="detail.returnStatus === 2" color="green">已通过</a-tag>
        <a-tag v-else-if="detail.returnStatus === 3" color="red">已驳回</a-tag>
        <a-tag v-else-if="detail.returnStatus === 4" color="purple">已完成</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="供应商名称">{{ detail.supplierName }}</a-descriptions-item>
      <a-descriptions-item label="退货类型">
        <span v-if="detail.returnType === 1">质量问题</span>
        <span v-else-if="detail.returnType === 2">过期退货</span>
        <span v-else-if="detail.returnType === 3">订单错误</span>
        <span v-else-if="detail.returnType === 4">其他</span>
      </a-descriptions-item>
      <a-descriptions-item label="申请人">{{ detail.applyUserName }}</a-descriptions-item>
      <a-descriptions-item label="申请时间">{{ detail.applyTime }}</a-descriptions-item>
      <a-descriptions-item label="审核人">{{ detail.auditUserName || '-' }}</a-descriptions-item>
      <a-descriptions-item label="审核时间">{{ detail.auditTime || '-' }}</a-descriptions-item>
      <a-descriptions-item label="审核意见" :span="2">{{ detail.auditOpinion || '-' }}</a-descriptions-item>
      <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
    </a-descriptions>

    <a-divider orientation="left">退货明细</a-divider>

    <a-table :dataSource="detail.detailList" :columns="detailColumns" :pagination="false" rowKey="id" size="small" bordered />
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { supplierReturnApi } from '/@/api/spd/supplier-return-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const detail = reactive({
    returnCode: '',
    returnStatus: undefined,
    returnType: undefined,
    supplierName: '',
    applyUserName: '',
    applyTime: '',
    auditUserName: '',
    auditTime: '',
    auditOpinion: '',
    remark: '',
    detailList: [],
  });

  const detailColumns = ref([
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '批号', dataIndex: 'batchNo', width: 150 },
    { title: '退货数量', dataIndex: 'returnQuantity', width: 100 },
    { title: '实际数量', dataIndex: 'actualQuantity', width: 100 },
    { title: '退货原因', dataIndex: 'returnReason', width: 200 },
    { title: '备注', dataIndex: 'remark', width: 150 },
  ]);

  /**
   * 显示抽屉
   */
  async function showDrawer(supplierReturnId) {
    visible.value = true;
    try {
      const res = await supplierReturnApi.getDetail(supplierReturnId);
      Object.assign(detail, res.data);
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  defineExpose({ showDrawer });
</script>
