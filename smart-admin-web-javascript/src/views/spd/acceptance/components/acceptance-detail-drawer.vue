<template>
  <a-drawer title="验收单详情" :width="900" :open="visible" @close="handleClose" :maskClosable="false">
    <a-spin :spinning="loading">
      <a-descriptions :column="2" bordered size="small">
        <a-descriptions-item label="验收单号">{{ detail.acceptanceNo }}</a-descriptions-item>
        <a-descriptions-item label="采购单号">{{ detail.purchaseNo }}</a-descriptions-item>
        <a-descriptions-item label="仓库">{{ detail.warehouseName }}</a-descriptions-item>
        <a-descriptions-item label="验收人">{{ detail.acceptanceByName }}</a-descriptions-item>
        <a-descriptions-item label="验收日期">{{ detail.acceptanceDate }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag v-if="detail.acceptanceStatus === 0" color="default">草稿</a-tag>
          <a-tag v-else-if="detail.acceptanceStatus === 1" color="orange">待验收</a-tag>
          <a-tag v-else-if="detail.acceptanceStatus === 2" color="green">已验收</a-tag>
          <a-tag v-else-if="detail.acceptanceStatus === 9" color="red">已拒绝</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="总金额">¥ {{ detail.totalAmount }}</a-descriptions-item>
        <a-descriptions-item label="创建人">{{ detail.createBy }}</a-descriptions-item>
        <a-descriptions-item label="创建时间" :span="2">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>

      <a-divider>验收明细</a-divider>
      <a-table :dataSource="detail.detailList" :columns="columns" size="small" :pagination="false" bordered>
        <template #bodyCell="{ text, column }">
          <template v-if="column.dataIndex === 'totalPrice'">
            ¥ {{ text }}
          </template>
          <template v-if="column.dataIndex === 'unitPrice'">
            ¥ {{ text }}
          </template>
        </template>
      </a-table>
    </a-spin>

    <template #footer>
      <a-button @click="handleClose">关闭</a-button>
    </template>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { acceptanceApi } from '/@/api/spd/acceptance-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);
  const detail = reactive({
    acceptanceNo: '',
    purchaseNo: '',
    warehouseName: '',
    acceptanceByName: '',
    acceptanceDate: '',
    acceptanceStatus: 0,
    totalAmount: 0,
    createBy: '',
    createTime: '',
    remark: '',
    detailList: [],
  });

  const columns = [
    { title: '耗材名称', dataIndex: 'materialName', width: 150 },
    { title: '批号', dataIndex: 'batchNo', width: 120 },
    { title: '验收数量', dataIndex: 'acceptanceQuantity', width: 100 },
    { title: '合格数量', dataIndex: 'qualifiedQuantity', width: 100 },
    { title: '不合格数量', dataIndex: 'unqualifiedQuantity', width: 100 },
    { title: '单价', dataIndex: 'unitPrice', width: 100 },
    { title: '总价', dataIndex: 'totalPrice', width: 100 },
    { title: '质量状态', dataIndex: 'qualityStatus', width: 100 },
    { title: '备注', dataIndex: 'remark', width: 150 },
  ];

  async function showDrawer(acceptanceId) {
    visible.value = true;
    loading.value = true;
    try {
      const res = await acceptanceApi.getDetail(acceptanceId);
      if (res.data) {
        Object.assign(detail, res.data);
      }
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      loading.value = false;
    }
  }

  function handleClose() {
    visible.value = false;
    Object.assign(detail, {
      acceptanceNo: '',
      purchaseNo: '',
      warehouseName: '',
      acceptanceByName: '',
      acceptanceDate: '',
      acceptanceStatus: 0,
      totalAmount: 0,
      createBy: '',
      createTime: '',
      remark: '',
      detailList: [],
    });
  }

  defineExpose({ showDrawer });
</script>
