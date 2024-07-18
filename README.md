
## Bootpay Java Server Side Library
부트페이 공식 Java 라이브러리 입니다 (서버사이드 용)

java언어로 작성된 어플리케이션, 프레임워크 등에서 사용가능합니다.

* PG 결제창 연동은 클라이언트 라이브러리에서 수행됩니다. (Javascript, Android, iOS, React Native, Flutter 등)
* 결제 검증 및 취소, 빌링키 발급, 본인인증 등의 수행은 서버사이드에서 진행됩니다. (Java, PHP, Python, Ruby, Node.js, Go, ASP.NET 등)


- [사용하기](#사용하기)
   - [1. 토큰 발급](#1-토큰-발급)
   - [2. 결제 단건 조회](#2-결제-단건-조회)
   - [3. 결제 취소 (전액 취소 / 부분 취소)](#3-결제-취소-전액-취소--부분-취소)
   - [4. 자동/빌링/정기 결제](#4-자동빌링정기-결제)
      - [4-1. 카드 빌링키 발급](#4-1-카드-빌링키-발급)
      - [4-2. 계좌 빌링키 발급](#4-2-계좌-빌링키-발급)
      - [4-3. 결제 요청하기](#4-3-결제-요청하기)
      - [4-4. 결제 예약하기](#4-4-결제-예약하기)
      - [4-5. 예약 조회하기](#4-5-예약-조회하기)
      - [4-6. 예약 취소하기](#4-6-예약-취소하기)
      - [4-7. 빌링키 삭제하기](#4-7-빌링키-삭제하기)
      - [4-8. 빌링키 조회하기](#4-8-빌링키-조회하기)
   - [5. 회원 토큰 발급요청](#5-회원-토큰-발급요청)
   - [6. 서버 승인 요청](#6-서버-승인-요청)
   - [7. 본인 인증 결과 조회](#7-본인-인증-결과-조회)
   - [8. 에스크로 이용시 PG사로 배송정보 보내기](#8-에스크로-이용시-pg사로-배송정보-보내기)
   - [9-1. 현금영수증 발행하기](#9-1-현금영수증-발행하기)
   - [9-2. 현금영수증 발행 취소](#9-2-현금영수증-발행-취소)
   - [9-3. 별건 현금영수증 발행](#9-3-별건-현금영수증-발행)
   - [9-4. 별건 현금영수증 발행 취소](#9-4-별건-현금영수증-발행-취소)
- [Example 프로젝트](#example-프로젝트)
- [Documentation](#documentation)
- [기술문의](#기술문의)
- [License](#license)


## Gradle로 설치하기   

build.gradle (project)
```
dependencies {
    implementation 'io.github.bootpay:backend:+' // + 는 최신버전을 의미 
}
```

# 사용하기 
BootpayExample.java
```java 
import com.google.gson.Gson;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.BankCode;
import kr.co.bootpay.model.request.*;
import kr.co.bootpay.model.response.ResDefault;
import kr.co.bootpay.model.response.data.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;


public class BootpayExample {
    static Bootpay bootpay;
    public static void main(String[] args) {
        bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
        goGetToken();
    }
    
     public static void goGetToken() {
        try {
            HashMap<String, Object> res = bootpay.getAccessToken();
            if(res.get("error_code") == null) { //success
                System.out.println("goGetToken success: " + res);
            } else {
                System.out.println("goGetToken false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
함수 단위의 샘플 코드는 [이곳](https://github.com/bootpay/backend-java/blob/main/src/test/java/BootpayExample.java)을 참조하세요.

## 1. 토큰 발급 

부트페이와 서버간 통신을 하기 위해서는 부트페이 서버로부터 토큰을 발급받아야 합니다.  
발급된 토큰은 30분간 유효하며, 최초 발급일로부터 30분이 지날 경우 토큰 발급 함수를 재호출 해주셔야 합니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
try {
   HashMap<String, Object> res = bootpay.getAccessToken();
   if(res.get("error_code") == null) { //success
       System.out.println("goGetToken success: " + res);
   } else {
       System.out.println("goGetToken false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 2. 결제 단건 조회
결제창 및 정기결제에서 승인/취소된 결제건에 대하여 올바른 결제건인지 서버간 통신으로 결제검증을 합니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

try {
   HashMap<String, Object> res = bootpay.getReceipt(receiptId);
   if(res.get("error_code") == null) { //success
       System.out.println("getReceipt success: " + res);
   } else {
       System.out.println("getReceipt false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 3. 결제 취소 (전액 취소 / 부분 취소)
price를 지정하지 않으면 전액취소 됩니다. 
* 휴대폰 결제의 경우 이월될 경우 이통사 정책상 취소되지 않습니다
* 정산받으실 금액보다 취소금액이 클 경우 PG사 정책상 취소되지 않을 수 있습니다. 이때 PG사에 문의하시면 되겠습니다.
* 가상계좌의 경우 CMS 특약이 되어있지 않으면 취소되지 않습니다. 그러므로 결제 테스트시에는 가상계좌로 테스트 하지 않길 추천합니다. 

부분취는 카드로 결제된 건만 가능하며, 일부 PG사만 지원합니다. 요청시 price에 금액을 지정하시면 되겠습니다. 
* (지원가능 PG사: 이니시스, kcp, 다날, 페이레터, 나이스페이, 카카오페이, 페이코)

간혹 개발사에서 실수로 여러번 부분취소를 보내서 여러번 취소되는 경우가 있기때문에, 부트페이에서는 부분취소 중복 요청을 막기 위해 cancel_id 라는 필드를 추가했습니다. cancel_id를 지정하시면, 해당 건에 대해 중복 요청방지가 가능합니다.  
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

Cancel cancel = new Cancel();
cancel.receiptId = "628b2206d01c7e00209b6087";
cancel.name = "관리자";
cancel.reason = "테스트 결제";
//        cancel.price = 1000.0; //부분취소 요청시
//        cancel.cancelId = "12342134"; //부분취소 요청시, 중복 부분취소 요청하는 실수를 방지하고자 할때 지정
//        RefundData refund = new RefundData(); // 가상계좌 환불 요청시, 단 CMS 특약이 되어있어야만 환불요청이 가능하다.
//        refund.account = "675601012341234"; //환불계좌
//        refund.accountholder = "홍길동"; //환불계좌주
//        refund.bankcode = BankCode.getCode("국민은행");//은행코드
//        cancel.refund = refund;

try {
   HashMap<String, Object> res = bootpay.receiptCancel(cancel);
   if(res.get("error_code") == null) { //success
       System.out.println("receiptCancel success: " + res);
   } else {
       System.out.println("receiptCancel false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 4. 자동/빌링/정기 결제
## 4-1. 카드 빌링키 발급
REST API 방식으로 고객으로부터 카드 정보를 전달하여, PG사에게 빌링키를 발급받을 수 있습니다.
발급받은 빌링키를 저장하고 있다가, 원하는 시점, 원하는 금액에 결제 승인 요청하여 좀 더 자유로운 결제시나리오에 적용이 가능합니다.
* 비인증 정기결제(REST API) 방식을 지원하는 PG사만 사용 가능합니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

Subscribe subscribe = new Subscribe();
subscribe.orderName = "정기결제 테스트 아이템";
subscribe.subscriptionId = "" + (System.currentTimeMillis() / 1000);
subscribe.pg = "payapp";
subscribe.cardNo = "5570**********1074"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
subscribe.cardPw = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
subscribe.cardExpireYear = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
subscribe.cardExpireMonth = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
subscribe.cardIdentityNo = ""; //생년월일 또는 사업자 등록번호 (- 없이 입력)


subscribe.user = new User();
subscribe.user.username = "홍길동";
subscribe.user.phone = "01011112222";

try {
   HashMap<String, Object> res = bootpay.getBillingKey(subscribe);
   JSONObject json =  new JSONObject(res);
   System.out.printf( "JSON: %s", json);
   
   if(res.get("error_code") == null) { //success
       System.out.println("getBillingKey success: " + res);
   } else {
       System.out.println("getBillingKey false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```
## 4-2. 계좌 빌링키 발급
REST API 방식으로 고객의 계좌 정보를 전달하여, PG사에게 빌링키 발급을 요청합니다. 요청 후 빌링키가 바로 발급되진 않고, 출금동의 확인 절차까지 진행해야 빌링키가 발급됩니다.
먼저 빌링키를 요청합니다.
```java
public static void getBillingKeyTransfer() {
     try {
         Subscribe subscribe = new Subscribe();
         subscribe.orderName = "테스트 결제";

         subscribe.pg = "나이스페이";
         subscribe.bankName = "국민";
         subscribe.bankAccount = "67512341234472";
         subscribe.username = "홍길동";
         subscribe.identityNo = "901014";
         subscribe.phone = "01012341234";
         subscribe.subscriptionId = "" + (System.currentTimeMillis() / 1000);
//            subscribe.tax

         HashMap<String, Object> res = bootpay.getBillingKeyTransfer(subscribe);
         if(res.get("error_code") == null) { //success
             System.out.println("success: " + res);
         } else {
             System.out.println("false: " + res);
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
```

이후 빌링키 발급 요청시 응답받은 receipt_id로, 출금 동의 확인을 요청합니다.
```java
public static void publishBillingKeyTransfer() {
     try {
         HashMap<String, Object> res = bootpay.publishBillingKeyTransfer("66541bc4ca4517e69343e24c");
         if(res.get("error_code") == null) { //success
             System.out.println("success: " + res);

         } else {
             System.out.println("false: " + res);
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
```

## 4-3. 결제 요청하기
발급된 빌링키로 원하는 시점에 원하는 금액으로 결제 승인 요청을 할 수 있습니다. 잔액이 부족하거나 도난 카드 등의 특별한 건이 아니면 PG사에서 결제를 바로 승인합니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

SubscribePayload payload = new SubscribePayload();
payload.billingKey = "628b2644d01c7e00209b6092";
payload.orderName = "아이템01";
payload.price = 1000;
payload.user = new User();
payload.user.phone = "01012345678";
payload.orderId = "" + (System.currentTimeMillis() / 1000);

try {
   HashMap<String, Object> res = bootpay.requestSubscribe(payload);
   JSONObject json =  new JSONObject(res);
   System.out.printf( "JSON: %s", json);
   
   if(res.get("error_code") == null) { //success
       System.out.println("requestSubscribe success: " + res);
   } else {
       System.out.println("requestSubscribe false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```
## 4-4. 결제 예약하기
원하는 시점에 4-1로 결제 승인 요청을 보내도 되지만, 빌링키 발급 이후에 바로 결제 예약 할 수 있습니다. (빌링키당 최대 10건)
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

SubscribePayload payload = new SubscribePayload(); 
payload.billingKey = "628b2644d01c7e00209b6092";
payload.orderName = "아이템01";
payload.price = 1000;
payload.orderId = "" + (System.currentTimeMillis() / 1000);

Date now = new Date();
now.setTime(now.getTime() + 10 * 1000); //10초 뒤 결제
//
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss XXX");
sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
payload.reserveExecuteAt = sdf.format(now); // 결제 승인 시점

try {
   HashMap<String, Object> res = bootpay.reserveSubscribe(payload);
   if(res.get("error_code") == null) { //success
       System.out.println("reserveSubscribe success: " + res);
   } else {
       System.out.println("reserveSubscribe false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 4-5. 예약 조회하기 
예약시 응답받은 reserveId로 예약된 건을 조회합니다.
```java 
String reserveId = "6490149ca575b40024f0b70d";
try {
   HashMap<String, Object> res = bootpay.reserveSubscribeLookup(reserveId);
   JSONObject json =  new JSONObject(res);
   System.out.printf( "JSON: %s", json);
   if(res.get("error_code") == null) { //success
       System.out.println("getReceipt success: " + res);
   } else {
       System.out.println("getReceipt false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```


## 4-6. 예약 취소하기
예약시 응답받은 reserveId로 예약된 건을 취소합니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

String reserveId = "628b316cd01c7e00219b6081";
try {
        HashMap<String, Object> res = bootpay.reserveCancelSubscribe(reserveId);
        if(res.get("error_code") == null) { //success
            System.out.println("reserveCancelSubscribe success: " + res);
        } else {
            System.out.println("reserveCancelSubscribe false: " + res);
        }
} catch (Exception e) {
    e.printStackTrace();
}
```


## 4-7. 빌링키 삭제
발급된 빌링키를 삭제합니다. 
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

String receiptId = "628b2644d01c7e00209b6092";
try {
   HashMap<String, Object> res = bootpay.destroyBillingKey(receiptId);
   JSONObject json =  new JSONObject(res);
   System.out.printf( "JSON: %s", json);
   if(res.get("error_code") == null) { //success
       System.out.println("destroyBillingKey success: " + res);
   } else {
       System.out.println("destroyBillingKey false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```


## 4-8. 빌링키 조회
클라이언트에서 빌링키 발급시, 보안상 클라이언트 이벤트에 빌링키를 전달해주지 않습니다. 그러므로 이 API를 통해 조회해야 합니다.
다음은 빌링키 발급 요청했던 receiptId 로 빌링키를 조회합니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

String receiptId = "62c7ccebcf9f6d001b3adcd4";
try {
   HashMap<String, Object> res = bootpay.lookupBillingKey(receiptId);
   JSONObject json =  new JSONObject(res);
   System.out.printf( "JSON: %s", json);
   if(res.get("error_code") == null) { //success
       System.out.println("getReceipt success: " + res);
   } else {
       System.out.println("getReceipt false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

아래는 billingKey로 조회합니다.
```java 
String billingKey = "66542dfb4d18d5fc7b43e1b6";
try {
   HashMap<String, Object> res = bootpay.lookupBillingKeyByKey(billingKey);
   JSONObject json =  new JSONObject(res);
   System.out.printf( "JSON: %s", json);
   if(res.get("error_code") == null) { //success
       System.out.println("success: " + res);
   } else {
       System.out.println("false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 5. 구매자 토큰 발급 
(부트페이 단독) 부트페이에서 제공하는 간편결제창, 생체인증 기반의 결제 사용을 위해서는 개발사에서 회원 고유번호를 관리해야하며, 해당 회원에 대한 사용자 토큰을 발급합니다.
이 토큰값을 기반으로 클라이언트에서 결제요청 하시면 되겠습니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

UserToken userToken = new UserToken();
userToken.userId = "1234"; // 개발사에서 관리하는 회원 고유 번호
try {
   HashMap<String, Object> res = bootpay.getUserToken(userToken);
   if(res.get("error_code") == null) { //success
       System.out.println("getUserToken success: " + res);
   } else {
       System.out.println("getUserToken false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
``` 

## 6. 서버 승인 요청 
결제승인 방식은 클라이언트 승인 방식과, 서버 승인 방식으로 총 2가지가 있습니다.

클라이언트 승인 방식은 javascript나 native 등에서 confirm 함수에서 진행하는 일반적인 방법입니다만, 경우에 따라 서버 승인 방식이 필요할 수 있습니다.

필요한 이유 
1. 100% 안정적인 결제 후 고객 안내를 위해 - 클라이언트에서 PG결제 진행 후 승인 완료될 때 onDone이 수행되지 않아 (인터넷 환경 등), 결제 이후 고객에게 안내하지 못할 수 있습니다  
2. 단일 트랜잭션의 개념이 필요할 경우 - 재고파악이 중요한 커머스를 운영할 경우 트랜잭션 개념이 필요할 수 있겠으며, 이를 위해서는 서버 승인을 사용해야 합니다. 

```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

String receiptId = "62876963d01c7e00209b6028";
try {
   HashMap<String, Object> res = bootpay.confirm(receiptId);
   if(res.get("error_code") == null) { //success
       System.out.println("confirm success: " + res);
   } else {
       System.out.println("confirm false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 7. 본인 인증 결과 조회 
다날 본인인증 후 결과값을 조회합니다. 
다날 본인인증에서 통신사, 외국인여부, 전화번호 이 3가지 정보는 다날에 추가로 요청하셔야 받으실 수 있습니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

try {
   HashMap<String, Object> res = bootpay.certificate(receiptId);
   if(res.get("error_code") == null) { //success
       System.out.println("certificate success: " + res);
   } else {
       System.out.println("certificate false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

8. (에스크로 이용시) PG사로 배송정보 보내기
   현금 거래에 한해 구매자의 안전거래를 보장하는 방법으로, 판매자와 구매자의 온라인 전자상거래가 원활하게 이루어질 수 있도록 중계해주는 매매보호서비스입니다. 국내법에 따라 전자상거래에서 반드시 적용이 되어 있어야합니다. PG에서도 에스크로 결제를 지원하며, 에스크로 결제 사용을 원하시면 PG사 가맹시에 에스크로결제를 미리 얘기하고나서 진행을 하시는 것이 수월합니다.

PG사로 배송정보( 이니시스, KCP만 지원 )를 보내서 에스크로 상태를 변경하는 API 입니다.
```java 
Bootpay bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
bootpay.getAccessToken();

Shipping shipping = new Shipping();
shipping.receiptId = "628ae7ffd01c7e001e9b6066";
shipping.trackingNumber = "123456";
shipping.deliveryCorp = "CJ대한통운";
ShippingUser user = new ShippingUser();
user.username = "홍길동";
user.phone = "01000000000";
user.address = "서울특별시 종로구";
user.zipcode = "08490";
shipping.user = user;
try {
   HashMap<String, Object> res = bootpay.shippingStart(shipping);
   if(res.get("error_code") == null) { //success
       System.out.println("certificate success: " + res);
   } else {
       System.out.println("certificate false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 9-1. 현금영수증 발행하기
bootpay api를 통해 결제된 건에 대하여 현금영수증을 발행합니다.
```java 
CashReceipt cashReceipt = new CashReceipt();
cashReceipt.receiptId = "62e0f11f1fc192036b1b3c92";

cashReceipt.username = "테스트";
cashReceipt.email = "test@bootpay.co.kr";
cashReceipt.phone = "01000000000";

cashReceipt.identityNo = "01000000000";
cashReceipt.cashReceiptType = "소득공제";


try {
   HashMap<String, Object> res = bootpay.requestCashReceiptByBootpay(cashReceipt);
   if(res.get("error_code") == null) { //success
       System.out.println("cashReceiptBootpay success: " + res);
   } else {
       System.out.println("cashReceiptBootpay false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 9-2. 현금영수증 발행 취소
9-1을 통해 발행한 현금영수증을 취소합니다.
```java 
Cancel cancel = new Cancel(); 

cancel.receiptId = "62e0f11f1fc192036b1b3c92";
cancel.cancelMessage = "테스트 결제";
cancel.cancelUsername = "테스트 관리 

try {
   HashMap<String, Object> res = bootpay.requestCashReceiptCancelByBootpay(cancel);
   if(res.get("error_code") == null) { //success
       System.out.println("cashReceiptBootpayCancel success: " + res);
   } else {
       System.out.println("cashReceiptBootpayCancel false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 9-3. (별건) 현금영수증 발행
부트페이 결제와 상관없이 금액, 상품명, 현금영수증 발행정보 등을 보내 현금영수증을 발행하는 API 입니다
```java 
CashReceipt cashReceipt = new CashReceipt();
cashReceipt.pg = "토스";
cashReceipt.price = 1000;
cashReceipt.orderName = "테스트";
cashReceipt.cashReceiptType = "소득공제";
cashReceipt.identityNo = "01000000000";

Date now = new Date();
now.setTime(now.getTime()); //10초 뒤 결제

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss XXX");
sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
cashReceipt.purchasedAt = sdf.format(now); // 결제 승인 시점
cashReceipt.orderId = String.valueOf(now.getTime());


try {
   HashMap<String, Object> res = bootpay.requestCashReceipt(cashReceipt);
   if(res.get("error_code") == null) { //success
       System.out.println("cashReceipt success: " + res);
   } else {
       System.out.println("cashReceipt false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## 9-4. (별건) 현금영수증 발행 취소
9-3을 통해 발행한 현금영수증을 취소합니다.
```java 
Cancel cancel = new Cancel();
cancel.receiptId = "62f48ae41fc192036f9f4b54";
cancel.cancelMessage = "테스트 결제";
cancel.cancelUsername = "테스트 관리자";


try {
   HashMap<String, Object> res = bootpay.requestCashReceiptCancel(cancel);
   if(res.get("error_code") == null) { //success
       System.out.println("cashReceiptCancel success: " + res);
   } else {
       System.out.println("cashReceiptCancel false: " + res);
   }
} catch (Exception e) {
   e.printStackTrace();
}
```

## Example 프로젝트

[적용한 샘플 프로젝트](https://github.com/bootpay/backend-java-example)을 참조해주세요

## Documentation

[부트페이 개발매뉴얼](https://developer.bootpay.co.kr/)을 참조해주세요

## 기술문의

[부트페이 홈페이지](https://www.bootpay.co.kr) 우측 하단 채팅을 통해 기술문의 주세요!

## License

[MIT License](https://opensource.org/licenses/MIT).

