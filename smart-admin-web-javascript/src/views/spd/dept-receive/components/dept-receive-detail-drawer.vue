<template>
  <a-drawer
    title="科室收货单详情"
    v-model:visible="visible"
    width="800"
    :maskClosable="false"
  >
    <a-descriptions title="基本信息" :column="2" bordered>
      <a-descriptions-item label="收货单号">{{ detail.receiveCode }}</a-descriptions-item>
      <a-descriptions-item label="收货状态">
        <a-tag v-if="detail.receiveStatus === 1" color="blue">待收货</a-tag>
        <a-tag v-else-if="detail.receiveStatus === 2" color="green">已收货</a-tag>
        <a-tag v-else-if="detail.receiveStatus === 3" color="orange">部分拒收</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="科室名称">{{ detail.deptName }}</a-descriptions-item>
      <a-descriptions-item label="仓库名称">{{ detail.warehouseName }}</a-descriptions-item>
      <a-descriptions-item label="收货类型">
        <span v-if="detail.receiveType === 1">正常收货</span>
        <span v-else-if="detail.receiveType === 2">紧急收货</span>
      </a-descriptions-item>
      <a-descriptions-item label="总数量">{{ detail.totalQuantity }}</a-descriptions-item>
      <a-descriptions-item label="已收数量">{{ detail.receivedQuantity }}</a-descriptions-item>
      <a-descriptions-item label="拒收数量">{{ detail.rejectedQuantity }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
      <a-descriptions-item label="收货人">{{ detail.receiveBy || '-' }}</a-descriptions-item>
      <a-descriptions-item label="收货时间">{{ detail.receiveTime || '-' }}</a-descriptions-item>
      <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
    </a-descriptions>

    <a-divider orientation="left">收货明细</a-divider>

    <a-table :dataSource="detail.detailList" :columns="detailColumns" :pagination="false" rowKey="id" size="small" bordered />
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { deptReceiveApi } from '/@/api/spd/dept-receive-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const detail = reactive({
    receiveCode: '',
    receiveStatus: undefined,
    receiveType: undefined,
    deptName: '',
    warehouseName: '',
    totalQuantity: 0,
    receivedQuantity: 0,
    rejectedQuantity: 0,
    createTime: '',
    receiveBy: '',
    receiveTime: '',
    remark: '',
    detailList: [],
  });

  const detailColumns = ref([
    { title: '标签码', dataIndex: 'labelCode', width: 150 },
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '批号', dataIndex: 'batchNo', width: 150 },
    { title: '定数包数量', dataIndex: 'packageQuantity', width: 100 },
    { title: '收货状态', dataIndex: 'receiveStatus', width: 100,
      customRender: ({ text }) => {
        if (text === 1) return '待收货';
        if (text === 2) return '已收货';
        if (text === 3) return '已拒收';
        return '-';
      }
    },
    { title: '拒收原因', dataIndex: 'rejectReason', width: 200 },
  ]);

  /**
   * 显示抽屉
   */
  async function showDrawer(receiveId) {
    visible.value = true;
    try {
      const res = await deptReceiveApi.getDetail(receiveId);
      Object.assign(detail, res.data);
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  defineExpose({ showDrawer });
</script>
