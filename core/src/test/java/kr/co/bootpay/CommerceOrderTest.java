package kr.co.bootpay;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commerce API - Order (주문, 주문취소, 구독, 구독청구)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommerceOrderTest {

    private static BootpayStore store;

    @BeforeAll
    static void setUp() throws Exception {
        store = TestConfig.createBootpayStoreWithToken();
    }

    // ══════════════════════════════════════════════════════════
    // Order (주문)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("order.list - 주문 목록 조회")
    void testOrderList() throws Exception {
        OrderListParams params = new OrderListParams();
        params.page = 1;
        params.limit = 10;

        BootpayStoreResponse res = store.order.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("order.list 응답: " + res);
    }

    @Test
    @Order(2)
    @DisplayName("order.list - 상태 필터 조회")
    void testOrderListWithStatus() throws Exception {
        OrderListParams params = new OrderListParams();
        params.page = 1;
        params.limit = 10;
        params.status = Arrays.asList(1, 2);

        BootpayStoreResponse res = store.order.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("order.list (status) 응답: " + res);
    }

    @Test
    @Order(3)
    @DisplayName("order.detail - 주문 상세 조회")
    void testOrderDetail() throws Exception {
        BootpayStoreResponse res = store.order.detail("test_order_id");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("order.detail 응답: " + res);
    }

    @Test
    @Order(4)
    @DisplayName("order.month - 월별 주문 조회")
    void testOrderMonth() throws Exception {
        BootpayStoreResponse res = store.order.month("test_user_group_id", "2026-03");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("order.month 응답: " + res);
    }

    // ══════════════════════════════════════════════════════════
    // OrderCancel (주문 취소)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(5)
    @DisplayName("orderCancel.list - 주문 취소 목록 조회")
    void testOrderCancelList() throws Exception {
        OrderCancelListParams params = new OrderCancelListParams();

        BootpayStoreResponse res = store.orderCancel.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("orderCancel.list 응답: " + res);
    }

    @Test
    @Order(6)
    @DisplayName("orderCancel.withdraw - 주문 취소 철회")
    void testOrderCancelWithdraw() throws Exception {
        BootpayStoreResponse res = store.orderCancel.withdraw("test_cancel_history_id");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("orderCancel.withdraw 응답: " + res);
    }

    // ══════════════════════════════════════════════════════════
    // OrderSubscription (정기구독)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(7)
    @DisplayName("orderSubscription.list - 정기구독 목록 조회")
    void testOrderSubscriptionList() throws Exception {
        OrderSubscriptionListParams params = new OrderSubscriptionListParams();
        params.page = 1;
        params.limit = 10;

        BootpayStoreResponse res = store.orderSubscription.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("orderSubscription.list 응답: " + res);
    }

    @Test
    @Order(8)
    @DisplayName("orderSubscription.detail - 정기구독 상세 조회")
    void testOrderSubscriptionDetail() throws Exception {
        BootpayStoreResponse res = store.orderSubscription.detail("test_subscription_id");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("orderSubscription.detail 응답: " + res);
    }

    // ══════════════════════════════════════════════════════════
    // OrderSubscription - RequestIng (구독 요청 진행 중)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(9)
    @DisplayName("orderSubscription.requestIng.calculateTerminationFee - 해지 위약금 계산")
    void testCalculateTerminationFee() throws Exception {
        BootpayStoreResponse res = store.orderSubscription.requestIng.calculateTerminationFee("test_subscription_id");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("calculateTerminationFee 응답: " + res);
    }

    @Test
    @Order(10)
    @DisplayName("orderSubscription.requestIng.calculateTerminationFeeByOrderNumber - 주문번호로 해지 위약금 계산")
    void testCalculateTerminationFeeByOrderNumber() throws Exception {
        BootpayStoreResponse res = store.orderSubscription.requestIng.calculateTerminationFeeByOrderNumber("test_order_number");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("calculateTerminationFeeByOrderNumber 응답: " + res);
    }

    // ══════════════════════════════════════════════════════════
    // OrderSubscriptionBill (정기구독 청구)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(11)
    @DisplayName("orderSubscriptionBill.list - 구독 청구 목록 조회")
    void testOrderSubscriptionBillList() throws Exception {
        OrderSubscriptionBillListParams params = new OrderSubscriptionBillListParams();
        params.page = 1;
        params.limit = 10;

        BootpayStoreResponse res = store.orderSubscriptionBill.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("orderSubscriptionBill.list 응답: " + res);
    }

    @Test
    @Order(12)
    @DisplayName("orderSubscriptionBill.detail - 구독 청구 상세 조회")
    void testOrderSubscriptionBillDetail() throws Exception {
        BootpayStoreResponse res = store.orderSubscriptionBill.detail("test_bill_id");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("orderSubscriptionBill.detail 응답: " + res);
    }

    // ══════════════════════════════════════════════════════════
    // Invoice (청구서)
    // ══════════════════════════════════════════════════════════

    @Test
    @Order(13)
    @DisplayName("invoice.list - 청구서 목록 조회")
    void testInvoiceList() throws Exception {
        ListParams params = new ListParams();
        params.page = 1;
        params.limit = 10;

        BootpayStoreResponse res = store.invoice.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("invoice.list 응답: " + res);
    }

    @Test
    @Order(14)
    @DisplayName("invoice.detail - 청구서 상세 조회")
    void testInvoiceDetail() throws Exception {
        BootpayStoreResponse res = store.invoice.detail("test_invoice_id");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("invoice.detail 응답: " + res);
    }
}
