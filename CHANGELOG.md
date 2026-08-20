### 3.3.0

PG 와 Commerce 의 코드 스타일 통일 + Commerce 청구서/인증 정합성. **기존 표면은 그대로 동작한다.**

#### 통일 API (신규 표면)

- 공통 타입 추가 (`kr.co.bootpay.common`)
  - `BootpayMode` — 환경 enum (DEVELOPMENT / TEST / STAGE / PRODUCTION, 기본 PRODUCTION). 기존 `String devMode` 대체.
  - `BootpayRole` — Commerce `BOOTPAY-ROLE` 헤더 enum (user / manager / partner / vendor / supervisor).
  - `BootpayResponse` — PG/Commerce 공용 응답. `isSuccess()` / `getData()` / `getErrorCode()` / `getMessage()` / `asMap()`. `getData()` 는 응답 본문만 담고 `http_status` 를 제외한다.
- 생성 방식 통일 — `Bootpay.builder()`, `BootpayCommerce.builder()`
  - PG 는 client_key/secret_key 와 application_id/private_key 를 같은 빌더로 생성. 키가 짝을 이루지 않으면 `build()` 에서 즉시 `IllegalStateException`.
  - Commerce 는 `TokenPayload` 래퍼 없이 `clientKey`/`secretKey` 를 직접 지정.
  - 환경 문자열 오타로 Commerce baseUrl 이 null 로 남던 문제 해소.
- PG 모듈 표면 추가 — `bootpay.payment` / `billing` / `auth` / `cash` / `escrow` / `user` / `wallet`. 기존 평면 메서드 31개와 **동일한 HTTP 요청**을 만든다 (테스트로 전수 대조).
- Commerce 진입점 추가 — `BootpayCommerce`. `BootpayStore` 를 상속하지 않고 위임하므로 기존 타입 계층에 영향이 없다. `unwrap()` 으로 내부 인스턴스 접근 가능.
  - 이름 정리: `userLogin`→`mallLogin`, `userSession`→`mallSession`, `userLogout`→`mallLogout`, `userJoin`→`mallJoin`, `userJoinCheck`→`mallJoinCheck`, `product.products`→`product.mallList`, `product.productDetail`→`product.mallDetail`.
  - 중복 별칭 정리: `mallSetting.getMallSetting`/`updateMallSetting` 은 `detail`/`update` 하나로 노출.
  - `subscriptionSetting` 모듈 노출 — 기존 `BootpayStore` 에는 배선 누락으로 도달할 수 없었다.
- 토큰 발급 이름 통일 — 양쪽 모두 `issueAccessToken()` 이 `BootpayResponse` 를 반환. 기존 `getAccessToken()` 은 그대로 유지.

#### Commerce 인증 정합성 (동작 변경)

- `Authorization` 헤더 규칙을 기준 SDK(NodeJS) 및 Ruby / Go / Python / PHP / .NET 과 일치시켰다.
  - **토큰이 발급되어 있으면 `Bearer {token}`**, 없으면 client_key/secret_key `Basic`, 둘 다 없으면 **헤더를 붙이지 않는다** (기존에는 항상 Basic 을 보냈고, 인증 정보가 없으면 빈 문자열을 보냈다).
  - `RequestContext` 의 토큰이 인스턴스 토큰보다 우선한다 (`RequestContext.token` 필드가 그동안 무시되고 있었다).
  - `authorizationHeader()` / `authorizationHeader(RequestContext)` 추가. `requestAccessToken()` 은 Basic 값 계산으로 그대로 유지된다.
  - ⚠️ `getAccessToken()` 을 호출해 토큰을 발급받은 코드는 이제 Basic 이 아니라 Bearer 로 전송된다. 토큰 만료(30분) 시 재발급이 필요하다.

#### Commerce 청구서

- 청구서 생성 파라미터 확장 (ruby SDK `request_checkout` parity). `SInvoice` 에 추가 — 기존 필드·시그니처는 그대로다.
  - `user` (`SInvoiceUser`) — 구매자 정보. 가입 회원이면 `userId` 만으로 충분하고, 비회원 청구서는 `membershipType = "guest"` 와 이름·연락처를 함께 지정한다.
  - `products` (`List<SInvoiceProduct>`) — 등록된 상품을 참조해 청구한다 (`invoiceItems` 는 이름·금액을 직접 적는 기존 방식으로 그대로 유지).
    - `SInvoiceProduct`: `productId` / `productOptionId` / `duration` / `quantity` / `priceAdjustments`
    - `SInvoicePriceAdjustment`: `priceAdjustmentId` / `startAt` / `endAt` / `name` / `cycles`
    - `SInvoicePriceAdjustmentCycle`: `duration` / `adjustmentType` / `name` / `value` / `minValue` / `maxValue` (`discount_percent` · `discount_price` · `setup_fee` 상수 제공)
  - `deliveryPrice`, `useNotification`, `useAutoLogin`, `usageApiUrl`, `sdk`
  - `extra` (`SInvoiceExtra`) — `separatelyConfirmed` / `createOrderImmediately`
