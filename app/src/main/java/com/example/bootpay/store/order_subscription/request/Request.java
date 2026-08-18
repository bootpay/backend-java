package com.example.bootpay.store.order_subscription.request;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;


public class Request {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
            list();
//            detail();
//            approve();
//            reject();
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

    // 구독 변경요청 목록 조회
    // projectId를 지정하면 supervisor(프로젝트 전체 검색), 없으면 user(본인 요청)로 조회한다
    public static void list() {
        try {
            OrderSubscriptionRequestListParams params = new OrderSubscriptionRequestListParams();
            params.orderSubscriptionId = "686dc2f2b0eacea5cd974ca2";
            params.page = 1;
            params.limit = 20;

            BootpayStoreResponse res = bootpayStore.orderSubscription.request.list(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription request list success: " + res.getData());
            } else {
                System.out.println("orderSubscription request list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 변경요청 상세 조회
    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.orderSubscription.request.detail("686dc2f2b0eacea5cd974ca2");
            if(res.isSuccess()) {
                System.out.println("orderSubscription request detail success: " + res.getData());
            } else {
                System.out.println("orderSubscription request detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 변경요청 승인
    public static void approve() {
        try {
            OrderSubscriptionRequestUpdateParams params = new OrderSubscriptionRequestUpdateParams();
            params.requestHistoryId = "686dc2f2b0eacea5cd974ca2";
            params.reason = "승인 처리";

            BootpayStoreResponse res = bootpayStore.orderSubscription.request.approve(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription request approve success: " + res.getData());
            } else {
                System.out.println("orderSubscription request approve false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 변경요청 반려
    public static void reject() {
        try {
            OrderSubscriptionRequestUpdateParams params = new OrderSubscriptionRequestUpdateParams();
            params.requestHistoryId = "686dc2f2b0eacea5cd974ca2";
            params.reason = "반려 처리";

            BootpayStoreResponse res = bootpayStore.orderSubscription.request.reject(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription request reject success: " + res.getData());
            } else {
                System.out.println("orderSubscription request reject false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
