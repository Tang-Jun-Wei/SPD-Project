<template>
  <a-drawer
    title="调拨单详情"
    v-model:visible="visible"
    width="800"
    :maskClosable="false"
  >
    <a-descriptions title="基本信息" :column="2" bordered>
      <a-descriptions-item label="调拨单号">{{ detail.allocationCode }}</a-descriptions-item>
      <a-descriptions-item label="状态">
        <a-tag v-if="detail.allocationStatus === 1" color="blue">待审核</a-tag>
        <a-tag v-else-if="detail.allocationStatus === 2" color="green">已通过</a-tag>
        <a-tag v-else-if="detail.allocationStatus === 3" color="red">已驳回</a-tag>
        <a-tag v-else-if="detail.allocationStatus === 4" color="purple">已完成</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="调出节点">{{ detail.fromNodeName }}</a-descriptions-item>
      <a-descriptions-item label="调入节点">{{ detail.toNodeName }}</a-descriptions-item>
      <a-descriptions-item label="申请人">{{ detail.applyUserName }}</a-descriptions-item>
      <a-descriptions-item label="申请时间">{{ detail.applyTime }}</a-descriptions-item>
      <a-descriptions-item label="审核人">{{ detail.auditUserName || '-' }}</a-descriptions-item>
      <a-descriptions-item label="审核时间">{{ detail.auditTime || '-' }}</a-descriptions-item>
      <a-descriptions-item label="审核意见" :span="2">{{ detail.auditOpinion || '-' }}</a-descriptions-item>
      <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
    </a-descriptions>

    <a-divider orientation="left">调拨明细</a-divider>

    <a-table :dataSource="detail.detailList" :columns="detailColumns" :pagination="false" rowKey="id" size="small" bordered />
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { allocationApi } from '/@/api/spd/allocation-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const detail = reactive({
    allocationCode: '',
    allocationStatus: undefined,
    fromNodeName: '',
    toNodeName: '',
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
    { title: '调拨数量', dataIndex: 'allocationQuantity', width: 100 },
    { title: '实际数量', dataIndex: 'actualQuantity', width: 100 },
    { title: '备注', dataIndex: 'remark', width: 200 },
  ]);

  /**
   * 显示抽屉
   */
  async function showDrawer(allocationId) {
    visible.value = true;
    try {
      const res = await allocationApi.getDetail(allocationId);
      Object.assign(detail, res.data);
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  defineExpose({ showDrawer });
</script>
