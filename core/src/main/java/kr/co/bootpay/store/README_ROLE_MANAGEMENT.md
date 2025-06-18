# Bootpay Store SDK - Role 관리 가이드

## 개요

Bootpay Store SDK는 API 호출 시 role 기반 권한 관리를 지원합니다. 기본값은 "user"로 설정되어 있으며, API 호출 시 명시적으로 role을 지정할 수 있습니다.

## 주요 특징

- **기본값**: 모든 API 호출은 기본적으로 "user" role로 실행됩니다.
- **메서드 체이닝**: `.asUser()`, `.asManager()` 등의 메서드를 통해 role을 지정할 수 있습니다.
- **토큰 발급**: 토큰은 API를 통해 발급받아야 하며, 직접 설정할 수 없습니다.
- **5가지 Role**: user, manager, partner, vendor, supervisor를 지원합니다.
- **객체지향 응답**: 모든 API는 `BootpayStoreResponse` 객체를 반환하여 안전하고 직관적인 사용이 가능합니다.

## 기본 사용법

### 1. 초기 설정

```java
TokenPayload tokenPayload = new TokenPayload();
tokenPayload.clientKey = "your_client_key";
tokenPayload.secretKey = "your_secret_key";

BootpayStore bootpay = new BootpayStore(tokenPayload, "PRODUCTION");
```

### 2. 토큰 발급

```java
// API를 통해 토큰 발급
bootpay.withToken();

// 토큰 발급 확인
System.out.println("토큰 발급 완료: " + bootpay.hasToken()); // true
```

### 3. 기본값으로 API 호출

```java
// role을 명시하지 않으면 기본값 "user"로 호출
BootpayStoreResponse result = bootpay.user.login("user123", "password123");

// 응답 처리
if (result.isSuccess()) {
    HashMap<String, Object> data = result.getDataAsMap();
    System.out.println("로그인 성공: " + data.get("user_id"));
} else {
    System.out.println("로그인 실패: " + result.getError());
}
```

## Role 지정 방법

### 5가지 기본 Role

```java
// 1. 일반 사용자
BootpayStoreResponse userResult = bootpay
    .asUser()
    .user.login("user123", "password123");

// 2. 매니저
BootpayStoreResponse managerResult = bootpay
    .asManager()
    .order.list(null);

// 3. 파트너
BootpayStoreResponse partnerResult = bootpay
    .asPartner()
    .product.list(null);

// 4. 벤더
BootpayStoreResponse vendorResult = bootpay
    .asVendor()
    .invoice.list(null);

// 5. 슈퍼바이저
BootpayStoreResponse supervisorResult = bootpay
    .asSupervisor()
    .user.list(null);
```

### 커스텀 Role

```java
// 커스텀 role 지정
BootpayStoreResponse customResult = bootpay
    .withRole("custom_role")
    .product.list(null);
```

### Role 초기화

```java
// role을 기본값 "user"로 초기화
bootpay.clearRole();

// 초기화 후 API 호출 (기본값 "user"로 호출)
BootpayStoreResponse result = bootpay.user.detail("user123");
```

## 토큰 관리

### 토큰 발급

```java
// API를 통해 토큰 발급
bootpay.withToken();

// 토큰 발급 확인
if (bootpay.hasToken()) {
    System.out.println("토큰이 발급되었습니다.");
}
```

### 토큰 재발급

```java
// 토큰이 만료된 경우 재발급
bootpay.withToken();
```

### 현재 토큰 확인

```java
String currentToken = bootpay.getCurrentToken();
System.out.println("현재 토큰: " + currentToken);
```

## 실제 사용 예제

### 순차적 Role 사용

