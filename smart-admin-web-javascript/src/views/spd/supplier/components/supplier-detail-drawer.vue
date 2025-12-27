<template>
  <a-drawer
    title="供应商详情"
    :width="800"
    :visible="visible"
    @close="handleClose"
    :bodyStyle="{ paddingBottom: '80px' }"
  >
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="供应商名称">{{ detail.supplierName }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag v-if="detail.status === 1" color="green">启用</a-tag>
          <a-tag v-else color="red">禁用</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="联系人">{{ detail.contactPerson }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ detail.contactPhone }}</a-descriptions-item>
        <a-descriptions-item label="邮箱">{{ detail.email || '-' }}</a-descriptions-item>
        <a-descriptions-item label="营业执照号">{{ detail.businessLicense || '-' }}</a-descriptions-item>
        <a-descriptions-item label="地址" :span="2">{{ detail.address || '-' }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="更新时间">{{ detail.updateTime || '-' }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { supplierApi } from '/@/api/spd/supplier-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);

  const detail = reactive({
    supplierName: '',
    status: undefined,
    contactPerson: '',
    contactPhone: '',
    email: '',
    businessLicense: '',
    address: '',
    createTime: '',
    updateTime: '',
    remark: '',
  });

  async function showDrawer(id) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await supplierApi.getDetail(id);
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
