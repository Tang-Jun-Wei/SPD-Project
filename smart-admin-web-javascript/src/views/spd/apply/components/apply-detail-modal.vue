<template>
  <a-drawer
    title="申领单详情"
    :width="900"
    :visible="visible"
    @close="handleClose"
    :bodyStyle="{ paddingBottom: '80px' }"
  >
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="申领单号">{{ detail.applyNo }}</a-descriptions-item>
        <a-descriptions-item label="申领状态">
          <a-tag v-if="detail.applyStatus === 1" color="orange">待审核</a-tag>
          <a-tag v-else-if="detail.applyStatus === 2" color="green">已审核</a-tag>
          <a-tag v-else-if="detail.applyStatus === 3" color="red">已驳回</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="申领科室">{{ detail.applyDeptName }}</a-descriptions-item>
        <a-descriptions-item label="申领人">{{ detail.applyUserName }}</a-descriptions-item>
        <a-descriptions-item label="申领时间">{{ detail.applyTime }}</a-descriptions-item>
        <a-descriptions-item label="审核人">{{ detail.approveUserName || '-' }}</a-descriptions-item>
        <a-descriptions-item label="审核时间">{{ detail.approveTime || '-' }}</a-descriptions-item>
        <a-descriptions-item label="审核意见">{{ detail.approveRemark || '-' }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>

      <a-divider>申领明细</a-divider>

      <a-table
        :columns="detailColumns"
        :data-source="detail.detailList"
        :pagination="false"
        rowKey="id"
        size="small"
        bordered
      />
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { applyApi } from '/@/api/spd/apply-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);

  const detail = reactive({
    applyNo: '',
    applyStatus: undefined,
    applyDeptName: '',
    applyUserName: '',
    applyTime: '',
    approveUserName: '',
    approveTime: '',
    approveRemark: '',
    remark: '',
    detailList: [],
  });

  const detailColumns = [
    {
      title: '序号',
      width: 60,
      customRender: ({ index }) => index + 1,
    },
    {
      title: '耗材名称',
      dataIndex: 'materialName',
      width: 200,
    },
    {
      title: '规格型号',
      dataIndex: 'specification',
      width: 150,
    },
    {
      title: '单位',
      dataIndex: 'unit',
      width: 80,
    },
    {
      title: '批号',
      dataIndex: 'batchNo',
      width: 120,
    },
    {
      title: '申领数量',
      dataIndex: 'applyQuantity',
      width: 100,
    },
    {
      title: '审批数量',
      dataIndex: 'approveQuantity',
      width: 100,
    },
  ];

  async function showModal(id) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await applyApi.getDetail(id);
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
    showModal,
  });
</script>
