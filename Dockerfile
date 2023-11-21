FROM amazoncorretto:17.0.6
ARG JAR_FILE=build/libs/backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-jar","/app.jar"]
