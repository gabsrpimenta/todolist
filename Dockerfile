# ETAPA 1: Build (Utiliza imagem oficial do Maven com Java 17)
FROM maven:3.8.4-openjdk-17 AS build

# Copia todo o código-fonte para dentro do container
COPY . .

# Compila o projeto gerando o .jar e ignora a execução dos testes no build
RUN mvn clean package -DskipTests

# ETAPA 2: Runtime (Imagem leve apenas para rodar a aplicação)
FROM openjdk:17-jdk-slim

# Expõe a porta 8080
EXPOSE 8080

# Copia o .jar gerado na etapa de build
COPY --from=build /target/todolist-1.0.0.jar app.jar

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]