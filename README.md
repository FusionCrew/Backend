# 🖥️ AI Kiosk Backend

Spring Boot 기반 백엔드 API 서버입니다.

---

## 📋 목차
- [기술 스택](#-기술-스택)
- [폴더 구조](#-폴더-구조)
- [사전 준비](#-사전-준비)
- [설치 및 실행](#-설치-및-실행)
- [API 엔드포인트](#-api-엔드포인트)
- [환경 설정](#-환경-설정)

---

## 🛠️ 기술 스택

| 구분 | 기술 | 버전 |
|------|-----|------|
| **언어** | Java | 21 |
| **프레임워크** | Spring Boot | 3.2.0 |
| **빌드 도구** | Gradle | 8.5 |
| **의존성 관리** | Spring Dependency Management | 1.1.4 |

---

## 📁 폴더 구조

```
Backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/fusioncrew/aikiosk/
│       │       ├── AiKioskApplication.java    # 메인 애플리케이션 진입점
│       │       └── controller/
│       │           └── HelloController.java   # REST API 컨트롤러
│       └── resources/
│           └── application.properties         # 애플리케이션 설정 파일
├── build.gradle                               # Gradle 빌드 설정
├── settings.gradle                            # Gradle 프로젝트 설정
├── gradlew                                    # Gradle Wrapper (Unix)
├── gradlew.bat                                # Gradle Wrapper (Windows)
└── gradle/
    └── wrapper/
        └── gradle-wrapper.properties          # Gradle Wrapper 설정
```

---

## ✅ 사전 준비

### 필수 설치
- **Java 21** 이상
  ```bash
  # 버전 확인
  java -version
  # 출력 예시: openjdk version "21.0.x"
  ```

> ⚠️ **주의**: Java 17 이하에서는 동작하지 않습니다. Java 21 이상을 설치해 주세요.

---

## 🚀 설치 및 실행

### 1. 의존성 설치 및 빌드
```bash
# 프로젝트 루트에서 실행
./gradlew build
```

### 2. 개발 모드 실행 (Hot Reload 지원)
```bash
./gradlew bootRun
```

### 3. JAR 파일로 실행
```bash
# 빌드
./gradlew build

# JAR 실행
java -jar build/libs/aikiosk-0.0.1-SNAPSHOT.jar
```

### 4. 클린 빌드 (문제 발생 시)
```bash
./gradlew clean build
```

---

## 🔗 API 엔드포인트

| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | Hello World 페이지 |
| GET | `/health` | 헬스 체크 |

### 서버 기본 포트
- **포트**: `80`

### 접속 확인
서버 실행 후 브라우저에서 확인:
```
http://localhost/
http://localhost/health
```

---

## ⚙️ 환경 설정

### application.properties
```properties
# 서버 포트 (기본값: 80)
server.port=80

# 애플리케이션 이름
spring.application.name=aikiosk-backend

# 로깅 레벨
logging.level.root=INFO
logging.level.com.fusioncrew=DEBUG
```

### 포트 변경
`src/main/resources/application.properties`에서 `server.port` 값을 수정하세요.

---

## 🐛 문제 해결

### "SpringApplication cannot be resolved" 에러
```bash
# 의존성 새로고침
./gradlew build --refresh-dependencies
```
이후 IDE에서 Gradle 프로젝트를 새로고침하세요.

### 포트 80 권한 문제 (Linux/Mac)
```bash
# 1024 이하 포트는 관리자 권한 필요
sudo java -jar build/libs/aikiosk-0.0.1-SNAPSHOT.jar

# 또는 application.properties에서 포트를 8080으로 변경
```

---

## 👥 팀 정보

**FusionCrew** © 2025~2026