```java
// 토큰 발급
bootpay.withToken();

// 1. 기본값(user)으로 로그인
BootpayStoreResponse defaultLogin = bootpay
    .user.login("user123", "password123");

if (defaultLogin.isSuccess()) {
    System.out.println("기본 로그인 성공");
}

// 2. 매니저로 주문 관리
BootpayStoreResponse managerOrder = bootpay
    .asManager()
    .order.list(null);

if (managerOrder.isSuccess()) {
    List<HashMap<String, Object>> orders = managerOrder.getDataAsList();
    System.out.println("매니저 주문 조회 성공: " + orders.size() + "개");
}

// 3. 파트너로 상품 관리
BootpayStoreResponse partnerProduct = bootpay
    .asPartner()
    .product.list(null);

if (partnerProduct.isSuccess()) {
    HashMap<String, Object> data = partnerProduct.getDataAsMap();
    System.out.println("파트너 상품 조회 성공");
}

// 4. 벤더로 인보이스 관리
BootpayStoreResponse vendorInvoice = bootpay
    .asVendor()
    .invoice.list(null);

if (vendorInvoice.isSuccess()) {
    System.out.println("벤더 인보이스 조회 성공");
}

// 5. 슈퍼바이저로 사용자 관리
BootpayStoreResponse supervisorUser = bootpay
    .asSupervisor()
    .user.list(null);

if (supervisorUser.isSuccess()) {
    System.out.println("슈퍼바이저 사용자 조회 성공");
}

// 6. role 초기화 후 기본값으로 API 호출
bootpay.clearRole();
BootpayStoreResponse afterClearResult = bootpay
    .user.detail("user123");
```

## BootpayStoreResponse 사용법

### 응답 객체 구조

```java
BootpayStoreResponse response = bootpay.user.login("user123", "password123");

// HTTP 상태 코드
int statusCode = response.getHttpStatus();

// 성공 여부 확인
boolean isSuccess = response.isSuccess();
boolean isFailed = response.isFailed();

// 데이터 접근
if (response.isSuccess()) {
    // HashMap 데이터 (단일 객체 응답)
    HashMap<String, Object> data = response.getDataAsMap();
    
    // List 데이터 (배열 응답)
    List<HashMap<String, Object>> dataList = response.getDataAsList();
    
    // Object 데이터 (타입에 관계없이)
    Object data = response.getData();
}

// 에러 처리
if (response.hasError()) {
    String errorMessage = response.getError();
    System.out.println("에러 발생: " + errorMessage);
}

// HashMap으로 변환 (기존 코드와의 호환성)
HashMap<String, Object> legacyResponse = response.toHashMap();
```

### 실제 사용 예제

```java
// 사용자 로그인
BootpayStoreResponse loginResponse = bootpay.user.login("user123", "password123");

if (loginResponse.isSuccess()) {
    HashMap<String, Object> userData = loginResponse.getDataAsMap();
    String userId = (String) userData.get("user_id");
    System.out.println("로그인 성공: " + userId);
} else {
    System.out.println("로그인 실패: " + loginResponse.getError());
}

// 주문 목록 조회 (배열 응답)
BootpayStoreResponse orderResponse = bootpay.order.list(null);

if (orderResponse.isSuccess()) {
    List<HashMap<String, Object>> orders = orderResponse.getDataAsList();
    System.out.println("주문 조회 성공: " + orders.size() + "개 주문");
    
    for (HashMap<String, Object> order : orders) {
        String orderId = (String) order.get("order_id");
        System.out.println("주문 ID: " + orderId);
    }
} else {
    System.out.println("주문 조회 실패: " + orderResponse.getError());
}

// 상품 상세 조회
BootpayStoreResponse productResponse = bootpay.product.detail("product123");

if (productResponse.isSuccess()) {
    HashMap<String, Object> productData = productResponse.getDataAsMap();
    String productName = (String) productData.get("name");
    System.out.println("상품 조회 성공: " + productName);
} else {
    System.out.println("상품 조회 실패: " + productResponse.getError());
}
```

## 구조 변경사항

### BootpayStoreObject 클래스

- `token` 필드를 `private`으로 변경하여 직접 접근 차단
- `setToken()` 메서드 제거 (API를 통해서만 토큰 설정 가능)
- `getToken()` 메서드 추가 (읽기 전용 접근)
- `responseToJsonObject()` 메서드 추가 (BootpayStoreResponse 반환)
- `responseToJsonArrayObject()` 메서드 추가 (배열 응답용 BootpayStoreResponse 반환)

### BootpayStore 클래스

- `withToken()` 메서드 추가 (API를 통해 토큰 발급)
- `getCurrentToken()` 메서드 추가 (현재 토큰 확인)
- `hasToken()` 메서드 추가 (토큰 발급 여부 확인)

### BootpayStoreResponse 클래스

- `getHttpStatus()`: HTTP 상태 코드 반환
- `isSuccess()`: 성공 여부 확인
- `isFailed()`: 실패 여부 확인
- `getData()`: Object 타입 데이터 반환
- `getDataAsMap()`: HashMap 타입 데이터 반환
- `getDataAsList()`: List 타입 데이터 반환
- `getError()`: 에러 메시지 반환
- `hasError()`: 에러 존재 여부 확인
- `toHashMap()`: 기존 코드와의 호환성을 위한 HashMap 변환

