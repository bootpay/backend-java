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

        bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");

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
        String receiptId = "62b12f4b6262500007629fec";
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
    }

    public static void confirm() {
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

    public static void receiptCancel() {
        Cancel cancel = new Cancel();
        cancel.receiptId = "664ae6621a10a75af2b4b085";
        cancel.cancelUsername = "관리자3";
        cancel.cancelMessage = "테스트 결제3";
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
            HashMap<String, Object> res = bootpay.publishBillingKeyTransfer("66541bc4ca4517e69343e24c");
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
        payload.billingKey = "692e6f9abe3f0224ea4ce8e1";
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

        payload.billingKey = "692e6f9abe3f0224ea4ce8e1";
        payload.orderName = "아이템01";
        payload.price = 1000.0;
        payload.orderId = "" + (System.currentTimeMillis() / 1000);

        Date now = new Date();
        now.setTime(now.getTime() + 10 * 1000); //10초 뒤 결제
//
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
        String reserveId = "6490149ca575b40024f0b70d";
        try {
            HashMap<String, Object> res = bootpay.reserveSubscribeLookup(reserveId);
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
        String reserveId = "692e701288acd62032ef1645";
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

    public static void lookupBillingKey() {
        String receiptId = "6317e646d01c7e0024170b47";
        try {
            HashMap<String, Object> res = bootpay.lookupBillingKey(receiptId);
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
        String billingKey = "66542dfb4d18d5fc7b43e1b6";
        try {
            HashMap<String, Object> res = bootpay.lookupBillingKeyByKey(billingKey);
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
        String receiptId = "628b2644d01c7e00209b6092";
        try {
            HashMap<String, Object> res = bootpay.destroyBillingKey(receiptId);
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
        String receiptId = "628ae7ffd01c7e001e9b6066";
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
        cancel.receiptId = "62f48ae41fc192036f9f4b54";
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
        cashReceipt.receiptId = "62e0f11f1fc192036b1b3c92";

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

        cancel.receiptId = "62e0f11f1fc192036b1b3c92";
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
