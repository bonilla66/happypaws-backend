# Etapa 1: Construcción (opcional, si querés compilar adentro de Docker)
# ---------------------------------------------
# FROM maven:3.9.5-eclipse-temurin-21 AS build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
# ---------------------------------------------
FROM eclipse-temurin:21-jre

# Directorio dentro del contenedor
WORKDIR /app

# Copiamos el JAR compilado desde tu máquina
COPY target/HappyPaws-backend-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone Spring Boot (Render necesita saberlo)
EXPOSE 8080

# Comando para correr la app
ENTRYPOINT ["java","-jar","/app/app.jar"]