- `invoice.create` 에 `Idempotency-Key` 헤더와 user role 부착 (list/detail/notify 와 동일한 규약, ruby SDK 와 parity). `create(invoice, idempotencyKey)` 오버로드 추가.

#### 브랜치 통합 (2-x-development)

- `main` 을 `2-x-development` 로 통합하고 `2-x-development` 를 기준 브랜치로 삼는다 (nodejs · ruby 와 동일).
- `orderSubscription.supervisorTerminate(orderSubscriptionId, SupervisorTerminateParams)` 추가 — 기존 `terminate(id[, reason])` 는 그대로 두고, 위약금·마지막 청구 환불액·최종 정산액·서비스 종료일·해지 기준일까지 지정할 수 있다.
- `OrderSubscriptionRequestUpdateParams` 에 정산 필드 추가 (`price` / `taxFreePrice` / `terminationFee` / `lastBillRefundPrice` / `finalFee` / `serviceEndAt`) 및 `APPROVAL_APPROVE` / `APPROVAL_REJECT` 상수. 서비스가 body 에 실어 전송하도록 배선.
- `SUserJoinService` 에 중복확인 key 상수 추가 (`EMAIL_EXIST` / `ID_EXIST` / `PHONE_EXIST` / `UID_EXIST` / `GROUP_BUSINESS_NUMBER_EXIST`).

#### 테스트

- 신규 표면과 기존 표면이 같은 요청(method / path / query / body / role 헤더)을 만드는지 대조하는 동등성 테스트 (`PgModuleParityTest`, `CommerceModuleParityTest`), 기존 표면 회귀 테스트 (`UnifiedSurfaceTest`), 인증 규칙 테스트 (`BootpayStoreObjectAuthTest`). 전부 네트워크 불필요.

### 3.2.0

NodeJS SDK 2.9.0 과 기능 동등(parity).

- PG: `lookupSequentialBillingKey(widgetKey, billingKey, userId)` 추가 — `GET subscribe/sequential_billing_key/{billing_key}?widget_key={widget_key}&user_id={user_id}` (우선순위/순차 결제 빌링키 조회).
- Commerce: 몰 설정 모듈 `mallSetting` 추가 (supervisor 전용) — `getMallSetting`/`detail`: `GET mall-setting`, `updateMallSetting`/`update`: `PUT mall-setting` (flatten 바디, null 값 미전송). `SMallSetting` pojo 에 parity 필드 추가 (`addr_1`/`addr_2`, 서버 오타 필드 `use_oder_cancel_approval` 직렬화 정정 포함).
- Commerce: `webhook.sendTest([headerContentType][, idempotencyKey])` 추가 — `POST webhook/test`.
- Commerce: 수시결제(온디맨드) charge_key 결제/해지 추가 (supervisor 전용)
  - `orderSubscription.supervisorCharge(SupervisorChargeParams)`: `POST order_subscriptions/charge` — charge_key 는 body 로만 전송 (URL/query 금지)
  - `orderSubscription.supervisorChargeRevoke(SupervisorChargeRevokeParams)`: `DELETE order_subscriptions/charge` — 해지 후 해당 키로 재결제 불가
