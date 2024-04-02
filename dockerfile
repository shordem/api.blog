FROM openjdk:17-jdk-alpine

LABEL author="Horlakz"

WORKDIR /app

COPY . .

RUN ./mvnw clean package

EXPOSE 8000

CMD ["java", "-jar", "target/*.jar"]