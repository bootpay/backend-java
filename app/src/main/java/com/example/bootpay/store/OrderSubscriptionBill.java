package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;

import java.util.HashMap;


public class OrderSubscriptionBill {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
    //        list();
    //        detail();
    //        update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getToken() {
        try {
            BootpayStoreResponse res = bootpayStore.getAccessToken();
            if(res.isSuccess()) {
                System.out.println("goGetToken success: " + res.getDataAsMap());
            } else {
                System.out.println("goGetToken false: " + res.getDataAsMap());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void list() {
        try {
            BootpayStoreResponse res = bootpayStore.orderSubscriptionBill.list(null);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionBill list success: " + res.getDataAsMap());
            } else {
                System.out.println("orderSubscriptionBill list false: " + res.getDataAsMap());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.orderSubscriptionBill.detail("67e5100d5ec892162491d111");
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionBill detail success: " + res.getDataAsMap());
            } else {
                System.out.println("orderSubscriptionBill detail false: " + res.getDataAsMap());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        try {
            SOrderSubscriptionBill orderSubscriptionBill = new SOrderSubscriptionBill();
            orderSubscriptionBill.orderSubscriptionBillId = "67e5100d5ec892162491d111";
            orderSubscriptionBill.status = 1; // 1: paid, 0: unpaid, 2: cancelled

            BootpayStoreResponse res = bootpayStore.orderSubscriptionBill.update(orderSubscriptionBill);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionBill update success: " + res.getDataAsMap());
            } else {
                System.out.println("orderSubscriptionBill update false: " + res.getDataAsMap());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

