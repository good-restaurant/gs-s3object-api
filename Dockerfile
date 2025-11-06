# ====== Build stage ======
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace
COPY gradlew* settings.gradle* build.gradle* /workspace/
COPY gradle /workspace/gradle
RUN chmod +x gradlew && ./gradlew --no-daemon --version
RUN ./gradlew dependencies || true
COPY src /workspace/src
RUN ./gradlew bootJar -x test

# ====== Runtime stage ======
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
RUN addgroup -S app && adduser -S app -G app
USER app
WORKDIR /app
# ↓↓↓ 반드시 builder 로 맞추기
COPY --from=builder /workspace/build/libs/*-SNAPSHOT.jar /app/app.jar

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s \
  CMD sh -c "curl -fsS http://localhost:${SERVER_PORT:-8088}/actuator/health | grep -q '\"status\":\"UP\"' || exit 1"

EXPOSE 8088
ENV SERVER_PORT=8088 \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseZGC"
ENTRYPOINT ["java","-jar","/app/app.jar"]
