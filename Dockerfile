# ETAPA 1: Build (Compilação da aplicação)
FROM ubuntu:latest AS build

# Atualiza os pacotes e instala o JDK 17 e Maven
RUN apt-get update && apt-get install -y openjdk-17-jdk maven

# Copia todo o código-fonte do projeto para o container
COPY . .

# Compila o projeto e gera o arquivo .jar na pasta /target
RUN mvn clean install

# ETAPA 2: Runtime (Execução em imagem leve)
FROM openjdk:17-jdk-slim

# Expõe a porta padrão que a aplicação Spring Boot utiliza
EXPOSE 8080

# Copia o arquivo .jar gerado na etapa de build renomeando para app.jar
COPY --from=build /target/todolist-1.0.0.jar app.jar

# Comando para rodar a aplicação Java
ENTRYPOINT ["java", "-jar", "app.jar"]