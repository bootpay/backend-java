package com.example.bootpay.store.order_subscription.request;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.response.orderSubscription.request.ing.CalcTerminateFeeResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Ing {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
//            list();
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
            String orderSubscriptionId = "686dc2f2b0eacea5cd974ca2";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.calculateTerminationFee(orderSubscriptionId);
            if(res.isSuccess()) {
                Gson gson = new Gson();
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
}

