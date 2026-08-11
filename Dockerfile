# Build stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# Final stage
FROM postgres:16.2

# Install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jre-headless && \
    rm -rf /var/lib/apt/lists/*

# Set environment variables for PostgreSQL
ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=admin
ENV POSTGRES_DB=store

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar
WORKDIR /app

# Copy the entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Expose the application port and PostgreSQL port
EXPOSE 8080 5433

ENTRYPOINT ["/app/entrypoint.sh"]
