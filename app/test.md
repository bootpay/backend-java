# Java SDK 테스트 실행 가이드

## 환경 설정

`Config.java` 파일에서 환경을 설정합니다:

```java
// "production" 또는 "development"로 설정
public static final String CURRENT_ENV = "production";
```

## 테스트 실행

### Gradle로 실행
```bash
cd /Users/taesupyoon/bootpay/server/sdk/java

# 빌드
./gradlew build

# 예제 실행
./gradlew :app:run
```

### IntelliJ IDEA에서 실행
1. `BootpayExample.java` 파일 열기
2. `main()` 메서드에서 실행할 테스트 주석 해제
3. Run 버튼 클릭 또는 `Ctrl+Shift+F10`

### 개별 테스트 활성화

`BootpayExample.java`의 `main()` 메서드에서 원하는 테스트의 주석을 해제합니다:

```java
public static void main(String[] args) {
    bootpay = Bootpay.withClientKey(Config.PG.getClientKey(), Config.PG.getSecretKey());

    goGetToken();           // 토큰 발급
//  getReceipt();           // 결제 조회
//  receiptCancel();        // 결제 취소
//  getBillingKey();        // 빌링키 발급
//  requestSubscribe();     // 정기결제 실행
//  reserveSubscribe();     // 예약 결제
//  reserveCancelSubscribe(); // 예약 결제 취소
//  destroyBillingKey();    // 빌링키 삭제
//  getUserToken();         // 사용자 토큰 발급
//  confirm();              // 결제 승인
//  certificate();          // 본인인증 조회
//  shippingStart();        // 에스크로 배송시작
//  getBillingKeyTransfer();    // 계좌 빌링키 발급
//  publishBillingKeyTransfer(); // 계좌 빌링키 발행
//  requestAuthentication();    // 본인인증 요청
//  confirmAuthentication();    // 본인인증 승인
//  realarmAuthentication();    // 본인인증 재요청
//  requestCashReceipt();       // 현금영수증 발행
//  requestCashReceiptCancel(); // 현금영수증 취소
//  requestCashReceiptByBootpay();       // 결제건 현금영수증 발행
//  requestCashReceiptCancelByBootpay(); // 결제건 현금영수증 취소
}
```

## 테스트 데이터

`Config.java`의 `TestData` 내부 클래스에서 테스트 데이터를 관리합니다:

```java
public static class TestData {
    public static final String RECEIPT_ID = "628b2206d01c7e00209b6087";
    public static final String RECEIPT_ID_CONFIRM = "62876963d01c7e00209b6028";
    public static final String RECEIPT_ID_CASH = "62e0f11f1fc192036b1b3c92";
    public static final String RECEIPT_ID_ESCROW = "628ae7ffd01c7e001e9b6066";
    public static final String RECEIPT_ID_BILLING = "62c7ccebcf9f6d001b3adcd4";
    public static final String RECEIPT_ID_TRANSFER = "66541bc4ca4517e69343e24c";
    public static final String BILLING_KEY = "628b2644d01c7e00209b6092";
    public static final String BILLING_KEY_2 = "66542dfb4d18d5fc7b43e1b6";
    public static final String RESERVE_ID = "6490149ca575b40024f0b70d";
    public static final String RESERVE_ID_2 = "628b316cd01c7e00219b6081";
    public static final String USER_ID = "1234";
    public static final String CERTIFICATE_RECEIPT_ID = "61b009aaec81b4057e7f6ecd";
}
```

## 폴더 구조

```
app/src/main/java/com/example/bootpay/
├── Config.java            # 환경 설정 및 테스트 데이터
├── BootpayExample.java    # PG API 예제
└── store/                 # Commerce API 예제
    ├── User.java
    ├── UserGroup.java
    ├── Product.java
    ├── Order.java
    ├── Invoice.java
    └── ...
```

## PG 인증 방식 토글 (BOOTPAY_AUTH_MODE)

PG 테스트는 기본적으로 신규 `client_key/secret_key` 방식으로 동작한다. 매 실행 시 환경변수로 레거시 `application_id/private_key` 방식으로 전환할 수 있다.

### 토글 contract

| `BOOTPAY_AUTH_MODE` | 동작 |
|---|---|
| `new` (기본, 미설정 시 동일) | `Bootpay.withClientKey(clientKey, secretKey, mode)` 로 인스턴스 생성. Basic Auth 헤더 자동 부착. |
| `legacy` | `new Bootpay(applicationId, privateKey, mode)` 로 인스턴스 생성. `getAccessToken()` 호출 후 `Bearer` 헤더 사용. |

키 값은 모두 `.env` (또는 환경변수) 로 주입한다 — `.env.example` 참고.

### 사용법

```bash
# (1) 기본 — env var 생략 (= new)
./gradlew :app:run

# (2) 한 번만 legacy 로 전환
BOOTPAY_AUTH_MODE=legacy ./gradlew :app:run

# (3) JUnit 통합 테스트도 동일하게 환경변수로 토글
BOOTPAY_AUTH_MODE=legacy ./gradlew :core:test --tests "kr.co.bootpay.pg.*"

# (4) 셸 세션 동안 legacy 고정
export BOOTPAY_AUTH_MODE=legacy
./gradlew :app:run
./gradlew :core:test
unset BOOTPAY_AUTH_MODE

# (5) 영구 전환 — .env 의 BOOTPAY_AUTH_MODE 값을 legacy 로 바꾸면 셸 export 없이도 동작
```

### 진입 헬퍼 — 어디서 토글이 흡수되는가

| 테스트 종류 | 위치 | 헬퍼 |
|---|---|---|
| Example 앱 (`app/src/main/...`) | `Config.java` | `Config.PG.createBootpay()` |
| JUnit 통합 테스트 (`core/src/test/...`) | `TestConfig.java` | `TestConfig.createBootpay()` / `createBootpayWithToken()` |

JUnit `createBootpayWithToken()` 은 legacy 모드에서만 실제로 토큰 발급을 수행한다 (ck/sk 모드는 매 요청 Basic Auth 로 처리되어 발급 불필요). 어느 모드든 테스트 코드는 동일하다:

```java
Bootpay bootpay = TestConfig.createBootpayWithToken();
```

### 실행 시 인증 모드 표시

`createBootpay()` 가 호출될 때마다 stdout 에 한 줄로 어떤 모드가 활성화됐는지 표시된다 (`./gradlew :app:run` 로그 또는 `./gradlew :core:test --info` 출력에서 확인):

```
[BOOTPAY_AUTH_MODE=new] PG: client_key/secret_key (Basic Auth) | env=production
[BOOTPAY_AUTH_MODE=legacy] PG: application_id/private_key (Bearer) | env=production
```

### 토글의 영향을 받지 않는 파일

다음 core 테스트는 두 모드를 한 함수 안에서 모두 검증하므로 환경변수에 무관하게 동일한 동작을 한다:

- `core/src/test/java/kr/co/bootpay/pg/PgTokenTest.java`
- `core/src/test/java/kr/co/bootpay/pg/LegacyCompatibilityTest.java`
