<template>
  <div class="smart-table-container">
    <!-- 查询表单 -->
    <a-form class="smart-query-form" layout="inline">
      <a-row class="smart-query-form-row">
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

        <a-form-item label="用户" class="smart-query-form-item">
          <a-input
            v-model:value="queryForm.userName"
            placeholder="请输入用户名或姓名"
            style="width: 200px"
            allowClear
          />
        </a-form-item>

        <a-form-item label="角色类型" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.roleType"
            placeholder="请选择角色类型"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="1">仓库管理员</a-select-option>
            <a-select-option :value="2">普通库管员</a-select-option>
            <a-select-option :value="3">拣货员</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="状态" class="smart-query-form-item">
          <a-select
            v-model:value="queryForm.status"
            placeholder="请选择状态"
            style="width: 200px"
            allowClear
          >
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
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
          <a-button v-privilege="'spd:warehouseUser:add'" type="primary" @click="showModal()">
            <template #icon>
              <PlusOutlined />
            </template>
            新增关系
          </a-button>

          <a-button
            v-privilege="'spd:warehouseUser:delete'"
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
        :scroll="{ x: 1400 }"
        :dataSource="tableData"
        :columns="columns"
        :loading="tableLoading"
        :pagination="false"
        rowKey="id"
        bordered
      >
        <template #bodyCell="{ text, record, column }">
          <template v-if="column.dataIndex === 'roleType'">
            <a-tag v-if="record.roleType === 1" color="red">仓库管理员</a-tag>
            <a-tag v-else-if="record.roleType === 2" color="blue">普通库管员</a-tag>
            <a-tag v-else color="green">拣货员</a-tag>
          </template>

          <template v-if="column.dataIndex === 'status'">
            <a-tag v-if="record.status === 1" color="green">启用</a-tag>
            <a-tag v-else color="red">禁用</a-tag>
          </template>

          <template v-if="column.dataIndex === 'operate'">
            <div class="smart-table-operate">
              <a-button type="link" size="small" @click="showDetail(record)">详情</a-button>
              <a-button
                v-privilege="'spd:warehouseUser:update'"
                type="link"
                size="small"
                @click="showModal(record)"
              >
                编辑
              </a-button>
              <a-button
                v-privilege="'spd:warehouseUser:updateStatus'"
                type="link"
                size="small"
                @click="updateStatus(record)"
              >
                {{ record.status === 1 ? '禁用' : '启用' }}
              </a-button>
              <a-button
                v-privilege="'spd:warehouseUser:delete'"
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
    <WarehouseUserFormModal ref="warehouseUserFormModal" @reloadList="queryData" />

    <!-- 详情抽屉 -->
    <WarehouseUserDetailDrawer ref="warehouseUserDetailDrawer" />
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
  import { warehouseUserApi } from '/@/api/spd/warehouse-user-api';
  import { orgNodeApi } from '/@/api/spd/org-node-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import WarehouseUserFormModal from './components/warehouse-user-form-modal.vue';
  import WarehouseUserDetailDrawer from './components/warehouse-user-detail-drawer.vue';

  // 查询表单
  const queryForm = reactive({
    warehouseId: undefined,
    userName: '',
    roleType: undefined,
    status: undefined,
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
      title: '关系业务ID',
      dataIndex: 'relationId',
      width: 180,
      ellipsis: true,
    },
    {
      title: '仓库名称',
      dataIndex: 'warehouseName',
      width: 150,
    },
    {
      title: '用户名',
      dataIndex: 'userName',
      width: 120,
    },
    {
      title: '真实姓名',
      dataIndex: 'actualName',
      width: 120,
    },
    {
      title: '角色类型',
      dataIndex: 'roleType',
      width: 120,
      align: 'center',
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
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
      width: 220,
    },
  ]);

  // 计算属性
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      const res = await warehouseUserApi.queryPage(queryForm);
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
    queryForm.warehouseId = undefined;
    queryForm.userName = '';
    queryForm.roleType = undefined;
    queryForm.status = undefined;
    queryForm.pageNum = 1;
    queryData();
  }

  // 选择变化
  function onSelectChange(selectedKeys) {
    selectedRowKeys.value = selectedKeys;
  }

  // 显示弹窗
  function showModal(row) {
    warehouseUserFormModal.value.showModal(row);
  }

  // 显示详情
  function showDetail(row) {
    warehouseUserDetailDrawer.value.showDrawer(row.id);
  }

  // 单个删除
  async function singleDelete(row) {
    const loading = SmartLoading.show();
    try {
      const res = await warehouseUserApi.delete(row.id);
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
        await warehouseUserApi.delete(id);
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

  // 更新状态
  async function updateStatus(row) {
    const newStatus = row.status === 1 ? 0 : 1;
    const loading = SmartLoading.show();
    try {
      const res = await warehouseUserApi.updateStatus(row.id, newStatus);
      if (res.data) {
        message.success('状态更新成功');
        queryData();
      } else {
        message.error(res.msg || '状态更新失败');
      }
    } catch (error) {
      smartSentry.captureError(error);
      message.error('状态更新失败');
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

  const warehouseUserFormModal = ref();
  const warehouseUserDetailDrawer = ref();

  onMounted(() => {
    queryData();
    loadWarehouseList();
  });
</script>

<style scoped lang="less"></style>
