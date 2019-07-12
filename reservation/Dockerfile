FROM maven:alpine as builder

WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn -DskipTests clean package

FROM java:8-alpine
COPY --from=builder /app/target/reservation-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","/app.jar"]
