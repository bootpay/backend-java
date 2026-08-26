package kr.co.bootpay.commerce;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SMallSetting;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.invoice.InvoiceListParams;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelActionParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorTerminateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeRevokeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPurchaseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTransferParams;
import kr.co.bootpay.store.model.request.orderSubscriptionAdjustment.OrderSubscriptionAdjustmentUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams;
import kr.co.bootpay.store.model.request.product.MallProductListParams;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.user.MallUserJoinParams;
import kr.co.bootpay.store.model.request.user.UserListParams;
import kr.co.bootpay.store.model.request.userGroup.UserGroupAggregateTransactionParams;
import kr.co.bootpay.store.model.request.userGroup.UserGroupLimitParams;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NodeJS SDK 2.9.0 parity — wire-format 검증 (네트워크 불필요).
 * 로컬 루프백 HTTP 서버로 SDK 가 실제 전송하는 method / path / header / body 를 캡처해 검증한다.
 */
@DisplayName("Commerce API - Wire Format (NodeJS 2.9.0 parity)")
class CommerceWireFormatTest {

    private static HttpServer server;
    private static BootpayStore store;

    // 마지막 요청 캡처
    private static volatile String lastMethod;
    private static volatile String lastPath;
    private static volatile String lastQuery;
    private static volatile String lastBody;
    private static volatile String lastRole;
    private static volatile String lastIdempotencyKey;
    private static volatile String lastUserJwt;
    private static volatile String lastContentType;
    private static volatile String lastAuthorization;

    @BeforeAll
    static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", CommerceWireFormatTest::capture);
        server.start();

