FROM openjdk:17-jdk-alpine

LABEL author="Horlakz"

WORKDIR /app

COPY target/*.jar /app/app.jar
COPY .env  /app/.env

# RUN ./mvnw clean package

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]