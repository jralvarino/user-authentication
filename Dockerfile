FROM openjdk:11

MAINTAINER ALVARINO JUNIOR

COPY build/libs/user-authentication-0.0.1-SNAPSHOT.jar users-authentication.jar

ENTRYPOINT ["java", "-jar", "users-authentication.jar"]