@echo off
:: SPD系统服务停止脚本
:: 停止Redis和后端Java进程

echo 正在停止SPD系统相关服务...

:: 停止Redis服务
tasklist /FI "IMAGENAME eq redis-server.exe" 2>NUL | find /I /N "redis-server.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo 正在停止Redis服务...
    taskkill /F /IM redis-server.exe >nul 2>&1
    echo Redis服务已停止
) else (
    echo Redis服务未运行
)

:: 停止后端Java进程(sa-admin)
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST ^| findstr /C:"PID:"') do (
    set pid=%%a
    wmic process where "ProcessId=!pid!" get CommandLine 2>nul | findstr /C:"sa-admin" >nul
    if !errorlevel! equ 0 (
        echo 正在停止后端服务 (PID: !pid!)...
        taskkill /F /PID !pid! >nul 2>&1
    )
)

echo.
echo 所有服务已停止！
pause