## 주의사항

1. **토큰 직접 설정 불가**: 토큰은 반드시 API를 통해 발급받아야 합니다.
2. **기본값 자동 적용**: role을 명시하지 않으면 기본값 "user"가 자동으로 적용됩니다.
3. **메서드 체이닝**: role 지정은 메서드 체이닝 방식으로만 가능합니다.
4. **토큰 발급 필수**: API 호출 전에 반드시 `withToken()`을 호출하여 토큰을 발급받아야 합니다.
5. **응답 객체 사용**: 모든 API는 `BootpayStoreResponse` 객체를 반환하므로 안전한 타입 캐스팅과 에러 처리가 가능합니다.

## 지원하는 Role 목록

| Role | 설명 | 사용 예시 |
|------|------|-----------|
| user | 일반 사용자 | 로그인, 개인정보 조회 |
| manager | 매니저 | 주문 관리, 팀 관리 |
| partner | 파트너 | 상품 관리, 파트너십 관리 |
| vendor | 벤더 | 인보이스 관리, 공급업체 관리 |
| supervisor | 슈퍼바이저 | 사용자 관리, 시스템 관리 |

이 구조를 통해 안전하고 체계적인 권한 관리가 가능합니다.

## 제공되는 메서드

### Role 설정 메서드 (BootpayStore)

- `withRole(String role)`: 특정 role을 설정
- `asUser()`: 일반 사용자 role로 설정 (기본값)
- `asManager()`: 매니저 role로 설정
- `asPartner()`: 파트너 role로 설정
- `asVendor()`: 벤더 role로 설정
- `asSupervisor()`: 슈퍼바이저 role로 설정
- `clearRole()`: role을 기본값(user)으로 초기화
- `getCurrentRole()`: 현재 설정된 role 반환

### RequestContext Builder 메서드

- `role(String role)`: role 설정
- `token(String token)`: token 설정
- `build()`: RequestContext 객체 생성

## 사용 예시

### 시나리오 1: 기본값으로 API 호출

```java
BootpayStore bootpay = new BootpayStore(tokenPayload, "PRODUCTION");

// 기본값(user)으로 로그인
BootpayStoreResponse defaultLogin = bootpay
    .user.login("user123", "password123"); // 기본값 "user"로 호출

if (defaultLogin.isSuccess()) {
    HashMap<String, Object> userData = defaultLogin.getDataAsMap();
    System.out.println("로그인 성공");
}

// 기본값(user)으로 사용자 정보 조회
BootpayStoreResponse userDetail = bootpay
    .user.detail("user123"); // 기본값 "user"로 호출

if (userDetail.isSuccess()) {
    HashMap<String, Object> data = userDetail.getDataAsMap();
    System.out.println("사용자 정보 조회 성공");
}

// 기본값(user)으로 주문 조회
BootpayStoreResponse userOrders = bootpay
    .order.list(null); // 기본값 "user"로 호출

if (userOrders.isSuccess()) {
    List<HashMap<String, Object>> orders = userOrders.getDataAsList();
    System.out.println("주문 조회 성공: " + orders.size() + "개");
}
```

### 시나리오 2: 5가지 role로 API 호출

```java
BootpayStore bootpay = new BootpayStore(tokenPayload, "PRODUCTION");

// 1. 일반 사용자 권한
BootpayStoreResponse userResult = bootpay
    .asUser()
    .user.detail("user123");

if (userResult.isSuccess()) {
    System.out.println("사용자 권한으로 조회 성공");
}

// 2. 매니저 권한
BootpayStoreResponse managerResult = bootpay
    .asManager()
    .order.list(null);

if (managerResult.isSuccess()) {
    List<HashMap<String, Object>> orders = managerResult.getDataAsList();
    System.out.println("매니저 권한으로 주문 조회 성공: " + orders.size() + "개");
}

// 3. 파트너 권한
BootpayStoreResponse partnerResult = bootpay
    .asPartner()
    .product.list(null);

if (partnerResult.isSuccess()) {
    System.out.println("파트너 권한으로 상품 조회 성공");
}

// 4. 벤더 권한
BootpayStoreResponse vendorResult = bootpay
    .asVendor()
    .invoice.list(null);

if (vendorResult.isSuccess()) {
    System.out.println("벤더 권한으로 인보이스 조회 성공");
}

// 5. 슈퍼바이저 권한
BootpayStoreResponse supervisorResult = bootpay
    .asSupervisor()
    .user.list(null);

if (supervisorResult.isSuccess()) {
    List<HashMap<String, Object>> users = supervisorResult.getDataAsList();
    System.out.println("슈퍼바이저 권한으로 사용자 조회 성공: " + users.size() + "명");
}
```

