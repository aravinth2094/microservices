FROM openjdk:8-jdk-alpine
RUN addgroup -S microservice && adduser -S microservice -G microservice
USER microservice:microservice
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]