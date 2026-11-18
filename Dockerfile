# ---- Build ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -ntp clean package -Pproduction -DskipTests

# ---- Run ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV APP_PORT=8080 UPLOAD_DIR=/app/uploads
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
