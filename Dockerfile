# Use Maven with JDK 21 (since my project requires Java 21)
FROM maven:3.9.11-eclipse-temurin-21

# Set working directory
WORKDIR /app

# Copy only pom.xml first (for dependency caching)
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Default command to run tests
CMD ["mvn", "clean", "test"]