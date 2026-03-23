package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.pg.model.request.Subscribe;
import kr.co.bootpay.pg.model.request.SubscribePayload;
import kr.co.bootpay.pg.model.request.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Billing (빌링키 발급/조회/삭제, 정기결제)")
class PgBillingTest {

    private static Bootpay bootpay;

    @BeforeAll
    static void setUp() throws Exception {
        bootpay = TestConfig.createBootpayWithToken();
    }

    // ── 빌링키 발급 (카드) ────────────────────────────────────

    @Test
    @DisplayName("getBillingKey - 카드 빌링키 발급")
    void testGetBillingKey() throws Exception {
        Subscribe subscribe = new Subscribe();
        subscribe.pg = "nicepay";
        subscribe.orderName = "테스트 정기결제 상품";
        subscribe.subscriptionId = "test_sub_" + System.currentTimeMillis();
        subscribe.cardNo = "5570********1074";
        subscribe.cardPw = "**";
        subscribe.cardIdentityNo = "******";
        subscribe.cardExpireYear = "**";
        subscribe.cardExpireMonth = "**";
        subscribe.price = 0.0;
        subscribe.taxFree = 0.0;

        User user = new User();
        user.username = "테스트유저";
        user.phone = "01012345678";
        subscribe.user = user;

        HashMap<String, Object> res = bootpay.getBillingKey(subscribe);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getBillingKey 응답: " + res);
    }

    // ── 빌링키 발급 (계좌이체) ────────────────────────────────

    @Test
    @DisplayName("getBillingKeyTransfer - 계좌 자동이체 빌링키 발급")
    void testGetBillingKeyTransfer() throws Exception {
        Subscribe subscribe = new Subscribe();
        subscribe.pg = "nicepay";
        subscribe.orderName = "테스트 자동이체 상품";
        subscribe.subscriptionId = "test_transfer_sub_" + System.currentTimeMillis();
        subscribe.authType = "ARS";
        subscribe.username = "홍길동";
        subscribe.bankName = "국민은행";
        subscribe.bankAccount = "1234567890";
        subscribe.identityNo = "900101";
        subscribe.phone = "01012345678";

        HashMap<String, Object> res = bootpay.getBillingKeyTransfer(subscribe);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getBillingKeyTransfer 응답: " + res);
    }

    // ── 빌링키 조회 ──────────────────────────────────────────

    @Test
    @DisplayName("lookupBillingKey - receipt_id로 빌링키 조회")
    void testLookupBillingKey() throws Exception {
        String receiptId = "test_receipt_id_0001";
        HashMap<String, Object> res = bootpay.lookupBillingKey(receiptId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("lookupBillingKey 응답: " + res);
    }

    @Test
    @DisplayName("lookupBillingKeyByKey - billing_key로 빌링키 조회")
    void testLookupBillingKeyByKey() throws Exception {
        String billingKey = "test_billing_key_0001";
        HashMap<String, Object> res = bootpay.lookupBillingKeyByKey(billingKey);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("lookupBillingKeyByKey 응답: " + res);
    }

    // ── 빌링키 삭제 ──────────────────────────────────────────

    @Test
    @DisplayName("destroyBillingKey - 빌링키 삭제")
    void testDestroyBillingKey() throws Exception {
        String billingKey = "test_billing_key_0001";
        HashMap<String, Object> res = bootpay.destroyBillingKey(billingKey);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("destroyBillingKey 응답: " + res);
    }

    // ── 정기결제 실행 ─────────────────────────────────────────

    @Test
    @DisplayName("requestSubscribe - 정기결제 실행")
    void testRequestSubscribe() throws Exception {
        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "test_billing_key_0001";
        payload.orderName = "테스트 정기결제";
        payload.price = 1000.0;
        payload.taxFree = 0.0;
        payload.orderId = "test_order_" + System.currentTimeMillis();

        User user = new User();
        user.username = "테스트유저";
        user.phone = "01012345678";
        payload.user = user;

        HashMap<String, Object> res = bootpay.requestSubscribe(payload);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestSubscribe 응답: " + res);
    }

    // ── 정기결제 예약 ─────────────────────────────────────────

    @Test
    @DisplayName("reserveSubscribe - 정기결제 예약")
    void testReserveSubscribe() throws Exception {
        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "test_billing_key_0001";
        payload.orderName = "테스트 예약 결제";
        payload.price = 1000.0;
        payload.taxFree = 0.0;
        payload.orderId = "test_reserve_" + System.currentTimeMillis();
        payload.reserveExecuteAt = "2026-12-31T23:59:59 +0900";

        HashMap<String, Object> res = bootpay.reserveSubscribe(payload);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("reserveSubscribe 응답: " + res);
    }

    @Test
    @DisplayName("reserveSubscribeLookup - 예약 결제 조회")
    void testReserveSubscribeLookup() throws Exception {
        String reserveId = "test_reserve_id_0001";
        HashMap<String, Object> res = bootpay.reserveSubscribeLookup(reserveId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("reserveSubscribeLookup 응답: " + res);
    }

    @Test
    @DisplayName("reserveCancelSubscribe - 예약 결제 취소")
    void testReserveCancelSubscribe() throws Exception {
        String reserveId = "test_reserve_id_0001";
        HashMap<String, Object> res = bootpay.reserveCancelSubscribe(reserveId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("reserveCancelSubscribe 응답: " + res);
    }

    // ── 계좌이체 빌링키 발행 ──────────────────────────────────

    @Test
    @DisplayName("publishBillingKeyTransfer - 계좌이체 빌링키 발행 확인")
    void testPublishBillingKeyTransfer() throws Exception {
        String receiptId = "test_receipt_id_0001";
        HashMap<String, Object> res = bootpay.publishBillingKeyTransfer(receiptId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("publishBillingKeyTransfer 응답: " + res);
    }
}
