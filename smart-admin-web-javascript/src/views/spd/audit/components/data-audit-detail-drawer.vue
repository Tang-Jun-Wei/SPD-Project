<template>
  <a-drawer
    title="审核详情"
    :width="900"
    :visible="visible"
    @close="handleClose"
    :bodyStyle="{ paddingBottom: '80px' }"
  >
    <a-spin :spinning="loading">
      <a-descriptions title="基本信息" :column="2" bordered>
        <a-descriptions-item label="审核业务ID">{{ detail.auditId }}</a-descriptions-item>
        <a-descriptions-item label="审核类型">
          <a-tag color="blue">{{ detail.auditTypeName }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="目标业务ID">{{ detail.targetId }}</a-descriptions-item>
        <a-descriptions-item label="目标名称">{{ detail.targetName }}</a-descriptions-item>
        <a-descriptions-item label="操作类型">
          <a-tag :color="detail.actionType === 1 ? 'green' : 'orange'">
            {{ detail.actionTypeName }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="审核状态">
          <a-tag v-if="detail.auditStatus === 0" color="gold">待审核</a-tag>
          <a-tag v-else-if="detail.auditStatus === 1" color="green">已通过</a-tag>
          <a-tag v-else-if="detail.auditStatus === 2" color="red">已拒绝</a-tag>
          <a-tag v-else color="gray">已撤销</a-tag>
        </a-descriptions-item>
      </a-descriptions>

      <a-divider />

      <a-descriptions title="数据对比" :column="1" bordered>
        <a-descriptions-item label="修改前数据" v-if="detail.oldData">
          <pre style="max-height: 300px; overflow: auto; background: #f5f5f5; padding: 10px;">{{ formatJson(detail.oldData) }}</pre>
        </a-descriptions-item>
        <a-descriptions-item label="新数据/修改后数据">
          <pre style="max-height: 300px; overflow: auto; background: #f5f5f5; padding: 10px;">{{ formatJson(detail.newData) }}</pre>
        </a-descriptions-item>
      </a-descriptions>

      <a-divider />

      <a-descriptions title="审核信息" :column="2" bordered>
        <a-descriptions-item label="提交理由" :span="2">{{ detail.submitReason || '-' }}</a-descriptions-item>
        <a-descriptions-item label="提交人">{{ detail.submitUserName || detail.submitUser }}</a-descriptions-item>
        <a-descriptions-item label="提交时间">{{ detail.submitTime }}</a-descriptions-item>
        <a-descriptions-item label="审核意见" :span="2">{{ detail.auditOpinion || '-' }}</a-descriptions-item>
        <a-descriptions-item label="审核人">{{ detail.auditUserName || detail.auditUser || '-' }}</a-descriptions-item>
        <a-descriptions-item label="审核时间">{{ detail.auditTime || '-' }}</a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { dataAuditApi } from '/@/api/spd/data-audit-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);

  const detail = reactive({
    auditId: '',
    auditType: undefined,
    auditTypeName: '',
    targetId: '',
    targetName: '',
    actionType: undefined,
    actionTypeName: '',
    oldData: '',
    newData: '',
    auditStatus: undefined,
    auditStatusName: '',
    submitReason: '',
    auditOpinion: '',
    submitUser: '',
    submitUserName: '',
    submitTime: '',
    auditUser: '',
    auditUserName: '',
    auditTime: '',
  });

  async function showDrawer(id) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await dataAuditApi.getDetail(id);
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

  function formatJson(jsonStr) {
    if (!jsonStr) return '';
    try {
      const obj = JSON.parse(jsonStr);
      return JSON.stringify(obj, null, 2);
    } catch (e) {
      return jsonStr;
    }
  }

  defineExpose({
    showDrawer,
  });
</script>
