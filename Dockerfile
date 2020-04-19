# stage-0 for build
FROM maven:3-jdk-8
WORKDIR /tmp
## cache dependencies seperatly
## only runs if pom.xml changes
COPY pom.xml .
RUN mvn dependency:resolve-plugins dependency:resolve verify clean --fail-never
## build from source
COPY src/ ./src/
RUN mvn package spring-boot:repackage

# stage-1 for execution
FROM openjdk:8-jre
WORKDIR /app
COPY --from=0 /tmp/target/rdfservices-*.jar /app/rdfservices.jar
ENTRYPOINT ["java","-jar","rdfservices.jar"]
EXPOSE 8080