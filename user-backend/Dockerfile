FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk

WORKDIR /app
COPY target/user-backend-0.0.1-SNAPSHOT.jar /app/backend1.jar

ENTRYPOINT ["java", "-jar", "backend1.jar"]
