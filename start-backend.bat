@echo off
chcp 65001 >nul
title SPD后端服务

:: 设置JDK环境
set "JAVA_HOME=D:\Program Files (x86)\Spd_Project\jdk"
set "PATH=%JAVA_HOME%\bin;%PATH%"

:: 进入后端目录
cd /d "D:\Program Files (x86)\Spd_Project\smart-admin-api-java17-springboot3\sa-admin"

:: 启动Spring Boot
mvn spring-boot:run -DskipTests -Dspring-boot.run.profiles=dev

:: 如果服务停止，保持窗口
pause
