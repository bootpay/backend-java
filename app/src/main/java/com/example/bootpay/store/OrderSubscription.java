package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;

/**
 * 구독(OrderSubscription) API 사용 예제
 *
 * 모든 API에서 orderSubscriptionId 파라미터는 다음을 지원합니다:
 * - 부트페이 ID: "687a1b2c3d4e5f6789012345" (24자리 ObjectId)
 * - external_uid: "my_subscription_12345" (가맹점 지정 고유 ID)
 *
 * external_uid를 사용하면 가맹점 자체 시스템의 ID로 구독을 조회/관리할 수 있습니다.
 */
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

    /**
     * 부트페이 ID로 구독 상세 조회
     */
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

    /**
     * external_uid(가맹점 고유 ID)로 구독 상세 조회
     * 가맹점에서 지정한 external_uid로도 조회 가능합니다.
     */
    public static void detailByExternalUid() {
        try {
            // external_uid를 사용하여 조회
            String externalUid = "my_subscription_12345";
            BootpayStoreResponse res = bootpayStore.orderSubscription.detail(externalUid);
            if(res.isSuccess()) {
                System.out.println("orderSubscription detail by external_uid success: " + res.getData());
            } else {
                System.out.println("orderSubscription detail by external_uid false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 구독 승인 (관리자)
     * 승인 대기 중인 구독을 승인 처리합니다.
     */
    public static void approve() {
        try {
            String orderSubscriptionId = "67e5100c5ec892162491d108";
            // 사유 없이 승인
            BootpayStoreResponse res = bootpayStore.orderSubscription.approve(orderSubscriptionId);
            if(res.isSuccess()) {
                System.out.println("orderSubscription approve success: " + res.getData());
            } else {
                System.out.println("orderSubscription approve false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 구독 승인 with 사유 (관리자)
     * external_uid로도 승인 가능합니다.
     */
    public static void approveWithReason() {
        try {
            // external_uid 사용 예시
            String externalUid = "my_subscription_12345";
            String reason = "심사 완료";
            BootpayStoreResponse res = bootpayStore.orderSubscription.approve(externalUid, reason);
            if(res.isSuccess()) {
                System.out.println("orderSubscription approve success: " + res.getData());
            } else {
                System.out.println("orderSubscription approve false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 구독 거절 (관리자)
     * 거절 시 사유는 필수입니다.
     */
    public static void reject() {
        try {
            String orderSubscriptionId = "67e5100c5ec892162491d108";
            String reason = "심사 기준 미달";  // 필수
            BootpayStoreResponse res = bootpayStore.orderSubscription.reject(orderSubscriptionId, reason);
            if(res.isSuccess()) {
                System.out.println("orderSubscription reject success: " + res.getData());
            } else {
                System.out.println("orderSubscription reject false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 부트페이 ID로 구독 정보 수정
     */
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

    /**
     * external_uid(가맹점 고유 ID)로 구독 정보 수정
     * orderSubscriptionId 필드에 external_uid를 사용할 수 있습니다.
     */
    public static void updateByExternalUid() {
        try {
            OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
            // external_uid 사용
            params.orderSubscriptionId = "my_subscription_12345";
            params.orderName = "구독계약 변경 테스트 (external_uid)";

            BootpayStoreResponse res = bootpayStore.asSupervisor().orderSubscription.update(params);
            if(res.isSuccess()) {
                System.out.println("orderSubscription update by external_uid success: " + res.getData());
            } else {
                System.out.println("orderSubscription update by external_uid false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

