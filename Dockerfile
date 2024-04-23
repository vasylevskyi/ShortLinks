FROM eclipse-temurin:17-jre
COPY build/libs/ShortLinks-0.0.1-SNAPSHOT.jar /application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/application.jar"]


