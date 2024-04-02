FROM openjdk:17-jdk-alpine

LABEL author="Horlakz"

WORKDIR /app

COPY target/*.jar /app/app.jar

# RUN ./mvnw clean package

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]