<template>
  <a-drawer
    title="库存安全量配置详情"
    :open="visible"
    :width="600"
    @close="handleClose"
  >
    <a-spin :spinning="loading">
      <a-descriptions :column="1" bordered size="small">
        <a-descriptions-item label="安全量业务ID">
          {{ detailData.limitId }}
        </a-descriptions-item>
        <a-descriptions-item label="耗材名称">
          {{ detailData.materialName }}
        </a-descriptions-item>
        <a-descriptions-item label="仓库名称">
          {{ detailData.warehouseName }}
        </a-descriptions-item>
        <a-descriptions-item label="当前库存">
          <a-tag v-if="detailData.stockStatus === 0" color="green">{{ detailData.currentStock }}</a-tag>
          <a-tag v-else-if="detailData.stockStatus === 1" color="orange">{{ detailData.currentStock }}</a-tag>
          <a-tag v-else-if="detailData.stockStatus === 2" color="red">{{ detailData.currentStock }}</a-tag>
          <a-tag v-else-if="detailData.stockStatus === 3" color="purple">{{ detailData.currentStock }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="最小库存量">
          {{ detailData.minStock }}
        </a-descriptions-item>
        <a-descriptions-item label="预警库存量">
          {{ detailData.warningStock || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="最大库存量">
          {{ detailData.maxStock }}
        </a-descriptions-item>
        <a-descriptions-item label="库存状态">
          <a-tag v-if="detailData.stockStatus === 0" color="green">正常</a-tag>
          <a-tag v-else-if="detailData.stockStatus === 1" color="orange">低于预警</a-tag>
          <a-tag v-else-if="detailData.stockStatus === 2" color="red">低于下限</a-tag>
          <a-tag v-else-if="detailData.stockStatus === 3" color="purple">超过上限</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="备注">
          {{ detailData.remark || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ detailData.createTime }}
        </a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </a-drawer>
</template>

<script setup>
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { stockLimitApi } from '/@/api/spd/stock-limit-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);
  const detailData = reactive({
    limitId: '',
    materialName: '',
    warehouseName: '',
    currentStock: 0,
    minStock: 0,
    warningStock: undefined,
    maxStock: 0,
    stockStatus: 0,
    remark: '',
    createTime: '',
  });

  // 显示抽屉
  async function showDrawer(id) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await stockLimitApi.getDetail(id);
      if (res.data) {
        Object.assign(detailData, res.data);
      } else {
        message.error(res.msg || '查询详情失败');
      }
    } catch (error) {
      smartSentry.captureError(error);
      message.error('查询详情失败');
    } finally {
      loading.value = false;
    }
  }

  // 关闭抽屉
  function handleClose() {
    visible.value = false;
  }

  defineExpose({
    showDrawer,
  });
</script>

<style scoped lang="less"></style>
