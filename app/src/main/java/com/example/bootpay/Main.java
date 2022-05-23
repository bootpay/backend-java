package com.example.bootpay;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.*;
import kr.co.bootpay.model.response.ResDefault;

import java.util.HashMap;

public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello world!");
//    }

    static Bootpay bootpay;
    public static void main(String[] args) {
        bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");
//        bootpay = new Bootpay("59b731f084382614ebf72215", "WwDv0UjfwFa04wYG0LJZZv1xwraQnlhnHE375n52X0U=");

        goGetToken();
        getReceipt();
        receiptCancel();
        getBillingKey();
        requestSubscribe();
        reserveSubscribe();
        reserveCancelSubscribe();
        destroyBillingKey();
        getUserToken();
        requestLink();
        confirm();
        certificate();
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
        subscribe.pg = "payapp";
        subscribe.cardNo = "5570**********1074"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardPw = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireYear = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardExpireMonth = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribe.cardIdentifyNumber = ""; //주민등록번호 또는 사업자 등록번호 (- 없이 입력)

        subscribe.user = new User();
        subscribe.user.username = "홍길동";
        subscribe.user.phone = "01011112222";

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

    public static void destroyBillingKey() {
        String receiptId = "6100e7ea0d681b001fd4de69";
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

    public static void requestSubscribe() {
        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "618c661d019943003944d5ec";
        payload.orderName = "아이템01";
        payload.price = 1000;
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

        payload.billingKey = "619af3fd27018000269761d4";
        payload.orderName = "아이템01";
        payload.price = 1000;
        payload.orderId = "" + (System.currentTimeMillis() / 1000);
        payload.reserveExecuteAt = (System.currentTimeMillis() / 1000) + 10; // 결제 승인 시점

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
        String receiptId = "618c66320d681b00445194b5";
        try {
            HashMap<String, Object> res = bootpay.reserveCancelSubscribe(receiptId);
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
        cancel.receiptId = "6100e77a019943003650f4d5";
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
        payload.price = 1000;
        payload.name = "테스트 결제";
        payload.pg = "payapp";
//        payload.method = "vbank";

        User user = new User();
        user.username = "홍길동";
        user.phone = "01012341234";
        payload.userInfo = user;

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
        String receiptId = "6100e8e7019943003850f9b0";
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

    public static void getReceipt() {
        String receiptId = "62875039d01c7e00219b5ffe";
        try {
            HashMap<String, Object> res = bootpay.getReceipt(receiptId);
            if(res.get("error_code") == null) { //success
                System.out.println("getReceipt success: " + res);
            } else {
                System.out.println("getReceipt false: " + res);
            }
//            System.out.println(res.get("receipt_id"));
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
}