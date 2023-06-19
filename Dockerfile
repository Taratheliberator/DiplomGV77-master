FROM openjdk:18-jdk-alpine3.13

EXPOSE 8080

ADD target/DiplomGV77-1.0-SNAPSHOT.jar diplomgv77.jar

ENTRYPOINT ["java", "-jar", "diplomgv77.jar"]