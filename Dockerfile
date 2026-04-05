# Stage 1: Build frontend
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json* ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# Stage 2: Build backend
FROM gradle:8.8-jdk21 AS backend-build
WORKDIR /app
COPY backend/ ./
RUN gradle bootJar --no-daemon

# Stage 3: Production image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/build/libs/*.jar app.jar
COPY --from=frontend-build /app/frontend/dist /app/static
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
