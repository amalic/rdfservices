# stage-0 for build
FROM maven:3-jdk-11 as builder
WORKDIR /tmp
## cache dependencies seperatly
## only runs if pom.xml changes
COPY pom.xml .
RUN mvn dependency:resolve-plugins dependency:resolve verify clean --fail-never
## build from source
COPY src/ ./src/
RUN mvn package spring-boot:repackage

# stage-1 for execution
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /tmp/target/rdfservices-*.jar ./rdfservices.jar
ENTRYPOINT ["java","-jar","rdfservices.jar"]
EXPOSE 8080