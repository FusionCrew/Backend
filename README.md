# AI Kiosk Backend - 초기 세팅 가이드

## 💻 시스템 요구사항

- **Java**: 17 이상
- **Gradle**: 8.x (Wrapper 포함)
- **IDE**: IntelliJ IDEA 권장
- **OS**: Windows, macOS, Linux

## 📋 프로젝트 구조

```
Backend/
├── src/main/java/com/fusioncrew/aikiosk/
│   ├── global/                    # 공통 설정 및 유틸리티
│   │   ├── config/               # 설정 클래스
│   │   │   ├── SecurityConfig.java
│   │   │   ├── WebConfig.java
│   │   │   └── JpaConfig.java
│   │   ├── security/             # 보안 관련
│   │   │   ├── JwtTokenProvider.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   ├── exception/            # 예외 처리
│   │   │   ├── CustomException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── common/               # 공통 응답 객체
│   │   │   └── ApiResponse.java
│   │   └── entity/               # 공통 엔티티
│   │       └── BaseEntity.java
│   │
│   ├── domain/                   
│   │   ├── admin/               
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   └── dto/
│   │   │
│   │   ├── menu/                
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   └── dto/
│   │   │
│   │   ├── kiosk/                
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   └── dto/
│   │   │
│   │   └── ai/                  
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── entity/
│   │       └── dto/
│   │
│   └── AiKioskApplication.java
│
└── src/main/resources/
    ├── application.properties
    └── application-local.properties (gitignore됨)
```

## 🔐 보안 설정 완료

### 1. Spring Security + JWT

- **JWT 토큰 기반 인증** 구현
- `JwtTokenProvider`: 토큰 생성 및 검증
- `JwtAuthenticationFilter`: 요청마다 토큰 검증
- `SecurityConfig`: 엔드포인트별 권한 설정

### 2. CORS 설정

- 프론트엔드 (localhost:3000, localhost:5173) 허용
- 프로덕션 환경에서는 실제 도메인으로 변경 필요

### 3. 데이터베이스 설정

- **개발 환경**: H2 인메모리 DB (자동 설정)
- **프로덕션**: MySQL 설정 준비됨
- JPA Auditing 활성화 (createdAt, updatedAt 자동 관리)

## � 담당자별 작업 영역

### 📌 다희 - Admin 도메인

**담당 API:**

- 관리자 로그인/로그아웃
- 관리자 계정 관리
- 역할 관리 (ROLE_ADMIN)
- 대시보드 요약 정보
- AI 메트릭스

**작업 경로:** `src/main/java/com/fusioncrew/aikiosk/domain/admin/`

**주요 엔드포인트:**

```
POST   /api/v1/admin/auth/login
POST   /api/v1/admin/auth/logout
POST   /api/v1/admin/auth/refresh
GET    /api/v1/admin/users
POST   /api/v1/admin/users
PATCH  /api/v1/admin/users/{adminId}
DELETE /api/v1/admin/users/{adminId}
GET    /api/v1/admin/dashboard/summary
GET    /api/v1/admin/ai/metrics
```

---

### 📌 나영진 - Menu 도메인

**담당 API:**

- 메뉴 관리 (CRUD)
- 재료 관리
- 메뉴-재료 매핑

**작업 경로:** `src/main/java/com/fusioncrew/aikiosk/domain/menu/`

**주요 엔드포인트:**

```
GET    /api/v1/admin/menu-items (관리자)
POST   /api/v1/admin/menu-items
PATCH  /api/v1/admin/menu-items/{menuItemId}
DELETE /api/v1/admin/menu-items/{menuItemId}
GET    /api/v1/admin/ingredients
POST   /api/v1/admin/ingredients
PATCH  /api/v1/admin/ingredients/{ingredientId}
DELETE /api/v1/admin/ingredients/{ingredientId}
POST   /api/v1/admin/menu-items/{menuItemId}/ingredients
DELETE /api/v1/admin/menu-items/{menuItemId}/ingredients/{ingredientId}
```

---

### � 현근 - Kiosk 도메인

**담당 API:**

- 재고 관리
- 주문 관리
- 결제 관리
- 티켓 관리
- 세션 관리
- 세션 이벤트
- 직원 호출

**작업 경로:** `src/main/java/com/fusioncrew/aikiosk/domain/kiosk/`

**주요 엔드포인트:**

