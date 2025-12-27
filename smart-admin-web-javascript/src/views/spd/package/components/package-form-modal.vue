<template>
  <a-modal
    :title="isEdit ? '编辑打包单' : '新增打包单'"
    v-model:visible="visible"
    :width="1000"
    :maskClosable="false"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" ref="formRef">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-item label="仓库" name="warehouseId" :rules="[{ required: true, message: '请选择仓库' }]">
            <a-select v-model:value="form.warehouseId" placeholder="请选择仓库">
              <a-select-option v-for="item in warehouseOptions" :key="item.nodeId" :value="item.nodeId">{{ item.nodeName }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="打包类型" name="packageType" :rules="[{ required: true, message: '请选择打包类型' }]">
            <a-select v-model:value="form.packageType" placeholder="请选择打包类型">
              <a-select-option :value="1">日常打包</a-select-option>
              <a-select-option :value="2">收货打包</a-select-option>
              <a-select-option :value="3">拆包重打</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      
      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item label="备注" name="remark" :label-col="{ span: 3 }" :wrapper-col="{ span: 21 }">
            <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-divider>打包明细</a-divider>
      
      <a-button type="primary" @click="addDetail" style="margin-bottom: 16px"><PlusOutlined />添加明细</a-button>
      
      <a-table
        :dataSource="form.detailList"
        :columns="detailColumns"
        :pagination="false"
        size="small"
        bordered
        rowKey="key"
      >
        <template #bodyCell="{ text, record, column, index }">
          <template v-if="column.dataIndex === 'materialId'">
            <a-select 
              v-model:value="record.materialId" 
              placeholder="请选择耗材" 
              style="width: 100%"
              show-search
              :filter-option="filterMaterial"
            >
              <a-select-option v-for="item in materialOptions" :key="item.materialId" :value="item.materialId">
                {{ item.materialName }} - {{ item.spec }}
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.dataIndex === 'batchId'">
            <a-select v-model:value="record.batchId" placeholder="请选择批号" style="width: 100%">
              <a-select-option v-for="item in batchOptions" :key="item.batchId" :value="item.batchId">
                {{ item.batchNo }} ({{ item.validDate }})
              </a-select-option>
            </a-select>
          </template>
          <template v-else-if="column.dataIndex === 'packageNum'">
            <a-input-number v-model:value="record.packageNum" :min="1" style="width: 100%" />
          </template>
          <template v-else-if="column.dataIndex === 'packageSpec'">
            <a-input v-model:value="record.packageSpec" placeholder="如: 10支/包" />
          </template>
          <template v-else-if="column.dataIndex === 'operate'">
            <a-button type="link" danger size="small" @click="deleteDetail(index)">删除</a-button>
          </template>
        </template>
      </a-table>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { ref, reactive, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { packageApi } from '/@/api/spd/package-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { materialApi } from '/@/api/spd/material-api';
  import { batchApi } from '/@/api/spd/batch-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['reloadList']);

  const visible = ref(false);
  const isEdit = ref(false);
  const formRef = ref();
  const form = reactive({
    packageId: '',
    warehouseId: undefined,
    packageType: 1,
    remark: '',
    detailList: [],
  });

  const warehouseOptions = ref([]);
  const materialOptions = ref([]);
  const batchOptions = ref([]);

  const detailColumns = [
    { title: '耗材', dataIndex: 'materialId', width: 250 },
    { title: '批号', dataIndex: 'batchId', width: 180 },
    { title: '打包数量', dataIndex: 'packageNum', width: 120 },
    { title: '打包规格', dataIndex: 'packageSpec', width: 150 },
    { title: '备注', dataIndex: 'remark', width: 150 },
    { title: '操作', dataIndex: 'operate', width: 80, fixed: 'right' },
  ];

  let detailKeyCounter = 0;

  async function showModal(record) {
    visible.value = true;
    isEdit.value = !!record;
    
    await loadOptions();
    
    if (record) {
      Object.assign(form, {
        packageId: record.packageId,
        warehouseId: record.warehouseId,
        packageType: record.packageType || 1,
        remark: record.remark,
        detailList: [],
      });
      
      const loading = SmartLoading.show();
      try {
        const res = await packageApi.getDetail(record.packageId);
        form.detailList = (res.data.detailList || []).map(item => ({
          ...item,
          key: detailKeyCounter++
        }));
      } catch (error) {
        smartSentry.captureError(error);
      } finally {
        SmartLoading.hide(loading);
      }
    } else {
      Object.assign(form, {
        packageId: '',
        warehouseId: undefined,
        packageType: 1,
        remark: '',
        detailList: [],
      });
    }
    
    nextTick(() => formRef.value?.clearValidate());
  }

  async function loadOptions() {
    try {
      const [warehouseRes, materialRes, batchRes] = await Promise.all([
        orgNodeApi.queryWarehouseList(),
        materialApi.queryAll(),
        batchApi.queryAll(),
      ]);
      warehouseOptions.value = warehouseRes.data || [];
      materialOptions.value = materialRes.data || [];
      batchOptions.value = batchRes.data || [];
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  function addDetail() {
    form.detailList.push({
      key: detailKeyCounter++,
      materialId: undefined,
      batchId: undefined,
      packageNum: 1,
      packageSpec: '',
      remark: '',
    });
  }

  function deleteDetail(index) {
    form.detailList.splice(index, 1);
  }

  function filterMaterial(input, option) {
    return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  }

  async function handleSubmit() {
    try {
      await formRef.value.validate();
      
      if (!form.detailList.length) {
        return message.warning('请添加至少一条打包明细');
      }
      
      const loading = SmartLoading.show();
      try {
        if (isEdit.value) {
          await packageApi.update(form);
          message.success('修改成功');
        } else {
          await packageApi.add(form);
          message.success('新增成功');
        }
        handleCancel();
        emit('reloadList');
      } catch (error) {
        smartSentry.captureError(error);
      } finally {
        SmartLoading.hide(loading);
      }
    } catch (error) {
      console.error('表单验证失败:', error);
    }
  }

  function handleCancel() {
    visible.value = false;
    formRef.value?.resetFields();
    form.detailList = [];
  }

  defineExpose({ showModal });
</script>
