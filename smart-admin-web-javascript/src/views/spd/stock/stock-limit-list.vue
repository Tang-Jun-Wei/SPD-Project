<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
        <a-form-item label="耗材名称" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.materialName"
            placeholder="请输入耗材名称"
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
            <a-select-option v-for="item in warehouseList" :key="item.nodeId" :value="item.nodeId">
              {{ item.nodeName }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="库存状态" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.stockStatus"
            placeholder="请选择库存状态"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="0">正常</a-select-option>
            <a-select-option :value="1">低于预警</a-select-option>
            <a-select-option :value="2">低于下限</a-select-option>
            <a-select-option :value="3">超过上限</a-select-option>
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
          <a-button v-privilege="'spd:stockLimit:add'" type="primary" @click="showModal()">
            <template #icon>
              <PlusOutlined />
            </template>
            新增配置
          </a-button>

          <a-button
            v-privilege="'spd:stockLimit:delete'"
            type="primary"
            danger
            @click="batchDelete"
            :disabled="!hasSelected"
          >
            <template #icon>
              <DeleteOutlined />
            </template>
            批量删除
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
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        size="small"
        :scroll="{ x: 1800 }"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'stockStatus'">
            <a-tag v-if="record.stockStatus === 0" color="green">正常</a-tag>
            <a-tag v-else-if="record.stockStatus === 1" color="orange">低于预警</a-tag>
            <a-tag v-else-if="record.stockStatus === 2" color="red">低于下限</a-tag>
            <a-tag v-else-if="record.stockStatus === 3" color="purple">超过上限</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button
                v-privilege="'spd:stockLimit:update'"
                type="link"
                size="small"
                @click="showModal(record)"
              >
                编辑
              </a-button>
              <a-button
                v-privilege="'spd:stockLimit:delete'"
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
    <StockLimitFormModal ref="stockLimitFormModal" @reloadList="queryData" />

    <!-- 详情弹窗 -->
    <StockLimitDetailDrawer ref="stockLimitDetailDrawer" />
  </div>
</template>

<script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import {
    SearchOutlined,
    ReloadOutlined,
    PlusOutlined,
    DeleteOutlined,
  } from '@ant-design/icons-vue';
  import { stockLimitApi } from '/@/api/spd/stock-limit-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import StockLimitFormModal from './components/stock-limit-form-modal.vue';
  import StockLimitDetailDrawer from './components/stock-limit-detail-drawer.vue';

  // 查询表单
  const queryForm = reactive({
    materialName: '',
    warehouseId: undefined,
    stockStatus: undefined,
    pageNum: 1,
    pageSize: 10,
  });

  // 表格数据
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);
  const selectedRowKeys = ref([]);
  const warehouseList = ref([]);

  // 表格列定义
  const columns = ref([
    {
      title: '安全量业务ID',
      dataIndex: 'limitId',
      width: 180,
      ellipsis: true,
    },
    {
      title: '耗材名称',
      dataIndex: 'materialName',
      width: 200,
      ellipsis: true,
    },
    {
      title: '仓库名称',
      dataIndex: 'warehouseName',
      width: 150,
    },
    {
      title: '当前库存',
      dataIndex: 'currentStock',
      width: 100,
      align: 'center',
    },
    {
      title: '最小库存',
      dataIndex: 'minStock',
      width: 100,
      align: 'center',
    },
    {
      title: '预警库存',
      dataIndex: 'warningStock',
      width: 100,
      align: 'center',
    },
    {
      title: '最大库存',
      dataIndex: 'maxStock',
      width: 100,
      align: 'center',
    },
    {
      title: '库存状态',
      dataIndex: 'stockStatus',
      width: 120,
      align: 'center',
    },
    {
      title: '备注',
      dataIndex: 'remark',
      width: 200,
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 180,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      fixed: 'right',
      width: 200,
    },
  ]);

  // 计算属性
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      const res = await stockLimitApi.queryPage(queryForm);
      if (res.data) {
        tableData.value = res.data.list || [];
        total.value = res.data.total || 0;
      }
    } catch (error) {
      smartSentry.captureError(error);
      message.error('查询失败');
    } finally {
      tableLoading.value = false;
    }
  }

  // 重置查询
  function resetQuery() {
    queryForm.materialName = '';
    queryForm.warehouseId = undefined;
    queryForm.stockStatus = undefined;
    queryForm.pageNum = 1;
    queryData();
  }

  // 选择变化
  function onSelectChange(selectedKeys) {
    selectedRowKeys.value = selectedKeys;
  }

  // 显示弹窗
  function showModal(row) {
    stockLimitFormModal.value.showModal(row);
  }

  // 显示详情
  function showDetail(row) {
    stockLimitDetailDrawer.value.showDrawer(row.id);
  }

  // 单个删除
  async function singleDelete(row) {
    const loading = SmartLoading.show();
    try {
      const res = await stockLimitApi.delete(row.id);
      if (res.data) {
        message.success('删除成功');
        queryData();
      } else {
        message.error(res.msg || '删除失败');
      }
    } catch (error) {
      smartSentry.captureError(error);
      message.error('删除失败');
    } finally {
      SmartLoading.hide(loading);
    }
  }

  // 批量删除
  async function batchDelete() {
    if (!hasSelected.value) {
      message.warning('请先选择要删除的数据');
      return;
    }

    const loading = SmartLoading.show();
    try {
      for (const id of selectedRowKeys.value) {
        await stockLimitApi.delete(id);
      }
      message.success('批量删除成功');
      selectedRowKeys.value = [];
      queryData();
    } catch (error) {
      smartSentry.captureError(error);
      message.error('批量删除失败');
    } finally {
      SmartLoading.hide(loading);
    }
  }

  // 加载仓库列表
  async function loadWarehouseList() {
    try {
      const res = await orgNodeApi.queryByType({ nodeType: 2 });
      if (res.data) {
        warehouseList.value = res.data || [];
      }
    } catch (error) {
      smartSentry.captureError(error);
    }
  }

  const stockLimitFormModal = ref();
  const stockLimitDetailDrawer = ref();

  onMounted(() => {
    queryData();
    loadWarehouseList();
  });
</script>

<style scoped lang="less"></style>
