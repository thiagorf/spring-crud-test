FROM openjdk:11-jdk-alpine as build
WORKDIR /usr/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package

COPY target/*.jar app.jar

FROM openjdk:11-jdk-alpine

COPY --from=build /usr/app/app.jar .

ENTRYPOINT ["java", "-jar", "app.jar"]
