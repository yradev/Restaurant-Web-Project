FROM openjdk:18

VOLUME /tmp
COPY target/Restaurant_Web_Project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Xms256m -Xmx512m", "-jar","/app.jar"]