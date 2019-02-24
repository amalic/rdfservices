FROM maven:3-jdk-8

WORKDIR /tmp

COPY pom.xml .
RUN mvn verify clean --fail-never

COPY src/ ./src/
RUN mvn package && \
  mkdir /app && \
  mv target/ServiceFromData-0.0.1-SNAPSHOT.jar /app/ServiceFromData.jar && \
  rm -rf /tmp/*

WORKDIR /app

ENTRYPOINT ["java","-jar","ServiceFromData.jar"]
