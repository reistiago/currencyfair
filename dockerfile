FROM openjdk:8u181-jdk-slim

RUN mkdir -p /opt/exercise-fat
COPY ./target/exercise-fat.jar /opt/exercise-fat/exercise-fat.jar

EXPOSE 8080
ENTRYPOINT exec java -jar /opt/exercise-fat/exercise-fat.jar