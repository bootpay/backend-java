package com.example.bootpay;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.*;
import kr.co.bootpay.model.response.ResDefault;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.simple.JSONObject;

import static kr.co.bootpay.service.BillingService.getBillingKeyTransfer;


public class Main {

    static Bootpay bootpay;
    public static void main(String[] args) {
        bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
//        bootpay = new Bootpay("6560203cca8deb00600959cc", "NVznyFF+WKVbT54ImpulaeYzROKFhg28RWw7h8yt0/A=", "https://dev-api.bootpay.co.kr/v2/");


        goGetToken();
//        getReceipt();
//        receiptCancel();
//        lookupBillingKey();
//        lookupBillingKeyByKey();
//        lookupPaymentMethods();
//        getBillingKey();
//        requestSubscribe();
//        reserveSubscribe();
//        startShipping();

//        reserveSubscribeLookup();
//        reserveCancelSubscribe();
//        destroyBillingKey();
//        getUserToken();
////        requestLink();
//        confirm();
//        certificate();
//        shippingStart();
////
//        cashReceipt();
//        cashReceiptCancel();
//        cashReceiptBootpay();
//        cashReceiptBootpayCancel();
//
//        authRequest();
//        authConfirm();
//        authRealarm();
//        lookupOrderId();

//        getBillingKeyTransfer();
//        publishBillingKeyTransfer();
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

    public static void getBillingKey() {
        Subscribe subscribe = new Subscribe();
        subscribe.orderName = "정기결제 테스트 아이템";
        subscribe.subscriptionId = "" + (System.currentTimeMillis() / 1000);
        subscribe.pg = "나이스페이";

        subscribe.cardNo = "5570**********1074"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardPw = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireYear = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireMonth = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardIdentityNo = ""; //생년월일 또는 사업자 등록번호 (- 없이 입력)

        subscribe.user = new User();
        subscribe.user.username = "홍길동";
        subscribe.user.phone = "01011112222";
        subscribe.extra = new SubscribeExtra();
        subscribe.extra.subscribeTestPayment = 1;

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
    }

    public static void destroyBillingKey() {
        String billingKey = "64376421755e27001feb65bb";
        try {
            HashMap<String, Object> res = bootpay.destroyBillingKey(billingKey);
            if(res.get("error_code") == null) { //success
                System.out.println("success: " + res);
            } else {
                System.out.println("false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestSubscribe() {
        SubscribePayload payload = new SubscribePayload();
//        payload.billingKey = "66541c40419d33cdcb43e268";
        payload.billingKey = "66541c40419d33cdcb43e268";

        payload.orderName = "아이템01";
        payload.price = 100;
        payload.user = new User();
        payload.user.phone = "01012345678";
        payload.orderId = "" + (System.currentTimeMillis() / 1000);
        payload.metadata.put("0", "1234");

        try {
            HashMap<String, Object> res = bootpay.requestSubscribe(payload);
            if(res.get("error_code") == null) { //success
                System.out.println("requestSubscribe success: " + res);
            } else {
                System.out.println("requestSubscribe false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reserveSubscribe() {
        SubscribePayload payload = new SubscribePayload();

        payload.billingKey = "649013eb00be040023cf7838";
        payload.orderName = "아이템01";
        payload.price = 1000;
        payload.orderId = "" + (System.currentTimeMillis() / 1000);
        payload.metadata.put("0", "1234");
        payload.metadata.put("1", "5678");

        Date now = new Date();
        now.setTime(now.getTime() + 5 * 1000); //5초 뒤 결제

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
    }

    public static void startShipping() {
        Shipping shipping = new Shipping();
        shipping.receiptId = "664ab4724b704372318b6fb7";
        shipping.deliveryCorp = "CJ대한통운";
        shipping.trackingNumber = "1234";
        shipping.redirectUrl = "https://www.naver.com";
        shipping.user = new ShippingUser();
        shipping.user.username = "홍길동";
        shipping.user.phone = "01012341234";
        shipping.user.zipcode = "08389";
        shipping.user.address = "동해 번쩍 서해 번쩍";
        shipping.company = new ShippingCompany();
        shipping.company.name = "배송업체";

        try {
            HashMap<String, Object> res = bootpay.shippingStart(shipping);
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
    }

    public static void reserveSubscribeLookup() {
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
    }

    public static void reserveCancelSubscribe() {
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
    }

    public static void receiptCancel() {
        Cancel cancel = new Cancel();
        cancel.receiptId = "664ab4724b704372318b6fb7";
        cancel.cancelUsername = "관리자2";
        cancel.cancelMessage = "테스트 결제2";
        cancel.cancelPrice = 1000d;
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
    }

    public static void getUserToken() {
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
    }

    @Deprecated
    public static void requestLink() {
        Payload payload = new Payload();
        payload.orderId = "1234";
        payload.price = 1000d;
        payload.orderName = "테스트 결제";
        payload.pg = "payapp";
//        payload.method = "vbank";

        User user = new User();
        user.username = "홍길동";
        user.phone = "01012341234";
        payload.user = user;

        Extra extra = new Extra();
        payload.extra = extra;


        try {
            ResDefault res = bootpay.requestLink(payload);
            System.out.println("requestLink:" + res.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void confirm() {
        String receiptId = "643cb6bb9f326b001fccaddc";
        try {
            HashMap<String, Object> res = bootpay.confirm(receiptId);
            System.out.println(res.get("status").toString());
            JSONObject json =  new JSONObject(res);
            System.out.printf( "JSON: %s", json);
            if(res.get("error_code") == null) { //success
                System.out.println("confirm success: " + res);
            } else {
                System.out.println("confirm false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public static void getReceipt() {
        String receiptId = "643cb3cd9f326b0023ccad3a";
        try {
            HashMap<String, Object> res = bootpay.getReceipt(receiptId);
            JSONObject json =  new JSONObject(res);
            System.out.printf( "JSON: %s", json);
            if(res.get("error_code") == null) { //success
//                System.out.println("getReceipt success: " + res);
            } else {
//                System.out.println("getReceipt false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//

    public static void lookupBillingKey() {
        String receiptId = "6317e646d01c7e0024170b47";
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
    }

    public static void lookupBillingKeyByKey() {
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
    }

    public static void lookupPaymentMethods() {
//        String receiptId = "62e1e2f2cf9f6d002705a7fa";
        try {
            HashMap<String, Object> res = bootpay.lookupPaymentMethods();
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
    }

    public static void certificate() {
        String receiptId = "640e65283049c8001e5618fc";
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
    }

    public static void shippingStart() {
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
    }

    public static void cashReceipt() {
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
    }

    public static void cashReceiptCancel() {
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
    }

    public static void cashReceiptBootpay() {
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
    }

    public static void cashReceiptBootpayCancel() {
        Cancel cancel = new Cancel();

        cancel.receiptId = "62e0f11f1fc192036b1b3c92";
        cancel.cancelMessage = "테스트 결제";
        cancel.cancelUsername = "테스트 관리자";


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
    }

    public static void authRequest() {
        Authentication authentication = new Authentication();
        authentication.pg = "다날";
        authentication.method = "본인인증";
        authentication.username = "사용자명";
        authentication.identityNo = "0000000"; //생년월일 + 주민번호 뒷 1자리
        authentication.carrier = "SKT"; //통신사
        authentication.phone = "01010002000"; //사용자 전화번호
        authentication.siteUrl = "https://www.bootpay.co.kr"; //본인인증 하는 url 또는 App 명
        authentication.orderName = "회원 본인인증";
        authentication.authenticationId = "" + (System.currentTimeMillis() / 1000);



        try {
            HashMap<String, Object> res = bootpay.requestAuthentication(authentication);
            if(res.get("error_code") == null) { //success
                System.out.println("authRequest success: " + res);
            } else {
                System.out.println("authRequest false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void authConfirm() {
//        6369dc33d01c7e00271cccad
        try {
            HashMap<String, Object> res = bootpay.confirmAuthentication("636a0bc4d01c7e00331cd25e", "556659");
            if(res.get("error_code") == null) { //success
                System.out.println("authConfirm success: " + res);
            } else {
                System.out.println("authConfirm false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void authRealarm() {
        try {
            HashMap<String, Object> res = bootpay.realarmAuthentication("6369dc33d01c7e00271cccad");
            if(res.get("error_code") == null) { //success
                System.out.println("authRealarm success: " + res);
            } else {
                System.out.println("authRealarm false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void lookupOrderId() {
        try {
            HashMap<String, Object> res = bootpay.lookupOrderId("1706621409416");
            if(res.get("error_code") == null) { //success
                System.out.println("authRealarm success: " + res);
            } else {
                System.out.println("authRealarm false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
//                {cancelled_price=0, metadata={}, cancelled_tax_free=0, method=계좌자동이체, gateway_url=https://gw.bootpay.co.kr, sandbox=true, receipt_id=66541bc4ca4517e69343e24c, method_origin=계좌자동이체, order_name=테스트 결제, method_origin_symbol=automatic_transfer_rest, method_symbol=automatic_transfer_rest, tax_free=0, price=0, company_name=윤태섭, pg=나이스페이먼츠, status_locale=자동결제빌링키발급이전, currency=KRW, http_status=200, order_id=1716788164, requested_at=2024-05-27T14:36:04+09:00, status=41}
            } else {
                System.out.println("false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void publishBillingKeyTransfer() {
        try {
            HashMap<String, Object> res = bootpay.publishBillingKeyTransfer("66541bc4ca4517e69343e24c");
            if(res.get("error_code") == null) { //success
                System.out.println("success: " + res);
//                {metadata={}, method=계좌자동이체, gateway_url=https://gw.bootpay.co.kr, receipt_id=66541bc4ca4517e69343e24c, method_origin=계좌자동이체, subscription_id=1716788164, billing_data={bank_name=국민, bank_code=004, bank_account=0000000000000000, username=윤태*}, method_origin_symbol=automatic_transfer_rest, billing_expire_at=2099-12-31T23:59:59+09:00, method_symbol=automatic_transfer_rest, pg=나이스페이먼츠, status_locale=빌링키발급완료, http_status=200, published_at=2024-05-27T14:38:08+09:00, billing_key=66541c40419d33cdcb43e268, requested_at=2024-05-27T14:36:04+09:00, status=11}

            } else {
                System.out.println("false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}