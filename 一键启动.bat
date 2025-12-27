@echo off
chcp 65001 >nul
:: ====================================================================
:: SPD耗材管理系统 - 一键启动脚本
:: ====================================================================
:: 本脚本会自动启动以下服务:
:: 1. Redis数据库 (嵌入式,端口6379)
:: 2. 后端API服务 (Spring Boot,端口1024)
:: ====================================================================

title SPD系统启动器
color 0A

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║          SPD耗材管理系统 - 启动中...                      ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

:: 设置嵌入式Java环境
set JAVA_HOME=%~dp0jdk
set PATH=%JAVA_HOME%\bin;%PATH%

:: 设置Redis路径
set REDIS_HOME=%~dp0Redis-x64-3.0.504

echo [1/2] 检查Redis服务状态...
:: 检查Redis是否已经在运行
tasklist /FI "IMAGENAME eq redis-server.exe" 2>NUL | find /I /N "redis-server.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo       ✓ Redis服务已在运行
) else (
    echo       → 正在启动Redis服务...
    start "Redis Server" /MIN "%REDIS_HOME%\redis-server.exe" "%REDIS_HOME%\redis.windows.conf"
    timeout /t 2 /nobreak >nul
    echo       ✓ Redis服务启动成功 (端口:6379)
)

echo.
echo [2/2] 启动后端服务...
echo       → 使用嵌入式JDK: %JAVA_HOME%
echo       → 正在编译并启动Spring Boot应用...
echo.

:: 进入后端项目目录
cd /d "%~dp0smart-admin-api-java17-springboot3\sa-admin"

:: 编译并启动项目（跳过测试）
call mvn spring-boot:run -DskipTests -Dspring-boot.run.profiles=dev

pause
