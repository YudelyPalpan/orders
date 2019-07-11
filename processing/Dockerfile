FROM maven:alpine as builder

WORKDIR /app
COPY pom.xml .
COPY ./src ./src
RUN mvn -DskipTests clean package
RUN ls target

FROM java:8-alpine
COPY --from=builder /app/target/processing-0.0.1-SNAPSHOT.jar /app.jar
RUN ls -a
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","/app.jar"]