- Commerce: V1 Mall 회원 endpoint 추가 (복수형 `users/...` 경로) — `user.userLogin`, `user.userSession`, `user.userLogout`, `user.userJoin(MallUserJoinParams)`, `user.userJoinCheck(type, pk)`, `user.uidExist(uid)`. 세션 호출은 회원 JWT 를 `Bootpay-User-JWT` 헤더로 전달 (값이 있을 때만 부착).
- Commerce: 상품 조회 Mall API — `product.products(MallProductListParams)` (`page`/`limit` 기본 1/20, `category_id`/`sort`/`user_jwt` 지원), `product.productDetail(productId, userJwt)`.
- Commerce: `product.create` 는 이미지가 없으면 JSON, 있으면 multipart 로 전송. multipart 이미지 필드를 `images[0]`, `images[1]` … 인덱싱으로 정정 (Rails 는 반복된 `images` 를 배열로 받지 않는다).
- Commerce: `orderSubscription.requestIng.purchase`(중도인수) / `requestIng.transfer`(이전·승계) 추가 — `POST order_subscriptions/requests/ing/{purchase,transfer}`.
- Commerce: 인자·요청 규약 정정
  - `invoice.list(InvoiceListParams)` 오버로드 추가 — `limit` 기본값 24, `cs_type`/`user_id`/`product_type`/`css_at`/`cse_at` 지원. 응답은 `{ list, count }` 구조 (`{ items, total }` 아님).
  - `invoice.notify` 의 sendTypes 선택화 — `notify(invoiceId)` 오버로드 추가.
  - `orderCancel` approve/reject 인자명을 `orderCancellationRequestId` 로 통일 (구 이름 `orderCancelRequestHistoryId` 도 계속 동작).
  - `orderSubscriptionAdjustment.delete` 는 대상 ID 를 query 가 아니라 body 로 전송.
  - `order.list` 에 `searchDateFrom`/`searchDateTo` 추가 (`cssAt`/`cseAt` 는 서버 별칭으로 계속 지원).
  - `orderSubscription.list` 에 `searchDateFrom`/`searchDateTo` 추가, 기존 `status` 파라미터 실제 전송 배선.
  - `orderSubscriptionRequest.list` 에 `orderSubscriptionId`/`userId`/`userGroupId` 추가, `page`/`limit` 기본 1/20.
- Commerce: 서버가 요구하는 scope 를 endpoint 별로 명시 — 상품 쓰기/그룹 한도는 `manager`, 구독 계약변경·조정항목·요청 승인·charge·mallSetting 은 `supervisor`, 나머지는 `user`. `orderSubscriptionRequest.list`/`detail` 은 `project_id` 가 있으면 `supervisor`, 없으면 `user`.
- Commerce: `Idempotency-Key` 헤더 자동 생성(UUID) 지원 — store 조회, invoice, mall 회원/상품, requests/ing, 조정항목, charge, mallSetting, webhook 등에 부착. 각 API 의 `idempotencyKey` 인자로 직접 지정 가능. `RequestContext` 에 `idempotencyKey`/`userJwt` 필드 추가.
- Commerce: `orderSubscriptionBill.list` parity — `page`/`limit` 기본 1/20 상시 전송, user scope + `Idempotency-Key` 부착 (`idempotencyKey` 필드로 직접 지정 가능).
- Commerce: `requestIng.calculateTerminationFee` 로직 결함 수정 — `order_subscription_id` 와 `order_number` 를 동시에 지정하면 둘 다 전송한다 (기존에는 `order_number` 가 조용히 유실됐다).
- Commerce: 파라미터 모델 optional 필드 추가 — `OrderSubscriptionUpdateParams` 에 `nextBillingAt`/`billingKey`/`status`/`paymentNextAt`, `OrderSubscriptionResumeParams` 에 `resumeAt`.
- 테스트: 라이브 테스트 env 게이트 도입 — `BOOTPAY_ENV=development` 또는 로컬 base URL 오버라이드(`BOOTPAY_PG_BASE_URL`/`BOOTPAY_COMMERCE_BASE_URL`)가 없으면 라이브 테스트를 skip 하여 production 실서버 호출을 차단.

### 3.1.0
- 인증: client_key/secret_key Basic Auth 지원 (PG + Commerce 공통).
  - 기존 application_id/private_key Bearer 방식 하위 호환 유지.
  - PG: `Bootpay.withClientKey(clientKey, secretKey [, devMode])` 팩토리 추가.
    - ck/sk 모드에서는 매 요청 자동 Basic Auth 헤더 부착 — `getAccessToken()` 은 합성 응답을 반환하며 `request/token` 호출이 발생하지 않음.
    - 한쪽만 지정 + legacy 키도 없으면 검증 실패.
  - Commerce: `BootpayStore` 의 모든 호출이 `tokenPayload` 의 ck/sk 로 Basic Auth 사용.
  - `BootpayObject.hasAuth()` / `getBasicAuthValue()` 헬퍼 추가 — 서비스 계층의 인증 전제 검사를 통합.
