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
    bootpay = new Bootpay(Config.PG.getApplicationId(), Config.PG.getPrivateKey());

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
