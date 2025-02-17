FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/dpdgroupproject-0.0.1-SNAPSHOT.jar /app/dpdgroupproject-0.0.1-SNAPSHOT.jar
COPY src/main/resources/application.properties /app/application.properties

EXPOSE 8080

CMD ["java", "-jar", "dpdgroupproject-0.0.1-SNAPSHOT.jar"]
