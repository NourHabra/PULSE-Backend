# Use OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/yourapp.jar /app/yourapp.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/yourapp.jar"]

# Expose port 8080 (Spring Boot default)
EXPOSE 8080
