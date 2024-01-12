FROM openjdk:17.0.1-slim

ADD ../core/target/core*.jar ./context/App.jar

CMD ["java", "-jar", "-Xmx256m", "-Dspring.profiles.active=prod", "App.jar"]
