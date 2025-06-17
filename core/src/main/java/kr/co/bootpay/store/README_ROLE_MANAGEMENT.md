# Bootpay Store SDK - Role 관리 가이드

## 개요

Bootpay Store SDK는 API 호출 시 role 기반 권한 관리를 지원합니다. 기본값은 "user"로 설정되어 있으며, API 호출 시 명시적으로 role을 지정할 수 있습니다.

## 주요 특징

- **기본값**: 모든 API 호출은 기본적으로 "user" role로 실행됩니다.
- **메서드 체이닝**: `.asUser()`, `.asManager()` 등의 메서드를 통해 role을 지정할 수 있습니다.
- **토큰 발급**: 토큰은 API를 통해 발급받아야 하며, 직접 설정할 수 없습니다.
- **5가지 Role**: user, manager, partner, vendor, supervisor를 지원합니다.

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
HashMap<String, Object> result = bootpay.user.login("user123", "password123");
```

## Role 지정 방법

### 5가지 기본 Role

```java
// 1. 일반 사용자
HashMap<String, Object> userResult = bootpay
    .asUser()
    .user.login("user123", "password123");

// 2. 매니저
HashMap<String, Object> managerResult = bootpay
    .asManager()
    .order.list(null);

// 3. 파트너
HashMap<String, Object> partnerResult = bootpay
    .asPartner()
    .product.list(null);

// 4. 벤더
HashMap<String, Object> vendorResult = bootpay
    .asVendor()
    .invoice.list(null);

// 5. 슈퍼바이저
HashMap<String, Object> supervisorResult = bootpay
    .asSupervisor()
    .user.list(null);
```

### 커스텀 Role

```java
// 커스텀 role 지정
HashMap<String, Object> customResult = bootpay
    .withRole("custom_role")
    .product.list(null);
```

### Role 초기화

```java
// role을 기본값 "user"로 초기화
bootpay.clearRole();

// 초기화 후 API 호출 (기본값 "user"로 호출)
HashMap<String, Object> result = bootpay.user.detail("user123");
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
HashMap<String, Object> defaultLogin = bootpay
    .user.login("user123", "password123");

// 2. 매니저로 주문 관리
HashMap<String, Object> managerOrder = bootpay
    .asManager()
    .order.list(null);

// 3. 파트너로 상품 관리
HashMap<String, Object> partnerProduct = bootpay
    .asPartner()
    .product.list(null);

// 4. 벤더로 인보이스 관리
HashMap<String, Object> vendorInvoice = bootpay
    .asVendor()
    .invoice.list(null);

// 5. 슈퍼바이저로 사용자 관리
HashMap<String, Object> supervisorUser = bootpay
    .asSupervisor()
    .user.list(null);

// 6. role 초기화 후 기본값으로 API 호출
bootpay.clearRole();
HashMap<String, Object> afterClearResult = bootpay
    .user.detail("user123");
```

## 구조 변경사항

### BootpayStoreObject 클래스

- `token` 필드를 `private`으로 변경하여 직접 접근 차단
- `setToken()` 메서드 제거 (API를 통해서만 토큰 설정 가능)
- `getToken()` 메서드 추가 (읽기 전용 접근)

### BootpayStore 클래스

- `withToken()` 메서드 추가 (API를 통해 토큰 발급)
- `getCurrentToken()` 메서드 추가 (현재 토큰 확인)
- `hasToken()` 메서드 추가 (토큰 발급 여부 확인)

## 주의사항

1. **토큰 직접 설정 불가**: 토큰은 반드시 API를 통해 발급받아야 합니다.
2. **기본값 자동 적용**: role을 명시하지 않으면 기본값 "user"가 자동으로 적용됩니다.
3. **메서드 체이닝**: role 지정은 메서드 체이닝 방식으로만 가능합니다.
4. **토큰 발급 필수**: API 호출 전에 반드시 `withToken()`을 호출하여 토큰을 발급받아야 합니다.

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
HashMap<String, Object> defaultLogin = bootpay
    .user.login("user123", "password123"); // 기본값 "user"로 호출

// 기본값(user)으로 사용자 정보 조회
HashMap<String, Object> userDetail = bootpay
    .user.detail("user123"); // 기본값 "user"로 호출

// 기본값(user)으로 주문 조회
HashMap<String, Object> userOrders = bootpay
    .order.list(null); // 기본값 "user"로 호출
```

