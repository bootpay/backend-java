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


public class Main {

    static Bootpay bootpay;
    public static void main(String[] args) {
        bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
//        bootpay = new Bootpay("5b9f51264457636ab9a07cde", "sfilSOSVakw+PZA+PRux4Iuwm7a//9CXXudCq9TMDHk=", "https://dev-api.bootpay.co.kr/v2/");


        goGetToken();
        getReceipt();
        receiptCancel();
        lookupBillingKey();
        lookupPaymentMethods();
        getBillingKey();
        requestSubscribe();
        reserveSubscribe();
        reserveCancelSubscribe();
        destroyBillingKey();
        getUserToken();
//        requestLink();
        confirm();
        certificate();
        shippingStart();
////
//        cashReceipt();
//        cashReceiptCancel();
//        cashReceiptBootpay();
//        cashReceiptBootpayCancel();
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
        String billingKey = "628b2644d01c7e00209b6092";
        try {
            HashMap<String, Object> res = bootpay.destroyBillingKey(billingKey);
            if(res.get("error_code") == null) { //success
                System.out.println("destroyBillingKey success: " + res);
            } else {
                System.out.println("destroyBillingKey false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestSubscribe() {
        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "63114b39d01c7e0025fb75e5";
        payload.orderName = "아이템01";
        payload.price = 1000;
        payload.user = new User();
        payload.user.phone = "01012345678";
        payload.orderId = "" + (System.currentTimeMillis() / 1000);

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

        payload.billingKey = "632402f6d01c7e003c91f49b";
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
        cancel.receiptId = "62d8e199cf9f6d001aa6cb06";
        cancel.cancelUsername = "관리자";
        cancel.cancelMessage = "테스트 결제";
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
        String receiptId = "6319773ad01c7e0032171270";
        try {
            HashMap<String, Object> res = bootpay.confirm(receiptId);
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
        String receiptId = "6323e0dcd01c7e001e91f151";
        try {
            HashMap<String, Object> res = bootpay.getReceipt(receiptId);
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
        String receiptId = "630d5997d01c7e003f5aa109";
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
}