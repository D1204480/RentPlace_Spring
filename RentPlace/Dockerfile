# 使用 Java 17 作為基礎映像
FROM openjdk:17-jdk-slim

# 設定工作目錄
WORKDIR /app

# 複製 Maven 構建出來的 JAR 檔案到容器中
COPY target/*.jar app.jar

# 設定容器對外開放的埠號
EXPOSE 8080

# 設定啟動指令
ENTRYPOINT ["java","-jar","app.jar"]