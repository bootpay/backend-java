package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class OrderSubscriptionBill {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
//            list();
            detail();
    //        update();
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
            OrderSubscriptionBillListParams params = new OrderSubscriptionBillListParams();
            params.orderSubscriptionId = "685b7b10b0eacea5cd974a93";
//            params.status = List.of(1);

            BootpayStoreResponse res = bootpayStore.orderSubscriptionBill.list(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionBill list success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionBill list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.orderSubscriptionBill.detail("6863851f9ee6691f52f21fc1");
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionBill detail success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionBill detail false: " + res.getData());
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
                System.out.println("orderSubscriptionBill update success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionBill update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

