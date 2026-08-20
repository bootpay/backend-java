package kr.co.bootpay.pg;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.model.request.Authentication;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.CashReceipt;
import kr.co.bootpay.pg.model.request.Shipping;
import kr.co.bootpay.pg.model.request.Subscribe;
import kr.co.bootpay.pg.model.request.SubscribePayload;
import kr.co.bootpay.pg.model.request.UserToken;
import kr.co.bootpay.pg.model.request.WalletPayment;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 신규 모듈 표면(3.3.0) 이 기존 flat 표면과 <b>완전히 같은 HTTP 요청</b>을 만드는지 검증한다.
 *
 * <p>기존 메서드를 호출해 요청을 캡처하고, 대응하는 신규 모듈 메서드를 호출해 다시 캡처한 뒤
 * method / path / query / body 를 전부 대조한다. 하나라도 어긋나면 신규 표면이 기존 동작을
 * 바꿨다는 뜻이므로 실패한다. 네트워크는 필요 없다.</p>
 */
@DisplayName("PG API - 신규 모듈 표면 ↔ 기존 표면 요청 동등성")
class PgModuleParityTest {

    private static HttpServer server;
    private static Bootpay bootpay;

    private static volatile String lastMethod;
    private static volatile String lastPath;
    private static volatile String lastQuery;
    private static volatile String lastBody;

    @BeforeAll
    static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", PgModuleParityTest::capture);
        server.start();

