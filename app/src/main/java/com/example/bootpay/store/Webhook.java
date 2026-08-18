package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.webhook.TestWebhookParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;


public class Webhook {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
            sendTest();
//            sendTestWithContentType();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getToken() {
        try {
            BootpayStoreResponse res = bootpayStore.getAccessToken();
            if(res.isSuccess()) {
                System.out.println("goGetToken success: " + res.getData());
            } else {
                System.out.println("goGetToken false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 테스트 웹훅 발송 (서버 기본 content type 사용)
    public static void sendTest() {
        try {
            BootpayStoreResponse res = bootpayStore.webhook.sendTest();
            if(res.isSuccess()) {
                System.out.println("webhook sendTest success: " + res.getData());
            } else {
                System.out.println("webhook sendTest false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 테스트 웹훅 발송 (수신 content type 지정)
    public static void sendTestWithContentType() {
        try {
            TestWebhookParams params = new TestWebhookParams();
            params.headerContentType = "application/json";

            BootpayStoreResponse res = bootpayStore.webhook.sendTest(params);
            if(res.isSuccess()) {
                System.out.println("webhook sendTest success: " + res.getData());
            } else {
                System.out.println("webhook sendTest false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
