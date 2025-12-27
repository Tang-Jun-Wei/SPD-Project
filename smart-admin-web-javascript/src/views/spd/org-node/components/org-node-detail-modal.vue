<template>
  <a-modal
    title="节点详情"
    :open="visible"
    @cancel="handleCancel"
    :footer="null"
    width="700px"
  >
    <a-spin :spinning="loading">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="节点ID">{{ detail.nodeId }}</a-descriptions-item>
        <a-descriptions-item label="节点名称">{{ detail.nodeName }}</a-descriptions-item>
        <a-descriptions-item label="节点类型">
          <a-tag v-if="detail.nodeType === 1" color="blue">科室</a-tag>
          <a-tag v-else-if="detail.nodeType === 2" color="green">仓库</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="上级节点">{{ detail.parentNodeName || '-' }}</a-descriptions-item>
        <a-descriptions-item label="负责人">{{ detail.responsiblePerson || '-' }}</a-descriptions-item>
        <a-descriptions-item label="联系电话">{{ detail.contactPhone || '-' }}</a-descriptions-item>
        <a-descriptions-item label="地址" :span="2">{{ detail.address || '-' }}</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag v-if="detail.nodeStatus === 1" color="green">启用</a-tag>
          <a-tag v-else color="red">禁用</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        <a-descriptions-item label="更新时间">{{ detail.updateTime }}</a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </a-modal>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);
  const detail = reactive({});

  async function showModal(nodeId) {
    visible.value = true;
    loading.value = true;
    try {
      const res = await orgNodeApi.detail(nodeId);
      Object.assign(detail, res.data);
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      loading.value = false;
    }
  }

  function handleCancel() {
    visible.value = false;
    Object.keys(detail).forEach(key => delete detail[key]);
  }

  defineExpose({ showModal });
</script>
