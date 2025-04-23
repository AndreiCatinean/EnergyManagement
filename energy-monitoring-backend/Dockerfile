FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk

WORKDIR /app

COPY /target/energy-monitoring-0.0.1-SNAPSHOT.jar /app/backend3.jar


ENTRYPOINT ["java", "-jar", "backend3.jar"]
