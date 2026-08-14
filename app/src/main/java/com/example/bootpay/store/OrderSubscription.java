package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionChargeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionChargeRevokeParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;

import java.util.HashMap;
import java.util.Map;


public class OrderSubscription {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
            list();
//            detail();
//            update();
//            supervisorCharge();
//            supervisorChargeRevoke();
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
            params.orderSubscriptionId = "685b7b10b0eacea5cd974a93";
            params.orderName = "구독계약 변경 테스트";

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

    // 수시결제(온디맨드) charge_key 즉시 결제
    public static void supervisorCharge() {
        try {
            SupervisorOrderSubscriptionChargeParams params = new SupervisorOrderSubscriptionChargeParams();
            params.chargeKey = "6d1f1a2b3c4d5e6f70819200";
            params.price = 1000d;
            params.taxFreePrice = 0d;

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("memo", "수시결제 테스트");
            params.metadata = metadata;

            BootpayStoreResponse res = bootpayStore.asSupervisor().orderSubscription.supervisorCharge(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription supervisorCharge success: " + res.getData());
            } else {
                System.out.println("orderSubscription supervisorCharge false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 수시결제(온디맨드) charge_key 해지
    public static void supervisorChargeRevoke() {
        try {
            SupervisorOrderSubscriptionChargeRevokeParams params = new SupervisorOrderSubscriptionChargeRevokeParams();
            params.chargeKey = "6d1f1a2b3c4d5e6f70819200";

            BootpayStoreResponse res = bootpayStore.asSupervisor().orderSubscription.supervisorChargeRevoke(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription supervisorChargeRevoke success: " + res.getData());
            } else {
                System.out.println("orderSubscription supervisorChargeRevoke false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void approve() {

    }

    public static void reject() {

    }
}

