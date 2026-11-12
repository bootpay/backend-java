package com.example.bootpay.store;

import com.example.bootpay.Config;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.deposit.DepositCreateParams;
import kr.co.bootpay.store.model.request.deposit.DepositNotifyParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;

/**
 * 뱅크알람 입금대기 예제 (/v1/sdk/deposits)
 *
 * 모든 요청은 인스턴스 role 과 무관하게 supervisor 로 전송된다.
 * 웹훅 비밀값 발급·등록·취소는 서버 상태를 바꾸므로 main 에서는 실행하지 않는다.
 */
public class Deposit {

    static BootpayStore bootpayStore;

    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload(
                    Config.Commerce.getClientKey(),
                    Config.Commerce.getSecretKey()
            );
            bootpayStore = new BootpayStore(tokenPayload, Config.CURRENT_ENV);

            accounts();
//            webhookSecret();
//            create();
//            detail(System.getenv("BOOTPAY_TEST_COMMERCE_DEPOSIT_ID"));
//            cancel(System.getenv("BOOTPAY_TEST_COMMERCE_DEPOSIT_ID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 판매자 입금 계좌 목록 — 첫 계좌가 대표 계좌(primary: true)
    public static void accounts() {
        try {
            print("deposit accounts", bootpayStore.deposit.accounts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 웹훅 서명 비밀값 발급 — 원문은 이 응답에서만 받고, 다시 부르면 바뀐다
    public static void webhookSecret() {
        try {
            print("deposit webhookSecret", bootpayStore.deposit.webhookSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 입금대기 등록 — 판매자 입금 계좌가 등록돼 있어야 한다(18128)
    public static void create() {
        try {
            DepositCreateParams params = new DepositCreateParams();
            params.merchantOrderId = "sdk-test-" + System.currentTimeMillis();
            params.price = 1000;
            params.depositor = "홍길동";
            params.buyerName = "홍길동";
            params.buyerEmail = "buyer@example.com";
//            params.idempotencyKey = "DEPOSIT_CREATE_IDEMPOTENCY_KEY";

            // 구매자 알림톡은 유료이며 buyerPhone 이 필요하다
            DepositNotifyParams notify = new DepositNotifyParams();
            notify.depositGuide = true;
            notify.depositPaid = true;
            notify.link = "https://example.com/orders/ORDER_ID";
            params.buyerPhone = "01000000000";
            params.notify = notify;

            print("deposit create", bootpayStore.deposit.create(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 가맹점 주문번호 또는 부트페이 주문번호로 조회한다
    public static void detail(String id) {
        if (id == null || id.isEmpty()) return;
        try {
            print("deposit detail", bootpayStore.deposit.detail(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미 닫힌 주문은 상태를 바꾸지 않고 그대로 돌려준다
    public static void cancel(String id) {
        if (id == null || id.isEmpty()) return;
        try {
            print("deposit cancel", bootpayStore.deposit.cancel(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void print(String label, BootpayStoreResponse res) {
        if (res.isSuccess()) {
            System.out.println(label + " success: " + res.getData());
        } else {
            System.out.println(label + " false: " + res.getData());
        }
    }
}
