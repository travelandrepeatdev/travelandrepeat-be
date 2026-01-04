FROM openjdk:17.0.1 as builder
COPY src/ /src/
RUN javac /src/main/java/com/travelandrepeat/api/App.java -d /app

FROM openjdk:17.0.1
COPY --from=builder /app /app
WORKDIR /app

CMD ["java", "com.travelandrepeat.api.App"]