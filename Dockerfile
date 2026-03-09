FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle build.gradle ./

RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon dependencies || true

COPY src ./src
RUN ./gradlew --no-daemon bootJar

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
