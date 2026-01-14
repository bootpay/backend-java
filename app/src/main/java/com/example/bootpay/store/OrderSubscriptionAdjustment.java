package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscriptionAdjustment.OrderSubscriptionAdjustmentUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

import java.util.List;


public class OrderSubscriptionAdjustment {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
    //        update();
//            create();
//            update();
            delete();
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


    public static void create() {
        try {
            String orderSubscriptionId = "6964abf14cb8149d077124e8";
            SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment();
            adjustment.name = "추가비용2";
            adjustment.price = 500.0;
            adjustment.duration = 1;

            BootpayStoreResponse res = bootpayStore.asSupervisor().orderSubscriptionAdjustment.create(orderSubscriptionId, adjustment);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionAdjustment update success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionAdjustment update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        try {
            OrderSubscriptionAdjustmentUpdateParams params = new OrderSubscriptionAdjustmentUpdateParams();
            params.orderSubscriptionId = "6964abf14cb8149d077124e8";
            params.duration = 2;
            params.adjustments = List.of(new SOrderSubscriptionAdjustment("추가비용2 전체갱신", 600.0));

            BootpayStoreResponse res = bootpayStore.asSupervisor().orderSubscriptionAdjustment.update(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionAdjustment update success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionAdjustment update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete() {
        try {
            String orderSubscriptionId = "6964abf14cb8149d077124e8";
            String orderSubscriptionAdjustmentId = "6965fced4cb8149d0771253a";

            BootpayStoreResponse res = bootpayStore.asSupervisor().orderSubscriptionAdjustment.delete(orderSubscriptionId, orderSubscriptionAdjustmentId);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionAdjustment update success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionAdjustment update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

