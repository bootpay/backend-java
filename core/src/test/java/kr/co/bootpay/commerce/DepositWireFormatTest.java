package kr.co.bootpay.commerce;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import kr.co.bootpay.common.BootpayRole;
import kr.co.bootpay.store.BootpayCommerce;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SConstant;
import kr.co.bootpay.store.module.DepositModule;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.deposit.DepositCreateParams;
import kr.co.bootpay.store.model.request.deposit.DepositNotifyParams;
import kr.co.bootpay.store.model.response.deposit.CommerceDeposit;
import kr.co.bootpay.store.model.response.deposit.DepositAccountsResponse;
import kr.co.bootpay.store.model.response.deposit.DepositWebhookSecretResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** 뱅크알람 입금대기 5종 — 요청 객체를 mock transport 로 캡처하는 wire-format 테스트 (네트워크 없음). */
@DisplayName("Commerce API - 뱅크알람 입금대기 Wire Format (NodeJS 2.15.0 parity)")
class DepositWireFormatTest {
    private static CapturingStore store;
    private static DepositModule module;
    private static BootpayCommerce commerce;
    private static volatile String method;
    private static volatile String path;
    private static volatile String query;
    private static volatile String body;
    private static volatile String role;
    private static volatile String key;
    private static volatile String contentType;

    @BeforeEach
    void resetCapture() {
        store = new CapturingStore();
        store.baseUrl = "https://commerce.example/v1/";
        store.setMessageApiUrl("https://message.example/");
        module = new DepositModule(store);
        commerce = BootpayCommerce.builder().clientKey("test_ck").secretKey("test_sk").build();
        method = path = query = body = role = key = contentType = null;
        store.asManager();
        commerce.role(BootpayRole.MANAGER);
    }

    private static class CapturingStore extends BootpayStore {
        CapturingStore() {
            super(new TokenPayload("test_ck", "test_sk"), "PRODUCTION");
        }

