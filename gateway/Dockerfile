FROM solomkinmv/openjdk:10-slim
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xmx200m", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8008/actuator/health || exit 1

EXPOSE 8008