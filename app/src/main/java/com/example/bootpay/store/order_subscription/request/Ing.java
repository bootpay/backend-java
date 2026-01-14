package com.example.bootpay.store.order_subscription.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.response.orderSubscription.request.ing.CalcTerminateFeeResponse;
import kr.co.bootpay.store.model.response.orderSubscription.request.ing.CalcTerminateFeeResponseWrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Ing {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
//            pause();
//            resume();
//            calcTerminateFeeBySubscriptionId();
//            calcTerminateFeeByOrderNumber();
//            termination();
            terminationWithCalcResult();
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

    /**
     * orderSubscriptionId로 해지 수수료 계산
     */
    public static CalcTerminateFeeResponse calcTerminateFeeBySubscriptionId() {
        try {
            String orderSubscriptionId = "6966f2cf4cb8149d077125cd";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.calculateTerminationFee(orderSubscriptionId);
            if(res.isSuccess()) {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();

                String json = gson.toJson(res.getData());
                // 응답은 { termination: {...}, has_pending_request: boolean } 구조
                CalcTerminateFeeResponseWrapper wrapper = gson.fromJson(json, CalcTerminateFeeResponseWrapper.class);
                System.out.println("orderSubscription calcTerminateFee (by subscriptionId) success: " + json);
                return wrapper.termination;
            } else {
                System.out.println("orderSubscription calcTerminateFee (by subscriptionId) false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * orderNumber로 해지 수수료 계산
     */
    public static CalcTerminateFeeResponse calcTerminateFeeByOrderNumber() {
        try {
            String orderNumber = "25071173847426287179";

            BootpayStoreResponse res = bootpayStore.orderSubscription.requestIng.calculateTerminationFeeByOrderNumber(orderNumber);
            if(res.isSuccess()) {
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();

                String json = gson.toJson(res.getData());
                // 응답은 { termination: {...}, has_pending_request: boolean } 구조
                CalcTerminateFeeResponseWrapper wrapper = gson.fromJson(json, CalcTerminateFeeResponseWrapper.class);
                System.out.println("orderSubscription calcTerminateFee (by orderNumber) success: " + json);
                return wrapper.termination;
            } else {
                System.out.println("orderSubscription calcTerminateFee (by orderNumber) false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 직접 파라미터를 설정하여 해지 요청
     */
    public static void termination() {
        try {
            OrderSubscriptionTerminationParams params = new OrderSubscriptionTerminationParams();

            params.orderSubscriptionId = "6964abf14cb8149d077124e8";
            params.finalFee = 10000.0;
            params.terminationFee = 0.0;
            params.serviceEndAt = "2025-07-31";
            params.lastBillRefundPrice = 0.0;
            params.reason = "중도 해지 요청";

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

    /**
     * calculateTerminationFee 결과를 사용하여 해지 요청 (권장 방법)
     * - calculate 결과에서 terminationFee, lastBillRefundPrice, finalFee를 가져와서 params에 설정
     * - serviceEndAt은 오늘 + 1일로 설정
     */
    public static void terminationWithCalcResult() {
        try {
            // 1. 먼저 해지 수수료 계산
            CalcTerminateFeeResponse calcResult = calcTerminateFeeBySubscriptionId();
            if (calcResult == null) {
                System.out.println("calcTerminateFee 실패");
                return;
            }

            System.out.println("계산된 해지 수수료 정보:");
            System.out.println("  - orderSubscriptionId: " + calcResult.orderSubscriptionId);
            System.out.println("  - terminationFee: " + calcResult.terminationFee);
            System.out.println("  - lastBillRefundPrice: " + calcResult.lastBillRefundPrice);
            System.out.println("  - finalFee: " + calcResult.finalFee);
//            System.out.println("  - serviceEndAt: " + calcResult.serviceEndAt);

            // 2. 계산 결과를 사용하여 params 구성
            OrderSubscriptionTerminationParams params = new OrderSubscriptionTerminationParams();
            params.orderSubscriptionId = calcResult.orderSubscriptionId;
            params.terminationFee = calcResult.terminationFee;
            params.lastBillRefundPrice = calcResult.lastBillRefundPrice;
            params.finalFee = calcResult.finalFee;
            params.serviceEndAt = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // 오늘 + 1일
            params.reason = "중도 해지 요청";

            // 3. 해지 요청
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