        store = new BootpayStore(new TokenPayload("test_ck", "test_sk"), "PRODUCTION");
        store.baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/v1/";
    }

    @AfterAll
    static void tearDown() {
        if (server != null) server.stop(0);
    }

    @BeforeEach
    void resetCapture() {
        lastMethod = lastPath = lastQuery = lastBody = lastRole = lastIdempotencyKey = lastUserJwt = lastContentType = lastAuthorization = null;
    }

    private static void capture(HttpExchange exchange) throws java.io.IOException {
        lastMethod = exchange.getRequestMethod();
        lastPath = exchange.getRequestURI().getPath();
        lastQuery = exchange.getRequestURI().getQuery();
        lastRole = exchange.getRequestHeaders().getFirst("BOOTPAY-ROLE");
        lastIdempotencyKey = exchange.getRequestHeaders().getFirst("Idempotency-Key");
        lastUserJwt = exchange.getRequestHeaders().getFirst("Bootpay-User-JWT");
        lastContentType = exchange.getRequestHeaders().getFirst("Content-Type");
        lastAuthorization = exchange.getRequestHeaders().getFirst("Authorization");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream in = exchange.getRequestBody()) {
            byte[] chunk = new byte[4096];
            int read;
            while ((read = in.read(chunk)) != -1) buffer.write(chunk, 0, read);
        }
        lastBody = new String(buffer.toByteArray(), StandardCharsets.UTF_8);

        String responseBody = "/v1/request/token".equals(lastPath)
                ? "{\"access_token\":\"issued-token\"}"
                : "{\"ok\":true}";
        byte[] response = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(response);
        }
    }

    @Test
    @DisplayName("getAccessToken - Basic 헤더와 client_key/secret_key 본문을 함께 전송")
    void testGetAccessTokenWireFormat() throws Exception {
        store.getAccessToken();

        String expectedBasic = "Basic " + Base64.getEncoder()
                .encodeToString("test_ck:test_sk".getBytes(StandardCharsets.UTF_8));
        assertAll(
                () -> assertEquals("POST", lastMethod),
                () -> assertEquals("/v1/request/token", lastPath),
                () -> assertEquals(expectedBasic, lastAuthorization),
                () -> assertTrue(lastBody.contains("\"client_key\":\"test_ck\""), lastBody),
                () -> assertTrue(lastBody.contains("\"secret_key\":\"test_sk\""), lastBody),
                () -> assertEquals("issued-token", store.getCurrentToken())
        );
    }

    @Test
    @DisplayName("저장된 Commerce 토큰은 일반 요청 인증 방식을 바꾸지 않는다")
    void testStoredTokenDoesNotReplaceBasicAuthorization() throws Exception {
        store.setTokenFromAPI("issued-token");
        store.store.info();

        String expectedBasic = "Basic " + Base64.getEncoder()
                .encodeToString("test_ck:test_sk".getBytes(StandardCharsets.UTF_8));
        assertEquals(expectedBasic, lastAuthorization);
    }

    // ══════════════════════════════════════════════════════════
    // orderSubscription.supervisorCharge / supervisorChargeRevoke
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("supervisorCharge - POST order_subscriptions/charge, charge_key 는 body 로만, supervisor + Idempotency-Key")
    void testSupervisorCharge() throws Exception {
        SupervisorChargeParams params = new SupervisorChargeParams();
        params.chargeKey = "charge_key_test";
        params.price = 1000.0;
        store.orderSubscription.supervisorCharge(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/order_subscriptions/charge", lastPath);
        assertNull(lastQuery, "charge_key 는 query 로 전송되면 안 됩니다");
        assertTrue(lastBody.contains("\"charge_key\":\"charge_key_test\""), "charge_key 는 body 로 전송: " + lastBody);
        assertTrue(lastBody.contains("\"price\":1000"), "price body 전송: " + lastBody);
        assertFalse(lastBody.contains("idempotency"), "idempotencyKey 는 body 에 포함되면 안 됩니다: " + lastBody);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey, "Idempotency-Key 자동 생성");
        assertFalse(lastIdempotencyKey.isEmpty());
    }

    @Test
    @DisplayName("supervisorCharge - idempotencyKey 직접 지정 가능")
    void testSupervisorChargeExplicitIdempotencyKey() throws Exception {
        SupervisorChargeParams params = new SupervisorChargeParams();
        params.chargeKey = "charge_key_test";
        params.price = 1000.0;
        params.idempotencyKey = "my-key-123";
        store.orderSubscription.supervisorCharge(params);

        assertEquals("my-key-123", lastIdempotencyKey);
    }

    @Test
    @DisplayName("supervisorChargeRevoke - DELETE order_subscriptions/charge, charge_key 는 body 로")
    void testSupervisorChargeRevoke() throws Exception {
        SupervisorChargeRevokeParams params = new SupervisorChargeRevokeParams();
        params.chargeKey = "charge_key_test";
        store.orderSubscription.supervisorChargeRevoke(params);

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/order_subscriptions/charge", lastPath);
        assertNull(lastQuery);
        assertTrue(lastBody.contains("\"charge_key\":\"charge_key_test\""), "charge_key 는 body 로 전송: " + lastBody);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // orderSubscriptionAdjustment.delete — ID 는 body 로
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("adjustment.delete - 대상 ID 는 query 가 아니라 body 로 전송, supervisor")
    void testAdjustmentDeleteIdInBody() throws Exception {
        store.orderSubscriptionAdjustment.delete("OS_1", "ADJ_1");

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1/adjustments", lastPath);
        assertNull(lastQuery, "ID 는 query 로 전송되면 안 됩니다");
        assertEquals("{\"order_subscription_adjustment_id\":\"ADJ_1\"}", lastBody);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // mallSetting — supervisor 전용, flatten 바디 (null 미전송)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("mallSetting.detail - GET mall-setting, supervisor + Idempotency-Key")
    void testMallSettingDetail() throws Exception {
        store.mallSetting.detail();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/mall-setting", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("mallSetting.update - PUT mall-setting, flatten 바디에 null 미전송")
    void testMallSettingUpdateCompactBody() throws Exception {
        SMallSetting setting = new SMallSetting();
        setting.name = "테스트몰";
        setting.addr1 = "서울시";
        store.mallSetting.update(setting);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/mall-setting", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"name\":\"테스트몰\""), lastBody);
        assertTrue(lastBody.contains("\"addr_1\":\"서울시\""), "addr1 은 addr_1 로 직렬화: " + lastBody);
        assertFalse(lastBody.contains("null"), "null 값은 전송하지 않는다: " + lastBody);
        assertFalse(lastBody.contains("seller_name"), "미지정 필드는 전송하지 않는다: " + lastBody);
    }

    @Test
    @DisplayName("mallSetting.update - 서버 오타 필드명 use_oder_cancel_approval 로 직렬화")
    void testMallSettingOderCancelApprovalSerialization() throws Exception {
        SMallSetting setting = new SMallSetting();
        setting.useOrderCancelApproval = true;
        store.mallSetting.update(setting);

        assertTrue(lastBody.contains("\"use_oder_cancel_approval\":true"),
                "서버 필드명 오타(use_oder_...) 그대로 직렬화해야 한다: " + lastBody);
        assertFalse(lastBody.contains("use_order_cancel_approval"),
                "정상 철자(use_order_...)로 보내면 서버가 읽지 못한다: " + lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // webhook.sendTest
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("webhook.sendTest - POST webhook/test, header_content_type 미지정시 빈 바디, user role")
    void testWebhookSendTestDefault() throws Exception {
        store.webhook.sendTest();

        assertEquals("POST", lastMethod);
        assertEquals("/v1/webhook/test", lastPath);
        assertEquals("{}", lastBody);
        assertNotNull(lastIdempotencyKey);
        assertEquals("user", lastRole, "webhook 은 기본(user) role 로 요청한다");
    }

    @Test
    @DisplayName("webhook.sendTest(1) - header_content_type body 전송")
    void testWebhookSendTestWithContentType() throws Exception {
        store.webhook.sendTest(1);

        assertEquals("{\"header_content_type\":1}", lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // user — V1 Mall 회원 (복수형 users/... 경로, Bootpay-User-JWT)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("user.userLogin - POST users/login, password + corporate_type 기본 0")
    void testUserLogin() throws Exception {
        store.user.userLogin("tester", "pw1234");

        assertEquals("POST", lastMethod);
        assertEquals("/v1/users/login", lastPath);
        assertTrue(lastBody.contains("\"login_id\":\"tester\""), lastBody);
        assertTrue(lastBody.contains("\"password\":\"pw1234\""), lastBody);
        assertTrue(lastBody.contains("\"corporate_type\":0"), "corporate_type 미지정시 0: " + lastBody);
        assertNotNull(lastIdempotencyKey);
        assertNull(lastUserJwt, "로그인 요청에는 JWT 가 없어야 합니다");
    }

    @Test
    @DisplayName("user.userSession - GET users/session, Bootpay-User-JWT 헤더")
    void testUserSession() throws Exception {
        store.user.userSession("jwt-abc");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/users/session", lastPath);
        assertEquals("jwt-abc", lastUserJwt);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("user.userLogout - DELETE users/session, Bootpay-User-JWT 헤더")
    void testUserLogout() throws Exception {
        store.user.userLogout("jwt-abc");

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/users/session", lastPath);
        assertEquals("jwt-abc", lastUserJwt);
    }

    @Test
    @DisplayName("user.uidExist - GET users/join/uid-exist?pk={uid}, user role")
    void testUidExist() throws Exception {
        store.user.uidExist("ex_uid_1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/users/join/uid-exist", lastPath);
        assertEquals("pk=ex_uid_1", lastQuery);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("user.userJoinCheck - GET users/join/{type}?pk={pk}")
    void testUserJoinCheck() throws Exception {
        store.user.userJoinCheck("email-exist", "a@b.com");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/users/join/email-exist", lastPath);
        // URI.getQuery() 는 디코딩된 값을 반환한다 — 원문은 pk=a%40b.com
        assertEquals("pk=a@b.com", lastQuery);
        assertNotNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // invoice.list — page/limit 기본 1/24
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("invoice.list - limit 미지정시 24 전송, user role + Idempotency-Key")
    void testInvoiceListDefaults() throws Exception {
        store.invoice.list(new InvoiceListParams());

        assertEquals("GET", lastMethod);
        assertEquals("/v1/invoices", lastPath);
        assertTrue(lastQuery.contains("page=1"), lastQuery);
        assertTrue(lastQuery.contains("limit=24"), lastQuery);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("invoice.list - cs_type/user_id/product_type/css_at/cse_at 전송")
    void testInvoiceListExtendedParams() throws Exception {
        InvoiceListParams params = new InvoiceListParams();
        params.csType = "month";
        params.userId = "U1";
        params.productType = 2;
        params.cssAt = "2026-01-01";
        params.cseAt = "2026-01-31";
        store.invoice.list(params);

        assertTrue(lastQuery.contains("cs_type=month"), lastQuery);
        assertTrue(lastQuery.contains("user_id=U1"), lastQuery);
        assertTrue(lastQuery.contains("product_type=2"), lastQuery);
        assertTrue(lastQuery.contains("css_at=2026-01-01"), lastQuery);
        assertTrue(lastQuery.contains("cse_at=2026-01-31"), lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // product — Mall 조회 + 쓰기 manager scope + multipart 인덱싱
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("product.products - page/limit 기본 1/20")
    void testMallProductsDefaults() throws Exception {
        store.product.products();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/products", lastPath);
        assertTrue(lastQuery.contains("page=1"), lastQuery);
        assertTrue(lastQuery.contains("limit=20"), lastQuery);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("product.create - 이미지 없으면 JSON 전송, manager role")
    void testProductCreateJsonWithoutImages() throws Exception {
        SProduct product = new SProduct();
        product.name = "테스트상품";
        store.product.create(product);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/products", lastPath);
        assertEquals("manager", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastContentType.startsWith("application/json"), lastContentType);
        assertTrue(lastBody.contains("\"name\":\"테스트상품\""), lastBody);
    }

    @Test
    @DisplayName("product.create - 이미지 있으면 multipart, images[0]/images[1] 인덱싱 + boundary 유지")
    void testProductCreateMultipartIndexedImages() throws Exception {
        File image0 = File.createTempFile("bootpay-test-image0", ".png");
        File image1 = File.createTempFile("bootpay-test-image1", ".png");
        try (FileOutputStream out0 = new FileOutputStream(image0); FileOutputStream out1 = new FileOutputStream(image1)) {
            out0.write(new byte[]{1, 2, 3});
            out1.write(new byte[]{4, 5, 6});
        }
        image0.deleteOnExit();
        image1.deleteOnExit();

        SProduct product = new SProduct();
        product.name = "테스트상품";
        List<URL> images = new ArrayList<>();
        images.add(image0.toURI().toURL());
        images.add(image1.toURI().toURL());
        store.product.create(product, images);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/products", lastPath);
        assertEquals("manager", lastRole);
        assertTrue(lastContentType.startsWith("multipart/form-data"), lastContentType);
        assertTrue(lastContentType.contains("boundary="), "boundary 유실 금지: " + lastContentType);
        assertTrue(lastBody.contains("name=\"images[0]\""), "images[0] 인덱싱: " + lastBody);
        assertTrue(lastBody.contains("name=\"images[1]\""), "images[1] 인덱싱: " + lastBody);
        assertFalse(lastBody.contains("name=\"images\"" + "\r"), "인덱스 없는 images 필드 금지");
    }

    // ══════════════════════════════════════════════════════════
    // orderSubscriptionRequest — role 분기 + 기본 page/limit
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("orderSubscriptionRequest.list - project_id 없으면 user role, page/limit 기본 1/20")
    void testRequestListUserMode() throws Exception {
        store.orderSubscriptionRequest.list();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/order-subscription-requests", lastPath);
        assertTrue(lastQuery.contains("page=1"), lastQuery);
        assertTrue(lastQuery.contains("limit=20"), lastQuery);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("orderSubscriptionRequest.list - project_id 있으면 supervisor role")
    void testRequestListSupervisorMode() throws Exception {
        OrderSubscriptionRequestListParams params = new OrderSubscriptionRequestListParams();
        params.projectId = "P1";
        params.orderSubscriptionId = "OS1";
        params.userId = "U1";
        params.userGroupId = "G1";
        store.orderSubscriptionRequest.list(params);

        assertEquals("supervisor", lastRole);
        assertTrue(lastQuery.contains("project_id=P1"), lastQuery);
        assertTrue(lastQuery.contains("order_subscription_id=OS1"), lastQuery);
        assertTrue(lastQuery.contains("user_id=U1"), lastQuery);
        assertTrue(lastQuery.contains("user_group_id=G1"), lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // requestIng.purchase / transfer
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("requestIng.purchase - POST order_subscriptions/requests/ing/purchase, user role")
    void testRequestIngPurchase() throws Exception {
        OrderSubscriptionPurchaseParams params = new OrderSubscriptionPurchaseParams();
        params.orderSubscriptionId = "OS1";
        params.price = 5000.0;
        store.orderSubscription.requestIng.purchase(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/order_subscriptions/requests/ing/purchase", lastPath);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"order_subscription_id\":\"OS1\""), lastBody);
    }

    @Test
    @DisplayName("requestIng.transfer - POST order_subscriptions/requests/ing/transfer, user role")
    void testRequestIngTransfer() throws Exception {
        OrderSubscriptionTransferParams params = new OrderSubscriptionTransferParams();
        params.orderSubscriptionId = "OS1";
        params.newUserId = "U2";
        store.orderSubscription.requestIng.transfer(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/order_subscriptions/requests/ing/transfer", lastPath);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"new_user_id\":\"U2\""), lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // orderCancel — 정식 인자명 + 구 이름 하위호환
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("orderCancel.approve - orderCancellationRequestId 정식 이름, supervisor role")
    void testOrderCancelApproveNewName() throws Exception {
        OrderCancelActionParams params = new OrderCancelActionParams();
        params.orderCancellationRequestId = "OCR_1";
        params.message = "승인";
        store.orderCancel.approve(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order/cancel/OCR_1/approve", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("orderCancel.reject - 구 이름 orderCancelRequestHistoryId 도 계속 동작")
    void testOrderCancelRejectLegacyName() throws Exception {
        OrderCancelActionParams params = new OrderCancelActionParams();
        params.orderCancelRequestHistoryId = "OCR_2";
        params.message = "반려";
        store.orderCancel.reject(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order/cancel/OCR_2/reject", lastPath);
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("orderCancel.withdraw - user role + Idempotency-Key")
    void testOrderCancelWithdraw() throws Exception {
        store.orderCancel.withdraw("OCR_3");

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order/cancel/OCR_3/withdraw", lastPath);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // store — 조회에 Idempotency-Key
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("store.info - Idempotency-Key 부착, 직접 지정 가능")
    void testStoreInfoIdempotencyKey() throws Exception {
        store.store.info();
        assertNotNull(lastIdempotencyKey);

        store.store.info("store-key-1");
        assertEquals("store-key-1", lastIdempotencyKey);
    }

    @Test
    @DisplayName("store.detail - GET store/detail + Idempotency-Key")
    void testStoreDetailIdempotencyKey() throws Exception {
        store.store.detail();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/store/detail", lastPath);
        assertNotNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // user.userJoin — corporate_type 기본 0, null 미전송
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("user.userJoin - POST users/join, corporate_type 기본 0 + null 미전송")
    void testUserJoinDefaults() throws Exception {
        MallUserJoinParams params = new MallUserJoinParams();
        params.loginId = "tester";
        params.password = "pw1234";
        params.name = "테스터";
        store.user.userJoin(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/users/join", lastPath);
        assertTrue(lastBody.contains("\"login_id\":\"tester\""), lastBody);
        assertTrue(lastBody.contains("\"password\":\"pw1234\""), lastBody);
        assertTrue(lastBody.contains("\"corporate_type\":0"), "corporate_type 미지정시 0: " + lastBody);
        assertFalse(lastBody.contains("email"), "null 값(email)은 전송하지 않는다: " + lastBody);
        assertFalse(lastBody.contains("idempotency"), "idempotencyKey 는 body 에 포함되면 안 됩니다: " + lastBody);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("user.userJoin - corporate_type 명시 지정시 그 값을 전송")
    void testUserJoinExplicitCorporateType() throws Exception {
        MallUserJoinParams params = new MallUserJoinParams();
        params.loginId = "tester";
        params.password = "pw1234";
        params.corporateType = 1;
        store.user.userJoin(params);

        assertTrue(lastBody.contains("\"corporate_type\":1"), lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // product — Mall 조회 파라미터·JWT / productDetail
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("product.list - 서버가 읽는 category_id/ex_uid/sort 를 전송한다")
    void testProductListSendsServerReadFilters() throws Exception {
        ProductListParams params = new ProductListParams();
        params.page = 1;
        params.limit = 10;
        params.keyword = "coffee";
        params.categoryId = "CAT1";
        params.exUid = "EX-1";
        params.sort = "-price";
        store.product.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/products", lastPath);
        assertTrue(lastQuery.contains("category_id=CAT1"), lastQuery);
        assertTrue(lastQuery.contains("ex_uid=EX-1"), lastQuery);
        assertTrue(lastQuery.contains("sort=-price"), lastQuery);
        assertTrue(lastQuery.contains("keyword=coffee"), lastQuery);
        assertTrue(lastQuery.contains("page=1"), lastQuery);
        assertTrue(lastQuery.contains("limit=10"), lastQuery);
    }

    @Test
    @DisplayName("product.products - category_id/sort 쿼리 + Bootpay-User-JWT 헤더")
    void testMallProductsParams() throws Exception {
        MallProductListParams params = new MallProductListParams();
        params.categoryId = "CAT1";
        params.sort = "-created_at";
        params.userJwt = "jwt-prod";
        store.product.products(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/products", lastPath);
        assertTrue(lastQuery.contains("category_id=CAT1"), lastQuery);
        assertTrue(lastQuery.contains("sort=-created_at"), lastQuery);
        assertEquals("jwt-prod", lastUserJwt);
        assertFalse(lastQuery.contains("jwt"), "userJwt 는 query 로 전송되면 안 됩니다: " + lastQuery);
    }

    @Test
    @DisplayName("product.productDetail - GET products/{id}, Bootpay-User-JWT 는 값 있을 때만")
    void testProductDetailWithJwt() throws Exception {
        store.product.productDetail("P1", "jwt-detail");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/products/P1", lastPath);
        assertEquals("jwt-detail", lastUserJwt);
        assertNotNull(lastIdempotencyKey);

        store.product.productDetail("P1", null);
        assertNull(lastUserJwt, "JWT 미지정시 헤더가 붙으면 안 됩니다");
    }

    // ══════════════════════════════════════════════════════════
    // requestIng — 기존 메서드에도 user role + Idempotency-Key
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("requestIng.termination - POST, user role + Idempotency-Key (변경분 회귀)")
    void testRequestIngTermination() throws Exception {
        OrderSubscriptionTerminationParams params = new OrderSubscriptionTerminationParams();
        params.orderSubscriptionId = "OS1";
        params.reason = "해지";
        store.orderSubscription.requestIng.termination(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/order_subscriptions/requests/ing/termination", lastPath);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertFalse(lastBody.contains("idempotency"), lastBody);
    }

    @Test
    @DisplayName("requestIng.resume - PUT 유지 (POST 로 바꾸면 안 됨) + user role")
    void testRequestIngResumeStaysPut() throws Exception {
        OrderSubscriptionResumeParams params = new OrderSubscriptionResumeParams();
        params.orderSubscriptionId = "OS1";
        store.orderSubscription.requestIng.resume(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/requests/ing/resume", lastPath);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("requestIng.calculateTerminationFee - user role + Idempotency-Key")
    void testCalculateTerminationFeeHeaders() throws Exception {
        store.orderSubscription.requestIng.calculateTerminationFee("OS1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/order_subscriptions/requests/ing/calculate_termination_fee", lastPath);
        assertEquals("order_subscription_id=OS1", lastQuery);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // invoice — notify sendTypes 선택화 / detail 헤더 (변경분)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("invoice.notify(invoiceId) - sendTypes 미전달시 body 에 send_types 없음")
    void testInvoiceNotifyWithoutSendTypes() throws Exception {
        store.invoice.notify("INV1");

        assertEquals("POST", lastMethod);
        assertEquals("/v1/invoices/INV1/notify", lastPath);
        assertFalse(lastBody.contains("send_types"), "sendTypes 미전달시 전송하지 않는다: " + lastBody);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("invoice.notify(invoiceId, sendTypes) - 기존 시그니처 유지 + send_types 전송")
    void testInvoiceNotifyWithSendTypes() throws Exception {
        store.invoice.notify("INV1", java.util.Arrays.asList(1, 2));

        assertEquals("/v1/invoices/INV1/notify", lastPath);
        assertTrue(lastBody.contains("\"send_types\":[1,2]"), lastBody);
    }

    @Test
    @DisplayName("invoice.list(null) - 기존 사용 패턴이 컴파일·동작 유지 (source-compat 회귀)")
    void testInvoiceListNullStillCompiles() throws Exception {
        // InvoiceListParams 가 ListParams 를 상속하므로 list(null) 은 모호성 없이
        // 더 구체적인 InvoiceListParams 오버로드로 해석된다 (컴파일 자체가 검증).
        store.invoice.list(null);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/invoices", lastPath);
        assertTrue(lastQuery.contains("page=1"), lastQuery);
        assertTrue(lastQuery.contains("limit=24"), lastQuery);
    }

    @Test
    @DisplayName("invoice.detail - user role + Idempotency-Key (변경분 회귀)")
    void testInvoiceDetailHeaders() throws Exception {
        store.invoice.detail("INV1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/invoices/INV1", lastPath);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // adjustment — create/update supervisor (변경분)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("adjustment.create - POST adjustments, supervisor + Idempotency-Key")
    void testAdjustmentCreate() throws Exception {
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("할인", -1000.0);
        store.orderSubscriptionAdjustment.create("OS_1", adjustment);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1/adjustments", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"name\":\"할인\""), lastBody);
        assertTrue(lastBody.contains("\"price\":-1000"), lastBody);
    }

    @Test
    @DisplayName("adjustment.update - PUT adjustments, adjustments 배열 직렬화 + supervisor")
    void testAdjustmentUpdateWithArray() throws Exception {
        OrderSubscriptionAdjustmentUpdateParams params = new OrderSubscriptionAdjustmentUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.duration = 2;
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("교체할인", -500.0);
        params.adjustments = java.util.Arrays.asList(adjustment);
        store.orderSubscriptionAdjustment.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1/adjustments", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"duration\":2"), lastBody);
        assertTrue(lastBody.contains("\"adjustments\":[{"), "adjustments 배열 직렬화: " + lastBody);
        assertTrue(lastBody.contains("\"name\":\"교체할인\""), lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // userGroup — limit 신규 필드 + manager role (변경분)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("userGroup.limit - limit_month_purchase/limit_week_purchase body 전송, manager role")
    void testUserGroupLimitNewFields() throws Exception {
        UserGroupLimitParams params = new UserGroupLimitParams();
        params.userGroupId = "UG_1";
        params.useLimit = true;
        params.limitMonthPurchase = 100000.0;
        params.limitWeekPurchase = 30000.0;
        params.limitMessage = "한도초과";
        store.userGroup.limit(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/user-groups/UG_1/limit", lastPath);
        assertEquals("manager", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"limit_month_purchase\":100000"), lastBody);
        assertTrue(lastBody.contains("\"limit_week_purchase\":30000"), lastBody);
        assertTrue(lastBody.contains("\"use_limit\":true"), lastBody);
        assertFalse(lastBody.contains("idempotency"), "idempotencyKey 는 body 에 포함되면 안 됩니다: " + lastBody);
    }

    @Test
    @DisplayName("userGroup.aggregateTransaction - manager role + Idempotency-Key (변경분 회귀)")
    void testUserGroupAggregateTransaction() throws Exception {
        UserGroupAggregateTransactionParams params = new UserGroupAggregateTransactionParams();
        params.userGroupId = "UG_1";
        params.useSubscriptionAggregateTransaction = true;
        params.subscriptionMonthDay = 25;
        store.userGroup.aggregateTransaction(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/user-groups/UG_1/aggregate-transaction", lastPath);
        assertEquals("manager", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"subscription_month_day\":25"), lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // order.list / orderSubscription.list — 검색 파라미터 확장 (변경분)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("order.list - search_date_from/to 전송, css_at/cse_at 별칭 공존")
    void testOrderListSearchDateParams() throws Exception {
        OrderListParams params = new OrderListParams();
        params.searchDateFrom = "2026-01-01";
        params.searchDateTo = "2026-01-31";
        params.cssAt = "2026-02-01";
        params.cseAt = "2026-02-28";
        store.order.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/orders", lastPath);
        assertTrue(lastQuery.contains("search_date_from=2026-01-01"), lastQuery);
        assertTrue(lastQuery.contains("search_date_to=2026-01-31"), lastQuery);
        assertTrue(lastQuery.contains("css_at=2026-02-01"), "css_at 별칭 계속 지원: " + lastQuery);
        assertTrue(lastQuery.contains("cse_at=2026-02-28"), lastQuery);
    }

    @Test
    @DisplayName("orderSubscription.list - search_date_from/to + status 전송 (status 미전송 버그 회귀)")
    void testOrderSubscriptionListSearchParams() throws Exception {
        OrderSubscriptionListParams params = new OrderSubscriptionListParams();
        params.searchDateFrom = "2026-01-01";
        params.searchDateTo = "2026-01-31";
        params.status = 3;
        store.orderSubscription.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/order_subscriptions", lastPath);
        assertTrue(lastQuery.contains("search_date_from=2026-01-01"), lastQuery);
        assertTrue(lastQuery.contains("search_date_to=2026-01-31"), lastQuery);
        assertTrue(lastQuery.contains("status=3"), "status 파라미터 실제 전송: " + lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // orderSubscription.update / orderSubscriptionRequest.update — supervisor (변경분)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("orderSubscription.update - PUT order_subscriptions/{id}, supervisor + Idempotency-Key")
    void testOrderSubscriptionUpdateSupervisor() throws Exception {
        OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.orderName = "변경구독";
        store.orderSubscription.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"order_name\":\"변경구독\""), lastBody);
    }

    @Test
    @DisplayName("orderSubscriptionRequest.update - supervisor, body 는 approval/reason 만 (ID 는 URL)")
    void testOrderSubscriptionRequestUpdateSupervisor() throws Exception {
        OrderSubscriptionRequestUpdateParams params = new OrderSubscriptionRequestUpdateParams();
        params.orderSubscriptionRequestHistoryId = "OSR_1";
        params.approval = "approve";
        params.reason = "승인";
        store.orderSubscriptionRequest.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order-subscription-requests/OSR_1", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"approval\":\"approve\""), lastBody);
        assertFalse(lastBody.contains("OSR_1"), "ID 는 body 에서 제외: " + lastBody);
    }

    @Test
    @DisplayName("orderSubscriptionRequest.detail - project_id 유무로 role 분기")
    void testRequestDetailRoleBranch() throws Exception {
        store.orderSubscriptionRequest.detail("OSR_1");
        assertEquals("user", lastRole);

        store.orderSubscriptionRequest.detail("OSR_1", "P1");
        assertEquals("supervisor", lastRole);
        assertEquals("project_id=P1", lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // BOOTPAY-ROLE — 요청별 role 이 기본 role 을 덮고, 기본 role 은 유지 (변경분)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("role - 요청별 role(context)이 인스턴스 기본 role 을 덮는다")
    void testPerRequestRoleOverridesDefault() throws Exception {
        try {
            store.asUser();
            store.mallSetting.detail();
            assertEquals("supervisor", lastRole, "supervisor 전용 endpoint 는 기본 role 과 무관하게 supervisor");

            store.user.uidExist("uid1");
            assertEquals("user", lastRole);
        } finally {
            store.clearRole();
        }
    }

    // ══════════════════════════════════════════════════════════
    // orderSubscriptionBill.list — page/limit 기본 1/20 + user role (P1 보강)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("orderSubscriptionBill.list - page/limit 기본 1/20 상시 전송, user role + Idempotency-Key")
    void testBillListDefaults() throws Exception {
        store.orderSubscriptionBill.list(null);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/order_subscription_bills", lastPath);
        assertTrue(lastQuery.contains("page=1"), lastQuery);
        assertTrue(lastQuery.contains("limit=20"), lastQuery);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("orderSubscriptionBill.list - 지정 파라미터 전송 + Idempotency-Key 직접 지정")
    void testBillListWithParams() throws Exception {
        kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams params =
                new kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams();
        params.orderSubscriptionId = "OS1";
        params.page = 2;
        params.limit = 5;
        params.status.add(1);
        params.status.add(2);
        params.idempotencyKey = "bill-key-1";
        store.orderSubscriptionBill.list(params);

        assertTrue(lastQuery.contains("order_subscription_id=OS1"), lastQuery);
        assertTrue(lastQuery.contains("page=2"), lastQuery);
        assertTrue(lastQuery.contains("limit=5"), lastQuery);
        assertTrue(lastQuery.contains("status=1,2"), lastQuery);
        assertEquals("bill-key-1", lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // calculateTerminationFee — 두 파라미터 동시 전송 (P1 로직 결함 회귀)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("requestIng.calculateTerminationFee - order_subscription_id 와 order_number 동시 지정시 둘 다 전송")
    void testCalculateTerminationFeeBothParams() throws Exception {
        store.orderSubscription.requestIng.calculateTerminationFee("OS1", "ORD-001");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/order_subscriptions/requests/ing/calculate_termination_fee", lastPath);
        assertTrue(lastQuery.contains("order_subscription_id=OS1"), lastQuery);
        assertTrue(lastQuery.contains("order_number=ORD-001"), "order_number 가 유실되면 안 됩니다: " + lastQuery);
    }

    @Test
    @DisplayName("requestIng.calculateTerminationFee - order_number 단독 지정도 계속 동작")
    void testCalculateTerminationFeeOrderNumberOnly() throws Exception {
        store.orderSubscription.requestIng.calculateTerminationFeeByOrderNumber("ORD-002");

        assertEquals("order_number=ORD-002", lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // 신규 optional 필드 직렬화 (P1 보강)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("orderSubscription.update - next_billing_at/billing_key/status/payment_next_at 직렬화")
    void testOrderSubscriptionUpdateNewFields() throws Exception {
        OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.nextBillingAt = "2026-09-01 00:00:00";
        params.billingKey = "BK_1";
        params.status = 2;
        params.paymentNextAt = "2026-09-02 00:00:00";
        store.orderSubscription.update(params);

        assertTrue(lastBody.contains("\"next_billing_at\":\"2026-09-01 00:00:00\""), lastBody);
        assertTrue(lastBody.contains("\"billing_key\":\"BK_1\""), lastBody);
        assertTrue(lastBody.contains("\"status\":2"), lastBody);
        assertTrue(lastBody.contains("\"payment_next_at\":\"2026-09-02 00:00:00\""), lastBody);
    }

    @Test
    @DisplayName("requestIng.resume - resume_at 직렬화")
    void testResumeAtSerialization() throws Exception {
        OrderSubscriptionResumeParams params = new OrderSubscriptionResumeParams();
        params.orderSubscriptionId = "OS_1";
        params.resumeAt = "2026-09-10";
        store.orderSubscription.requestIng.resume(params);

        assertTrue(lastBody.contains("\"resume_at\":\"2026-09-10\""), lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // multipart boolean 소문자 회귀 (P1 보강)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("product.create multipart - boolean 값은 소문자 true/false 로 전송 (Rails 캐스팅 회귀)")
    void testMultipartBooleanLowercase() throws Exception {
        File image = File.createTempFile("bootpay-test-bool", ".png");
        try (FileOutputStream out = new FileOutputStream(image)) {
            out.write(new byte[]{1, 2, 3});
        }
        image.deleteOnExit();

        SProduct product = new SProduct();
        product.name = "불리언상품";
        product.useStock = true;
        List<URL> images = new ArrayList<>();
        images.add(image.toURI().toURL());
        store.product.create(product, images);

        assertTrue(lastContentType.startsWith("multipart/form-data"), lastContentType);
        assertTrue(lastBody.contains("name=\"use_stock\""), lastBody);
        assertFalse(lastBody.contains("True"), "boolean 은 대문자 True 로 전송되면 안 됩니다 (Rails 캐스팅 drift)");
        assertFalse(lastBody.contains("False"), "boolean 은 대문자 False 로 전송되면 안 됩니다");
        assertTrue(lastBody.contains("\r\n\r\ntrue\r\n"), "소문자 true 로 전송: " + lastBody);
    }

    @Test
    @DisplayName("role - context 없는 요청은 인스턴스 기본 role 을 그대로 사용")
    void testDefaultRolePreservedWithoutContext() throws Exception {
        try {
            store.user.detail("U1");
            assertEquals("user", lastRole, "기본 role user");

            store.withRole("partner");
            store.user.detail("U1");
            assertEquals("partner", lastRole, "설정한 기본 role 이 덮어써지면 안 됩니다");
        } finally {
            store.clearRole();
        }
    }

    // ══════════════════════════════════════════════════════════
    // orderSubscription.supervisorTerminate (2-x-development 에서 통합)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("supervisorTerminate - PUT order_subscriptions/{id}/terminate, 정산 항목 전부 body 전송")
    void testSupervisorTerminate() throws Exception {
        SupervisorTerminateParams params = new SupervisorTerminateParams();
        params.reason = "고객 요청";
        params.terminationFee = 5000.0;
        params.lastBillRefundPrice = 1200.0;
        params.finalFee = 3800.0;
        params.serviceEndAt = "2026-09-01T00:00:00+09:00";
        params.cancelDate = "2026-08-20";

        store.orderSubscription.supervisorTerminate("subscription_1", params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/subscription_1/terminate", lastPath);
        assertTrue(lastBody.contains("\"reason\":\"고객 요청\""), lastBody);
        assertTrue(lastBody.contains("\"termination_fee\":5000"), lastBody);
        assertTrue(lastBody.contains("\"last_bill_refund_price\":1200"), lastBody);
        assertTrue(lastBody.contains("\"final_fee\":3800"), lastBody);
        assertTrue(lastBody.contains("\"service_end_at\":\"2026-09-01T00:00:00+09:00\""), lastBody);
        assertTrue(lastBody.contains("\"cancel_date\":\"2026-08-20\""), lastBody);
    }

    @Test
    @DisplayName("supervisorTerminate - params 가 null 이면 빈 바디, 기존 terminate 와 같은 엔드포인트")
    void testSupervisorTerminateNullParams() throws Exception {
        store.orderSubscription.supervisorTerminate("subscription_1", null);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/subscription_1/terminate", lastPath);
        assertEquals("{}", lastBody);
    }

    @Test
    @DisplayName("orderSubscriptionRequest.update - 정산 항목(price/termination_fee 등) body 전송")
    void testOrderSubscriptionRequestUpdateSettlementFields() throws Exception {
        kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams params =
                new kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams();
        params.orderSubscriptionRequestHistoryId = "history_1";
        params.approval = kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams.APPROVAL_APPROVE;
        params.reason = "승인";
        params.price = 10000.0;
        params.taxFreePrice = 0.0;
        params.terminationFee = 500.0;
        params.lastBillRefundPrice = 100.0;
        params.finalFee = 9600.0;
        params.serviceEndAt = "2026-09-01T00:00:00+09:00";

        store.orderSubscriptionRequest.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order-subscription-requests/history_1", lastPath);
        assertTrue(lastBody.contains("\"approval\":\"approve\""), lastBody);
        assertTrue(lastBody.contains("\"price\":10000"), lastBody);
        assertTrue(lastBody.contains("\"termination_fee\":500"), lastBody);
        assertTrue(lastBody.contains("\"last_bill_refund_price\":100"), lastBody);
        assertTrue(lastBody.contains("\"final_fee\":9600"), lastBody);
        assertTrue(lastBody.contains("\"service_end_at\":"), lastBody);
        assertFalse(lastBody.contains("order_subscription_request_history_id"), "ID 는 URL 에만: " + lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // invoice.create — ruby SDK request_checkout parity (3.3.0)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("invoice.create - user/products/delivery_price/use_* /usage_api_url/extra 전송, user role + Idempotency-Key")
    void testInvoiceCreateRubyParity() throws Exception {
        kr.co.bootpay.store.model.pojo.SInvoice invoice = new kr.co.bootpay.store.model.pojo.SInvoice();
        invoice.name = "테스트 청구서";
        invoice.memo = "테스트 청구서 상세 메모";
        invoice.price = 1000.0;
        invoice.taxFreePrice = 0.0;
        invoice.deliveryPrice = 2500.0;
        invoice.redirectUrl = "https://example.com";
        invoice.requestId = "test1";
        invoice.useNotification = true;
        invoice.useAutoLogin = true;
        invoice.usageApiUrl = "https://dev-api.bootapi.com/v1/billing/usage";
        invoice.sdk = false;

        kr.co.bootpay.store.model.pojo.SInvoiceUser user = new kr.co.bootpay.store.model.pojo.SInvoiceUser();
        user.membershipType = kr.co.bootpay.store.model.pojo.SInvoiceUser.MEMBERSHIP_TYPE_GUEST;
        user.name = "부트페이";
        user.userId = "test123";
        user.phone = "01095735114";
        invoice.user = user;

        kr.co.bootpay.store.model.pojo.SInvoiceProduct product = new kr.co.bootpay.store.model.pojo.SInvoiceProduct();
        product.productId = "66fa14954eac568eab4fc2d0";
        product.productOptionId = "68ede8c675febc5627363fb2";
        product.duration = 24;
        product.quantity = 1;
        invoice.products = java.util.Collections.singletonList(product);

        kr.co.bootpay.store.model.pojo.SInvoiceExtra extra = new kr.co.bootpay.store.model.pojo.SInvoiceExtra();
        extra.separatelyConfirmed = false;
        extra.createOrderImmediately = true;
        invoice.extra = extra;

        store.invoice.create(invoice);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/invoices", lastPath);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey, "Idempotency-Key 자동 생성");

        assertTrue(lastBody.contains("\"delivery_price\":2500"), lastBody);
        assertTrue(lastBody.contains("\"use_notification\":true"), lastBody);
        assertTrue(lastBody.contains("\"use_auto_login\":true"), lastBody);
        assertTrue(lastBody.contains("\"usage_api_url\":\"https://dev-api.bootapi.com/v1/billing/usage\""), lastBody);
        assertTrue(lastBody.contains("\"sdk\":false"), lastBody);

        assertTrue(lastBody.contains("\"user\":{"), lastBody);
        assertTrue(lastBody.contains("\"membership_type\":\"guest\""), lastBody);
        assertTrue(lastBody.contains("\"user_id\":\"test123\""), lastBody);

        assertTrue(lastBody.contains("\"products\":[{"), lastBody);
        assertTrue(lastBody.contains("\"product_id\":\"66fa14954eac568eab4fc2d0\""), lastBody);
        assertTrue(lastBody.contains("\"product_option_id\":\"68ede8c675febc5627363fb2\""), lastBody);
        assertTrue(lastBody.contains("\"duration\":24"), lastBody);
        assertTrue(lastBody.contains("\"quantity\":1"), lastBody);

        assertTrue(lastBody.contains("\"extra\":{"), lastBody);
        assertTrue(lastBody.contains("\"separately_confirmed\":false"), lastBody);
        assertTrue(lastBody.contains("\"create_order_immediately\":true"), lastBody);
    }

    @Test
    @DisplayName("invoice.create - price_adjustments/cycles 중첩 직렬화")
    void testInvoiceCreatePriceAdjustments() throws Exception {
        kr.co.bootpay.store.model.pojo.SInvoice invoice = new kr.co.bootpay.store.model.pojo.SInvoice();
        invoice.name = "과금 청구서";
        invoice.price = 1000.0;

        kr.co.bootpay.store.model.pojo.SInvoicePriceAdjustmentCycle cycle =
                new kr.co.bootpay.store.model.pojo.SInvoicePriceAdjustmentCycle();
        cycle.duration = 1;
        cycle.adjustmentType = kr.co.bootpay.store.model.pojo.SInvoicePriceAdjustmentCycle.ADJUSTMENT_TYPE_DISCOUNT_PERCENT;
        cycle.name = "첫달 할인";
        cycle.value = 20.0;
        cycle.minValue = 100.0;
        cycle.maxValue = 500.0;

        kr.co.bootpay.store.model.pojo.SInvoicePriceAdjustment adjustment =
                new kr.co.bootpay.store.model.pojo.SInvoicePriceAdjustment();
        adjustment.priceAdjustmentId = "test1";
        adjustment.startAt = "2025-09-20 00:00:00";
        adjustment.endAt = "2025-12-30 23:59:59";
        adjustment.name = "첫 구매 할인 프로모션";
        adjustment.cycles = java.util.Collections.singletonList(cycle);

        kr.co.bootpay.store.model.pojo.SInvoiceProduct product = new kr.co.bootpay.store.model.pojo.SInvoiceProduct();
        product.productId = "66fa14954eac568eab4fc2d0";
        product.quantity = 1;
        product.priceAdjustments = java.util.Collections.singletonList(adjustment);
        invoice.products = java.util.Collections.singletonList(product);

        store.invoice.create(invoice);

        assertEquals("POST", lastMethod);
        assertTrue(lastBody.contains("\"price_adjustments\":[{"), lastBody);
        assertTrue(lastBody.contains("\"price_adjustment_id\":\"test1\""), lastBody);
        assertTrue(lastBody.contains("\"start_at\":\"2025-09-20 00:00:00\""), lastBody);
        assertTrue(lastBody.contains("\"cycles\":[{"), lastBody);
        assertTrue(lastBody.contains("\"adjustment_type\":\"discount_percent\""), lastBody);
        assertTrue(lastBody.contains("\"min_value\":100"), lastBody);
        assertTrue(lastBody.contains("\"max_value\":500"), lastBody);
    }

    @Test
    @DisplayName("invoice.create - 미지정 필드는 전송하지 않는다 (기존 사용 패턴 회귀)")
    void testInvoiceCreateOmitsUnsetFields() throws Exception {
        kr.co.bootpay.store.model.pojo.SInvoice invoice = new kr.co.bootpay.store.model.pojo.SInvoice();
        invoice.name = "테스트 청구서";
        invoice.price = 3000.0;

        store.invoice.create(invoice);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/invoices", lastPath);
        assertFalse(lastBody.contains("user"), "지정하지 않은 user 는 전송되면 안 된다: " + lastBody);
        assertFalse(lastBody.contains("products"), "지정하지 않은 products 는 전송되면 안 된다: " + lastBody);
        assertFalse(lastBody.contains("delivery_price"), lastBody);
        assertFalse(lastBody.contains("sdk"), lastBody);
    }

    @Test
    @DisplayName("invoice.create - idempotencyKey 직접 지정 가능")
    void testInvoiceCreateExplicitIdempotencyKey() throws Exception {
        kr.co.bootpay.store.model.pojo.SInvoice invoice = new kr.co.bootpay.store.model.pojo.SInvoice();
        invoice.name = "테스트 청구서";
        invoice.price = 1000.0;

        store.invoice.create(invoice, "my-idem-key");

        assertEquals("my-idem-key", lastIdempotencyKey);
        assertFalse(lastBody.contains("idempotency"), "idempotencyKey 는 body 에 포함되면 안 된다: " + lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // 구독 가격 변경 · 범위 회차조정 (ruby SDK parity)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("orderSubscription.update - price 는 회차 기준금액으로 body 전송")
    void testOrderSubscriptionUpdatePrice() throws Exception {
        OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.price = 12000.0;
        store.orderSubscription.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1", lastPath);
        assertEquals("supervisor", lastRole);
        assertTrue(lastBody.contains("\"price\":12000"), lastBody);
    }

    @Test
    @DisplayName("orderSubscription.update - price 미지정시 전송하지 않는다")
    void testOrderSubscriptionUpdateOmitsUnsetPrice() throws Exception {
        OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.orderName = "변경구독";
        store.orderSubscription.update(params);

        assertFalse(lastBody.contains("price"), "지정하지 않은 price 는 전송되면 안 된다: " + lastBody);
    }

    @Test
    @DisplayName("adjustment.create - duration 단건 지정시 범위 필드는 전송하지 않는다")
    void testAdjustmentCreateSingleDuration() throws Exception {
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("5회차 할인", -1000.0);
        adjustment.duration = 5;
        store.orderSubscriptionAdjustment.create("OS_1", adjustment);

        assertTrue(lastBody.contains("\"duration\":5"), lastBody);
        assertFalse(lastBody.contains("duration_from"), lastBody);
        assertFalse(lastBody.contains("duration_to"), lastBody);
        assertFalse(lastBody.contains("is_unlimited"), lastBody);
    }

    @Test
    @DisplayName("adjustment.create - duration_from/duration_to 범위 지정 전송")
    void testAdjustmentCreateDurationRange() throws Exception {
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("3~7회차 할인", -1000.0);
        adjustment.durationFrom = 3;
        adjustment.durationTo = 7;
        store.orderSubscriptionAdjustment.create("OS_1", adjustment);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1/adjustments", lastPath);
        assertEquals("supervisor", lastRole);
        assertNotNull(lastIdempotencyKey);
        assertTrue(lastBody.contains("\"duration_from\":3"), lastBody);
        assertTrue(lastBody.contains("\"duration_to\":7"), lastBody);
        assertFalse(lastBody.contains("is_unlimited"), lastBody);
    }

    @Test
    @DisplayName("adjustment.create - is_unlimited 로 시작회차부터 계약 끝까지 지정")
    void testAdjustmentCreateUnlimitedRange() throws Exception {
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("3회차부터 추가금", 500.0, 0.0);
        adjustment.durationFrom = 3;
        adjustment.isUnlimited = true;
        store.orderSubscriptionAdjustment.create("OS_1", adjustment);

        assertTrue(lastBody.contains("\"duration_from\":3"), lastBody);
        assertTrue(lastBody.contains("\"is_unlimited\":true"), lastBody);
        assertFalse(lastBody.contains("duration_to"), "duration_to 는 지정하지 않으면 전송하지 않는다: " + lastBody);
    }

    @Test
    @DisplayName("adjustment.create - 회차 미지정시 duration 1 로 전송 (ruby SDK 기본값)")
    void testAdjustmentCreateDefaultsDurationToOne() throws Exception {
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("할인", -1000.0);
        store.orderSubscriptionAdjustment.create("OS_1", adjustment);

        assertTrue(lastBody.contains("\"duration\":1"), lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // 누락 파라미터 보강 (변경분) — 서버가 읽고 있었으나 SDK 가 안 보내던 값들
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("order.list - order_subscription_ids 는 콤마로 join, subscription_billing_type 전송")
    void testOrderListSubscriptionFilters() throws Exception {
        OrderListParams params = new OrderListParams();
        params.orderSubscriptionIds = java.util.Arrays.asList("OS1", "OS2");
        params.subscriptionBillingType = OrderListParams.SUBSCRIPTION_BILLING_TYPE_GROUP;
        store.order.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/orders", lastPath);
        assertTrue(lastQuery.contains("order_subscription_ids=OS1%2COS2") || lastQuery.contains("order_subscription_ids=OS1,OS2"), lastQuery);
        assertTrue(lastQuery.contains("subscription_billing_type=2"), lastQuery);
    }

    @Test
    @DisplayName("order.list - 비어있는 status/payment_status 는 전송하지 않는다")
    void testOrderListOmitsEmptyStatusFilters() throws Exception {
        OrderListParams params = new OrderListParams();
        params.status = new ArrayList<>();
        params.paymentStatus = new ArrayList<>();
        params.orderSubscriptionIds = new ArrayList<>();
        store.order.list(params);

        assertFalse(lastQuery != null && lastQuery.contains("status="), "빈 status 는 전송하지 않는다: " + lastQuery);
        assertFalse(lastQuery != null && lastQuery.contains("order_subscription_ids="), "빈 목록은 전송하지 않는다: " + lastQuery);
    }

    @Test
    @DisplayName("orderSubscription.list - order_number 로 주문번호 역조회")
    void testOrderSubscriptionListOrderNumber() throws Exception {
        OrderSubscriptionListParams params = new OrderSubscriptionListParams();
        params.orderNumber = "ORD-20260826-001";
        store.orderSubscription.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/order_subscriptions", lastPath);
        assertTrue(lastQuery.contains("order_number=ORD-20260826-001"), lastQuery);
    }

    @Test
    @DisplayName("orderSubscription.update - memo 를 변경사유로 전송")
    void testOrderSubscriptionUpdateMemo() throws Exception {
        OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.price = 12000.0;
        params.memo = "고객 요청 금액 변경";
        store.orderSubscription.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1", lastPath);
        assertEquals("supervisor", lastRole);
        assertTrue(lastBody.contains("\"memo\":\"고객 요청 금액 변경\""), lastBody);
    }

    @Test
    @DisplayName("orderSubscription.update - memo 미지정시 전송하지 않는다")
    void testOrderSubscriptionUpdateOmitsUnsetMemo() throws Exception {
        OrderSubscriptionUpdateParams params = new OrderSubscriptionUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.orderName = "변경구독";
        store.orderSubscription.update(params);

        assertFalse(lastBody.contains("memo"), "지정하지 않은 memo 는 전송되면 안 된다: " + lastBody);
    }

    @Test
    @DisplayName("product.products - ex_uid 로 외부 UID 조회")
    void testMallProductsExUid() throws Exception {
        MallProductListParams params = new MallProductListParams();
        params.exUid = "EXT-1";
        store.product.products(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/products", lastPath);
        assertTrue(lastQuery.contains("ex_uid=EXT-1"), lastQuery);
    }

    @Test
    @DisplayName("product.detail - user_jwt 지정시 Bootpay-User-JWT 헤더 전송 (productDetail 과 동일 동작)")
    void testProductDetailLookupWithJwt() throws Exception {
        store.product.detail("P1", "jwt-lookup");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/products/P1", lastPath);
        assertEquals("jwt-lookup", lastUserJwt);
        assertNotNull(lastIdempotencyKey);

        store.product.detail("P1");
        assertNull(lastUserJwt, "JWT 미지정시 헤더가 붙으면 안 됩니다");
    }

    @Test
    @DisplayName("user.list - 회원등급 필터는 membership_type 으로 전송, memberType 은 별칭")
    void testUserListMembershipType() throws Exception {
        UserListParams params = new UserListParams();
        params.membershipType = 1;
        store.user.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/users", lastPath);
        assertTrue(lastQuery.contains("membership_type=1"), lastQuery);
        assertFalse(lastQuery.contains("member_type=1"), "서버가 읽지 않는 키는 전송하지 않는다: " + lastQuery);

        UserListParams legacy = new UserListParams();
        legacy.memberType = 2;
        store.user.list(legacy);

        assertTrue(lastQuery.contains("membership_type=2"), "memberType 은 membership_type 으로 매핑된다: " + lastQuery);
    }

    @Test
    @DisplayName("adjustment.create - type 미지정시 전송하지 않는다 (서버 자동 판정에 맡긴다)")
    void testAdjustmentCreateOmitsUnsetType() throws Exception {
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("할인", -1000.0);
        store.orderSubscriptionAdjustment.create("OS_1", adjustment);

        // primitive int 기본값 0 이 실리면 서버가 자동 판정 대신 정의되지 않은 유형 0 으로 저장한다
        assertFalse(lastBody.contains("type"), "지정하지 않은 type 은 전송되면 안 된다: " + lastBody);
    }

    @Test
    @DisplayName("adjustment.update - adjustments 배열의 type 미지정시 전송하지 않는다 (기존 유형 승계)")
    void testAdjustmentUpdateOmitsUnsetTypeInArray() throws Exception {
        OrderSubscriptionAdjustmentUpdateParams params = new OrderSubscriptionAdjustmentUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.duration = 3;
        params.adjustments = new ArrayList<>();
        params.adjustments.add(new SOrderSubscriptionAdjustment("주기 추가비용", 5000.0));
        store.orderSubscriptionAdjustment.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/order_subscriptions/OS_1/adjustments", lastPath);
        // 서버는 type 미전송일 때만 기존 유형을 승계한다 (0 은 Ruby 에서 present? 라 승계를 막는다)
        assertFalse(lastBody.contains("\"type\""), "지정하지 않은 type 은 전송되면 안 된다: " + lastBody);
    }

    @Test
    @DisplayName("adjustment.update - adjustments 배열의 type 을 명시하면 그대로 전송한다")
    void testAdjustmentUpdateSendsExplicitTypeInArray() throws Exception {
        OrderSubscriptionAdjustmentUpdateParams params = new OrderSubscriptionAdjustmentUpdateParams();
        params.orderSubscriptionId = "OS_1";
        params.duration = 3;
        params.adjustments = new ArrayList<>();
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("주기 추가비용", 5000.0);
        adjustment.type = 4;
        params.adjustments.add(adjustment);
        store.orderSubscriptionAdjustment.update(params);

        assertTrue(lastBody.contains("\"type\":4"), lastBody);
    }

    @Test
    @DisplayName("adjustment.create - type 을 명시하면 그대로 전송한다")
    void testAdjustmentCreateSendsExplicitType() throws Exception {
        SOrderSubscriptionAdjustment adjustment = new SOrderSubscriptionAdjustment("추가금", 500.0);
        adjustment.type = 2; // SETUP_PRICE
        store.orderSubscriptionAdjustment.create("OS_1", adjustment);

        assertTrue(lastBody.contains("\"type\":2"), lastBody);
    }

}
