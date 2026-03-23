package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.CashReceipt;
import kr.co.bootpay.pg.model.request.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Cash Receipt (현금영수증)")
class PgCashReceiptTest {

    private static Bootpay bootpay;

    @BeforeAll
    static void setUp() throws Exception {
        bootpay = TestConfig.createBootpayWithToken();
    }

    // ── 결제건에 대한 현금영수증 발행 ─────────────────────────

    @Test
    @DisplayName("requestCashReceiptByBootpay - 결제건 현금영수증 발행")
    void testRequestCashReceiptByBootpay() throws Exception {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.receiptId = "test_receipt_id_0001";
        cashReceipt.identityNo = "01012345678";
        cashReceipt.cashReceiptType = "소득공제";
        cashReceipt.username = "테스트유저";
        cashReceipt.email = "test@example.com";
        cashReceipt.phone = "01012345678";

        HashMap<String, Object> res = bootpay.requestCashReceiptByBootpay(cashReceipt);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestCashReceiptByBootpay 응답: " + res);
    }

    // ── 결제건에 대한 현금영수증 취소 ─────────────────────────

    @Test
    @DisplayName("requestCashReceiptCancelByBootpay - 결제건 현금영수증 취소")
    void testRequestCashReceiptCancelByBootpay() throws Exception {
        Cancel cancel = new Cancel();
        cancel.receiptId = "test_receipt_id_0001";
        cancel.cancelUsername = "테스트유저";
        cancel.cancelMessage = "현금영수증 취소 테스트";

        HashMap<String, Object> res = bootpay.requestCashReceiptCancelByBootpay(cancel);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestCashReceiptCancelByBootpay 응답: " + res);
    }

    // ── 별건 현금영수증 발행 ──────────────────────────────────

    @Test
    @DisplayName("requestCashReceipt - 별건 현금영수증 발행 (소득공제)")
    void testRequestCashReceiptIncome() throws Exception {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.pg = "nicepay";
        cashReceipt.price = 10000.0;
        cashReceipt.taxFree = 0.0;
        cashReceipt.orderName = "테스트 별건 현금영수증";
        cashReceipt.cashReceiptType = "소득공제";
        cashReceipt.identityNo = "01012345678";
        cashReceipt.orderId = "test_cash_order_" + System.currentTimeMillis();

        User user = new User();
        user.username = "테스트유저";
        user.phone = "01012345678";
        cashReceipt.user = user;

        HashMap<String, Object> res = bootpay.requestCashReceipt(cashReceipt);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestCashReceipt (소득공제) 응답: " + res);
    }

    @Test
    @DisplayName("requestCashReceipt - 별건 현금영수증 발행 (지출증빙)")
    void testRequestCashReceiptExpense() throws Exception {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.pg = "nicepay";
        cashReceipt.price = 20000.0;
        cashReceipt.taxFree = 0.0;
        cashReceipt.orderName = "테스트 지출증빙 현금영수증";
        cashReceipt.cashReceiptType = "지출증빙";
        cashReceipt.identityNo = "1234567890";
        cashReceipt.orderId = "test_cash_exp_" + System.currentTimeMillis();

        HashMap<String, Object> res = bootpay.requestCashReceipt(cashReceipt);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestCashReceipt (지출증빙) 응답: " + res);
    }

    // ── 별건 현금영수증 취소 ──────────────────────────────────

    @Test
    @DisplayName("requestCashReceiptCancel - 별건 현금영수증 취소")
    void testRequestCashReceiptCancel() throws Exception {
        Cancel cancel = new Cancel();
        cancel.receiptId = "test_receipt_id_0001";
        cancel.cancelUsername = "테스트유저";
        cancel.cancelMessage = "별건 현금영수증 취소 테스트";

        HashMap<String, Object> res = bootpay.requestCashReceiptCancel(cancel);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestCashReceiptCancel 응답: " + res);
    }
}
