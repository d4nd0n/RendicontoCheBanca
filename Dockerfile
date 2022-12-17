FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} test-app-1.0.jar
ENTRYPOINT ["java","-jar","-Xms256M", "-Xmx512M","/test-app-1.0.jar"]
