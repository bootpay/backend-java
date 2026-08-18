package com.example.bootpay.store.order_subscription.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPurchaseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTransferParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.response.orderSubscription.request.ing.CalcTerminateFeeResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Ing {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
//            detail();
//            pause();
//            resume();
            CalcTerminateFeeResponse response = calcTerminateFee();
            if (response != null) {
                termination(response);
            }
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

    public static void pause() {
        try {
            OrderSubscriptionPauseParams params = new OrderSubscriptionPauseParams();
//            OrderSubscriptionListParams params = new OrderSubscriptionListParams();
//            params.sAt = "2025-05-20";

            // 현재 날짜에서 20일 후 계산
            LocalDate futureDate = LocalDate.now().plusDays(20);
            String formattedDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


            params.orderSubscriptionId = "686dc2f2b0eacea5cd974ca2";
            params.expectedResumeAt = formattedDate;
            params.reason = "내 마음";


            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.pause(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription pause success: " + res.getData());
            } else {
                System.out.println("orderSubscription pause false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resume() {
        try {
            OrderSubscriptionResumeParams params = new OrderSubscriptionResumeParams();

            params.orderSubscriptionId = "686dc2f2b0eacea5cd974ca2";
            params.reason = "내 마음 리턴";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.resume(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription resume success: " + res.getData());
            } else {
                System.out.println("orderSubscription resume false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CalcTerminateFeeResponse calcTerminateFee() {
        try {
//            String orderSubscriptionId = "68709fa0b0eacea5cd974f2d";
            String orderNumber = "25071173847426287179";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.calculateTerminationFeeByOrderNumber(orderNumber);
            if(res.isSuccess()) {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();

                String json = gson.toJson(res.getData());
                CalcTerminateFeeResponse response = gson.fromJson(json, CalcTerminateFeeResponse.class);
                System.out.println("orderSubscription calcTerminateFee success: " + json);
                return response;
            } else {
                System.out.println("orderSubscription calcTerminateFee false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void termination(CalcTerminateFeeResponse calcResponse) {
        try {
            OrderSubscriptionTerminationParams params = new OrderSubscriptionTerminationParams();

            params.orderSubscriptionId = calcResponse.orderSubscriptionId;
            params.finalFee = calcResponse.finalFee;
            params.terminationFee = calcResponse.terminationFee;;
            params.serviceEndAt = calcResponse.serviceEndAt;
            params.lastBillRefundPrice = calcResponse.lastBillRefundPrice;
            params.reason = "중도 해지 요청";

            // terminationFee 등 필요한 값이 있으면 아래처럼 추가
            // params.terminationFee = calcResponse.terminationFee;
//            params.reason = "내 마음 리턴";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.termination(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription termination success: " + res.getData());
            } else {
                System.out.println("orderSubscription termination false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 중도인수 요청
    public static void purchase() {
        try {
            OrderSubscriptionPurchaseParams params = new OrderSubscriptionPurchaseParams();

            params.orderSubscriptionId = "686dc2f2b0eacea5cd974ca2";
            params.price = 10000.0;
            params.taxFreePrice = 0.0;
            params.reason = "중도 인수 요청";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.purchase(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription purchase success: " + res.getData());
            } else {
                System.out.println("orderSubscription purchase false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구독 이전/승계 요청
    public static void transfer() {
        try {
            OrderSubscriptionTransferParams params = new OrderSubscriptionTransferParams();

            params.orderSubscriptionId = "686dc2f2b0eacea5cd974ca2";
            params.newUserId = "6870a0c1b0eacea5cd974f3e";
            params.newUsername = "홍길동";
            params.newUserEmail = "help@bootpay.co.kr";
            params.newUserPhone = "01000000000";
            params.reason = "구독 승계 요청";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.transfer(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription transfer success: " + res.getData());
            } else {
                System.out.println("orderSubscription transfer false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