```
GET    /api/v1/admin/stocks
POST   /api/v1/admin/stocks
PATCH  /api/v1/admin/stocks/{stockId}
POST   /api/v1/admin/stocks/{stockId}/out-of-stock
GET    /api/v1/admin/orders
GET    /api/v1/admin/orders/{orderId}
POST   /api/v1/admin/orders/{orderId}/status
GET    /api/v1/admin/payments
GET    /api/v1/admin/payments/{paymentId}
GET    /api/v1/admin/tickets
POST   /api/v1/admin/tickets/{ticketId}/call
POST   /api/v1/admin/tickets/{ticketId}/serve
GET    /api/v1/admin/kiosk-sessions
GET    /api/v1/admin/kiosk-sessions/{sessionId}
GET    /api/v1/admin/session-events
GET    /api/v1/admin/session-events/{eventId}
GET    /api/v1/admin/staff-calls
POST   /api/v1/admin/staff-calls/{callId}/resolve
```

---

### 📌 N.M - AI 도메인 (AI 서버 연동)

**담당 API:**

- AI 서버와의 통신
- STT/TTS 처리

**작업 경로:** `src/main/java/com/fusioncrew/aikiosk/domain/ai/`

**주요 엔드포인트:**

```
GET    /api/v1/admin/ai/metrics
(AI 서버 연동 관련 추가 API)
```

## 🚀 시작하기

### 1. 의존성 다운로드

```bash
./gradlew clean build
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 3. H2 콘솔 접속 (개발용)

- URL: <http://localhost:8080/h2-console>
- JDBC URL: `jdbc:h2:mem:aikioskdb`
- Username: `sa`
- Password: (비어있음)

## 📝 개발 규칙

### 1. 공통 클래스 사용

- **응답 형식**: `ApiResponse<T>` 사용

  ```java
  return ResponseEntity.ok(ApiResponse.success(data));
  ```

- **예외 처리**: `CustomException` 사용

  ```java
  throw new CustomException(HttpStatus.NOT_FOUND, "User not found");
  ```

- **엔티티**: `BaseEntity` 상속 (자동 timestamp 관리)

  ```java
  public class User extends BaseEntity { ... }
  ```

### 2. 패키지 구조 준수

각 도메인은 다음 구조를 따릅니다:

```
domain/{domain_name}/
├── controller/     # REST API 컨트롤러
├── service/        # 비즈니스 로직
├── repository/     # 데이터 접근
├── entity/         # JPA 엔티티
└── dto/           # 요청/응답 DTO
```

### 3. API 명세 준수

- 이미지에 나온 API 엔드포인트를 정확히 구현
- HTTP 메서드 (GET, POST, PATCH, DELETE) 준수
- 응답 형식 통일

### 4. 보안 주의사항

- **JWT Secret**: 프로덕션에서는 환경 변수로 관리
- **비밀번호**: `PasswordEncoder` 사용 (BCrypt)
- **민감 정보**: `application-local.properties`에 저장 (gitignore됨)

## 🔧 환경 설정

### application.properties

- 공통 설정 (모든 환경)
- Git에 커밋됨

### application-local.properties

- 개발자별 로컬 설정
- **Git에 커밋되지 않음** (.gitignore)
- JWT secret, DB password 등 민감 정보

## 🛡️ 보안 엔드포인트 설정

### 인증 불필요 (Public)

- `/api/v1/admin/auth/**` - 로그인/회원가입
- `/h2-console/**` - H2 콘솔 (개발용)
- `/actuator/health` - 헬스체크

### 인증 필요 (Protected)

- `/api/v1/admin/**` - 모든 관리자 API
- JWT 토큰 필요: `Authorization: Bearer {token}`

## 📌 다음 단계

1. **각 담당자**: 자신의 도메인 패키지에 Entity 정의
2. **Repository** 작성 (JpaRepository 상속)
3. **Service** 비즈니스 로직 구현
4. **Controller** REST API 구현
5. **DTO** 요청/응답 객체 정의

## � 참고사항

- **충돌 방지**: 각자 자신의 도메인 패키지에서만 작업
- **공통 수정**: `global/` 패키지 수정 시 팀원들과 협의
- **코드 리뷰**: PR 전에 빌드 및 테스트 확인
- **API 문서**: Swagger/OpenAPI 추가 예정

---

**문의사항이 있으면 팀 채널에 공유해주세요!** 🚀
