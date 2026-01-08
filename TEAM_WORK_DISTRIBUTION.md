# 담당자별 작업 분기 (실제)

## 👥 담당자 구성

- **이다하**: 관리자 인증, 재고 관리, 장바구니, 주문, 대기표
- **김나영**: 키오스크 세션, 메뉴 조회, 추천, 피드백, 관리자 메뉴/재료 관리
- **유현곤**: 결제 시스템, 티켓, 직원호출, 대시보드, AI로그
- **이대연**: FastAPI 프록시 (AI 서버 연동)

---

## 📦 도메인 재구성

```
domain/
├── auth/           # 이다하 - 관리자 인증
├── stock/          # 이다하 - 재고 관리
├── cart/           # 이다하 - 장바구니
├── order/          # 이다하 - 주문
├── ticket/         # 이다하 - 대기표 발급/조회
│
├── kiosk/          # 김나영 - 키오스크 세션, 헬스체크
├── menu/           # 김나영 - 메뉴 조회/관리, 재료, 추천
├── feedback/       # 김나영 - 사용자 피드백
│
├── payment/        # 유현곤 - 결제 시스템
├── staff/          # 유현곤 - 직원호출, 티켓 호출/완료
├── dashboard/      # 유현곤 - 대시보드, AI로그
│
└── ai/             # 이대연 - FastAPI 프록시
```

---

## 🔀 충돌 가능성 분석

### ⚠️ 높은 충돌 가능성

#### 1. **이다하 ↔ 김나영** (메뉴 관련)

- **충돌 지점**: Menu Entity
  - 이다하: 주문에서 메뉴 참조
  - 김나영: 메뉴 CRUD, 조회
- **해결책**:
  - 김나영이 `Menu` Entity 생성
  - 이다하는 `Menu`를 참조만 (`@ManyToOne`)

#### 2. **이다하 ↔ 유현곤** (주문-결제 연동)

- **충돌 지점**: Order ↔ Payment 연결
  - 이다하: 주문 생성
  - 유현곤: 결제 처리
- **해결책**:
  - 이다하: `Order` Entity에 `paymentId` 필드만 추가
  - 유현곤: `Payment` Entity 독립 관리
  - API 호출로 연동

#### 3. **이다하 ↔ 유현곤** (대기표-티켓)

- **충돌 지점**: Ticket Entity
  - 이다하: 대기표 발급/조회
  - 유현곤: 티켓 호출/완료 처리
- **해결책**:
  - **한 명이 Ticket Entity 관리** (유현곤 추천)
  - 이다하는 Service만 작성, Entity는 유현곤 것 사용

#### 4. **김나영 ↔ 유현곤** (직원호출)

- **충돌 지점**: StaffCall Entity
  - 김나영: 직원호출 요청
  - 유현곤: 직원호출 처리
- **해결책**:
  - 유현곤이 `StaffCall` Entity 생성
  - 김나영은 참조만

---

## 🛠️ 권장 작업 순서

### Phase 1: Entity 정의 (협의 필수)

1. **김나영**: `Menu`, `Ingredient` Entity 먼저 생성
2. **유현곤**: `Payment`, `Ticket`, `StaffCall` Entity 생성
3. **이다하**: `Cart`, `Order` Entity 생성 (Menu, Ticket 참조)
4. **이대연**: AI 프록시 설정

### Phase 2: Repository & Service

- 각자 자신의 도메인 비즈니스 로직 작성

### Phase 3: Controller (API)

- 각자 담당 API 엔드포인트 구현

---

## 📋 Entity 의존 관계

```
Menu (김나영)
  ↓ 참조
Order (이다하) → Payment (유현곤)
  ↓ 참조
Ticket (유현곤)

StaffCall (유현곤)
  ↑ 요청
Feedback (김나영)
```

---

## 🚨 충돌 방지 규칙

### 1. Entity 생성 전 협의

- Menu, Ticket, StaffCall 등 **공유 Entity**는 한 명이 생성
- 다른 사람은 `@ManyToOne`으로 참조만

### 2. 패키지 명명 규칙

```
com.fusioncrew.aikiosk.domain.{도메인명}/
├── controller/
├── service/
├── repository/
├── entity/      # 여기서 충돌 주의!
└── dto/
```

### 3. API 엔드포인트 중복 방지

- 이다하: `/api/v1/admin/stocks`, `/api/v1/cart`, `/api/v1/orders`
- 김나영: `/api/v1/kiosk`, `/api/v1/menu`, `/api/v1/feedback`
- 유현곤: `/api/v1/payments`, `/api/v1/staff-calls`, `/api/v1/dashboard`
- 이대연: `/api/v1/ai/**` (FastAPI 프록시)

### 4. Git 작업 규칙

```bash
# 브랜치 전략
main
├── feature/daha-auth
├── feature/daha-cart
├── feature/nayoung-menu
├── feature/hyungon-payment
└── feature/daeyeon-ai-proxy
```

---

## 💡 추천 사항

### 1. 먼저 Entity 구조 회의

- Menu, Ticket, StaffCall 등 공유 Entity 담당자 결정
- 관계 설정 (`@OneToMany`, `@ManyToOne`) 합의

### 2. DTO 공유

- `global/dto/` 패키지에 공통 DTO 생성 가능
- 예: `MenuResponse`, `TicketResponse`

### 3. Service 간 통신

- 다른 도메인 Service 호출 시 `@Autowired`로 주입

```java
@Service
public class OrderService {
    private final MenuService menuService; // 김나영 것 사용
    private final PaymentService paymentService; // 유현곤 것 사용
}
```

---

## 🎯 결론

**충돌 가능성: 중간**

- Entity 관계가 복잡하므로 **사전 협의 필수**
- Menu, Ticket, StaffCall 같은 공유 Entity는 **한 명이 생성**
- 나머지는 각자 도메인에서 독립 작업 가능

**다음 단계**: 팀 회의에서 Entity 구조 합의 후 작업 시작! 🚀
