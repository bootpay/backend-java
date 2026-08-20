package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.pojo.SMallSetting;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;


public class MallSetting {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
            get();
//            update();
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

    // 몰 설정 조회 (supervisor scope 전용)
    public static void get() {
        try {
            BootpayStoreResponse res = bootpayStore.asSupervisor().mallSetting.detail();
            if(res.isSuccess()) {
                System.out.println("mallSetting get success: " + res.getData());
            } else {
                System.out.println("mallSetting get false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 몰 설정 수정 (supervisor scope 전용, 설정한 값만 전송된다)
    public static void update() {
        try {
            SMallSetting params = new SMallSetting();
            params.name = "부트페이 스토어";
            params.sellerName = "부트페이";
            params.bizEmail = "help@bootpay.co.kr";
            params.useCart = true;

            BootpayStoreResponse res = bootpayStore.asSupervisor().mallSetting.update(params);
            if(res.isSuccess()) {
                System.out.println("mallSetting update success: " + res.getData());
            } else {
                System.out.println("mallSetting update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
