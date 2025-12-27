<template>
  <a-card size="small" :bordered="false">
    <!-- 查询表单 -->
    <a-form :model="queryForm" layout="inline" class="smart-query-form">
      <a-form-item label="标签编码">
        <a-input v-model:value="queryForm.labelCode" placeholder="请输入标签编码/序列号" style="width: 250px" @pressEnter="queryTrack" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="queryTrack">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button class="smart-margin-left10" @click="resetQuery">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-form>

    <!-- 标签基本信息 -->
    <a-divider v-if="trackData.labelCode">标签基本信息</a-divider>
    <a-descriptions v-if="trackData.labelCode" :column="3" bordered size="small" style="margin-bottom: 20px">
      <a-descriptions-item label="标签编码">{{ trackData.labelCode }}</a-descriptions-item>
      <a-descriptions-item label="耗材名称">{{ trackData.materialName }}</a-descriptions-item>
      <a-descriptions-item label="规格型号">{{ trackData.specification }}</a-descriptions-item>
      <a-descriptions-item label="批号">{{ trackData.batchNo || '-' }}</a-descriptions-item>
      <a-descriptions-item label="序列号">{{ trackData.serialNo || '-' }}</a-descriptions-item>
      <a-descriptions-item label="生产厂家">{{ trackData.manufacturer || '-' }}</a-descriptions-item>
      <a-descriptions-item label="供应商">{{ trackData.supplierName || '-' }}</a-descriptions-item>
      <a-descriptions-item label="当前状态">
        <a-tag :color="getStatusColor(trackData.currentStatus)">{{ trackData.currentStatus }}</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="当前位置">{{ trackData.currentLocation }}</a-descriptions-item>
    </a-descriptions>

    <!-- 轨迹时间线 -->
    <a-divider v-if="trackData.trackList && trackData.trackList.length > 0">全流程追溯</a-divider>
    <a-empty v-if="!trackData.labelCode" description="请输入标签编码查询追溯信息" />
    
    <a-timeline v-if="trackData.trackList && trackData.trackList.length > 0" mode="left" style="margin-top: 20px">
      <a-timeline-item
        v-for="(item, index) in trackData.trackList"
        :key="index"
        :color="getOperationColor(item.operationType)"
      >
        <template #dot>
          <span class="timeline-icon">
            <CheckCircleOutlined v-if="item.operationType === 1" />
            <InboxOutlined v-else-if="item.operationType === 2" />
            <ExportOutlined v-else-if="item.operationType === 3" />
            <SendOutlined v-else-if="item.operationType === 4" />
            <MinusCircleOutlined v-else-if="item.operationType === 5" />
            <RollbackOutlined v-else-if="item.operationType === 6" />
            <SwapOutlined v-else-if="item.operationType === 7" />
          </span>
        </template>
        
        <a-card size="small" :bordered="false" class="timeline-card">
          <div class="track-header">
            <a-tag :color="getOperationColor(item.operationType)">{{ item.operationDesc }}</a-tag>
            <span class="track-time">{{ item.operationTime }}</span>
          </div>
          
          <a-row :gutter="[16, 8]" style="margin-top: 10px">
            <a-col :span="8" v-if="item.businessNo">
              <div class="track-info">
                <span class="track-label">业务单号：</span>
                <span class="track-value">{{ item.businessNo }}</span>
              </div>
            </a-col>
            <a-col :span="8" v-if="item.operatorName">
              <div class="track-info">
                <span class="track-label">操作人：</span>
                <span class="track-value">{{ item.operatorName }}</span>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="track-info">
                <span class="track-label">流转：</span>
                <span class="track-value">{{ item.fromLocation }} → {{ item.toLocation }}</span>
              </div>
            </a-col>
            <a-col :span="24" v-if="item.remark">
              <div class="track-info">
                <span class="track-label">备注：</span>
                <span class="track-value">{{ item.remark }}</span>
              </div>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>
    </a-timeline>
  </a-card>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import {
  SearchOutlined,
  ReloadOutlined,
  CheckCircleOutlined,
  InboxOutlined,
  ExportOutlined,
  SendOutlined,
  MinusCircleOutlined,
  RollbackOutlined,
  SwapOutlined,
} from '@ant-design/icons-vue';
import { labelTrackApi } from '/@/api/spd/label-track-api';
import { smartSentry } from '/@/lib/smart-sentry';

// 查询表单
const queryForm = reactive({
  labelCode: '',
});

// 追溯数据
const trackData = reactive({
  labelCode: '',
  materialName: '',
  specification: '',
  batchNo: '',
  serialNo: '',
  manufacturer: '',
  supplierName: '',
  currentStatus: '',
  currentLocation: '',
  trackList: [],
});

// 查询追溯信息
async function queryTrack() {
  if (!queryForm.labelCode) {
    message.warning('请输入标签编码');
    return;
  }

  try {
    const res = await labelTrackApi.queryTrack(queryForm);
    if (res.data) {
      Object.assign(trackData, res.data);
    }
  } catch (error) {
    smartSentry.captureError(error);
  }
}

// 重置查询
function resetQuery() {
  queryForm.labelCode = '';
  Object.assign(trackData, {
    labelCode: '',
    materialName: '',
    specification: '',
    batchNo: '',
    serialNo: '',
    manufacturer: '',
    supplierName: '',
    currentStatus: '',
    currentLocation: '',
    trackList: [],
  });
}

// 获取状态颜色
function getStatusColor(status) {
  const colorMap = {
    待打包: 'blue',
    已打包: 'cyan',
    已出库: 'orange',
    已分发: 'purple',
    已消耗: 'green',
    已退库: 'red',
  };
  return colorMap[status] || 'default';
}

// 获取操作类型颜色
function getOperationColor(type) {
  const colorMap = {
    1: 'green', // 入库
    2: 'blue', // 打包
    3: 'orange', // 出库
    4: 'purple', // 分发
    5: 'red', // 消耗
    6: 'volcano', // 退库
    7: 'cyan', // 调拨
  };
  return colorMap[type] || 'default';
}
</script>

<style scoped lang="less">
.timeline-icon {
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.timeline-card {
  background: #fafafa;
  border-radius: 4px;
  padding: 12px;
}

.track-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.track-time {
  color: #999;
  font-size: 13px;
}

.track-info {
  font-size: 13px;
  line-height: 22px;
}

.track-label {
  color: #666;
  font-weight: 500;
}

.track-value {
  color: #333;
}

:deep(.ant-timeline-item-content) {
  margin-left: 30px;
}

:deep(.ant-descriptions-item-label) {
  font-weight: 500;
  background: #fafafa;
}
</style>
