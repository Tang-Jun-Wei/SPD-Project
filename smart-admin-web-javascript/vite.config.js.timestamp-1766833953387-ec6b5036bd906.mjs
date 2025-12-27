// vite.config.js
import { resolve } from "path";
import vue from "file:///D:/Program%20Files%20(x86)/Spd_Project/smart-admin-web-javascript/node_modules/@vitejs/plugin-vue/dist/index.mjs";

// src/theme/custom-variables.js
import { theme } from "file:///D:/Program%20Files%20(x86)/Spd_Project/smart-admin-web-javascript/node_modules/ant-design-vue/lib/index.js";
import convertLegacyToken from "file:///D:/Program%20Files%20(x86)/Spd_Project/smart-admin-web-javascript/node_modules/ant-design-vue/lib/theme/convertLegacyToken.js";
var { defaultAlgorithm, defaultSeed } = theme;
var mapToken = defaultAlgorithm(defaultSeed);
var token = convertLegacyToken.default(mapToken);
var custom_variables_default = {
  "@primary-color": token["primary-color"],
  // 全局主色
  "@base-bg-color": "#fff",
  "@hover-bg-color": "rgba(0, 0, 0, 0.025)",
  "@hover-bg-color-night": "rgba(255, 255, 255, 0.025)",
  "@header-light-bg-hover-color": "#f6f6f6",
  "@header-height": "80px",
  "@header-user-height": "40px",
  "@page-tag-height": "40px",
  "@theme-list": ["light", "dark", "night"]
};

