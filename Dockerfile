# Fase de compilación con Maven y JDK 21
# Usamos una imagen oficial y reciente de Maven con una distribución de JDK 21 (Eclipse Temurin)
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia el pom.xml para cachear las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el resto del código fuente
COPY src ./src

# Compila el proyecto usando Java 21
RUN mvn clean package -DskipTests

# Fase de ejecución con un JRE de Java 21 optimizado
# Usamos una imagen JRE (Java Runtime Environment) que es más pequeña y segura para producción
FROM eclipse-temurin:21-jre-alpine

# Copiar el archivo JAR desde la fase de construcción
COPY --from=build /app/target/franchise-inventory-service-0.0.1-SNAPSHOT.jar franchise-inventory-service-app.jar

# Definir el punto de entrada
ENTRYPOINT ["java", "-jar", "/franchise-inventory-service-app.jar"]