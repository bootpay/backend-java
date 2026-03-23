package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.pg.model.request.Cancel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Payment (verify, cancel, confirm)")
class PgPaymentTest {

    private static Bootpay bootpay;

    @BeforeAll
    static void setUp() throws Exception {
        bootpay = TestConfig.createBootpayWithToken();
    }

    @Test
    @DisplayName("getReceipt - 결제 영수증 조회")
    void testGetReceipt() throws Exception {
        // 존재하지 않는 receiptId로 조회 - API 호출 자체가 정상 동작하는지 확인
        String receiptId = "test_receipt_id_0001";
        HashMap<String, Object> res = bootpay.getReceipt(receiptId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getReceipt 응답: " + res);
    }

    @Test
    @DisplayName("confirm - 결제 승인")
    void testConfirm() throws Exception {
        // 존재하지 않는 receiptId로 승인 요청
        String receiptId = "test_receipt_id_0001";
        HashMap<String, Object> res = bootpay.confirm(receiptId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("confirm 응답: " + res);
    }

    @Test
    @DisplayName("receiptCancel - 결제 취소 (전액)")
    void testReceiptCancelFull() throws Exception {
        Cancel cancel = new Cancel();
        cancel.receiptId = "test_receipt_id_0001";
        cancel.cancelMessage = "테스트 전액 취소";
        cancel.cancelUsername = "test_user";

        HashMap<String, Object> res = bootpay.receiptCancel(cancel);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("receiptCancel (전액) 응답: " + res);
    }

    @Test
    @DisplayName("receiptCancel - 결제 취소 (부분)")
    void testReceiptCancelPartial() throws Exception {
        Cancel cancel = new Cancel();
        cancel.receiptId = "test_receipt_id_0001";
        cancel.cancelPrice = 1000.0;
        cancel.cancelTaxFree = 0.0;
        cancel.cancelMessage = "테스트 부분 취소";
        cancel.cancelUsername = "test_user";
        cancel.cancelId = "partial_cancel_001";

        HashMap<String, Object> res = bootpay.receiptCancel(cancel);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("receiptCancel (부분) 응답: " + res);
    }

    @Test
    @DisplayName("certificate - 본인인증 결과 조회")
    void testCertificate() throws Exception {
        String receiptId = "test_receipt_id_0001";
        HashMap<String, Object> res = bootpay.certificate(receiptId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("certificate 응답: " + res);
    }

    @Test
    @DisplayName("lookupOrderId - 주문번호로 조회")
    void testLookupOrderId() throws Exception {
        String orderId = "test_order_id_0001";
        HashMap<String, Object> res = bootpay.lookupOrderId(orderId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("lookupOrderId 응답: " + res);
    }
}
