# SPD系统完整开发流程文档

## 1. 项目概述

### 1.1 技术栈
- **后端**: Java 17 + Spring Boot 3 + MyBatis-Plus + Swagger3
- **前端**: Vue 3 (Composition API) + Ant Design Vue + axios封装  
- **数据库**: MySQL 8.0，字符集utf8mb4，逻辑删除使用del_flag字段
- **框架**: 基于Smart-Admin V3.26框架

### 1.2 项目结构
```
├── smart-admin-api-java17-springboot3/    # 后端项目
│   └── sa-admin/
├── smart-admin-web-javascript/           # 前端项目
│   └── sa-admin/
├── 开发文档/                            # 开发文档
├── 数据库SQL脚本/                       # 数据库脚本
└── 启动服务.bat, 关闭服务.bat           # 服务管理脚本
```

## 2. 模块化开发规范

### 2.1 12大核心模块
1. **基础资料模块** - 耗材/厂家/科室仓库/分类/货位/批号维护
2. **基础资料审核（物资）模块** - 新增修改审核/调价审核/收费审核
3. **基础资料审核（物价）模块** - 物价调价审核/收费审核
4. **库存监控模块** - 安全量监控/效期监控/三层库存查询
5. **采购管理模块** - 安全量采购/采购申请/审核/查询
6. **收货管理模块** - 配送单下载/检验审核/打包/散货收货
7. **科室申领模块** - 申领维护/审核/打包单审核
8. **出库模块** - 拣配单审核/配送单查询
9. **科室管理模块** - 科室收货/消耗/反消耗-扫码
10. **库存管理模块** - 标签追溯/散货打包/定数包拆包/科室调拨
11. **科室退库管理模块** - 退库申请/审核
12. **退供应商管理模块** - 退供应商申请/审核

### 2.2 通用开发规范
- **前端布局**: 顶部查询区+左侧筛选区+中间数据展示区+底部分页区
- **按钮规范**: 核心按钮（新增/审核/提交）居左，辅助按钮（导入/导出/刷新）居右
- **状态标签**: 待审核=蓝色、已通过=绿色、已驳回=红色
- **数据关联**: 通过业务单号（如apply_code/purchase_code）关联主表与明细表
- **视图分类**: 每个节点对应3类基础视图（列表/操作/明细）

## 3. 后端开发流程

### 3.1 创建实体类 (Entity)
```java
// 例如：采购申请实体
@TableName("purchase_apply")
public class PurchaseApply {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applyCode;  // 业务单号
    private String applyName;
    private Integer applyStatus;  // 状态：0待审核 1已通过 2已驳回
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private Integer delFlag;  // 逻辑删除标志
    // getter/setter...
}
```

### 3.2 创建Mapper接口
```java
@Mapper
public interface PurchaseApplyMapper extends BaseMapper<PurchaseApply> {
    // 自定义查询方法
    List<PurchaseApplyVO> selectPageWithDetails(@Param("query") PurchaseApplyQuery query);
    
    // 关联查询明细
    List<PurchaseApplyDetail> selectDetailsByApplyId(@Param("applyId") Long applyId);
}
```

### 3.3 创建Service接口及实现
```java
// Service接口
public interface PurchaseApplyService extends IService<PurchaseApply> {
    PageResult<PurchaseApplyVO> selectPage(PurchaseApplyQuery query);
    void submitApply(Long id);
    void auditApply(Long id, Integer status, String auditReason);
    PurchaseApplyVO selectById(Long id);
}

// Service实现
@Service
public class PurchaseApplyServiceImpl extends ServiceImpl<PurchaseApplyMapper, PurchaseApply> 
    implements PurchaseApplyService {
    
    @Override
    public PageResult<PurchaseApplyVO> selectPage(PurchaseApplyQuery query) {
        // 分页查询实现
        IPage<PurchaseApplyVO> page = MybatisUtil.getPage(query);
        List<PurchaseApplyVO> records = baseMapper.selectPageWithDetails(query);
        return PageResult.build(page, records);
    }
    
    @Override
    @Transactional
    public void submitApply(Long id) {
        PurchaseApply apply = getById(id);
        apply.setApplyStatus(1); // 待审核
        updateById(apply);
    }
}
```

### 3.4 创建Controller
```java
@RestController
@RequestMapping("/api/spd/purchase-apply")
public class PurchaseApplyController {
    
    @Autowired
    private PurchaseApplyService purchaseApplyService;
    
    @GetMapping("/page")
    public R<PageResult<PurchaseApplyVO>> getPage(PurchaseApplyQuery query) {
        return R.ok(purchaseApplyService.selectPage(query));
    }
    
    @PostMapping("/submit")
    public R<Void> submit(@RequestBody @Validated IdReq req) {
        purchaseApplyService.submitApply(req.getId());
        return R.ok();
    }
    
    @PostMapping("/audit")
    public R<Void> audit(@RequestBody @Validated PurchaseApplyAuditReq req) {
        purchaseApplyService.auditApply(req.getId(), req.getStatus(), req.getAuditReason());
        return R.ok();
    }
}
```

## 4. 前端开发流程

