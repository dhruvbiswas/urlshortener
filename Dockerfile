FROM maven:3.6.0-jdk-11-slim

COPY ./target/*.jar /urlshortener/

EXPOSE 8080

ENTRYPOINT ["java","-jar","/urlshortener/longUrl-shortener-1.0-SNAPSHOT.jar"]
