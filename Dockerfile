
# Use the official OpenJDK 22 as a base image
FROM openjdk:22-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar file into the container
COPY target/authen-1.0.0.jar /app/app.jar

# Expose the port on which the Spring Boot app will run (default is 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


