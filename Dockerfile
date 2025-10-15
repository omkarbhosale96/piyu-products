# ---------- Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app

# Copy pom and source
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ---------- Run Stage ----------
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9600

ENTRYPOINT ["java", "-jar", "app.jar"]
