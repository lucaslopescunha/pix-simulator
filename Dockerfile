# Estágio de Build - Alterado para JDK 21
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Garante permissão de execução para o mvnw
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Estágio de Execução - Alterado para JRE 21 (Alpine para manter leve)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia apenas o JAR gerado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
