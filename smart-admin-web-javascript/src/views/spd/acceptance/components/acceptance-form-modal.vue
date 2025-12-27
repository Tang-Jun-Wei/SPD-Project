<template>
  <a-modal
    :title="form.id ? '编辑验收单' : '新增验收单'"
    :open="visible"
    :confirm-loading="confirmLoading"
    :width="1000"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable="false"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 17 }">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="采购单" name="purchaseId">
            <a-select v-model:value="form.purchaseId" placeholder="请选择采购单" allowClear>
              <a-select-option v-for="item in purchaseOptions" :key="item.purchaseId" :value="item.purchaseId">
                {{ item.purchaseNo }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="仓库" name="warehouseId">
            <a-select v-model:value="form.warehouseId" placeholder="请选择仓库" allowClear>
              <a-select-option v-for="item in warehouseOptions" :key="item.nodeId" :value="item.nodeId">
                {{ item.nodeName }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="验收日期" name="acceptanceDate">
            <a-date-picker v-model:value="form.acceptanceDate" placeholder="请选择验收日期" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="验收人" name="acceptanceBy">
            <a-input v-model:value="form.acceptanceBy" placeholder="请输入验收人" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item label="备注" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>

    <a-divider>验收明细</a-divider>
    <a-button type="primary" size="small" @click="addDetail" style="margin-bottom: 10px"><PlusOutlined />添加明细</a-button>
    <a-table :dataSource="form.detailList" :columns="detailColumns" size="small" :pagination="false" bordered>
      <template #bodyCell="{ text, record, column, index }">
        <template v-if="column.dataIndex === 'materialId'">
          <a-select v-model:value="record.materialId" placeholder="请选择耗材" style="width: 100%" allowClear>
            <a-select-option v-for="item in materialOptions" :key="item.materialId" :value="item.materialId">
              {{ item.materialName }}
            </a-select-option>
          </a-select>
        </template>
        <template v-if="column.dataIndex === 'batchId'">
          <a-select v-model:value="record.batchId" placeholder="请选择批号" style="width: 100%" allowClear>
            <a-select-option v-for="item in batchOptions" :key="item.batchId" :value="item.batchId">
              {{ item.batchNo }}
            </a-select-option>
          </a-select>
        </template>
        <template v-if="column.dataIndex === 'acceptanceQuantity'">
          <a-input-number v-model:value="record.acceptanceQuantity" :min="0" :precision="2" style="width: 100%" />
        </template>
        <template v-if="column.dataIndex === 'qualifiedQuantity'">
          <a-input-number v-model:value="record.qualifiedQuantity" :min="0" :precision="2" style="width: 100%" />
        </template>
        <template v-if="column.dataIndex === 'unqualifiedQuantity'">
          <a-input-number v-model:value="record.unqualifiedQuantity" :min="0" :precision="2" style="width: 100%" />
        </template>
        <template v-if="column.dataIndex === 'unitPrice'">
          <a-input-number v-model:value="record.unitPrice" :min="0" :precision="2" style="width: 100%" />
        </template>
        <template v-if="column.dataIndex === 'qualityStatus'">
          <a-select v-model:value="record.qualityStatus" placeholder="请选择质量状态" style="width: 100%" allowClear>
            <a-select-option value="1">合格</a-select-option>
            <a-select-option value="2">不合格</a-select-option>
            <a-select-option value="3">待检验</a-select-option>
          </a-select>
        </template>
        <template v-if="column.dataIndex === 'remark'">
          <a-input v-model:value="record.remark" placeholder="备注" />
        </template>
        <template v-if="column.dataIndex === 'operate'">
          <a-button danger type="link" size="small" @click="removeDetail(index)">删除</a-button>
        </template>
      </template>
    </a-table>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { acceptanceApi } from '/@/api/spd/acceptance-api';
  import { purchaseApi } from '/@/api/spd/purchase-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { materialApi } from '/@/api/spd/material-api';
  import { batchApi } from '/@/api/spd/batch-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import dayjs from 'dayjs';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const confirmLoading = ref(false);
  const formRef = ref();
  const purchaseOptions = ref([]);
  const warehouseOptions = ref([]);
  const materialOptions = ref([]);
  const batchOptions = ref([]);

  const form = reactive({
    id: undefined,
    acceptanceId: undefined,
    purchaseId: undefined,
    warehouseId: undefined,
    acceptanceDate: undefined,
    acceptanceBy: '',
    remark: '',
    detailList: [],
  });

  const rules = {
    purchaseId: [{ required: true, message: '请选择采购单', trigger: 'change' }],
    warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
    acceptanceDate: [{ required: true, message: '请选择验收日期', trigger: 'change' }],
    acceptanceBy: [{ required: true, message: '请输入验收人', trigger: 'blur' }],
  };

  const detailColumns = [
    { title: '耗材', dataIndex: 'materialId', width: 200 },
    { title: '批号', dataIndex: 'batchId', width: 150 },
    { title: '验收数量', dataIndex: 'acceptanceQuantity', width: 120 },
    { title: '合格数量', dataIndex: 'qualifiedQuantity', width: 120 },
    { title: '不合格数量', dataIndex: 'unqualifiedQuantity', width: 120 },
    { title: '单价', dataIndex: 'unitPrice', width: 100 },
    { title: '质量状态', dataIndex: 'qualityStatus', width: 120 },
    { title: '备注', dataIndex: 'remark', width: 150 },
    { title: '操作', dataIndex: 'operate', width: 80, fixed: 'right' },
  ];

  async function showModal(record) {
    visible.value = true;
    await nextTick();
    formRef.value?.resetFields();
    
    if (record && record.id) {
      Object.assign(form, {
        id: record.id,
        acceptanceId: record.acceptanceId,
        purchaseId: record.purchaseId,
        warehouseId: record.warehouseId,
        acceptanceDate: record.acceptanceDate ? dayjs(record.acceptanceDate) : undefined,
        acceptanceBy: record.acceptanceBy || '',
        remark: record.remark || '',
        detailList: [],
      });
      loadDetail(record.acceptanceId);
    } else {
      Object.assign(form, {
        id: undefined,
        acceptanceId: undefined,
        purchaseId: undefined,
        warehouseId: undefined,
        acceptanceDate: dayjs(),
        acceptanceBy: '',
        remark: '',
        detailList: [],
      });
    }

    loadPurchaseOptions();
    loadWarehouseOptions();
    loadMaterialOptions();
    loadBatchOptions();
  }

  async function loadDetail(acceptanceId) {
    try {
      const res = await acceptanceApi.getDetail(acceptanceId);
      if (res.data && res.data.detailList) {
        form.detailList = res.data.detailList.map(item => ({
          materialId: item.materialId,
          batchId: item.batchId,
          acceptanceQuantity: item.acceptanceQuantity,
          qualifiedQuantity: item.qualifiedQuantity,
          unqualifiedQuantity: item.unqualifiedQuantity,
          unitPrice: item.unitPrice,
          qualityStatus: item.qualityStatus || '',
          remark: item.remark || '',
        }));
      }
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  async function loadPurchaseOptions() {
    try {
      const res = await purchaseApi.queryPage({ pageNum: 1, pageSize: 1000, purchaseStatus: 2 });
      purchaseOptions.value = res.data?.list || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  async function loadWarehouseOptions() {
    try {
      const res = await orgNodeApi.queryWarehouseList();
      warehouseOptions.value = res.data || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  async function loadMaterialOptions() {
    try {
      const res = await materialApi.queryPage({ pageNum: 1, pageSize: 1000 });
      materialOptions.value = res.data?.list || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  async function loadBatchOptions() {
    try {
      const res = await batchApi.queryPage({ pageNum: 1, pageSize: 1000 });
      batchOptions.value = res.data?.list || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  function addDetail() {
    form.detailList.push({
      materialId: undefined,
      batchId: undefined,
      acceptanceQuantity: 0,
      qualifiedQuantity: 0,
      unqualifiedQuantity: 0,
      unitPrice: 0,
      qualityStatus: '',
      remark: '',
    });
  }

  function removeDetail(index) {
    form.detailList.splice(index, 1);
  }

  async function handleOk() {
    try {
      await formRef.value.validate();
      
      if (!form.detailList.length) {
        return message.warning('请至少添加一条验收明细');
      }

      confirmLoading.value = true;
      const data = {
        ...form,
        acceptanceDate: form.acceptanceDate ? form.acceptanceDate.format('YYYY-MM-DD HH:mm:ss') : undefined,
      };

      if (form.id) {
        await acceptanceApi.update(data);
        message.success('编辑成功');
      } else {
        await acceptanceApi.add(data);
        message.success('新增成功');
      }

      handleCancel();
      emit('reloadList');
    } catch (error) {
      if (error.errorFields) return;
      smartSentry.captureError(error);
    } finally {
      confirmLoading.value = false;
    }
  }

  function handleCancel() {
    visible.value = false;
    Object.assign(form, {
      id: undefined,
      acceptanceId: undefined,
      purchaseId: undefined,
      warehouseId: undefined,
      acceptanceDate: undefined,
      acceptanceBy: '',
      remark: '',
      detailList: [],
    });
  }

  defineExpose({ showModal });
</script>
