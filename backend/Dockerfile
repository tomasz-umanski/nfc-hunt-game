FROM maven:3.9-amazoncorretto-21-alpine as builder

WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies (better for Docker layer caching)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001 -G spring

# Create directories
RUN mkdir -p /app/uploads /app/logs && chown -R spring:spring /app

WORKDIR /app

# Copy the built jar
COPY --from=builder /app/target/*.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
