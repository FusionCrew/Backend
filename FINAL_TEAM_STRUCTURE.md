# 🎯 백엔드 세팅 완료 - 실제 담당자 구조

## 👥 담당자별 도메인

### 🔵 이다하

**도메인**: `auth`, `stock`, `cart`, `order`, `ticket`

**담당 기능**:

- ✅ 관리자 로그인, 토큰 재발급, 로그아웃, 내정보조회
- ✅ 관리자 계정 목록/생성/수정/삭제
- ✅ 재고 조회/등록/초기화/수량변경/품절처리
- ✅ 장바구니 생성/조회/담기/수량변경/항목삭제/비우기
- ✅ 주문 생성(결제 전)/조회/취소/확정
- ✅ 대기표 발급 요청/조회

**작업 경로**:

```
domain/auth/
domain/stock/
domain/cart/
domain/order/
domain/ticket/
```

---

### 🟢 김나영

**도메인**: `kiosk`, `menu`, `feedback`

**담당 기능**:

- ✅ 헬스체크, 키오스크 세션 관련
- ✅ 메뉴 목록 조회, 재료/알레르기 필터, 추천메뉴
- ✅ 직원호출 요청
- ✅ 사용자 피드백 제출
- ✅ 관리자: 메뉴 목록/등록/수정/삭제
- ✅ 관리자: 재료 목록/등록/수정/삭제
- ✅ 관리자: 메뉴-재료 매핑 등록/삭제

**작업 경로**:

```
domain/kiosk/
domain/menu/
domain/feedback/
```

---

### 🟡 유현곤

**도메인**: `payment`, `staff`, `dashboard`

**담당 기능**:

- ✅ 결제 시스템 전체
- ✅ 결제 목록/상세
- ✅ 티켓 목록/호출/완료처리
- ✅ 직원호출 목록/처리
- ✅ 세션 목록/상세/이벤트 검색/상세
- ✅ 대시보드 요약
- ✅ AI로그

**작업 경로**:

```
domain/payment/
domain/staff/
domain/dashboard/
```

---

### 🟣 이대연

**도메인**: `ai`

**담당 기능**:

- ✅ FastAPI 프록시 (Spring → FastAPI)
- ✅ AI 서버 연동 전체

**작업 경로**:

```
domain/ai/
```

---

## ⚠️ 충돌 주의 사항

### 1. Entity 공유 필요

- **Menu** (김나영 생성 → 이다하 참조)
- **Ticket** (유현곤 or 이다하 협의)
- **StaffCall** (유현곤 생성 → 김나영 참조)
- **Payment** (유현곤 생성 → 이다하 참조)

### 2. 협의 필요한 부분

```
Order (이다하) ←→ Payment (유현곤)
Order (이다하) ←→ Menu (김나영)
Ticket (이다하) ←→ Staff (유현곤)
StaffCall (김나영) ←→ Staff (유현곤)
```

### 3. 작업 순서 권장

1. **김나영**: Menu, Ingredient Entity 먼저 생성
2. **유현곤**: Payment, StaffCall Entity 생성
3. **이다하**: Order, Cart Entity 생성 (Menu, Payment 참조)
4. **Ticket Entity**: 이다하 & 유현곤 협의 후 결정

---

## 📁 최종 도메인 구조

```
domain/
├── auth/           # 이다하 - 관리자 인증
├── stock/          # 이다하 - 재고 관리
├── cart/           # 이다하 - 장바구니
├── order/          # 이다하 - 주문
├── ticket/         # 이다하 - 대기표 발급/조회
│
├── kiosk/          # 김나영 - 키오스크 세션
├── menu/           # 김나영 - 메뉴 조회/관리, 재료
├── feedback/       # 김나영 - 사용자 피드백
│
├── payment/        # 유현곤 - 결제 시스템
├── staff/          # 유현곤 - 직원호출, 티켓 처리
├── dashboard/      # 유현곤 - 대시보드, AI로그
│
└── ai/             # 이대연 - FastAPI 프록시
```

---

## 🚀 다음 단계

1. **팀 회의**: Entity 구조 협의
2. **Entity 생성**: 김나영 → 유현곤 → 이다하 순서
3. **Repository & Service**: 각자 비즈니스 로직
4. **Controller**: API 엔드포인트 구현

---

## ✅ 완료된 공통 설정

- ✅ Spring Security + JWT
- ✅ CORS 설정
- ✅ H2 Database (개발용)
- ✅ JPA Auditing
- ✅ 예외 처리
- ✅ API 응답 형식 통일

**이제 각자 도메인에서 작업 시작 가능!** 🎉
