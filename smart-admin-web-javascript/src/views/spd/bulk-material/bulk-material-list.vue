<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="散货ID" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.bulkId"
            placeholder="请输入散货ID"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="耗材名称" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.materialName"
            placeholder="请输入耗材名称"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="批号" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.batchNo"
            placeholder="请输入批号"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="仓库" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.warehouseId"
            placeholder="请选择仓库"
            style="width: 200px"
            allowClear
          >
            <!-- 仓库选项需要从后端获取 -->
          </a-select>
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="queryData">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery">
              <template #icon>
                <ReloadOutlined />
              </template>
              重置
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <!-- 操作按钮和表格 -->
    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
          <a-button v-privilege="'spd:bulk:add'" type="primary" @click="showModal()">
            <template #icon>
              <PlusOutlined />
            </template>
            新增散货
          </a-button>
        </div>
        <div class="smart-table-setting-block">
          <a-button @click="queryData">
            <template #icon>
              <ReloadOutlined />
            </template>
          </a-button>
        </div>
      </a-row>

      <!-- 表格 -->
      <a-table
        size="small"
        :scroll="{ x: 1500 }"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="bulkId"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'remainingNum'">
            <span :style="{ color: text <= 10 ? 'red' : text <= 50 ? 'orange' : 'green' }">
              {{ text }}
            </span>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button
                v-privilege="'spd:bulk:consume'"
                type="link"
                size="small"
                @click="showConsumeModal(record)"
                :disabled="record.remainingNum === 0"
              >
                消耗
              </a-button>
              <a-button
                v-privilege="'spd:bulk:update'"
                type="link"
                size="small"
                @click="showModal(record)"
              >
                编辑
              </a-button>
              <a-button
                v-privilege="'spd:bulk:delete'"
                danger
                type="link"
                size="small"
                @click="singleDelete(record)"
              >
                删除
              </a-button>
            </div>
          </template>
        </template>
      </a-table>

      <!-- 分页 -->
      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          @showSizeChange="queryData"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <BulkMaterialFormModal ref="bulkMaterialFormModal" @reloadList="queryData" />

    <!-- 消耗弹窗 -->
    <BulkConsumeModal ref="bulkConsumeModal" @reloadList="queryData" />

    <!-- 详情弹窗 -->
    <BulkDetailDrawer ref="bulkDetailDrawer" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
  } from '@ant-design/icons-vue';
  import { bulkMaterialApi } from '/@/api/spd/bulk-material-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import BulkMaterialFormModal from './components/bulk-material-form-modal.vue';
  import BulkConsumeModal from './components/bulk-consume-modal.vue';
  import BulkDetailDrawer from './components/bulk-detail-drawer.vue';

  // 查询表单
  const queryForm = reactive({
    bulkId: '',
    materialName: '',
    batchNo: '',
    warehouseId: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  // 表格数据
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  // 表格列定义
  const columns = ref([
    {
      title: '散货ID',
      dataIndex: 'bulkId',
      width: 160,
      fixed: 'left',
    },
    {
      title: '耗材名称',
      dataIndex: 'materialName',
      width: 200,
    },
    {
      title: '规格型号',
      dataIndex: 'specification',
      width: 150,
    },
    {
      title: '批号',
      dataIndex: 'batchNo',
      width: 120,
    },
    {
      title: '总数量',
      dataIndex: 'totalNum',
      width: 100,
    },
    {
      title: '已用数量',
      dataIndex: 'usedNum',
      width: 100,
    },
    {
      title: '剩余数量',
      dataIndex: 'remainingNum',
      width: 100,
    },
    {
      title: '单位',
      dataIndex: 'unit',
      width: 60,
    },
    {
      title: '仓库',
      dataIndex: 'warehouseName',
      width: 120,
    },
    {
      title: '入库时间',
      dataIndex: 'createTime',
      width: 160,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      fixed: 'right',
      width: 200,
    },
  ]);

  // 查询数据
  async function queryData() {
    try {
      tableLoading.value = true;
      const res = await bulkMaterialApi.queryPage(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // 重置查询
  function resetQuery() {
    queryForm.bulkId = '';
    queryForm.materialName = '';
    queryForm.batchNo = '';
    queryForm.warehouseId = undefined;
    queryForm.pageNum = 1;
    queryData();
  }

  // 显示表单弹窗
  const bulkMaterialFormModal = ref();
  function showModal(record) {
    bulkMaterialFormModal.value.showModal(record);
  }

  // 显示消耗弹窗
  const bulkConsumeModal = ref();
  function showConsumeModal(record) {
    bulkConsumeModal.value.showModal(record);
  }

  // 显示详情弹窗
  const bulkDetailDrawer = ref();
  function showDetail(record) {
    bulkDetailDrawer.value.showDrawer(record.bulkId);
  }

  // 单个删除
  function singleDelete(record) {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该散货吗？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await bulkMaterialApi.delete(record.bulkId);
          message.success('删除成功');
          queryData();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
    });
  }

  onMounted(() => {
    queryData();
  });
</script>

<style scoped lang="less">
  .smart-table-container {
    padding: 10px;
  }
</style>
