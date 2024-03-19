FROM openjdk:17

COPY target/*.jar app.jar
RUN mvn clean install

ENTRYPOINT ["java","-jar","/app.jar"]