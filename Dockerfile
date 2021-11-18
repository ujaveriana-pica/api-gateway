FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
COPY "./build/libs/api-gateway-1.0.jar" "app.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
ENV SPRING_PROFILES_ACTIVE docker