### 시나리오 3: 매니저 작업

```java
// 매니저로 로그인
BootpayStoreResponse managerLogin = bootpay
    .asManager()
    .user.login("manager", "password");

if (managerLogin.isSuccess()) {
    System.out.println("매니저 로그인 성공");
}

// 담당 팀의 주문 조회
BootpayStoreResponse teamOrders = bootpay
    .asManager()
    .order.list(null);

if (teamOrders.isSuccess()) {
    List<HashMap<String, Object>> orders = teamOrders.getDataAsList();
    System.out.println("팀 주문 조회 성공: " + orders.size() + "개");
}

// 주문 취소 요청 승인
BootpayStoreResponse approveCancel = bootpay
    .asManager()
    .orderCancel.approve(cancelParams);

if (approveCancel.isSuccess()) {
    System.out.println("주문 취소 승인 성공");
} else {
    System.out.println("주문 취소 승인 실패: " + approveCancel.getError());
}
```

### 시나리오 4: 파트너 작업

```java
// 파트너로 로그인
BootpayStoreResponse partnerLogin = bootpay
    .asPartner()
    .user.login("partner", "password");

if (partnerLogin.isSuccess()) {
    System.out.println("파트너 로그인 성공");
}

// 파트너 상품 관리
BootpayStoreResponse partnerProducts = bootpay
    .asPartner()
    .product.list(null);

if (partnerProducts.isSuccess()) {
    List<HashMap<String, Object>> products = partnerProducts.getDataAsList();
    System.out.println("파트너 상품 조회 성공: " + products.size() + "개");
}

// 파트너 주문 관리
BootpayStoreResponse partnerOrders = bootpay
    .asPartner()
    .order.list(null);

if (partnerOrders.isSuccess()) {
    System.out.println("파트너 주문 조회 성공");
}
```

### 시나리오 5: 벤더 작업

```java
// 벤더로 로그인
BootpayStoreResponse vendorLogin = bootpay
    .asVendor()
    .user.login("vendor", "password");

if (vendorLogin.isSuccess()) {
    System.out.println("벤더 로그인 성공");
}

// 벤더 인보이스 관리
BootpayStoreResponse vendorInvoices = bootpay
    .asVendor()
    .invoice.list(null);

if (vendorInvoices.isSuccess()) {
    List<HashMap<String, Object>> invoices = vendorInvoices.getDataAsList();
    System.out.println("벤더 인보이스 조회 성공: " + invoices.size() + "개");
}

// 벤더 상품 관리
BootpayStoreResponse vendorProducts = bootpay
    .asVendor()
    .product.list(null);

if (vendorProducts.isSuccess()) {
    System.out.println("벤더 상품 조회 성공");
}
```

### 시나리오 6: 슈퍼바이저 작업

```java
// 슈퍼바이저로 로그인
BootpayStoreResponse supervisorLogin = bootpay
    .asSupervisor()
    .user.login("supervisor", "password");

if (supervisorLogin.isSuccess()) {
    System.out.println("슈퍼바이저 로그인 성공");
}

// 전체 사용자 관리
BootpayStoreResponse allUsers = bootpay
    .asSupervisor()
    .user.list(null);

if (allUsers.isSuccess()) {
    List<HashMap<String, Object>> users = allUsers.getDataAsList();
    System.out.println("전체 사용자 조회 성공: " + users.size() + "명");
}

// 시스템 전체 관리
BootpayStoreResponse systemManagement = bootpay
    .asSupervisor()
    .order.list(null);

if (systemManagement.isSuccess()) {
    System.out.println("시스템 전체 관리 성공");
}
```

### 시나리오 7: 동일 인스턴스에서 여러 role 사용

