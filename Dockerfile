#
# Build stage
#
FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn -B -DskipTests clean package

#
# Run stage
#
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/phone_backend.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/app/phone_backend.jar"]