### 시나리오 2: 5가지 role로 API 호출

```java
BootpayStore bootpay = new BootpayStore(tokenPayload, "PRODUCTION");

// 1. 일반 사용자 권한
HashMap<String, Object> userResult = bootpay
    .asUser()
    .user.detail("user123");

// 2. 매니저 권한
HashMap<String, Object> managerResult = bootpay
    .asManager()
    .order.list(null);

// 3. 파트너 권한
HashMap<String, Object> partnerResult = bootpay
    .asPartner()
    .product.list(null);

// 4. 벤더 권한
HashMap<String, Object> vendorResult = bootpay
    .asVendor()
    .invoice.list(null);

// 5. 슈퍼바이저 권한
HashMap<String, Object> supervisorResult = bootpay
    .asSupervisor()
    .user.list(null);
```

### 시나리오 3: 매니저 작업

```java
// 매니저로 로그인
HashMap<String, Object> managerLogin = bootpay
    .asManager()
    .user.login("manager", "password");

// 담당 팀의 주문 조회
HashMap<String, Object> teamOrders = bootpay
    .asManager()
    .order.list(null);

// 주문 취소 요청 승인
HashMap<String, Object> approveCancel = bootpay
    .asManager()
    .orderCancel.approve(cancelParams);
```

### 시나리오 4: 파트너 작업

```java
// 파트너로 로그인
HashMap<String, Object> partnerLogin = bootpay
    .asPartner()
    .user.login("partner", "password");

// 파트너 상품 관리
HashMap<String, Object> partnerProducts = bootpay
    .asPartner()
    .product.list(null);

// 파트너 주문 관리
HashMap<String, Object> partnerOrders = bootpay
    .asPartner()
    .order.list(null);
```

### 시나리오 5: 벤더 작업

```java
// 벤더로 로그인
HashMap<String, Object> vendorLogin = bootpay
    .asVendor()
    .user.login("vendor", "password");

// 벤더 인보이스 관리
HashMap<String, Object> vendorInvoices = bootpay
    .asVendor()
    .invoice.list(null);

// 벤더 상품 관리
HashMap<String, Object> vendorProducts = bootpay
    .asVendor()
    .product.list(null);
```

### 시나리오 6: 슈퍼바이저 작업

```java
// 슈퍼바이저로 로그인
HashMap<String, Object> supervisorLogin = bootpay
    .asSupervisor()
    .user.login("supervisor", "password");

// 전체 사용자 관리
HashMap<String, Object> allUsers = bootpay
    .asSupervisor()
    .user.list(null);

// 시스템 전체 관리
HashMap<String, Object> systemManagement = bootpay
    .asSupervisor()
    .order.list(null);
```

### 시나리오 7: 동일 인스턴스에서 여러 role 사용

```java
BootpayStore bootpay = new BootpayStore(tokenPayload, "PRODUCTION");

// 1. 기본값(user)으로 시스템 접근
HashMap<String, Object> defaultAccess = bootpay
    .user.detail("user123");

// 2. 매니저로 주문 관리
HashMap<String, Object> managerOrder = bootpay
    .asManager()
    .order.list(null);

// 3. 파트너로 상품 관리
HashMap<String, Object> partnerProduct = bootpay
    .asPartner()
    .product.list(null);

// 4. 벤더로 인보이스 관리
HashMap<String, Object> vendorInvoice = bootpay
    .asVendor()
    .invoice.list(null);

// 5. 슈퍼바이저로 사용자 관리
HashMap<String, Object> supervisorUser = bootpay
    .asSupervisor()
    .user.list(null);

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
HashMap<String, Object> result = bootpay.user.login("user123", "password"); // 기본값 "user"로 호출

// 방법 2: 메서드 체이닝 (권장)
HashMap<String, Object> result = bootpay
    .asManager()
    .user.login("manager", "password");

// 방법 3: RequestContext 사용
RequestContext context = RequestContext.builder()
    .role("partner")
    .token("custom_token")
    .build();
HashMap<String, Object> result = bootpay.user.login("partner", "password", context);
```

**중요**: 
- 기존의 `bootpay.role = "admin"` 방식은 더 이상 지원하지 않습니다. 반드시 메서드 체이닝이나 RequestContext를 사용해야 합니다.
- API 호출시 role을 명시하지 않으면 기본값 "user"로 호출됩니다.
- 지원되는 role: user, manager, partner, vendor, supervisor 