### 4.1 创建页面组件
```vue
<!-- 采购申请页面 -->
<template>
  <div class="page-container">
    <!-- 查询区 -->
    <a-card class="query-card">
      <a-form :model="queryParams" layout="inline">
        <a-form-item label="申请单号">
          <a-input v-model:value="queryParams.applyCode" placeholder="请输入申请单号" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery">查询</a-button>
          <a-button @click="resetQuery">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 操作按钮区 -->
    <div class="operation-buttons">
      <a-button type="primary" @click="handleAdd">新增</a-button>
      <a-button type="primary" @click="handleBatchSubmit" :disabled="!selectedRowKeys.length">
        批量提交
      </a-button>
      <a-button @click="handleExport">导出</a-button>
    </div>

    <!-- 数据表格 -->
    <a-table 
      :columns="columns" 
      :data-source="list" 
      :loading="loading"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      :pagination="pagination"
      row-key="id"
    >
      <template #status="{ record }">
        <a-tag :color="getStatusColor(record.applyStatus)">
          {{ getStatusText(record.applyStatus) }}
        </a-tag>
      </template>
      <template #action="{ record }">
        <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
        <a-button type="link" size="small" @click="handleView(record)">查看</a-button>
        <a-button type="link" size="small" @click="handleSubmit(record)" :disabled="record.applyStatus !== 0">
          提交
        </a-button>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { purchaseApplyApi } from '@/api/spd/purchase-apply';

// 数据定义
const list = ref([]);
const loading = ref(false);
const selectedRowKeys = ref([]);

// 查询参数
const queryParams = reactive({
  applyCode: null,
  applyStatus: null,
  pageNum: 1,
  pageSize: 10
});

// 表格列定义
const columns = [
  {
    title: '申请单号',
    dataIndex: 'applyCode',
    width: 150
  },
  {
    title: '申请名称',
    dataIndex: 'applyName',
    width: 200
  },
  {
    title: '申请状态',
    dataIndex: 'applyStatus',
    width: 100,
    slots: { customRender: 'status' }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 150
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    slots: { customRender: 'action' }
  }
];

// 方法定义
const handleQuery = () => {
  getList();
};

const getList = async () => {
  loading.value = true;
  try {
    const response = await purchaseApplyApi.getPage(queryParams);
    list.value = response.data.records;
    pagination.total = response.data.total;
  } catch (error) {
    message.error('获取数据失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  getList();
});
</script>
```

### 4.2 创建API接口
```javascript
// api/spd/purchase-apply.js
import request from '@/utils/request';

export const purchaseApplyApi = {
  // 分页查询
  getPage: (params) => {
    return request({
      url: '/api/spd/purchase-apply/page',
      method: 'get',
      params
    });
  },
  
  // 新增
  add: (data) => {
    return request({
      url: '/api/spd/purchase-apply',
      method: 'post',
      data
    });
  },
  
  // 提交申请
  submit: (data) => {
    return request({
      url: '/api/spd/purchase-apply/submit',
      method: 'post',
      data
    });
  },
  
  // 审核
  audit: (data) => {
    return request({
      url: '/api/spd/purchase-apply/audit',
      method: 'post',
      data
    });
  }
};
```

## 5. 数据库设计规范

### 5.1 表结构规范
```sql
-- 采购申请主表
CREATE TABLE `purchase_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `apply_code` varchar(50) NOT NULL COMMENT '申请单号',
  `apply_name` varchar(100) NOT NULL COMMENT '申请名称',
  `apply_status` tinyint NOT NULL DEFAULT '0' COMMENT '申请状态: 0-待审核 1-已通过 2-已驳回',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志: 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_apply_code` (`apply_code`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_apply_status` (`apply_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购申请表';

-- 采购申请明细表
CREATE TABLE `purchase_apply_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `apply_id` bigint NOT NULL COMMENT '申请单ID',
  `goods_id` bigint NOT NULL COMMENT '耗材ID',
  `quantity` int NOT NULL COMMENT '申请数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_apply_id` (`apply_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购申请明细表';
```

### 5.2 视图设计
```sql
-- 采购申请列表视图
CREATE VIEW `v_purchase_apply_list` AS
SELECT 
    pa.id,
    pa.apply_code,
    pa.apply_name,
    pa.apply_status,
    u1.nick_name as create_by_name,
    pa.create_time,
    u2.nick_name as update_by_name,
    pa.update_time
FROM purchase_apply pa
LEFT JOIN sys_user u1 ON pa.create_by = u1.user_id
LEFT JOIN sys_user u2 ON pa.update_by = u2.user_id
WHERE pa.del_flag = 0;

-- 采购申请操作视图（包含更多字段用于编辑）
CREATE VIEW `v_purchase_apply_form` AS
SELECT 
    pa.*,
    u.nick_name as create_by_name
FROM purchase_apply pa
LEFT JOIN sys_user u ON pa.create_by = u.user_id
WHERE pa.del_flag = 0;
```

## 6. 开发流程总结

### 6.1 开发顺序
按照业务流程和依赖关系，优先完成：
1. **基础资料** → 2. **采购收货** → 3. **申领出库** → 4. **科室管理** → 5. **退库退货**

### 6.2 完整开发步骤
1. **数据库设计** - 创建主表和明细表
2. **后端开发** - Entity → Mapper → Service → Controller
3. **前端开发** - API接口 → 页面组件 → 表单验证
4. **测试验证** - 功能测试 → 数据验证 → 权限验证
5. **Git提交** - 代码提交 → 推送远程 → 合并分支

### 6.3 质量控制
- 代码规范：遵循项目编码规范
- 数据验证：前后端双重验证
- 异常处理：完善的异常捕获和处理
- 日志记录：关键操作日志记录
- 性能优化：分页查询、索引优化