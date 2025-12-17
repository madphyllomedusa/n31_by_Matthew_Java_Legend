FROM maven:3.8.4-openjdk-17 as builder
LABEL authors="N31"

WORKDIR /app
COPY . .
RUN mvn  clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
#временно
COPY .env .env
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]