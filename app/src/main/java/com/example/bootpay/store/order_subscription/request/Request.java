package com.example.bootpay.store.order_subscription.request;

import com.example.bootpay.Config;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

/**
 * 구독 변경요청(order-subscription-requests) 예제.
 */
public class Request {

    static BootpayStore bootpayStore;

    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload(Config.Commerce.getClientKey(), Config.Commerce.getSecretKey());
            bootpayStore = new BootpayStore(tokenPayload, Config.CURRENT_ENV);
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

            BootpayStoreResponse res = bootpayStore.orderSubscriptionRequest.list(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionRequest list success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionRequest list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 변경요청 상세 조회
    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.orderSubscriptionRequest.detail("686dc2f2b0eacea5cd974ca2");
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionRequest detail success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionRequest detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 변경요청 승인 — 승인/반려는 approval 값으로 갈린다
    public static void approve() {
        try {
            OrderSubscriptionRequestUpdateParams params = new OrderSubscriptionRequestUpdateParams();
            params.orderSubscriptionRequestHistoryId = "686dc2f2b0eacea5cd974ca2";
            params.approval = OrderSubscriptionRequestUpdateParams.APPROVAL_APPROVE;
            params.reason = "승인 처리";
            // 정산 항목까지 확정할 때 함께 지정한다
//            params.price = 10000.0;
//            params.terminationFee = 0.0;
//            params.serviceEndAt = "2026-09-01T00:00:00+09:00";

            BootpayStoreResponse res = bootpayStore.orderSubscriptionRequest.update(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionRequest approve success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionRequest approve false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 변경요청 반려
    public static void reject() {
        try {
            OrderSubscriptionRequestUpdateParams params = new OrderSubscriptionRequestUpdateParams();
            params.orderSubscriptionRequestHistoryId = "686dc2f2b0eacea5cd974ca2";
            params.approval = OrderSubscriptionRequestUpdateParams.APPROVAL_REJECT;
            params.reason = "반려 처리";

            BootpayStoreResponse res = bootpayStore.orderSubscriptionRequest.update(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscriptionRequest reject success: " + res.getData());
            } else {
                System.out.println("orderSubscriptionRequest reject false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
