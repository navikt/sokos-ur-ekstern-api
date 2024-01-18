FROM gcr.io/distroless/java21-debian12:nonroot
COPY build/libs/app*.jar /app/app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

WORKDIR /app
CMD ["app.jar"]