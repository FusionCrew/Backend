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
│   │   └── entity/               # 공통 엔티티 (BaseEntity)
│   │       └── BaseEntity.java
│   │
│   ├── domain/                   
│   │   ├── auth/                 # 관리자 인증 (이대연)
│   │   ├── stock/                # 재고 관리 (이다하)
│   │   ├── cart/                 # 장바구니 (이다하)
│   │   ├── order/                # 주문 관리 (이다하)
│   │   ├── ticket/               # 대기표 (이다하/유현곤 협의)
│   │   ├── kiosk/                # 키오스크 세션/헬스체크 (김나영)
│   │   ├── menu/                 # 메뉴/재료 관리 (김나영)
│   │   ├── feedback/             # 피드백 (김나영)
│   │   ├── payment/              # 결제 시스템 (유현곤)
│   │   ├── staff/                # 직원 호출/티켓 처리 (유현곤)
│   │   ├── dashboard/            # 대시보드/로그 (유현곤)
│   │   └── ai/                   # AI 프록시 (이대연)
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

- **개발 환경**: H2 인메모리 DB
- **프로덕션**: MySQL 설정 준비됨
- JPA Auditing 활성화 (createdAt, updatedAt 자동 관리)

## 👤 담당자별 작업 영역

### 📌 이다하 (Inventory, User Cart/Order)

**담당 기능:**

- **일반**: 장바구니 CRUD, 주문 생성/조회/취소/확정, 대기표 발급 요청/조회
- **관리**: 재고 CRUD, 품절 처리
**작업 경로:** `domain/stock/`, `domain/cart/`, `domain/order/`, `domain/ticket/`

---

### 📌 김나영 (Kiosk Session, Menu, User Feedback)

**담당 기능:**

- **일반**: 헬스체크, 세션 관련, 메뉴 목록/재료/필터/추천 조회, 직원호출 요청, 피드백 제출
- **관리**: 메뉴/재료 CRUD, 메뉴-재료 매핑 관리
**작업 경로:** `domain/kiosk/`, `domain/menu/`, `domain/feedback/`

---

### 📌 유현곤 (Payment, Staff Support, Dashboard, AI Logs)

**담당 기능:**

- **결제**: 결제 시스템 전체 및 목록/상세
- **관리**: 티켓 호출/완료 처리, 세션 목록/이벤트/상세, 직원호출 목록/처리, 대시보드 요약, AI 로그
**작업 경로:** `domain/payment/`, `domain/staff/`, `domain/dashboard/`

---

### 📌 이대연 (Admin Auth, AI Proxy)

**담당 기능:**

- **보안**: 관리자 로그인/로그아웃/토큰/내정보 관리 (Auth 전체)
- **AI**: Spring 내부에서 FastAPI로 API를 쏘는 프록시 코드 전체
**작업 경로:** `domain/auth/`, `domain/ai/`

---

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
