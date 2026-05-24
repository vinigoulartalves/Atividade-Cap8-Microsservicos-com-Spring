# syntax=docker/dockerfile:1
#
# EcoDenuncia API - imagem de runtime
#
# Pre-requisito: execute "mvn clean package -DskipTests" antes do build
# para que o jar exista em target/.
#
FROM eclipse-temurin:21-alpine

COPY target/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
