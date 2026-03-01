# Use official Eclipse Temurin Java 17 image
FROM eclipse-temurin:17-jdk


# Set working directory inside container
WORKDIR /app

# Copy everything
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project inside Docker (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# Expose port (matches Render PORT env)
EXPOSE 8081

# Set dynamic port for Spring Boot
ENV SERVER_PORT=${PORT}

# Run the jar
CMD ["java", "-jar", "target/swipenow-0.0.1-SNAPSHOT.jar"]
