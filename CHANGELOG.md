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
