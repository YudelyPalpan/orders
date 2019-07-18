FROM maven as builder
# Target app directory and configuring Maven
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and building app
COPY ./src ./src
RUN mvn -DskipTests package

# Create a Java 8 Container
FROM java:8
COPY ./wait-for-it.sh /wait-for-it.sh
COPY --from=builder /app/target/processing-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE ${PORT}

# ENTRYPOINT ["java","-jar","/app.jar"]
