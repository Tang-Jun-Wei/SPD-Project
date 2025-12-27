@echo off
:: SPD系统后端启动脚本
:: 设置JAVA_HOME和Maven环境

:: 设置Java环境
set JAVA_HOME=D:\Program Files (x86)\Spd_Project\smart-admin-api-java17-springboot3\jdk-17.0.12
set PATH=%JAVA_HOME%\bin;%PATH%

:: 进入后端项目目录
cd /d "D:\Program Files (x86)\Spd_Project\smart-admin-api-java17-springboot3"

:: 编译并启动项目（跳过测试）
echo 正在编译并启动后端项目...
call mvn spring-boot:run -DskipTests -pl sa-admin

pause
