/**
 * 数据导出工具类
 */
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/framework/smart-loading';

/**
 * 导出Excel文件
 * @param {Function} exportApi - 导出API方法
 * @param {Object} params - 导出参数
 * @param {String} fileName - 文件名（不含扩展名）
 */
export async function exportExcel(exportApi, params, fileName) {
  try {
    SmartLoading.show('正在导出数据...');
    
    const res = await exportApi(params);
    
    // 创建Blob对象
    const blob = new Blob([res], { 
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
    });
    
    // 创建下载链接
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${fileName}_${new Date().getTime()}.xlsx`;
    
    // 触发下载
    document.body.appendChild(link);
    link.click();
    
    // 清理
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
    
    message.success('导出成功');
  } catch (error) {
    console.error('导出失败：', error);
    message.error('导出失败，请稍后重试');
    throw error;
  } finally {
    SmartLoading.hide();
  }
}

/**
 * 导出CSV文件
 * @param {Array} data - 数据数组
 * @param {Array} columns - 列配置
 * @param {String} fileName - 文件名（不含扩展名）
 */
export function exportCSV(data, columns, fileName) {
  try {
    // 生成CSV表头
    const headers = columns.map(col => col.title).join(',');
    
    // 生成CSV数据行
    const rows = data.map(row => {
      return columns.map(col => {
        const value = row[col.dataIndex] || '';
        // 处理包含逗号的值
        return typeof value === 'string' && value.includes(',') 
          ? `"${value}"` 
          : value;
      }).join(',');
    });
    
    // 合并表头和数据
    const csvContent = [headers, ...rows].join('\n');
    
    // 添加BOM以支持中文
    const BOM = '\uFEFF';
    const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' });
    
    // 创建下载链接
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${fileName}_${new Date().getTime()}.csv`;
    
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
    
    message.success('导出成功');
  } catch (error) {
    console.error('导出失败：', error);
    message.error('导出失败，请稍后重试');
  }
}

export default {
  exportExcel,
  exportCSV,
};
