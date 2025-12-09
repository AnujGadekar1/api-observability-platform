# collector/Dockerfile
FROM gradle:7-jdk17 AS build
WORKDIR /app
COPY . .
# Skip tests to speed up build
RUN ./gradlew bootJar -x test --no-daemon

# CHANGED: Using Eclipse Temurin instead of OpenJDK
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]