        @Override
        public HttpResponse execute(HttpRequestBase request) throws Exception {
            assertEquals("commerce.example", request.getURI().getHost());
            method = request.getMethod();
            path = request.getURI().getRawPath();
            query = request.getURI().getRawQuery();
            role = request.getFirstHeader("BOOTPAY-ROLE").getValue();
            key = request.getFirstHeader("Idempotency-Key").getValue();
            contentType = request.getFirstHeader("Content-Type").getValue();
            body = request instanceof HttpEntityEnclosingRequestBase
                    ? EntityUtils.toString(((HttpEntityEnclosingRequestBase) request).getEntity(), "UTF-8") : "";
            BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 200, "OK");
            response.setEntity(new StringEntity("{\"merchant_order_id\":\"m-1\",\"status\":\"awaiting\"}", "UTF-8"));
            return response;
        }
    }

    private static DepositCreateParams params() {
        DepositCreateParams params = new DepositCreateParams();
        params.merchantOrderId = "m-1";
        params.price = 15000;
        params.depositor = "홍길동";
        return params;
    }

    private static void assertRequest(String expectedMethod, String expectedPath) {
        assertEquals(expectedMethod, method);
        assertEquals("/v1/" + expectedPath, path);
        assertNull(query);
        assertEquals("supervisor", role);
        assertNotNull(key);
        assertFalse(key.isEmpty());
        assertEquals("manager", store.getCurrentRole());
        assertEquals(BootpayRole.MANAGER, commerce.role());
    }

    @Test
    void accounts() throws Exception {
        assertTrue(store.deposit.accounts().isSuccess());
        assertRequest("GET", "sdk/deposits/accounts");
        assertEquals("", body);
    }

    @Test
    void webhookSecret() throws Exception {
        store.deposit.webhookSecret("secret-key");
        assertRequest("POST", "sdk/deposits/webhook_secret");
        assertEquals("{}", body);
        assertEquals("secret-key", key);
        assertTrue(contentType.contains("application/json"));
    }

    @Test
    void createCompactsNullAndPreservesFalse() throws Exception {
        DepositCreateParams params = params();
        params.accountId = "acc1";
        params.notify = new DepositNotifyParams();
        params.notify.depositGuide = true;
        params.notify.depositPaid = false;
        params.notify.link = "https://example.com/o/m-1";
        params.idempotencyKey = "idem-deposit";
        store.deposit.create(params);
        assertRequest("POST", "sdk/deposits");
        assertEquals("idem-deposit", key);
        assertEquals(JsonParser.parseString("{\"merchant_order_id\":\"m-1\",\"price\":15000,\"depositor\":\"홍길동\",\"account_id\":\"acc1\","
                + "\"notify\":{\"deposit_guide\":true,\"deposit_paid\":false,\"link\":\"https://example.com/o/m-1\"}}"), JsonParser.parseString(body));
        assertTrue(contentType.contains("application/json"));
    }

    @Test
    void createSendsAllOptionalFields() throws Exception {
        DepositCreateParams params = params();
        params.price = 15000.5;
        params.orderName = "주문명";
        params.memo = "메모";
        params.buyerName = "구매자";
        params.buyerPhone = "01012345678";
        params.buyerEmail = "buyer@example.com";
        params.accountId = "acc1";
        params.webhookUrl = "https://example.com/hooks/deposit";
        params.notify = new DepositNotifyParams();
        params.notify.depositPaid = false;
        store.deposit.create(params);
        assertRequest("POST", "sdk/deposits");
        assertEquals(JsonParser.parseString("{\"merchant_order_id\":\"m-1\",\"price\":15000.5,\"depositor\":\"홍길동\","
                + "\"order_name\":\"주문명\",\"memo\":\"메모\",\"buyer_name\":\"구매자\",\"buyer_phone\":\"01012345678\","
                + "\"buyer_email\":\"buyer@example.com\",\"account_id\":\"acc1\",\"webhook_url\":\"https://example.com/hooks/deposit\","
                + "\"notify\":{\"deposit_paid\":false}}"), JsonParser.parseString(body));
    }

    @Test
    void detail() throws Exception {
        assertEquals("m-1", store.deposit.detail("m-1").getData().get("merchant_order_id"));
        assertRequest("GET", "sdk/deposits/m-1");
        assertEquals("", body);
    }

    @Test
    void cancel() throws Exception {
        store.deposit.cancel("m-1", "cancel-key");
        assertRequest("DELETE", "sdk/deposits/m-1");
        assertEquals("cancel-key", key);
        assertEquals("", body);
    }

    @Test
    void idsAreEncodedAsOnePathSegment() throws Exception {
        store.deposit.detail("a/b");
        assertRequest("GET", "sdk/deposits/a%2Fb");
        store.deposit.cancel("a/b");
        assertRequest("DELETE", "sdk/deposits/a%2Fb");
        store.deposit.detail("a b+%?#한");
        assertRequest("GET", "sdk/deposits/a%20b%2B%25%3F%23%ED%95%9C");
    }

    @Test
    void accountsAndDetailRespectExplicitIdempotencyKey() throws Exception {
        store.deposit.accounts("idem-accounts");
        assertRequest("GET", "sdk/deposits/accounts");
        assertEquals("idem-accounts", key);
        store.deposit.detail("m-1", "idem-detail");
        assertRequest("GET", "sdk/deposits/m-1");
        assertEquals("idem-detail", key);
        module.accounts("idem-module");
        assertEquals("idem-module", key);
        module.detail("m-1", "idem-module-detail");
        assertEquals("idem-module-detail", key);
    }

    @Test
    void generatesNewUuidForEveryCall() throws Exception {
        Set<String> keys = new HashSet<>();
        store.deposit.accounts();
        keys.add(UUID.fromString(key).toString());
        store.deposit.accounts();
        keys.add(UUID.fromString(key).toString());
        store.deposit.webhookSecret();
        keys.add(UUID.fromString(key).toString());
        store.deposit.webhookSecret("");
        keys.add(UUID.fromString(key).toString());
        store.deposit.create(params());
        keys.add(UUID.fromString(key).toString());
        store.deposit.detail("m-1");
        keys.add(UUID.fromString(key).toString());
        store.deposit.cancel("m-1");
        keys.add(UUID.fromString(key).toString());
        assertEquals(7, keys.size());
    }

    @Test
    void unifiedCommerceSurface() throws Exception {
        assertNotNull(commerce.deposit);
        assertTrue(module.accounts().isSuccess());
        assertRequest("GET", "sdk/deposits/accounts");
        module.webhookSecret("modern-secret");
        assertRequest("POST", "sdk/deposits/webhook_secret");
        assertEquals("modern-secret", key);
        assertEquals("{}", body);
        module.webhookSecret();
        assertNotNull(UUID.fromString(key));
        DepositCreateParams params = params();
        params.idempotencyKey = "modern-create";
        assertEquals("m-1", module.create(params).getData().get("merchant_order_id"));
        assertRequest("POST", "sdk/deposits");
        assertEquals("modern-create", key);
        module.detail("a/b");
        assertRequest("GET", "sdk/deposits/a%2Fb");
        module.cancel("a/b", "modern-cancel");
        assertRequest("DELETE", "sdk/deposits/a%2Fb");
        assertEquals("modern-cancel", key);
        module.cancel("m-1");
        assertRequest("DELETE", "sdk/deposits/m-1");
        assertNotNull(UUID.fromString(key));
    }

    @Test
    void requiredValuesRejectBeforeTransport() {
        assertThrows(Exception.class, () -> store.deposit.create(null));
        assertThrows(Exception.class, () -> store.deposit.create(new DepositCreateParams()));
        DepositCreateParams params = params();
        params.price = null;
        assertThrows(Exception.class, () -> store.deposit.create(params));
        params.price = 1;
        params.depositor = "";
        assertThrows(Exception.class, () -> store.deposit.create(params));
        assertThrows(Exception.class, () -> store.deposit.detail(null));
        assertThrows(Exception.class, () -> store.deposit.cancel(""));
        assertNull(path);
    }

    @Test
    void responseModels() {
        Gson gson = new Gson();
        CommerceDeposit deposit = gson.fromJson("{\"merchant_order_id\":\"m-1\",\"order_number\":\"o-1\",\"order_name\":\"입금\",\"amount\":15000,"
                + "\"status\":\"awaiting\",\"buyer_name\":\"구매자\",\"depositor\":\"입금자\","
                + "\"account\":{\"id\":\"acc1\",\"bank_name\":\"은행\",\"account_no\":\"123\",\"holder\":\"예금주\"},"
                + "\"deadline\":\"2026-10-24\",\"deadline_label\":\"내일\",\"paid_at\":null,"
                + "\"notify\":{\"deposit_guide\":true,\"deposit_paid\":false},"
                + "\"deposit\":{\"key\":\"d-1\",\"bank_name\":\"은행\",\"account_tail\":\"123\",\"depositor\":\"입금자\",\"amount\":15000,\"received_at\":\"2026-10-23\"}}", CommerceDeposit.class);
        assertEquals("m-1", deposit.merchantOrderId);
        assertEquals("o-1", deposit.orderNumber);
        assertEquals("입금", deposit.orderName);
        assertEquals(15000, deposit.amount.intValue());
        assertEquals(CommerceDeposit.Status.AWAITING, deposit.status);
        assertEquals("구매자", deposit.buyerName);
        assertEquals("입금자", deposit.depositor);
        assertEquals("acc1", deposit.account.id);
        assertEquals("은행", deposit.account.bankName);
        assertEquals("123", deposit.account.accountNo);
        assertEquals("예금주", deposit.account.holder);
        // 주문에 붙은 계좌는 등록 때의 스냅샷이라 primary 가 없다 (계좌 목록에만 실린다)
        assertNull(deposit.account.primary);
        assertEquals("2026-10-24", deposit.deadline);
        assertEquals("내일", deposit.deadlineLabel);
        assertNull(deposit.paidAt);
        assertTrue(deposit.notify.depositGuide);
        assertFalse(deposit.notify.depositPaid);
        assertEquals("d-1", deposit.deposit.key);
        assertEquals("은행", deposit.deposit.bankName);
        assertEquals("123", deposit.deposit.accountTail);
        assertEquals("입금자", deposit.deposit.depositor);
        assertEquals(15000, deposit.deposit.amount.intValue());
        assertEquals("2026-10-23", deposit.deposit.receivedAt);
        for (CommerceDeposit.Status status : CommerceDeposit.Status.values()) {
            assertEquals(status, gson.fromJson(gson.toJson(status), CommerceDeposit.Status.class));
        }
        CommerceDeposit sparse = gson.fromJson("{\"status\":\"paid\"}", CommerceDeposit.class);
        assertEquals(CommerceDeposit.Status.PAID, sparse.status);
        assertNull(sparse.account);
        assertNull(sparse.notify);
        assertNull(sparse.deposit);
        assertEquals("acc1", gson.fromJson("{\"accounts\":[{\"id\":\"acc1\"}]}", DepositAccountsResponse.class).accounts.get(0).id);
        assertEquals("secret", gson.fromJson("{\"secret\":\"secret\"}", DepositWebhookSecretResponse.class).secret);
    }

    @Test
    void webhookEventConstants() {
        assertArrayEquals(new int[]{400, 401, 402, 403, 404, 405, 406, 407}, new int[]{
                SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_RECEIVED, SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_MATCHED,
                SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_AMBIGUOUS, SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_UNMATCHED,
                SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_CONFIRMED_MANUALLY, SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_EXPIRED,
                SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_CANCELLED, SConstant.BANK_DEPOSIT_WEBHOOK_EVENT_REVERTED});
    }
}
