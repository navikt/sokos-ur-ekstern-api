FROM gcr.io/distroless/java21-debian12:nonroot
COPY build/libs/app*.jar app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

CMD ["app.jar"]