FROM maven:3.9.6 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/lms-0.0.1-SNAPSHOT.jar lms.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "lms.jar"]
