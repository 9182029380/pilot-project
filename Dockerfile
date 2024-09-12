# Use an official OpenJDK image as a base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the application jar file into the container
COPY target/assessment-service.jar /app/assessment-service.jar

# Expose the port the application runs on
EXPOSE 9000

# Set environment variables for database access (optional, you can also keep it in application.properties)
# ENV SPRING_DATASOURCE_URL=jdbc:mysql://database-1.c3qavmjre86e.us-east-1.rds.amazonaws.com:3306/springboot
# ENV SPRING_DATASOURCE_USERNAME=admin
# ENV SPRING_DATASOURCE_PASSWORD=Sharath198

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/assessment-service.jar"]
