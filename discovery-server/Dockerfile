
FROM openjdk:17

EXPOSE 8761

ADD target/*.jar discovery-server.jar

ENTRYPOINT ["java", "-jar", "/discovery-server.jar"]