FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} umcservice.jar
ENTRYPOINT ["java","-jar","/umcservice.jar"]