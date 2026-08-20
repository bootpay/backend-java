package kr.co.bootpay.pg;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        byte[] response = "{\"ok\":true}".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(response);
        }
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
}
