FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} flory.jar
ENTRYPOINT ["java","-jar","/flory.jar"]