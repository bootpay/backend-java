package kr.co.bootpay.pg;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kr.co.bootpay.pg.model.request.CashReceipt;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NodeJS SDK 2.9.0 parity — PG wire-format 검증 (네트워크 불필요).
 */
@DisplayName("PG API - Wire Format (NodeJS 2.9.0 parity)")
class PgWireFormatTest {

    private static HttpServer server;
    private static Bootpay bootpay;

    private static volatile String lastMethod;
    private static volatile String lastPath;
    private static volatile String lastQuery;
    private static volatile String lastBody;

    @BeforeAll
    static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", PgWireFormatTest::capture);
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
        lastBody = readBody(exchange);

        byte[] response = "{\"ok\":true}".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(response);
        }
    }

    private static String readBody(HttpExchange exchange) throws java.io.IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream in = exchange.getRequestBody()) {
            byte[] chunk = new byte[4096];
            int read;
            while ((read = in.read(chunk)) != -1) buffer.write(chunk, 0, read);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("lookupSequentialBillingKey - GET subscribe/sequential_billing_key/{billing_key}?widget_key=&user_id=")
    void testLookupSequentialBillingKey() throws Exception {
        bootpay.lookupSequentialBillingKey("widget_key_1", "billing_key_1", "user 1");

        assertEquals("GET", lastMethod);
        assertEquals("/v2/subscribe/sequential_billing_key/billing_key_1", lastPath);
        assertTrue(lastQuery.contains("widget_key=widget_key_1"), lastQuery);
        assertTrue(lastQuery.contains("user_id=user"), "user_id URL 인코딩 전송: " + lastQuery);
    }

    @Test
    @DisplayName("lookupSequentialBillingKey - 필수 인자 검증")
    void testLookupSequentialBillingKeyValidation() {
        assertThrows(Exception.class, () -> bootpay.lookupSequentialBillingKey(null, "bk", "uid"));
        assertThrows(Exception.class, () -> bootpay.lookupSequentialBillingKey("wk", "", "uid"));
        assertThrows(Exception.class, () -> bootpay.lookupSequentialBillingKey("wk", "bk", null));
    }

    // ── 별건 현금영수증 — pg 는 선택 파라미터 (ruby SDK request_cash_receipt parity) ──

    @Test
    @DisplayName("requestCashReceipt - pg 없이도 발행 요청이 나간다 (기본 PG 사용)")
    void testRequestCashReceiptWithoutPg() throws Exception {
        bootpay.requestCashReceipt(cashReceiptWithoutPg());

        assertEquals("POST", lastMethod);
        assertEquals("/v2/request/cash/receipt", lastPath);
        assertFalse(lastBody.contains("\"pg\""), "pg 미지정 시 body 에 pg 가 실리지 않아야 합니다: " + lastBody);
        assertTrue(lastBody.contains("\"order_id\":\"cash_order_1\""), lastBody);
        assertTrue(lastBody.contains("\"cash_receipt_type\":\"소득공제\""), lastBody);
    }

    @Test
    @DisplayName("requestCashReceipt - pg 를 지정하면 그대로 전송된다")
    void testRequestCashReceiptWithPg() throws Exception {
        CashReceipt cashReceipt = cashReceiptWithoutPg();
        cashReceipt.pg = "nicepay";

        bootpay.requestCashReceipt(cashReceipt);

        assertEquals("POST", lastMethod);
        assertEquals("/v2/request/cash/receipt", lastPath);
        assertTrue(lastBody.contains("\"pg\":\"nicepay\""), lastBody);
    }

    @Test
    @DisplayName("requestCashReceipt - pg 외 필수 인자는 그대로 검증한다")
    void testRequestCashReceiptValidation() {
        assertThrows(Exception.class, () -> bootpay.requestCashReceipt(null));

        CashReceipt noOrderName = cashReceiptWithoutPg();
        noOrderName.orderName = null;
        assertThrows(Exception.class, () -> bootpay.requestCashReceipt(noOrderName));

        CashReceipt noOrderId = cashReceiptWithoutPg();
        noOrderId.orderId = "";
        assertThrows(Exception.class, () -> bootpay.requestCashReceipt(noOrderId));

        CashReceipt noIdentityNo = cashReceiptWithoutPg();
        noIdentityNo.identityNo = null;
        assertThrows(Exception.class, () -> bootpay.requestCashReceipt(noIdentityNo));

        CashReceipt noCashReceiptType = cashReceiptWithoutPg();
        noCashReceiptType.cashReceiptType = null;
        assertThrows(Exception.class, () -> bootpay.requestCashReceipt(noCashReceiptType));
    }

    private static CashReceipt cashReceiptWithoutPg() {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.orderName = "테스트 별건 현금영수증";
        cashReceipt.orderId = "cash_order_1";
        cashReceipt.identityNo = "01012345678";
        cashReceipt.cashReceiptType = "소득공제";
        cashReceipt.price = 10000.0;
        cashReceipt.taxFree = 0.0;
        return cashReceipt;
    }
}
