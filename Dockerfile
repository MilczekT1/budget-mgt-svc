FROM openjdk:11.0.3-jdk-stretch
ADD /target/budget-management-0.5.jar app.jar
HEALTHCHECK --start-period=15s --interval=10s --timeout=5s --retries=10 CMD curl -f http://localhost:8080/actuator/health
ENTRYPOINT ["java", "-jar", \
    "-Djava.security.egd=file:/dev/./urandom ", "app.jar", \
    "--spring.profiles.active=default" \
]
