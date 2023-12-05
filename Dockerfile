FROM openjdk:17-jdk-alpine
LABEL authors="seunkun"
WORKDIR /work
COPY /build/libs/healthcare-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/work/app.jar"]