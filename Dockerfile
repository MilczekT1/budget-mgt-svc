FROM openjdk:11.0.3-jdk-stretch
HEALTHCHECK --start-period=20s --interval=10s --timeout=5s --retries=10 CMD curl -f http://localhost:8080/actuator/health
ARG ARTIFACT=budget-management-*.jar
ADD /target/$ARTIFACT app.jar
ENTRYPOINT ["java", "-jar", \
    "-Djava.security.egd=file:/dev/./urandom ", "app.jar", \
    "--spring.profiles.active=default" \
]
