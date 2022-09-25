FROM openjdk:11 as build
WORKDIR /usr/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN ./mvnw package -DskipTests

COPY target/*.jar app.jar

FROM openjdk:11

COPY --from=build /usr/app/app.jar .

ENTRYPOINT ["java", "-jar", "/app.jar"]