// vite.config.js
var __vite_injected_original_dirname = "D:\\Program Files (x86)\\Spd_Project\\smart-admin-web-javascript";
var pathResolve = (dir) => {
  return resolve(__vite_injected_original_dirname, ".", dir);
};
var vite_config_default = {
  base: process.env.NODE_ENV === "production" ? "/" : "/",
  root: process.cwd(),
  resolve: {
    alias: [
      // 国际化替换
      {
        find: "vue-i18n",
        replacement: "vue-i18n/dist/vue-i18n.cjs.js"
      },
      // 绝对路径重命名：/@/xxxx => src/xxxx
      {
        find: /\/@\//,
        replacement: pathResolve("src") + "/"
      },
      {
        find: /^~/,
        replacement: ""
      }
    ]
  },
  server: {
    host: "0.0.0.0",
    port: 8081,
    server: {
      proxy: {
        // 代理路径
        "/": {
          target: "http://127.0.0.1:1024/",
          // 目标服务器地址
          changeOrigin: true,
          // 是否修改请求头中的 Origin 字段
          rewrite: (path) => path
          // 重写路径
        }
      }
    }
  },
  plugins: [vue()],
  optimizeDeps: {
    include: ["ant-design-vue/es/locale/zh_CN", "dayjs/locale/zh-cn", "ant-design-vue/es/locale/en_US"],
    exclude: ["vue-demi"]
  },
  build: {
    // 清除console和debugger
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    },
    rollupOptions: {
      output: {
        //配置这个是让不同类型文件放在不同文件夹，不会显得太乱
        chunkFileNames: "js/[name]-[hash].js",
        entryFileNames: "js/[name]-[hash].js",
        assetFileNames: "[ext]/[name]-[hash].[ext]",
        manualChunks(id) {
          if (id.includes("node_modules")) {
            return id.toString().split("node_modules/")[1].split("/")[0].toString();
          }
        }
      }
    },
    target: "esnext",
    outDir: "dist",
    // 指定输出路径
    assetsDir: "assets",
    // 指定生成静态文件目录
    assetsInlineLimit: "4096",
    // 小于此阈值的导入或引用资源将内联为 base64 编码
    chunkSizeWarningLimit: 500,
    // chunk 大小警告的限制
    minify: "terser",
    // 混淆器，terser构建后文件体积更小
    emptyOutDir: true
    //打包前先清空原有打包文件
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: custom_variables_default,
        javascriptEnabled: true
      }
    }
  },
  define: {
    __INTLIFY_PROD_DEVTOOLS__: false,
    "process.env": process.env
  }
};
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiLCAic3JjL3RoZW1lL2N1c3RvbS12YXJpYWJsZXMuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxQcm9ncmFtIEZpbGVzICh4ODYpXFxcXFNwZF9Qcm9qZWN0XFxcXHNtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0XCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJEOlxcXFxQcm9ncmFtIEZpbGVzICh4ODYpXFxcXFNwZF9Qcm9qZWN0XFxcXHNtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0XFxcXHZpdGUuY29uZmlnLmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9EOi9Qcm9ncmFtJTIwRmlsZXMlMjAoeDg2KS9TcGRfUHJvamVjdC9zbWFydC1hZG1pbi13ZWItamF2YXNjcmlwdC92aXRlLmNvbmZpZy5qc1wiO1x1RkVGRi8qXHJcbiAqIHZpdGVcdTkxNERcdTdGNkVcclxuICpcclxuICovXHJcbmltcG9ydCB7IHJlc29sdmUgfSBmcm9tICdwYXRoJztcclxuaW1wb3J0IHZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnO1xyXG5pbXBvcnQgY3VzdG9tVmFyaWFibGVzIGZyb20gJy9AL3RoZW1lL2N1c3RvbS12YXJpYWJsZXMuanMnO1xyXG5cclxuY29uc3QgcGF0aFJlc29sdmUgPSAoZGlyKSA9PiB7XHJcbiAgcmV0dXJuIHJlc29sdmUoX19kaXJuYW1lLCAnLicsIGRpcik7XHJcbn07XHJcbmV4cG9ydCBkZWZhdWx0IHtcclxuICBiYXNlOiBwcm9jZXNzLmVudi5OT0RFX0VOViA9PT0gJ3Byb2R1Y3Rpb24nID8gJy8nIDogJy8nLFxyXG4gIHJvb3Q6IHByb2Nlc3MuY3dkKCksXHJcbiAgcmVzb2x2ZToge1xyXG4gICAgYWxpYXM6IFtcclxuICAgICAgLy8gXHU1NkZEXHU5NjQ1XHU1MzE2XHU2NkZGXHU2MzYyXHJcbiAgICAgIHtcclxuICAgICAgICBmaW5kOiAndnVlLWkxOG4nLFxyXG4gICAgICAgIHJlcGxhY2VtZW50OiAndnVlLWkxOG4vZGlzdC92dWUtaTE4bi5janMuanMnLFxyXG4gICAgICB9LFxyXG4gICAgICAvLyBcdTdFRERcdTVCRjlcdThERUZcdTVGODRcdTkxQ0RcdTU0N0RcdTU0MERcdUZGMUEvQC94eHh4ID0+IHNyYy94eHh4XHJcbiAgICAgIHtcclxuICAgICAgICBmaW5kOiAvXFwvQFxcLy8sXHJcbiAgICAgICAgcmVwbGFjZW1lbnQ6IHBhdGhSZXNvbHZlKCdzcmMnKSArICcvJyxcclxuICAgICAgfSxcclxuICAgICAge1xyXG4gICAgICAgIGZpbmQ6IC9efi8sXHJcbiAgICAgICAgcmVwbGFjZW1lbnQ6ICcnLFxyXG4gICAgICB9LFxyXG4gICAgXSxcclxuICB9LFxyXG4gIHNlcnZlcjoge1xyXG4gICAgaG9zdDogJzAuMC4wLjAnLFxyXG4gICAgcG9ydDogODA4MSxcclxuICAgIHNlcnZlcjoge1xyXG4gICAgICBwcm94eToge1xyXG4gICAgICAgIC8vIFx1NEVFM1x1NzQwNlx1OERFRlx1NUY4NFxyXG4gICAgICAgICcvJzoge1xyXG4gICAgICAgICAgdGFyZ2V0OiAnaHR0cDovLzEyNy4wLjAuMToxMDI0LycsIC8vIFx1NzZFRVx1NjgwN1x1NjcwRFx1NTJBMVx1NTY2OFx1NTczMFx1NTc0MFxyXG4gICAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLCAvLyBcdTY2MkZcdTU0MjZcdTRGRUVcdTY1MzlcdThCRjdcdTZDNDJcdTU5MzRcdTRFMkRcdTc2ODQgT3JpZ2luIFx1NUI1N1x1NkJCNVxyXG4gICAgICAgICAgcmV3cml0ZTogKHBhdGgpID0+IHBhdGgsIC8vIFx1OTFDRFx1NTE5OVx1OERFRlx1NUY4NFxyXG4gICAgICAgIH0sXHJcbiAgICAgIH0sXHJcbiAgICB9XHJcbiAgfSxcclxuICBwbHVnaW5zOiBbdnVlKCldLFxyXG4gIG9wdGltaXplRGVwczoge1xyXG4gICAgaW5jbHVkZTogWydhbnQtZGVzaWduLXZ1ZS9lcy9sb2NhbGUvemhfQ04nLCAnZGF5anMvbG9jYWxlL3poLWNuJywgJ2FudC1kZXNpZ24tdnVlL2VzL2xvY2FsZS9lbl9VUyddLFxyXG4gICAgZXhjbHVkZTogWyd2dWUtZGVtaSddLFxyXG4gIH0sXHJcbiAgYnVpbGQ6IHtcclxuICAgIC8vIFx1NkUwNVx1OTY2NGNvbnNvbGVcdTU0OENkZWJ1Z2dlclxyXG4gICAgdGVyc2VyT3B0aW9uczoge1xyXG4gICAgICBjb21wcmVzczoge1xyXG4gICAgICAgIGRyb3BfY29uc29sZTogdHJ1ZSxcclxuICAgICAgICBkcm9wX2RlYnVnZ2VyOiB0cnVlLFxyXG4gICAgICB9LFxyXG4gICAgfSxcclxuICAgIHJvbGx1cE9wdGlvbnM6IHtcclxuICAgICAgb3V0cHV0OiB7XHJcbiAgICAgICAgLy9cdTkxNERcdTdGNkVcdThGRDlcdTRFMkFcdTY2MkZcdThCQTlcdTRFMERcdTU0MENcdTdDN0JcdTU3OEJcdTY1ODdcdTRFRjZcdTY1M0VcdTU3MjhcdTRFMERcdTU0MENcdTY1ODdcdTRFRjZcdTU5MzlcdUZGMENcdTRFMERcdTRGMUFcdTY2M0VcdTVGOTdcdTU5MkFcdTRFNzFcclxuICAgICAgICBjaHVua0ZpbGVOYW1lczogJ2pzL1tuYW1lXS1baGFzaF0uanMnLFxyXG4gICAgICAgIGVudHJ5RmlsZU5hbWVzOiAnanMvW25hbWVdLVtoYXNoXS5qcycsXHJcbiAgICAgICAgYXNzZXRGaWxlTmFtZXM6ICdbZXh0XS9bbmFtZV0tW2hhc2hdLltleHRdJyxcclxuICAgICAgICBtYW51YWxDaHVua3MoaWQpIHtcclxuICAgICAgICAgIC8vXHU5NzU5XHU2MDAxXHU4RDQ0XHU2RTkwXHU1MjA2XHU2MkM2XHU2MjUzXHU1MzA1XHJcbiAgICAgICAgICBpZiAoaWQuaW5jbHVkZXMoJ25vZGVfbW9kdWxlcycpKSB7XHJcbiAgICAgICAgICAgIHJldHVybiBpZC50b1N0cmluZygpLnNwbGl0KCdub2RlX21vZHVsZXMvJylbMV0uc3BsaXQoJy8nKVswXS50b1N0cmluZygpO1xyXG4gICAgICAgICAgfVxyXG4gICAgICAgIH0sXHJcbiAgICAgIH0sXHJcbiAgICB9LFxyXG4gICAgdGFyZ2V0OiAnZXNuZXh0JyxcclxuICAgIG91dERpcjogJ2Rpc3QnLCAvLyBcdTYzMDdcdTVCOUFcdThGOTNcdTUxRkFcdThERUZcdTVGODRcclxuICAgIGFzc2V0c0RpcjogJ2Fzc2V0cycsIC8vIFx1NjMwN1x1NUI5QVx1NzUxRlx1NjIxMFx1OTc1OVx1NjAwMVx1NjU4N1x1NEVGNlx1NzZFRVx1NUY1NVxyXG4gICAgYXNzZXRzSW5saW5lTGltaXQ6ICc0MDk2JywgLy8gXHU1QzBGXHU0RThFXHU2QjY0XHU5NjA4XHU1MDNDXHU3Njg0XHU1QkZDXHU1MTY1XHU2MjE2XHU1RjE1XHU3NTI4XHU4RDQ0XHU2RTkwXHU1QzA2XHU1MTg1XHU4MDU0XHU0RTNBIGJhc2U2NCBcdTdGMTZcdTc4MDFcclxuICAgIGNodW5rU2l6ZVdhcm5pbmdMaW1pdDogNTAwLCAvLyBjaHVuayBcdTU5MjdcdTVDMEZcdThCNjZcdTU0NEFcdTc2ODRcdTk2NTBcdTUyMzZcclxuICAgIG1pbmlmeTogJ3RlcnNlcicsIC8vIFx1NkRGN1x1NkRDNlx1NTY2OFx1RkYwQ3RlcnNlclx1Njc4NFx1NUVGQVx1NTQwRVx1NjU4N1x1NEVGNlx1NEY1M1x1NzlFRlx1NjZGNFx1NUMwRlxyXG4gICAgZW1wdHlPdXREaXI6IHRydWUsIC8vXHU2MjUzXHU1MzA1XHU1MjREXHU1MTQ4XHU2RTA1XHU3QTdBXHU1MzlGXHU2NzA5XHU2MjUzXHU1MzA1XHU2NTg3XHU0RUY2XHJcbiAgfSxcclxuICBjc3M6IHtcclxuICAgIHByZXByb2Nlc3Nvck9wdGlvbnM6IHtcclxuICAgICAgbGVzczoge1xyXG4gICAgICAgIG1vZGlmeVZhcnM6IGN1c3RvbVZhcmlhYmxlcyxcclxuICAgICAgICBqYXZhc2NyaXB0RW5hYmxlZDogdHJ1ZSxcclxuICAgICAgfSxcclxuICAgIH0sXHJcbiAgfSxcclxuICBkZWZpbmU6IHtcclxuICAgIF9fSU5UTElGWV9QUk9EX0RFVlRPT0xTX186IGZhbHNlLFxyXG4gICAgJ3Byb2Nlc3MuZW52JzogcHJvY2Vzcy5lbnYsXHJcbiAgfSxcclxufTtcclxuIiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxQcm9ncmFtIEZpbGVzICh4ODYpXFxcXFNwZF9Qcm9qZWN0XFxcXHNtYXJ0LWFkbWluLXdlYi1qYXZhc2NyaXB0XFxcXHNyY1xcXFx0aGVtZVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRDpcXFxcUHJvZ3JhbSBGaWxlcyAoeDg2KVxcXFxTcGRfUHJvamVjdFxcXFxzbWFydC1hZG1pbi13ZWItamF2YXNjcmlwdFxcXFxzcmNcXFxcdGhlbWVcXFxcY3VzdG9tLXZhcmlhYmxlcy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRDovUHJvZ3JhbSUyMEZpbGVzJTIwKHg4NikvU3BkX1Byb2plY3Qvc21hcnQtYWRtaW4td2ViLWphdmFzY3JpcHQvc3JjL3RoZW1lL2N1c3RvbS12YXJpYWJsZXMuanNcIjtpbXBvcnQgeyB0aGVtZSB9IGZyb20gJ2FudC1kZXNpZ24tdnVlL2xpYic7XHJcbmltcG9ydCBjb252ZXJ0TGVnYWN5VG9rZW4gZnJvbSAnYW50LWRlc2lnbi12dWUvbGliL3RoZW1lL2NvbnZlcnRMZWdhY3lUb2tlbic7XHJcblxyXG5jb25zdCB7IGRlZmF1bHRBbGdvcml0aG0sIGRlZmF1bHRTZWVkIH0gPSB0aGVtZTtcclxuXHJcbmNvbnN0IG1hcFRva2VuID0gZGVmYXVsdEFsZ29yaXRobShkZWZhdWx0U2VlZCk7XHJcbmNvbnN0IHRva2VuID0gY29udmVydExlZ2FjeVRva2VuLmRlZmF1bHQobWFwVG9rZW4pO1xyXG5cclxuZXhwb3J0IGRlZmF1bHQge1xyXG4gICdAcHJpbWFyeS1jb2xvcic6IHRva2VuWydwcmltYXJ5LWNvbG9yJ10sIC8vIFx1NTE2OFx1NUM0MFx1NEUzQlx1ODI3MlxyXG4gICdAYmFzZS1iZy1jb2xvcic6ICcjZmZmJyxcclxuICAnQGhvdmVyLWJnLWNvbG9yJzogJ3JnYmEoMCwgMCwgMCwgMC4wMjUpJyxcclxuICAnQGhvdmVyLWJnLWNvbG9yLW5pZ2h0JzogJ3JnYmEoMjU1LCAyNTUsIDI1NSwgMC4wMjUpJyxcclxuICAnQGhlYWRlci1saWdodC1iZy1ob3Zlci1jb2xvcic6ICcjZjZmNmY2JyxcclxuICAnQGhlYWRlci1oZWlnaHQnOiAnODBweCcsXHJcbiAgJ0BoZWFkZXItdXNlci1oZWlnaHQnOiAnNDBweCcsXHJcbiAgJ0BwYWdlLXRhZy1oZWlnaHQnOiAnNDBweCcsXHJcbiAgJ0B0aGVtZS1saXN0JzogWydsaWdodCcsICdkYXJrJywgJ25pZ2h0J10sXHJcbn07XHJcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFJQSxTQUFTLGVBQWU7QUFDeEIsT0FBTyxTQUFTOzs7QUNMaVosU0FBUyxhQUFhO0FBQ3ZiLE9BQU8sd0JBQXdCO0FBRS9CLElBQU0sRUFBRSxrQkFBa0IsWUFBWSxJQUFJO0FBRTFDLElBQU0sV0FBVyxpQkFBaUIsV0FBVztBQUM3QyxJQUFNLFFBQVEsbUJBQW1CLFFBQVEsUUFBUTtBQUVqRCxJQUFPLDJCQUFRO0FBQUEsRUFDYixrQkFBa0IsTUFBTSxlQUFlO0FBQUE7QUFBQSxFQUN2QyxrQkFBa0I7QUFBQSxFQUNsQixtQkFBbUI7QUFBQSxFQUNuQix5QkFBeUI7QUFBQSxFQUN6QixnQ0FBZ0M7QUFBQSxFQUNoQyxrQkFBa0I7QUFBQSxFQUNsQix1QkFBdUI7QUFBQSxFQUN2QixvQkFBb0I7QUFBQSxFQUNwQixlQUFlLENBQUMsU0FBUyxRQUFRLE9BQU87QUFDMUM7OztBRGxCQSxJQUFNLG1DQUFtQztBQVF6QyxJQUFNLGNBQWMsQ0FBQyxRQUFRO0FBQzNCLFNBQU8sUUFBUSxrQ0FBVyxLQUFLLEdBQUc7QUFDcEM7QUFDQSxJQUFPLHNCQUFRO0FBQUEsRUFDYixNQUFNLFFBQVEsSUFBSSxhQUFhLGVBQWUsTUFBTTtBQUFBLEVBQ3BELE1BQU0sUUFBUSxJQUFJO0FBQUEsRUFDbEIsU0FBUztBQUFBLElBQ1AsT0FBTztBQUFBO0FBQUEsTUFFTDtBQUFBLFFBQ0UsTUFBTTtBQUFBLFFBQ04sYUFBYTtBQUFBLE1BQ2Y7QUFBQTtBQUFBLE1BRUE7QUFBQSxRQUNFLE1BQU07QUFBQSxRQUNOLGFBQWEsWUFBWSxLQUFLLElBQUk7QUFBQSxNQUNwQztBQUFBLE1BQ0E7QUFBQSxRQUNFLE1BQU07QUFBQSxRQUNOLGFBQWE7QUFBQSxNQUNmO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLFFBQVE7QUFBQSxJQUNOLE1BQU07QUFBQSxJQUNOLE1BQU07QUFBQSxJQUNOLFFBQVE7QUFBQSxNQUNOLE9BQU87QUFBQTtBQUFBLFFBRUwsS0FBSztBQUFBLFVBQ0gsUUFBUTtBQUFBO0FBQUEsVUFDUixjQUFjO0FBQUE7QUFBQSxVQUNkLFNBQVMsQ0FBQyxTQUFTO0FBQUE7QUFBQSxRQUNyQjtBQUFBLE1BQ0Y7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUFBLEVBQ0EsU0FBUyxDQUFDLElBQUksQ0FBQztBQUFBLEVBQ2YsY0FBYztBQUFBLElBQ1osU0FBUyxDQUFDLGtDQUFrQyxzQkFBc0IsZ0NBQWdDO0FBQUEsSUFDbEcsU0FBUyxDQUFDLFVBQVU7QUFBQSxFQUN0QjtBQUFBLEVBQ0EsT0FBTztBQUFBO0FBQUEsSUFFTCxlQUFlO0FBQUEsTUFDYixVQUFVO0FBQUEsUUFDUixjQUFjO0FBQUEsUUFDZCxlQUFlO0FBQUEsTUFDakI7QUFBQSxJQUNGO0FBQUEsSUFDQSxlQUFlO0FBQUEsTUFDYixRQUFRO0FBQUE7QUFBQSxRQUVOLGdCQUFnQjtBQUFBLFFBQ2hCLGdCQUFnQjtBQUFBLFFBQ2hCLGdCQUFnQjtBQUFBLFFBQ2hCLGFBQWEsSUFBSTtBQUVmLGNBQUksR0FBRyxTQUFTLGNBQWMsR0FBRztBQUMvQixtQkFBTyxHQUFHLFNBQVMsRUFBRSxNQUFNLGVBQWUsRUFBRSxDQUFDLEVBQUUsTUFBTSxHQUFHLEVBQUUsQ0FBQyxFQUFFLFNBQVM7QUFBQSxVQUN4RTtBQUFBLFFBQ0Y7QUFBQSxNQUNGO0FBQUEsSUFDRjtBQUFBLElBQ0EsUUFBUTtBQUFBLElBQ1IsUUFBUTtBQUFBO0FBQUEsSUFDUixXQUFXO0FBQUE7QUFBQSxJQUNYLG1CQUFtQjtBQUFBO0FBQUEsSUFDbkIsdUJBQXVCO0FBQUE7QUFBQSxJQUN2QixRQUFRO0FBQUE7QUFBQSxJQUNSLGFBQWE7QUFBQTtBQUFBLEVBQ2Y7QUFBQSxFQUNBLEtBQUs7QUFBQSxJQUNILHFCQUFxQjtBQUFBLE1BQ25CLE1BQU07QUFBQSxRQUNKLFlBQVk7QUFBQSxRQUNaLG1CQUFtQjtBQUFBLE1BQ3JCO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLFFBQVE7QUFBQSxJQUNOLDJCQUEyQjtBQUFBLElBQzNCLGVBQWUsUUFBUTtBQUFBLEVBQ3pCO0FBQ0Y7IiwKICAibmFtZXMiOiBbXQp9Cg==
