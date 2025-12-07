# AI Kiosk Backend

Spring Boot 기반 백엔드 서버

## 기술 스택
- Java 17
- Spring Boot 3.2.0
- Gradle

## 실행 방법

```bash
# 개발 모드 실행
./gradlew bootRun

# 빌드
./gradlew build

# JAR 실행
java -jar build/libs/aikiosk-backend-0.0.1-SNAPSHOT.jar
```

## API 엔드포인트

| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | Hello World 페이지 |
| GET | `/health` | 헬스 체크 |

## 포트
- 기본: `80`
