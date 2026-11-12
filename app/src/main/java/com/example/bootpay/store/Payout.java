package com.example.bootpay.store;

import com.example.bootpay.Config;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.payout.PayoutCreateParams;
import kr.co.bootpay.store.model.request.payout.PayoutListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.response.payout.CommercePayout;

/**
 * 입점사 지급 요청 예제 (/v1/sdk/payouts)
 *
 * 모든 요청은 인스턴스 role 과 무관하게 supervisor 로 전송된다.
 * create 는 실제 지급 원장을 만들므로 main 에서는 실행하지 않는다.
 */
public class Payout {

    static BootpayStore bootpayStore;

    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload(
                    Config.Commerce.getClientKey(),
                    Config.Commerce.getSecretKey()
            );
            bootpayStore = new BootpayStore(tokenPayload, Config.CURRENT_ENV);

            list();
//            create();
//            detail(System.getenv("BOOTPAY_TEST_COMMERCE_PAYOUT_ID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 지급 목록 — 전체 개수 없이 이번 페이지의 배열만 반환한다
    public static void list() {
        try {
            PayoutListParams params = new PayoutListParams();
            params.status = CommercePayout.Status.READY;
            params.page = 1;
            params.limit = 20;
            print("payout list", bootpayStore.payout.list(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 실제 지급 원장에 등록되므로 필요한 값을 확인한 뒤 명시적으로 호출한다
    public static void create() {
        try {
            PayoutCreateParams params = new PayoutCreateParams();
            params.subSellerId = "SUB_SELLER_ID";
            params.amount = 150000L;
            params.merchantPayoutId = "PAYOUT_" + System.currentTimeMillis();
            params.orderNumber = "ORDER_1234";
//            params.payoutDate = "2026-11-20";
//            params.idempotencyKey = "PAYOUT_CREATE_IDEMPOTENCY_KEY";
            print("payout create", bootpayStore.payout.create(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // merchant_payout_id 또는 지급 항목 id 로 조회한다
    public static void detail(String id) {
        if (id == null || id.isEmpty()) return;
        try {
            print("payout detail", bootpayStore.payout.detail(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void print(String label, BootpayStoreResponse res) {
        if (res.isSuccess()) {
            System.out.println(label + " success: " + res.getRawData());
        } else {
            System.out.println(label + " false: " + res.getRawData());
        }
    }
}