```java
BootpayStore bootpay = new BootpayStore(tokenPayload, "PRODUCTION");

// 1. 기본값(user)으로 시스템 접근
BootpayStoreResponse defaultAccess = bootpay
    .user.detail("user123");

if (defaultAccess.isSuccess()) {
    System.out.println("기본 접근 성공");
}

// 2. 매니저로 주문 관리
BootpayStoreResponse managerOrder = bootpay
    .asManager()
    .order.list(null);

if (managerOrder.isSuccess()) {
    System.out.println("매니저 주문 관리 성공");
}

// 3. 파트너로 상품 관리
BootpayStoreResponse partnerProduct = bootpay
    .asPartner()
    .product.list(null);

if (partnerProduct.isSuccess()) {
    System.out.println("파트너 상품 관리 성공");
}

// 4. 벤더로 인보이스 관리
BootpayStoreResponse vendorInvoice = bootpay
    .asVendor()
    .invoice.list(null);

if (vendorInvoice.isSuccess()) {
    System.out.println("벤더 인보이스 관리 성공");
}

// 5. 슈퍼바이저로 사용자 관리
BootpayStoreResponse supervisorUser = bootpay
    .asSupervisor()
    .user.list(null);

if (supervisorUser.isSuccess()) {
    System.out.println("슈퍼바이저 사용자 관리 성공");
}

// 6. role 초기화 (기본값 user로)
bootpay.clearRole();
```

## 우선순위 규칙

1. **RequestContext의 role/token**: 가장 높은 우선순위
2. **BootpayStore의 role/token**: RequestContext가 없을 때 사용
3. **기본값**: role이 설정되지 않은 경우 "user"

## 주의사항

1. **기본 role**: 모든 BootpayStore 인스턴스의 기본 role은 "user"입니다.

2. **직접 접근 금지**: `bootpay.role = "admin"` 같은 직접 필드 접근은 불가능합니다. 반드시 메서드를 통해 role을 설정해야 합니다.

3. **Thread Safety**: 현재 구현은 thread-safe하지 않습니다. 멀티스레드 환경에서는 각 스레드별로 별도의 `BootpayStore` 인스턴스를 사용하거나, role 설정을 API 호출 직전에 수행해야 합니다.

4. **Token 관리**: RequestContext의 token은 "Bearer " 접두사 없이 전달되며, SDK에서 자동으로 추가합니다.

5. **응답 객체 사용**: 모든 API는 `BootpayStoreResponse` 객체를 반환하므로 안전한 타입 캐스팅과 에러 처리가 가능합니다.

## 확장 가능성

향후 다음과 같은 기능을 추가할 수 있습니다:

1. **ThreadLocal 기반 role 관리**: 멀티스레드 환경에서 안전한 role 관리
2. **Role 기반 권한 검증**: SDK 레벨에서 role 유효성 검증
3. **Role 그룹**: 여러 role을 그룹으로 관리
4. **Role 캐싱**: 자주 사용되는 role 조합을 캐싱
5. **Token 자동 갱신**: 토큰 만료 시 자동 갱신 기능

## 마이그레이션 가이드

기존 코드에서 새로운 role 관리 방식을 사용하려면:

### Before (기존 방식 - 더 이상 지원하지 않음)
```java
bootpay.role = "admin"; // ❌ 컴파일 오류
HashMap<String, Object> result = bootpay.user.login("admin", "password");
```

### After (새로운 방식)
```java
// 방법 1: 기본값 사용 (role을 명시하지 않음)
BootpayStoreResponse result = bootpay.user.login("user123", "password"); // 기본값 "user"로 호출

if (result.isSuccess()) {
    HashMap<String, Object> data = result.getDataAsMap();
    System.out.println("로그인 성공");
}

// 방법 2: 메서드 체이닝 (권장)
BootpayStoreResponse result = bootpay
    .asManager()
    .user.login("manager", "password");

if (result.isSuccess()) {
    System.out.println("매니저 로그인 성공");
}

// 방법 3: RequestContext 사용
RequestContext context = RequestContext.builder()
    .role("partner")
    .token("custom_token")
    .build();
BootpayStoreResponse result = bootpay.user.login("partner", "password", context);
```

**중요**: 
- 기존의 `bootpay.role = "admin"` 방식은 더 이상 지원하지 않습니다. 반드시 메서드 체이닝이나 RequestContext를 사용해야 합니다.
- API 호출시 role을 명시하지 않으면 기본값 "user"로 호출됩니다.
- 지원되는 role: user, manager, partner, vendor, supervisor
- 모든 API는 `BootpayStoreResponse` 객체를 반환하므로 안전한 타입 캐스팅과 에러 처리가 가능합니다. 