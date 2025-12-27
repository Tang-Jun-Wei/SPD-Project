<template>
  <a-drawer
    title="生产厂家详情"
    :width="800"
    :visible="visible"
    @close="handleClose"
    :bodyStyle="{ paddingBottom: '80px' }"
  >
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="厂家名称">{{ detail.manufacturerName }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag v-if="detail.status === 1" color="green">启用</a-tag>
          <a-tag v-else color="red">禁用</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="联系人">{{ detail.contactPerson }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ detail.contactPhone }}</a-descriptions-item>
        <a-descriptions-item label="邮箱">{{ detail.email || '-' }}</a-descriptions-item>
        <a-descriptions-item label="生产许可证号">{{ detail.licenseNo || '-' }}</a-descriptions-item>
        <a-descriptions-item label="许可证有效期" :span="2">{{ detail.licenseExpiry || '-' }}</a-descriptions-item>
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
  import { manufacturerApi } from '/@/api/spd/manufacturer-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);

  const detail = reactive({
    manufacturerName: '',
    status: undefined,
    contactPerson: '',
    contactPhone: '',
    email: '',
    licenseNo: '',
    licenseExpiry: '',
    address: '',
    createTime: '',
    updateTime: '',
    remark: '',
  });

  async function showDrawer(id) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await manufacturerApi.getDetail(id);
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
