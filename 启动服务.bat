@echo off
chcp 65001 >nul

title SPD系统启动器
color 0B

:: 切换到脚本所在目录
cd /d "%~dp0"

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║          SPD耗材管理系统 - 正在启动服务...                ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

:: 设置JDK环境(使用项目嵌入式JDK)
set "JAVA_HOME=%~dp0jdk"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo [1/3] JDK环境: %JAVA_HOME%

:: 设置Redis路径
set "REDIS_HOME=%~dp0Redis-x64-3.0.504"
echo [2/3] Redis路径: %REDIS_HOME%

:: 设置Node.js路径
set "NODE_HOME=D:\Program Files\nodejs"
set "PATH=%NODE_HOME%;%PATH%"
echo [3/3] Node.js路径: %NODE_HOME%
echo.

:: 启动Redis
echo 正在启动Redis...
start "Redis Server" /MIN "%REDIS_HOME%\redis-server.exe" "%REDIS_HOME%\redis.windows.conf"
timeout /t 2 /nobreak >nul
echo ✓ Redis已启动
echo.

:: 启动后端
echo 正在启动后端服务...
start "SPD后端服务" cmd /k "cd /d "%~dp0smart-admin-api-java17-springboot3\sa-admin" && set JAVA_HOME=%~dp0jdk && set PATH=%JAVA_HOME%\bin;%PATH% && title SPD后端服务 && mvn spring-boot:run -DskipTests -Dspring-boot.run.profiles=dev"
echo ✓ 后端服务启动中(约2-3分钟)
echo.

:: 启动前端
echo 正在启动前端服务...
start "SPD前端服务" powershell -NoExit -Command "cd 'D:\Program Files (x86)\Spd_Project\smart-admin-web-javascript'; $host.UI.RawUI.WindowTitle = 'SPD前端服务'; npm run dev"
echo ✓ 前端服务启动中
echo.

echo ╔════════════════════════════════════════════════════════════╗
echo ║          所有服务已启动!                                   ║
echo ╚════════════════════════════════════════════════════════════╝
echo.
echo 访问地址:
echo   - 前端: http://localhost:8081
echo   - 后端: http://localhost:1024
echo   - 文档: http://localhost:1024/doc.html
echo.
echo 登录账号: admin / 123456
echo.
echo ════════════════════════════════════════════════════════════
echo 启动完成,主窗口将在5秒后自动关闭...
echo 保留的窗口: SPD后端服务, SPD前端服务 (可查看日志)
echo ════════════════════════════════════════════════════════════
echo.

echo 窗口将在5秒后自动关闭...
timeout /t 5 /nobreak >nul
exit