- Wallet API (`requestWalletPayment`, `WalletPayment`) `@Deprecated` 표시 — 다음 메이저 버전에서 제거 예정.
- `ResDefault.http_status`, `BootpayStoreResponse.getHttpStatus()` `@Deprecated` 표시 — 다음 메이저 버전에서 제거 예정 (성공 여부는 `error_code == 0` / `isSuccess()`).
- 응답 파싱 견고성 개선:
  - `responseToJsonArray` — 서버가 객체로 답해도 그대로 노출 (e.g. lookup/order 오류 응답).
  - `responseToJson` — 서버가 배열로 답해도 `data` 로 wrapping (e.g. wallet 목록).
  - `STokenService` — 응답 스트림 1회 read 로 통합 (이중 read 시 closed-stream 예외 수정).
- 테스트 인프라: `.env` / `BOOTPAY_AUTH_MODE=new|legacy` / `BOOTPAY_ENV` 토글로 ck/sk · legacy 양쪽 검증, 테스트 디렉터리 `pg/`·`commerce/` 분리.
- PG: `getReceipt(receiptId, lookupUserData)` 오버로드 추가 — NodeJS `receipt(receiptId, lookupUserData)` 와 parity. 기존 `getReceipt(receiptId)` 는 내부적으로 `lookupUserData=false` 위임 (백워드 호환).

### 3.0.5
- orderSubscription.terminate 메서드 추가 (관리자 직접 구독 해지)

### 3.0.4
- user.token API에 corporate_type, membership_type 파라미터 추가

### 3.0.3
- 내부 로직 개선

### 3.0.2
- store api 여러 기능 추가 
- 
### 3.0.1
- store api 사용법 변경
- store 주문관리 api 추가 

### 3.0.0
- store api 추가 

### 2.4.4
- bankCode 신한 추가 

### 2.4.3
- gradle 6.8 지원하도록 다시 다운그레이드 

### 2.4.2
- 지갑 조회 API 추가
 

### 2.4.0
- 지갑으로 결제 요청하기 추가 

### 2.3.3
- SDK Version 버그 패치 

### 2.3.2
- refund.bankCode 칼럼명 카멜케이스로 변경 

### 2.3.1
- 배송등록 api 필드 추가 

### 2.3.0
- 빌링키로 빌링키 조회 api 추가 

### 2.2.6
- validation message 변경 

### 2.2.4
- 계좌 자동 결제를 위한 api 추가 
- 
### 2.2.3
- java 1.8 버전으로 down 

### 2.2.2
- orderId 로 주문조회 추가 
- 본인인증 authenticate_type 필드 추가 

### 2.2.1
- 예약결제 조회 API 추가 

### 2.2.0
- json parser 교체, int가 double로 리턴되던 현상 수정 

### 2.1.4
- REST API 본인인증 추가 - 오탈자 수정 

### 2.1.3
- 2.1.3 REST API 본인인증 추가

### 2.1.2
- 2.1.1 패치는 크리티컬 에러로 패치 실패한 버전임, 에러 수정 후 재배포 

### 2.1.1
- 활성화된 결제수단 가져오기 api 추가 

### 2.1.0
- return 타입에 http_status 추가 
- 현금영수증 API 추가 

### 2.0.9
- lookupBillingKey API 추가

### 2.0.8
- escrow api support, readme update

### 2.0.7
- shipping model field added

### 2.0.6
- escrow api 업데이트

### 2.0.5
- v1 -> v2 모델 업데이트
 
### 2.0.4
- v1 -> v2 모델 업데이트

### 2.0.3
- v1 -> v2 모델 업데이트 

### 2.0.2
- 에스크로, enum 추가 

### 2.0.1
- jdk 11 -> 8로 다운그레이드 후 배포 

### 2.0.0
- bootpay api v2 배포 

### 1.0.9
- data response format이 대부분 hashmap으로 수정되었습니다.

### 1.0.8
- subscribe payload model이 수정되었습니다 

### 1.0.7
- Extra 모델이 수정되었습니다 

### 1.0.6
- 모델별로 주석이 추가되었습니다 

### 1.0.5 
- 본인인증 api data type이 application/json으로 변경되었습니다.  

### 1.0.4
- 본인인증 model이 업데이트 되었습니다

### 1.0.3
- response model이 업데이트 되었습니다 

### 1.0.2
- artifact id가 변경되었습니다 

### 1.0.1
- 예제 코드가 업데이트 되었습니다 

### 1.0.0
- first release  
