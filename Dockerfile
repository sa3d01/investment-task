# Use a Maven base image for building
FROM openjdk:11-jdk-slim AS build

WORKDIR /app

# Copy the Maven Wrapper script
COPY mvnw .
COPY .mvn .mvn

# Copy the Maven project file
COPY pom.xml .
COPY src src

# Make the Maven Wrapper executable
RUN chmod +x mvnw

# Build the project using the Maven Wrapper
RUN ./mvnw clean package -Dmaven.test.skip=true

# Use a lightweight base image for running the application
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy the compiled jar file from the build stage
COPY --from=build /app/target/investmentTask-0.0.1-SNAPSHOT.jar /app/investmentTask.jar

EXPOSE 8090

CMD ["java", "-jar", "investmentTask.jar"]