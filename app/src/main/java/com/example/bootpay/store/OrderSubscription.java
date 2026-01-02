package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;


public class OrderSubscription {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
//            TokenPayload tokenPayload = new TokenPayload("uz0ZZtIrwS1LBLkDO5yCvw", "_1Byx_FQ2Z076_no3rGPBBqUrhpU-0_Tl883DCfBjFI="); //dev
//            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");

            TokenPayload tokenPayload = new TokenPayload("L4VKNqhkNxuo7d83-x0u_Q", "DnZD1royBjNICCatlnmi97TwzyJRSggVM22nv866i5A="); //dev
            bootpayStore = new BootpayStore(tokenPayload);





            getToken();
//            list();
//            detail();
//
            update();
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

    public static void list() {
        try {
            OrderSubscriptionListParams params = new OrderSubscriptionListParams();
            params.sAt = "2025-05-20";

            BootpayStoreResponse res = bootpayStore.orderSubscription.list(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription list success: " + res.getData());
            } else {
                System.out.println("orderSubscription list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            String orderSubscriptionId = "67e5100c5ec892162491d108";
            BootpayStoreResponse res = bootpayStore.orderSubscription.detail(orderSubscriptionId);
            if(res.isSuccess()) {
                System.out.println("orderSubscription detail success: " + res.getData());
            } else {
                System.out.println("orderSubscription detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void update() {
        try {
            OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
            params.orderSubscriptionId = "692ac59a763cd765570c4b63";
            params.orderName = "구독계약 변경 테스트2";

            BootpayStoreResponse res = bootpayStore.asSupervisor().orderSubscription.update(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription update success: " + res.getData());
            } else {
                System.out.println("orderSubscription update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

