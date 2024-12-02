# Build stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar o código fonte e construir o pacote, ignorando testes
COPY src ./src
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copiar o JAR construído na etapa de build
COPY --from=build /app/target/Ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta que a aplicação irá rodar
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]
