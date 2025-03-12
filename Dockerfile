FROM bellsoft/liberica-openjdk-alpine:17

COPY ./build/libs/transformer_project-*.jar app.jar
CMD ["java", "-jar", "app.jar"]
