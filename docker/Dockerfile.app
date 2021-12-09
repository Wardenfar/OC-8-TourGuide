FROM gradle:7.3.1-jdk11 as builder

WORKDIR /app
COPY ./ /app
RUN gradle build -x test :app:bootJar
RUN mv /app/app/build/libs/app*.jar /app/app.jar

FROM openjdk:11-jre-slim

COPY --from=builder /app/app.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]