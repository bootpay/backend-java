package com.example.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.pg.model.BankCode;
import kr.co.bootpay.pg.model.request.*;
import kr.co.bootpay.pg.model.response.ResDefault;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class BootpayExample {
    static Bootpay bootpay;

    public static void main(String[] args) {
        // Config에서 현재 환경에 맞는 키를 가져옴
        bootpay = new Bootpay(Config.PG.getApplicationId(), Config.PG.getPrivateKey());

        System.out.println("bootpay: " + BankCode.신한);

        goGetToken();
//        getReceipt();
//        receiptCancel();
//        getBillingKey();
//        requestSubscribe();
//        reserveSubscribe();
//        reserveCancelSubscribe();
//        destroyBillingKey();
//        getUserToken();
//        confirm();
//        certificate();
//        shippingStart();
//        getBillingKeyTransfer();
//        publishBillingKeyTransfer();
//        requestAuthentication();
//        confirmAuthentication();
//        realarmAuthentication();
//        requestCashReceipt();
//        requestCashReceiptCancel();
//        requestCashReceiptByBootpay();
//        requestCashReceiptCancelByBootpay();
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

    public static void getReceipt() {
        try {
            HashMap<String, Object> res = bootpay.getReceipt(Config.TestData.RECEIPT_ID);
            if(res.get("error_code") == null) { //success
                System.out.println("getReceipt success: " + res);
            } else {
                System.out.println("getReceipt false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void confirm() {
        try {
            HashMap<String, Object> res = bootpay.confirm(Config.TestData.RECEIPT_ID_CONFIRM);
            if(res.get("error_code") == null) { //success
                System.out.println("confirm success: " + res);
            } else {
                System.out.println("confirm false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getUserToken() {
        UserToken userToken = new UserToken();
        userToken.userId = Config.TestData.USER_ID;
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

    public static void receiptCancel() {
        Cancel cancel = new Cancel();
        cancel.receiptId = Config.TestData.RECEIPT_ID;
        cancel.cancelUsername = "관리자";
        cancel.cancelMessage = "테스트 결제 취소";
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

    public static void getBillingKey() {
        Subscribe subscribe = new Subscribe();
        subscribe.orderName = "정기결제 테스트 아이템";
        subscribe.subscriptionId = "" + (System.currentTimeMillis() / 1000);
        subscribe.pg = "라이트페이";

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
            if(res.get("error_code") == null) { //success
                System.out.println("getBillingKey success: " + res);
            } else {
                System.out.println("getBillingKey false: " + res);
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

            HashMap<String, Object> res = bootpay.getBillingKeyTransfer(subscribe);
            if(res.get("error_code") == null) { //success
                System.out.println("getBillingKeyTransfer success: " + res);
            } else {
                System.out.println("getBillingKeyTransfer false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void publishBillingKeyTransfer() {
        try {
            HashMap<String, Object> res = bootpay.publishBillingKeyTransfer(Config.TestData.RECEIPT_ID_TRANSFER);
            if(res.get("error_code") == null) { //success
                System.out.println("publishBillingKeyTransfer success: " + res);
            } else {
                System.out.println("publishBillingKeyTransfer false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestSubscribe() {
        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = Config.TestData.BILLING_KEY;
        payload.orderName = "아이템01";
        payload.price = 1000.0;
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

        payload.billingKey = Config.TestData.BILLING_KEY;
        payload.orderName = "아이템01";
        payload.price = 1000.0;
        payload.orderId = "" + (System.currentTimeMillis() / 1000);

        Date now = new Date();
        now.setTime(now.getTime() + 10 * 1000); //10초 뒤 결제

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss XXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        payload.reserveExecuteAt = sdf.format(now); // 결제 승인 시점

        System.out.println("reserveSubscribe reserveExecuteAt: " + payload.reserveExecuteAt);

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

    public static void reserveSubscribeLookup() {
        try {
            HashMap<String, Object> res = bootpay.reserveSubscribeLookup(Config.TestData.RESERVE_ID);
            if(res.get("error_code") == null) { //success
                System.out.println("reserveSubscribeLookup success: " + res);
            } else {
                System.out.println("reserveSubscribeLookup false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reserveCancelSubscribe() {
        try {
            HashMap<String, Object> res = bootpay.reserveCancelSubscribe(Config.TestData.RESERVE_ID);
            if(res.get("error_code") == null) { //success
                System.out.println("reserveCancelSubscribe success: " + res);
            } else {
                System.out.println("reserveCancelSubscribe false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void lookupBillingKey() {
        try {
            HashMap<String, Object> res = bootpay.lookupBillingKey(Config.TestData.RECEIPT_ID_BILLING);
            if(res.get("error_code") == null) { //success
                System.out.println("lookupBillingKey success: " + res);
            } else {
                System.out.println("lookupBillingKey false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void lookupBillingKeyByKey() {
        try {
            HashMap<String, Object> res = bootpay.lookupBillingKeyByKey(Config.TestData.BILLING_KEY_2);
            if(res.get("error_code") == null) { //success
                System.out.println("lookupBillingKeyByKey success: " + res);
            } else {
                System.out.println("lookupBillingKeyByKey false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroyBillingKey() {
        try {
            HashMap<String, Object> res = bootpay.destroyBillingKey(Config.TestData.BILLING_KEY);
            if(res.get("error_code") == null) { //success
                System.out.println("destroyBillingKey success: " + res);
            } else {
                System.out.println("destroyBillingKey false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void requestAuthentication() {
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
                System.out.println("requestAuthentication success: " + res);
            } else {
                System.out.println("requestAuthentication false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void confirmAuthentication() {
        try {
            HashMap<String, Object> res = bootpay.confirmAuthentication("636a0bc4d01c7e00331cd25e", "556659");
            if(res.get("error_code") == null) { //success
                System.out.println("confirmAuthentication success: " + res);
            } else {
                System.out.println("confirmAuthentication false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void realarmAuthentication() {
        try {
            HashMap<String, Object> res = bootpay.realarmAuthentication("6369dc33d01c7e00271cccad");
            if(res.get("error_code") == null) { //success
                System.out.println("realarmAuthentication success: " + res);
            } else {
                System.out.println("realarmAuthentication false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void certificate() {
        try {
            HashMap<String, Object> res = bootpay.certificate(Config.TestData.CERTIFICATE_RECEIPT_ID);
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
        shipping.receiptId = Config.TestData.RECEIPT_ID_ESCROW;
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
                System.out.println("shippingStart success: " + res);
            } else {
                System.out.println("shippingStart false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestCashReceipt() {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.pg = "토스";
        cashReceipt.price = 1000.0;
        cashReceipt.orderName = "테스트";
        cashReceipt.cashReceiptType = "소득공제";
        cashReceipt.identityNo = "01000000000";

        Date now = new Date();
        now.setTime(now.getTime()); //현재 시간

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss XXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        cashReceipt.purchasedAt = sdf.format(now);
        cashReceipt.orderId = String.valueOf(now.getTime());

        try {
            HashMap<String, Object> res = bootpay.requestCashReceipt(cashReceipt);
            if(res.get("error_code") == null) { //success
                System.out.println("requestCashReceipt success: " + res);
            } else {
                System.out.println("requestCashReceipt false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestCashReceiptCancel() {
        Cancel cancel = new Cancel();
        cancel.receiptId = Config.TestData.RECEIPT_ID_CASH;
        cancel.cancelMessage = "테스트 결제";
        cancel.cancelUsername = "테스트 관리자";

        try {
            HashMap<String, Object> res = bootpay.requestCashReceiptCancel(cancel);
            if(res.get("error_code") == null) { //success
                System.out.println("requestCashReceiptCancel success: " + res);
            } else {
                System.out.println("requestCashReceiptCancel false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestCashReceiptByBootpay() {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.receiptId = Config.TestData.RECEIPT_ID_CASH;

        cashReceipt.username = "테스트";
        cashReceipt.email = "test@bootpay.co.kr";
        cashReceipt.phone = "01000000000";

        cashReceipt.identityNo = "01000000000";
        cashReceipt.cashReceiptType = "소득공제";

        try {
            HashMap<String, Object> res = bootpay.requestCashReceiptByBootpay(cashReceipt);
            if(res.get("error_code") == null) { //success
                System.out.println("requestCashReceiptByBootpay success: " + res);
            } else {
                System.out.println("requestCashReceiptByBootpay false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestCashReceiptCancelByBootpay() {
        Cancel cancel = new Cancel();

        cancel.receiptId = Config.TestData.RECEIPT_ID_CASH;
        cancel.cancelMessage = "테스트 결제";
        cancel.cancelUsername = "테스트 관리자";

        try {
            HashMap<String, Object> res = bootpay.requestCashReceiptCancelByBootpay(cancel);
            if(res.get("error_code") == null) { //success
                System.out.println("requestCashReceiptCancelByBootpay success: " + res);
            } else {
                System.out.println("requestCashReceiptCancelByBootpay false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
