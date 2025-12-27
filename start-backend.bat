@echo off
:: SPD系统后端启动脚本
:: 使用嵌入式JDK，无需单独配置Java环境

:: 设置嵌入式Java环境
set JAVA_HOME=%~dp0jdk
set PATH=%JAVA_HOME%\bin;%PATH%

:: 进入后端项目目录
cd /d "%~dp0smart-admin-api-java17-springboot3\sa-admin"

:: 编译并启动项目（跳过测试）
echo 正在编译并启动后端项目...
call mvn spring-boot:run -DskipTests -Dspring-boot.run.profiles=dev

pause
