FROM openjdk:17-jdk-alpine
LABEL authors="Tinh"

ENV APP_HOME=/app
WORKDIR $APP_HOME

COPY target/FoodEase-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]