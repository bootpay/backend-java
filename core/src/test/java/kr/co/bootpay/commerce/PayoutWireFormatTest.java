package kr.co.bootpay.commerce;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import kr.co.bootpay.common.BootpayRole;
import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayCommerce;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.payout.PayoutCreateParams;
import kr.co.bootpay.store.model.request.payout.PayoutListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.response.payout.CommercePayout;
import kr.co.bootpay.store.module.PayoutModule;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** 입점사 지급 요청 3종 — 요청 객체를 mock transport 로 캡처하는 wire-format 테스트 (네트워크 없음). */
@DisplayName("Commerce API - 입점사 지급 요청 Wire Format (NodeJS 2.15.0 parity)")
class PayoutWireFormatTest {
    private static final String ITEM_JSON = "{\"id\":\"po-1\",\"merchant_payout_id\":\"mp-1\","
            + "\"sub_seller_id\":\"ss-1\",\"amount\":150000,\"status\":\"ready\","
            + "\"payout_date\":\"2026-11-20\",\"order_number\":null,\"last_code\":null,"
            + "\"last_message\":null,\"created_at\":\"2026-11-12T10:20:30+09:00\","
            + "\"updated_at\":\"2026-11-12T10:20:30+09:00\"}";

    private static CapturingStore store;
    private static PayoutModule module;
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
        module = new PayoutModule(store);
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
            boolean list = "GET".equals(method) && "/v1/sdk/payouts".equals(path);
            response.setEntity(new StringEntity(list ? "[" + ITEM_JSON + "]" : ITEM_JSON, "UTF-8"));
            return response;
        }
    }

    private static PayoutCreateParams createParams() {
        PayoutCreateParams params = new PayoutCreateParams();
        params.subSellerId = "ss-1";
        params.amount = 150000L;
        params.merchantPayoutId = "mp-1";
        return params;
    }

    private static void assertRequest(String expectedMethod, String expectedPath) {
        assertEquals(expectedMethod, method);
        assertEquals("/v1/" + expectedPath, path);
        assertEquals("supervisor", role);
        assertNotNull(key);
        assertFalse(key.isEmpty());
        assertEquals("manager", store.getCurrentRole());
        assertEquals(BootpayRole.MANAGER, commerce.role());
    }

    @Test
    void createCompactsNullAndUsesExplicitKey() throws Exception {
        PayoutCreateParams params = createParams();
        params.payoutDate = "2026-11-20";
        params.idempotencyKey = "idem-create";
        assertEquals("mp-1", store.payout.create(params).getData().get("merchant_payout_id"));
        assertRequest("POST", "sdk/payouts");
        assertNull(query);
        assertEquals("idem-create", key);
        assertEquals(JsonParser.parseString("{\"sub_seller_id\":\"ss-1\",\"amount\":150000,"
                + "\"merchant_payout_id\":\"mp-1\",\"payout_date\":\"2026-11-20\"}"),
                JsonParser.parseString(body));
        assertTrue(contentType.contains("application/json"));
    }

    @Test
    void detailEncodesOnePathSegmentAndSupportsExplicitKey() throws Exception {
        store.payout.detail("a/b c?d#e한", "idem-detail");
        assertRequest("GET", "sdk/payouts/a%2Fb%20c%3Fd%23e%ED%95%9C");
        assertNull(query);
        assertEquals("idem-detail", key);
        assertEquals("", body);
    }

    @Test
    void listOmitsUnsetQueryAndSendsSetFilters() throws Exception {
        BootpayStoreResponse bare = store.payout.list();
        assertRequest("GET", "sdk/payouts");
        assertNull(query);
        assertTrue(bare.isDataList());
        assertEquals(1, bare.getDataList().size());

        PayoutListParams params = new PayoutListParams();
        params.status = CommercePayout.Status.READY;
        params.subSellerId = "a/b c?d#e한";
        params.from = "2026-11-01";
        params.to = "2026-11-30";
        params.page = 2;
        params.limit = 50;
        params.idempotencyKey = "idem-list";
        store.payout.list(params);
        assertRequest("GET", "sdk/payouts");
        assertEquals("status=ready&sub_seller_id=a%2Fb+c%3Fd%23e%ED%95%9C&from=2026-11-01&to=2026-11-30&page=2&limit=50", query);
        assertEquals("idem-list", key);
    }

    @Test
    void generatesDistinctKeysForAllEndpoints() throws Exception {
        Set<String> keys = new HashSet<>();
        store.payout.create(createParams());
        keys.add(UUID.fromString(key).toString());
        store.payout.detail("mp-1");
        keys.add(UUID.fromString(key).toString());
        store.payout.list();
        keys.add(UUID.fromString(key).toString());
        assertEquals(3, keys.size());
    }

    @Test
    void unifiedCommerceSurfacePreservesArrayResponse() throws Exception {
        assertNotNull(commerce.payout);
        PayoutCreateParams create = createParams();
        create.idempotencyKey = "module-create";
        assertEquals("mp-1", module.create(create).getData().get("merchant_payout_id"));
        assertRequest("POST", "sdk/payouts");
        assertEquals("module-create", key);
        module.detail("mp-1", "module-detail");
        assertRequest("GET", "sdk/payouts/mp-1");
        assertEquals("module-detail", key);
        BootpayResponse list = module.list();
        assertRequest("GET", "sdk/payouts");
        assertTrue(list.isDataList());
        assertEquals(1, list.getDataList().size());
    }

    @Test
    void requiredValuesRejectBeforeTransport() {
        assertThrows(Exception.class, () -> store.payout.create(null));
        assertThrows(Exception.class, () -> store.payout.create(new PayoutCreateParams()));
        PayoutCreateParams missingSeller = createParams();
        missingSeller.subSellerId = "";
        assertThrows(Exception.class, () -> store.payout.create(missingSeller));
        PayoutCreateParams missingAmount = createParams();
        missingAmount.amount = null;
        assertThrows(Exception.class, () -> store.payout.create(missingAmount));
        PayoutCreateParams missingMerchantId = createParams();
        missingMerchantId.merchantPayoutId = "";
        assertThrows(Exception.class, () -> store.payout.create(missingMerchantId));
        assertThrows(Exception.class, () -> store.payout.detail(null));
        assertThrows(Exception.class, () -> store.payout.detail(""));
        assertNull(path);
    }

    @Test
    void responseArrayDeserializesToTypedModels() {
        Gson gson = new Gson();
        CommercePayout[] payouts = gson.fromJson("[" + ITEM_JSON + "]", CommercePayout[].class);
        assertEquals(1, payouts.length);
        CommercePayout payout = payouts[0];
        assertEquals("po-1", payout.id);
        assertEquals("mp-1", payout.merchantPayoutId);
        assertEquals("ss-1", payout.subSellerId);
        assertEquals(150000L, payout.amount.longValue());
        assertEquals(CommercePayout.Status.READY, payout.status);
        assertEquals("2026-11-20", payout.payoutDate);
        assertNull(payout.orderNumber);
        assertNull(payout.lastCode);
        assertNull(payout.lastMessage);
        assertEquals("2026-11-12T10:20:30+09:00", payout.createdAt);
        assertEquals("2026-11-12T10:20:30+09:00", payout.updatedAt);
        for (CommercePayout.Status status : CommercePayout.Status.values()) {
            assertEquals(status, gson.fromJson(gson.toJson(status), CommercePayout.Status.class));
            assertEquals(status.name().toLowerCase(), status.toString());
        }
    }
}
