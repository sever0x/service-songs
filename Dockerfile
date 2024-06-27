FROM maven:3.8.3-amazoncorretto-17 AS build

COPY . /app

WORKDIR /app

RUN mvn clean package -DskipTests

FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]