        bootpay = Bootpay.withClientKey("test_ck", "test_sk", "PRODUCTION");
        bootpay.baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/v2/";
    }

    @AfterAll
    static void tearDown() {
        if (server != null) server.stop(0);
    }

    private static void capture(HttpExchange exchange) throws java.io.IOException {
        lastMethod = exchange.getRequestMethod();
        lastPath = exchange.getRequestURI().getPath();
        lastQuery = exchange.getRequestURI().getQuery();
        lastBody = IOUtils.toString(exchange.getRequestBody(), "UTF-8");

        byte[] response = "{\"ok\":true}".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(response);
        }
    }

    /** 호출 하나가 만든 요청의 지문. */
    private static final class Request {
        final String method;
        final String path;
        final String query;
        final String body;

        Request(String method, String path, String query, String body) {
            this.method = method;
            this.path = path;
            this.query = query;
            this.body = body;
        }

        @Override
        public String toString() {
            return method + " " + path + (query == null ? "" : "?" + query)
                    + (body == null || body.isEmpty() ? "" : " " + body);
        }
    }

    private interface Call {
        void run() throws Exception;
    }

    private static Request record(Call call) throws Exception {
        lastMethod = null;
        lastPath = null;
        lastQuery = null;
        lastBody = null;
        call.run();
        return new Request(lastMethod, lastPath, lastQuery, lastBody);
    }

    /**
     * 기존 호출과 신규 호출이 같은 요청을 만드는지 대조한다.
     */
    private static void assertSameRequest(String label, Call legacy, Call modern) throws Exception {
        Request before = record(legacy);
        Request after = record(modern);

        assertNotNull(before.method, label + " — 기존 호출이 요청을 만들지 않았다");
        assertEquals(before.method, after.method, label + " — HTTP method 불일치");
        assertEquals(before.path, after.path, label + " — path 불일치");
        assertEquals(before.query, after.query, label + " — query 불일치");
        assertEquals(before.body, after.body, label + " — body 불일치");
    }

    // ========================================
    // payment
    // ========================================

    @Test
    @DisplayName("payment.get / getByOrderId / confirm / cancel / methods")
    void paymentModuleMatchesLegacy() throws Exception {
        assertSameRequest("getReceipt",
                () -> bootpay.getReceipt("receipt_1"),
                () -> bootpay.payment.get("receipt_1"));

        assertSameRequest("getReceipt(lookupUserData)",
                () -> bootpay.getReceipt("receipt_1", true),
                () -> bootpay.payment.get("receipt_1", true));

        assertSameRequest("lookupOrderId",
                () -> bootpay.lookupOrderId("order_1"),
                () -> bootpay.payment.getByOrderId("order_1"));

        assertSameRequest("confirm",
                () -> bootpay.confirm("receipt_1"),
                () -> bootpay.payment.confirm("receipt_1"));

        assertSameRequest("lookupPaymentMethods",
                () -> bootpay.lookupPaymentMethods(),
                () -> bootpay.payment.methods());

        assertSameRequest("receiptCancel",
                () -> bootpay.receiptCancel(cancel()),
                () -> bootpay.payment.cancel(cancel()));
    }

    @Test
    @DisplayName("payment.link")
    void paymentLinkMatchesLegacy() throws Exception {
        assertSameRequest("requestLink",
                () -> bootpay.requestLink(new kr.co.bootpay.pg.model.request.Payload()),
                () -> bootpay.payment.link(new kr.co.bootpay.pg.model.request.Payload()));
    }

    // ========================================
    // billing
    // ========================================

    @Test
    @DisplayName("billing.get / getByReceiptId / getSequential / destroy")
    void billingLookupMatchesLegacy() throws Exception {
        assertSameRequest("lookupBillingKeyByKey",
                () -> bootpay.lookupBillingKeyByKey("billing_1"),
                () -> bootpay.billing.get("billing_1"));

        assertSameRequest("lookupBillingKey",
                () -> bootpay.lookupBillingKey("receipt_1"),
                () -> bootpay.billing.getByReceiptId("receipt_1"));

        assertSameRequest("lookupSequentialBillingKey",
                () -> bootpay.lookupSequentialBillingKey("widget_1", "billing_1", "user_1"),
                () -> bootpay.billing.getSequential("widget_1", "billing_1", "user_1"));

        assertSameRequest("destroyBillingKey",
                () -> bootpay.destroyBillingKey("billing_1"),
                () -> bootpay.billing.destroy("billing_1"));
    }

    @Test
    @DisplayName("billing.issue / issueTransfer / publishTransfer")
    void billingIssueMatchesLegacy() throws Exception {
        assertSameRequest("getBillingKey",
                () -> bootpay.getBillingKey(subscribe()),
                () -> bootpay.billing.issue(subscribe()));

        assertSameRequest("getBillingKeyTransfer",
                () -> bootpay.getBillingKeyTransfer(transferSubscribe()),
                () -> bootpay.billing.issueTransfer(transferSubscribe()));

        assertSameRequest("publishBillingKeyTransfer",
                () -> bootpay.publishBillingKeyTransfer("receipt_1"),
                () -> bootpay.billing.publishTransfer("receipt_1"));
    }

    @Test
    @DisplayName("billing.pay / reserve / getReserve / cancelReserve")
    void billingPayMatchesLegacy() throws Exception {
        assertSameRequest("requestSubscribe",
                () -> bootpay.requestSubscribe(subscribePayload()),
                () -> bootpay.billing.pay(subscribePayload()));

        assertSameRequest("reserveSubscribe",
                () -> bootpay.reserveSubscribe(reservePayload()),
                () -> bootpay.billing.reserve(reservePayload()));

        assertSameRequest("reserveSubscribeLookup",
                () -> bootpay.reserveSubscribeLookup("reserve_1"),
                () -> bootpay.billing.getReserve("reserve_1"));

        assertSameRequest("reserveCancelSubscribe",
                () -> bootpay.reserveCancelSubscribe("reserve_1"),
                () -> bootpay.billing.cancelReserve("reserve_1"));
    }

    // ========================================
    // auth / cash / escrow / user / wallet
    // ========================================

    @Test
    @DisplayName("auth.request / confirm / realarm / certificate")
    void authModuleMatchesLegacy() throws Exception {
        assertSameRequest("requestAuthentication",
                () -> bootpay.requestAuthentication(authentication()),
                () -> bootpay.auth.request(authentication()));

        assertSameRequest("confirmAuthentication",
                () -> bootpay.confirmAuthentication("receipt_1", "123456"),
                () -> bootpay.auth.confirm("receipt_1", "123456"));

        assertSameRequest("realarmAuthentication",
                () -> bootpay.realarmAuthentication("receipt_1"),
                () -> bootpay.auth.realarm("receipt_1"));

        assertSameRequest("certificate",
                () -> bootpay.certificate("receipt_1"),
                () -> bootpay.auth.certificate("receipt_1"));
    }

    @Test
    @DisplayName("cash.request / cancel / requestByBootpay / cancelByBootpay")
    void cashModuleMatchesLegacy() throws Exception {
        assertSameRequest("requestCashReceipt",
                () -> bootpay.requestCashReceipt(standaloneCashReceipt()),
                () -> bootpay.cash.request(standaloneCashReceipt()));

        assertSameRequest("requestCashReceiptCancel",
                () -> bootpay.requestCashReceiptCancel(cashCancel()),
                () -> bootpay.cash.cancel(cashCancel()));

        assertSameRequest("requestCashReceiptByBootpay",
                () -> bootpay.requestCashReceiptByBootpay(bootpayCashReceipt()),
                () -> bootpay.cash.requestByBootpay(bootpayCashReceipt()));

        assertSameRequest("requestCashReceiptCancelByBootpay",
                () -> bootpay.requestCashReceiptCancelByBootpay(cashCancel()),
                () -> bootpay.cash.cancelByBootpay(cashCancel()));
    }

    @Test
    @DisplayName("escrow.shippingStart / user.token / wallet.list / wallet.pay")
    @SuppressWarnings("deprecation")
    void remainingModulesMatchLegacy() throws Exception {
        assertSameRequest("shippingStart",
                () -> bootpay.shippingStart(shipping()),
                () -> bootpay.escrow.shippingStart(shipping()));

        assertSameRequest("getUserToken",
                () -> bootpay.getUserToken(userToken()),
                () -> bootpay.user.token(userToken()));

        assertSameRequest("getUserWallets",
                () -> bootpay.getUserWallets("user_1", true),
                () -> bootpay.wallet.list("user_1", true));

        assertSameRequest("requestWalletPayment",
                () -> bootpay.requestWalletPayment(walletPayment()),
                () -> bootpay.wallet.pay(walletPayment()));
    }

    // ========================================
    // 응답 변환
    // ========================================

    @Test
    @DisplayName("모듈 응답은 기존 HashMap 과 같은 본문을 담아야 한다")
    void moduleResponseCarriesSameBody() throws Exception {
        java.util.HashMap<String, Object> legacy = bootpay.getReceipt("receipt_1");
        BootpayResponse modern = bootpay.payment.get("receipt_1");

        assertTrue(modern.isSuccess(), "error_code 가 없으므로 성공이어야 한다");
        assertEquals(legacy.get("ok"), modern.get("ok"));
        assertEquals(legacy.get("http_status"), modern.asMap().get("http_status"),
                "asMap() 은 가공 전 원본을 그대로 노출해야 한다");
        assertTrue(modern.getData().containsKey("ok"));
        assertTrue(!modern.getData().containsKey("http_status"),
                "getData() 는 응답 본문만 담아야 한다 (http_status 제외)");
    }

    // ========================================
    // fixtures
    // ========================================

    private static Cancel cancel() {
        Cancel cancel = new Cancel();
        cancel.receiptId = "receipt_1";
        cancel.cancelUsername = "관리자";
        cancel.cancelMessage = "테스트 취소";
        return cancel;
    }

    private static Cancel cashCancel() {
        return cancel();
    }

    private static Subscribe subscribe() {
        Subscribe subscribe = new Subscribe();
        subscribe.pg = "나이스페이";
        subscribe.orderName = "정기결제 테스트";
        subscribe.subscriptionId = "subscription_1";
        subscribe.cardNo = "5570000000001074";
        subscribe.cardPw = "00";
        subscribe.cardIdentityNo = "901004";
        subscribe.cardExpireYear = "27";
        subscribe.cardExpireMonth = "12";
        return subscribe;
    }

    private static Subscribe transferSubscribe() {
        Subscribe subscribe = new Subscribe();
        subscribe.pg = "나이스페이";
        subscribe.orderName = "자동이체 테스트";
        subscribe.subscriptionId = "subscription_1";
        subscribe.bankName = "국민은행";
        subscribe.bankAccount = "12345678901234";
        subscribe.username = "홍길동";
        subscribe.identityNo = "901004";
        return subscribe;
    }

    private static SubscribePayload subscribePayload() {
        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "billing_1";
        payload.orderName = "정기결제 테스트";
        payload.orderId = "order_1";
        payload.price = 1000.0;
        return payload;
    }

    private static SubscribePayload reservePayload() {
        SubscribePayload payload = subscribePayload();
        payload.reserveExecuteAt = "2030-01-01T00:00:00+09:00";
        return payload;
    }

    private static Authentication authentication() {
        Authentication authentication = new Authentication();
        authentication.pg = "다날";
        authentication.method = "sms";
        authentication.orderName = "본인인증 테스트";
        authentication.authenticationId = "authentication_1";
        authentication.identityNo = "9010041";
        authentication.username = "홍길동";
        authentication.carrier = "SKT";
        authentication.phone = "01012341234";
        return authentication;
    }

    private static CashReceipt standaloneCashReceipt() {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.pg = "나이스페이";
        cashReceipt.orderName = "현금영수증 테스트";
        cashReceipt.orderId = "order_1";
        cashReceipt.identityNo = "01012341234";
        cashReceipt.cashReceiptType = "소득공제";
        cashReceipt.price = 1000.0;
        return cashReceipt;
    }

    private static CashReceipt bootpayCashReceipt() {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.receiptId = "receipt_1";
        cashReceipt.username = "홍길동";
        cashReceipt.phone = "01012341234";
        cashReceipt.identityNo = "01012341234";
        cashReceipt.cashReceiptType = "소득공제";
        return cashReceipt;
    }

    private static Shipping shipping() {
        Shipping shipping = new Shipping();
        shipping.receiptId = "receipt_1";
        shipping.deliveryCorp = "CJ대한통운";
        shipping.trackingNumber = "1234567890";
        return shipping;
    }

    private static UserToken userToken() {
        UserToken userToken = new UserToken();
        userToken.userId = "user_1";
        return userToken;
    }

    private static WalletPayment walletPayment() {
        WalletPayment walletPayment = new WalletPayment();
        walletPayment.userId = "user_1";
        walletPayment.orderName = "월렛 결제 테스트";
        walletPayment.orderId = "order_1";
        walletPayment.price = 1000.0;
        return walletPayment;
    }
}
