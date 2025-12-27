<template>
  <a-drawer
    title="打包单详情"
    :width="900"
    :visible="visible"
    @close="handleClose"
  >
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="打包单号">{{ detail.packageCode }}</a-descriptions-item>
        <a-descriptions-item label="业务ID">{{ detail.packageId }}</a-descriptions-item>
        <a-descriptions-item label="仓库">{{ detail.warehouseName }}</a-descriptions-item>
        <a-descriptions-item label="打包类型">
          <a-tag v-if="detail.packageType === 1" color="blue">日常打包</a-tag>
          <a-tag v-else-if="detail.packageType === 2" color="green">收货打包</a-tag>
          <a-tag v-else-if="detail.packageType === 3" color="orange">拆包重打</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="打包状态">
          <a-tag v-if="detail.packageStatus === 0" color="blue">待打包</a-tag>
          <a-tag v-else-if="detail.packageStatus === 1" color="green">已打包</a-tag>
          <a-tag v-else-if="detail.packageStatus === 2" color="red">已作废</a-tag>
          <a-tag v-else-if="detail.packageStatus === 3" color="orange">待上架</a-tag>
          <a-tag v-else-if="detail.packageStatus === 4" color="purple">已上架</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="创建人">{{ detail.createUserName }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>

      <a-divider />

      <a-descriptions title="打包明细">
        <template #extra>
          <a-space>
            <span>共 {{ detail.detailList?.length || 0 }} 条明细</span>
          </a-space>
        </template>
      </a-descriptions>

      <a-table
        :dataSource="detail.detailList"
        :columns="detailColumns"
        :pagination="false"
        size="small"
        bordered
        rowKey="id"
      >
        <template #bodyCell="{ text, column }">
          <template v-if="column.dataIndex === 'remark'">
            {{ text || '-' }}
          </template>
        </template>
      </a-table>
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { packageApi } from '/@/api/spd/package-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);
  const detail = reactive({
    packageCode: '',
    packageId: '',
    warehouseName: '',
    packageType: undefined,
    packageStatus: undefined,
    createUserName: '',
    createTime: '',
    remark: '',
    detailList: [],
  });

  const detailColumns = [
    { title: '序号', width: 60, customRender: ({ index }) => index + 1 },
    { title: '耗材名称', dataIndex: 'materialName', width: 200 },
    { title: '规格', dataIndex: 'spec', width: 150 },
    { title: '批号', dataIndex: 'batchNo', width: 120 },
    { title: '效期', dataIndex: 'validDate', width: 120 },
    { title: '打包数量', dataIndex: 'packageNum', width: 100 },
    { title: '打包规格', dataIndex: 'packageSpec', width: 120 },
    { title: '备注', dataIndex: 'remark', width: 150, ellipsis: true },
  ];

  async function showDrawer(packageId) {
    visible.value = true;
    loading.value = true;
    
    try {
      const res = await packageApi.getDetail(packageId);
      Object.assign(detail, res.data || {});
      detail.detailList = res.data?.detailList || [];
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      loading.value = false;
    }
  }

  function handleClose() {
    visible.value = false;
    Object.assign(detail, {
      packageCode: '',
      packageId: '',
      warehouseName: '',
      packageType: undefined,
      packageStatus: undefined,
      createUserName: '',
      createTime: '',
      remark: '',
      detailList: [],
    });
  }

  defineExpose({ showDrawer });
</script>
