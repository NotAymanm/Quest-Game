FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the backend source code into the Docker image
COPY . /app

# Build the backend using Maven
RUN mvn -f /app/pom.xml clean package -DskipTests

# Expose port 8080 (default for Spring Boot)
EXPOSE 8080

# Command to run the backend
CMD ["java", "-jar", "target/A1-AymanMadani-101237658-1.0-SNAPSHOT.jar"]