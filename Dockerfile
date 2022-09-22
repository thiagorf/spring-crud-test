FROM openjdk:11-jdk-alpine
WORKDIR /usr/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package

COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
