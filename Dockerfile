FROM openjdk:10.0.2-jre
ADD /target/budget-management-0.5.jar app.jar
HEALTHCHECK --start-period=15s --interval=10s --timeout=5s --retries=10 CMD curl -f http://localhost:8080/actuator/health
ENTRYPOINT ["java", "-jar", \
    "-Djava.security.egd=file:/dev/./urandom ", "app.jar" \
]