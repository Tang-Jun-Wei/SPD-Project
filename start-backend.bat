@echo off
:: SPD系统后端启动脚本
:: 使用嵌入式JDK和Redis，无需单独配置环境

:: 设置嵌入式Java环境
set JAVA_HOME=%~dp0jdk
set PATH=%JAVA_HOME%\bin;%PATH%

:: 设置Redis路径
set REDIS_HOME=%~dp0Redis-x64-3.0.504

:: 检查Redis是否已经在运行
tasklist /FI "IMAGENAME eq redis-server.exe" 2>NUL | find /I /N "redis-server.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo Redis服务已在运行...
) else (
    echo 正在启动Redis服务...
    start "Redis Server" /MIN "%REDIS_HOME%\redis-server.exe" "%REDIS_HOME%\redis.windows.conf"
    timeout /t 2 /nobreak >nul
    echo Redis服务启动成功！
)

:: 进入后端项目目录
cd /d "%~dp0smart-admin-api-java17-springboot3\sa-admin"

:: 编译并启动项目（跳过测试）
echo 正在编译并启动后端项目...
call mvn spring-boot:run -DskipTests -Dspring-boot.run.profiles=dev

pause
