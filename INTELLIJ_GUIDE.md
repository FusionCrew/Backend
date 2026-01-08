# IntelliJ IDEA에서 프로젝트 실행 가이드

## 1️⃣ 프로젝트 Import (처음 한 번만)

1. **IntelliJ IDEA 실행**
2. **Open** 클릭
3. `c:\AIkiosk\Backend` 폴더 선택
4. **Trust Project** 클릭
5. Gradle 자동 Import 대기 (우측 하단 진행 바 확인)

## 2️⃣ JDK 설정 확인

1. `File` → `Project Structure` (Ctrl+Alt+Shift+S)
2. **Project** 탭:
   - SDK: Java 21 선택
   - Language level: 21
3. **Modules** 탭:
   - Language level: 21
4. **Apply** → **OK**

## 3️⃣ 실행 방법

### ✅ 방법 1: 메인 클래스 실행 (가장 쉬움)

1. `src/main/java/com/fusioncrew/aikiosk/AiKioskApplication.java` 열기
2. 파일 상단 또는 `main` 메서드 옆의 **녹색 ▶️** 버튼 클릭
3. `Run 'AiKioskApplication'` 선택

### ✅ 방법 2: Gradle bootRun

1. 우측 사이드바 **Gradle** 탭 열기
2. `Backend` → `Tasks` → `application` → `bootRun` 더블클릭

### ✅ 방법 3: Run Configuration

1. 상단 메뉴: `Run` → `Edit Configurations...`
2. `+` → `Spring Boot`
3. 설정:
   - Name: `AiKiosk Backend`
   - Main class: `com.fusioncrew.aikiosk.AiKioskApplication`
   - Module: `Backend.main`
4. **OK** → 상단 실행 버튼(▶️) 클릭

## 4️⃣ 실행 확인

실행 후 콘솔에서 다음 메시지 확인:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

...
Started AiKioskApplication in X.XXX seconds
```

## 5️⃣ 접속 확인

- **API 서버**: <http://localhost:8080>
- **H2 Console**: <http://localhost:8080/h2-console>
  - JDBC URL: `jdbc:h2:mem:aikioskdb`
  - Username: `sa`
  - Password: (비어있음)

## 🔧 문제 해결

### Gradle 동기화 안 됨

- 우측 Gradle 탭 → 새로고침 버튼(🔄) 클릭
- 또는 `File` → `Invalidate Caches...` → `Invalidate and Restart`

### JDK 없음

- `File` → `Project Structure` → `SDKs`
- `+` → `Download JDK...` → Amazon Corretto 21 다운로드

### Port 8080 이미 사용 중

- `application.properties`에서 포트 변경:

  ```properties
  server.port=8081
  ```

## 🎯 단축키

- **실행**: `Shift + F10`
- **디버그**: `Shift + F9`
- **중지**: `Ctrl + F2`
- **재실행**: `Ctrl + F5`

---

**이제 IntelliJ에서 편하게 개발하세요!** 🚀
