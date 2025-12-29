@echo off
chcp 65001 >nul

cd /d "%~dp0smart-admin-web-javascript"

:: 设置窗口标题（在npm启动前设置）
title SPD前端服务

:: 启动Vite开发服务器
call npm run dev

:: 如果服务停止，保持窗口
pause
