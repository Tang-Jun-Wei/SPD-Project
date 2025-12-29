@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

:: ====================================================================
:: SPD耗材管理系统 - 智能关闭脚本
:: ====================================================================
:: 本脚本会停止以下服务:
:: 1. 前端开发服务 (Vite)
:: 2. 后端API服务 (Spring Boot)
:: 3. Redis数据库
:: ====================================================================

title SPD系统服务关闭器
color 0C

:: 切换到脚本所在目录
cd /d "%~dp0"

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║          SPD耗材管理系统 - 正在关闭服务...            ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

set STOPPED_COUNT=0

:: ==========================================
:: 第1步: 停止前端服务
:: ==========================================
echo [1/3] 正在停止前端服务...
echo ▓░░░░░░░░░ 10%%

:: 查找并停止Node.js进程^(Vite^)
set FRONTEND_STOPPED=0
echo ▓▓░░░░░░░░ 30%% - 搜索前端进程...
for /f "tokens=5" %%p in ('netstat -ano ^| findstr ":8081.*LISTENING"') do (
    set PID=%%p
    echo ▓▓▓▓░░░░░░ 50%% - 正在停止前端服务 ^(PID: !PID!^)...
    taskkill /F /PID !PID! >nul 2>&1
    if not errorlevel 1 (
        set FRONTEND_STOPPED=1
        set /a STOPPED_COUNT+=1
        echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ✓ 前端服务已停止
    )
)

if "!FRONTEND_STOPPED!"=="0" (
    echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ○ 前端服务未运行
)
echo.

:: ==========================================
:: 第2步: 停止后端服务
:: ==========================================
echo [2/3] 正在停止后端服务...
echo ▓░░░░░░░░░ 10%%

:: 停止后端Java进程^(sa-admin^)
set BACKEND_STOPPED=0
echo ▓▓░░░░░░░░ 30%% - 搜索Java进程...
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST 2^^^>nul ^| findstr /C:"PID:"') do (
    set PID=%%a
    wmic process where "ProcessId=%%a" get CommandLine 2>nul | findstr /C:"net.lab1024.sa.admin.AdminApplication" >nul 2>&1
    if not errorlevel 1 (
        echo ▓▓▓▓░░░░░░ 50%% - 正在停止后端服务 ^(PID: %%a^)...
        taskkill /F /PID %%a >nul 2>&1
        if not errorlevel 1 (
            set BACKEND_STOPPED=1
            set /a STOPPED_COUNT+=1
            echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ✓ 后端服务已停止
        )
    ) else (
        wmic process where "ProcessId=%%a" get CommandLine 2>nul | findstr /C:"sa-admin" >nul 2>&1
        if not errorlevel 1 (
            echo ▓▓▓▓░░░░░░ 50%% - 正在停止后端相关进程 ^(PID: %%a^)...
            taskkill /F /PID %%a >nul 2>&1
            if not errorlevel 1 (
                set BACKEND_STOPPED=1
                set /a STOPPED_COUNT+=1
                echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ✓ 后端相关进程已停止
            )
        )
    )
)

:: 额外停止所有可能的后端Java进程
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST 2^^^>nul ^| findstr /C:"PID:"') do (
    set PID=%%a
    wmic process where "ProcessId=%%a" get CommandLine 2>nul | findstr /C:"smart-admin" >nul 2>&1
    if not errorlevel 1 (
        echo ▓▓▓▓░░░░░░ 60%% - 正在停止Smart-Admin相关进程 ^(PID: %%a^)...
        taskkill /F /PID %%a >nul 2>&1
        if not errorlevel 1 (
            set BACKEND_STOPPED=1
            set /a STOPPED_COUNT+=1
            echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ✓ Smart-Admin相关进程已停止
        )
    )
)

:: 强制停止项目目录下的所有Java进程
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST 2^^^>nul ^| findstr /C:"PID:"') do (
    set PID=%%a
    wmic process where "ProcessId=%%a" get CommandLine 2>nul | findstr /C:"Spd_Project" >nul 2>&1
    if not errorlevel 1 (
        echo ▓▓▓▓░░░░░░ 70%% - 正在停止项目相关Java进程 ^(PID: %%a^)...
        taskkill /F /PID %%a >nul 2>&1
        if not errorlevel 1 (
            set BACKEND_STOPPED=1
            set /a STOPPED_COUNT+=1
            echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ✓ 项目相关Java进程已停止
        )
    )
)

:: 最后强制停止所有Java进程（如果其他方法失败）
if exist "%~dp0smart-admin-api-java17-springboot3" (
    echo ▓▓▓▓▓░░░░░ 80%% - 强制停止所有Java进程...
    taskkill /F /IM java.exe >nul 2>&1
    if not errorlevel 1 (
        set BACKEND_STOPPED=1
        set /a STOPPED_COUNT+=1
        echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ✓ 所有Java进程已停止
    )
)

if "!BACKEND_STOPPED!"=="0" (
    echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ○ 后端服务未运行
)
echo.

:: ==========================================
:: 第3步: 停止Redis服务
:: ==========================================
echo [3/3] 正在停止Redis服务...
echo ▓░░░░░░░░░ 10%%

echo ▓▓░░░░░░░░ 30%% - 搜索Redis进程...
tasklist /FI "IMAGENAME eq redis-server.exe" 2>NUL | find /I /N "redis-server.exe">NUL
if not errorlevel 1 (
    echo ▓▓▓▓░░░░░░ 50%% - 正在停止Redis服务...
    taskkill /F /IM redis-server.exe >nul 2>&1
    if not errorlevel 1 (
        set /a STOPPED_COUNT+=1
        echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ✓ Redis服务已停止
    )
) else (
    echo ▓▓▓▓▓▓▓▓▓▓ 100%% - ○ Redis服务未运行
)
echo.

:: ==========================================
:: 第4步: 关闭相关的cmd窗口
:: ==========================================
echo [4/4] 正在关闭服务窗口...
for /f "tokens=2" %%a in ('tasklist /FI "WINDOWTITLE eq SPD*" /FO LIST 2^^^>nul ^| findstr /C:"PID:"') do (
    taskkill /F /PID %%a >nul 2>&1
)
:: 通过窗口标题关闭
taskkill /F /FI "WINDOWTITLE eq SPD前端服务*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq SPD后端服务*" >nul 2>&1
echo ✓ 服务窗口已关闭
echo.

:: ==========================================
:: 关闭完成 - 自动关闭倒计时
:: ==========================================
echo.
echo ╔════════════════════════════════════════════════════════════╗
if !STOPPED_COUNT! gtr 0 (
    color 0A
    echo ║          ✓ 成功停止 !STOPPED_COUNT! 个服务                              ║
) else (
    color 0E
    echo ║          ○ 没有运行中的服务                                ║
)
echo ╚════════════════════════════════════════════════════════════╝
echo.

if !STOPPED_COUNT! gtr 0 (
    echo 已停止的服务: !STOPPED_COUNT! 个
    echo.
    echo 提示:
    echo   - 所有SPD系统服务已关闭
    echo   - 重新启动请运行: 启动服务.bat
) else (
    echo 提示:
    echo   - 当前没有运行中的SPD服务
    echo   - 启动服务请运行: 启动服务.bat
)

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║                  窗口将在10秒后自动关闭                    ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

:: 倒计时10秒自动关闭
for /L %%i in (5,-1,1) do (
    echo   • 倒计时: %%i 秒...
    timeout /t 1 /nobreak >nul
)

echo.
echo   ✓ 所有窗口即将关闭...
timeout /t 1 /nobreak >nul
exit
