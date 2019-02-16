FROM openjdk:10.0.2-jre
ADD /target/account-activator-*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]