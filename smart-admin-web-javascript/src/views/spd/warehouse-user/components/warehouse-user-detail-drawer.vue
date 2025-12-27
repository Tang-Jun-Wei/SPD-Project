<template>
  <a-drawer
    title="仓库人员关系详情"
    :open="visible"
    :width="600"
    @close="handleClose"
  >
    <a-spin :spinning="loading">
      <a-descriptions :column="1" bordered size="small">
        <a-descriptions-item label="关系业务ID">
          {{ detailData.relationId }}
        </a-descriptions-item>
        <a-descriptions-item label="仓库名称">
          {{ detailData.warehouseName }}
        </a-descriptions-item>
        <a-descriptions-item label="用户名">
          {{ detailData.userName }}
        </a-descriptions-item>
        <a-descriptions-item label="真实姓名">
          {{ detailData.actualName }}
        </a-descriptions-item>
        <a-descriptions-item label="角色类型">
          <a-tag v-if="detailData.roleType === 1" color="red">仓库管理员</a-tag>
          <a-tag v-else-if="detailData.roleType === 2" color="blue">普通库管员</a-tag>
          <a-tag v-else color="green">拣货员</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag v-if="detailData.status === 1" color="green">启用</a-tag>
          <a-tag v-else color="red">禁用</a-tag>
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
  import { warehouseUserApi } from '/@/api/spd/warehouse-user-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visible = ref(false);
  const loading = ref(false);
  const detailData = reactive({
    relationId: '',
    warehouseName: '',
    userName: '',
    actualName: '',
    roleType: 1,
    status: 1,
    remark: '',
    createTime: '',
  });

  // 显示抽屉
  async function showDrawer(id) {
    visible.value = true;
    loading.value = true;

    try {
      const res = await warehouseUserApi.getDetail(id);
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
