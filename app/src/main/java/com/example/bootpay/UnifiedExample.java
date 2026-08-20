package com.example.bootpay;

import kr.co.bootpay.common.BootpayMode;
import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.common.BootpayRole;
import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.store.BootpayCommerce;
import kr.co.bootpay.store.model.request.user.UserListParams;

/**
 * 통일 API (3.3.0~) 예제.
 *
 * <p>PG 와 Commerce 를 같은 형태로 — 빌더로 생성하고, 모듈로 호출하고, {@link BootpayResponse} 로
 * 응답받습니다. 기존 표면({@code BootpayExample}, {@code store/*})은 그대로 동작하며, 이 예제는
 * 새로 짜는 코드에서 권장하는 형태를 보여줍니다.</p>
 */
public class UnifiedExample {

    public static void main(String[] args) {
        BootpayMode mode = BootpayMode.of(Config.CURRENT_ENV);

        pgExample(mode);
        commerceExample(mode);
    }

    private static void pgExample(BootpayMode mode) {
        Bootpay bootpay = Bootpay.builder()
                .clientKey(Config.PG.getClientKey())
                .secretKey(Config.PG.getSecretKey())
                .mode(mode)
                .build();

        try {
            print("pg.issueAccessToken", bootpay.issueAccessToken());
            print("pg.payment.get", bootpay.payment.get(Config.TestData.RECEIPT_ID));
            print("pg.billing.get", bootpay.billing.get(Config.TestData.BILLING_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void commerceExample(BootpayMode mode) {
        BootpayCommerce bootpay = BootpayCommerce.builder()
                .clientKey(Config.Commerce.getClientKey())
                .secretKey(Config.Commerce.getSecretKey())
                .mode(mode)
                .role(BootpayRole.USER)
                .build();

        try {
            print("commerce.issueAccessToken", bootpay.issueAccessToken());

            UserListParams params = new UserListParams();
            params.page = 1;
            params.limit = 10;
            print("commerce.user.list", bootpay.user.list(params));

            print("commerce.store.detail", bootpay.store.detail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 성공 / 실패 판정이 PG 와 Commerce 에서 동일하다는 점을 보여주는 출력 헬퍼.
     */
    private static void print(String label, BootpayResponse res) {
        if (res.isSuccess()) {
            System.out.println(label + " success: " + res.getData());
        } else {
            System.out.println(label + " failed: " + res.getErrorCode() + " " + res.getMessage());
        }
    }
}
