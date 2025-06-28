# min java runtime
FROM eclipse-temurin:21-jre-alpine

# set working dir
WORKDIR /app

# copy fat JAR and Dropwizard config
COPY target/coin-service-1.0-SNAPSHOT.jar app.jar
COPY config.yml .

# expose application port
EXPOSE 8080

# launch dropwizard
ENTRYPOINT ["java","-jar","app.jar","server","config.yml"]

