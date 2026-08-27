package kr.co.bootpay.commerce;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kr.co.bootpay.common.BootpayMode;
import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.common.BootpayRole;
import kr.co.bootpay.store.BootpayCommerce;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SMallSetting;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendParams;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.user.UserListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
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
 * 신규 {@link BootpayCommerce} 표면이 기존 {@link BootpayStore} 와
 * <b>완전히 같은 HTTP 요청</b>을 만드는지 검증한다. 네트워크는 필요 없다.
 */
@DisplayName("Commerce API - 신규 모듈 표면 ↔ 기존 표면 요청 동등성")
class CommerceModuleParityTest {

    private static HttpServer server;
    private static BootpayStore legacy;
    private static BootpayCommerce modern;

    private static volatile String lastMethod;
    private static volatile String lastPath;
    private static volatile String lastQuery;
    private static volatile String lastBody;
    private static volatile String lastRole;

    @BeforeAll
    static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", CommerceModuleParityTest::capture);
        server.start();

        String baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/v1/";

        legacy = new BootpayStore(new TokenPayload("test_ck", "test_sk"), "PRODUCTION");
        legacy.baseUrl = baseUrl;
        legacy.setTokenFromAPI("test_token");

        modern = BootpayCommerce.builder()
                .clientKey("test_ck")
                .secretKey("test_sk")
                .mode(BootpayMode.PRODUCTION)
                .build();
        modern.unwrap().baseUrl = baseUrl;
        modern.unwrap().setTokenFromAPI("test_token");
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
        lastRole = exchange.getRequestHeaders().getFirst("BOOTPAY-ROLE");

        byte[] response = "{\"ok\":true}".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(response);
        }
    }

    private interface Call {
        void run() throws Exception;
    }

    private static String[] record(Call call) throws Exception {
        lastMethod = null;
        lastPath = null;
        lastQuery = null;
        lastBody = null;
        lastRole = null;
        call.run();
        return new String[]{lastMethod, lastPath, lastQuery, lastBody, lastRole};
    }

    private static void assertSameRequest(String label, Call legacyCall, Call modernCall) throws Exception {
        String[] before = record(legacyCall);
        String[] after = record(modernCall);

        assertNotNull(before[0], label + " — 기존 호출이 요청을 만들지 않았다");
        assertEquals(before[0], after[0], label + " — HTTP method 불일치");
        assertEquals(before[1], after[1], label + " — path 불일치");
        assertEquals(before[2], after[2], label + " — query 불일치");
        assertEquals(before[3], after[3], label + " — body 불일치");
        assertEquals(before[4], after[4], label + " — BOOTPAY-ROLE 헤더 불일치");
    }

    @Test
    @DisplayName("store / project / user / userGroup")
    void coreModulesMatchLegacy() throws Exception {
        assertSameRequest("store.info", () -> legacy.store.info(), () -> modern.store.info());
        assertSameRequest("store.detail", () -> legacy.store.detail(), () -> modern.store.detail());
        assertSameRequest("project.me", () -> legacy.project.me(), () -> modern.project.me());

        assertSameRequest("user.list",
                () -> legacy.user.list(new UserListParams()),
                () -> modern.user.list(new UserListParams()));
        assertSameRequest("user.detail",
                () -> legacy.user.detail("user_1"),
                () -> modern.user.detail("user_1"));
        assertSameRequest("user.delete",
                () -> legacy.user.delete("user_1"),
                () -> modern.user.delete("user_1"));
        assertSameRequest("user.join",
                () -> legacy.user.join(user()),
                () -> modern.user.join(user()));
        assertSameRequest("user.token",
                () -> legacy.user.token("user_1"),
                () -> modern.user.token("user_1"));
        assertSameRequest("user.checkExist",
                () -> legacy.user.checkExist("id-exist", "abc"),
                () -> modern.user.checkExist("id-exist", "abc"));
    }

    @Test
    @DisplayName("이름이 바뀐 몰 프론트 API — userLogin → mallLogin 등")
    void renamedMallApisMatchLegacy() throws Exception {
        assertSameRequest("userLogin → mallLogin",
                () -> legacy.user.userLogin("id", "pw"),
                () -> modern.user.mallLogin("id", "pw"));
        assertSameRequest("userSession → mallSession",
                () -> legacy.user.userSession("jwt"),
                () -> modern.user.mallSession("jwt"));
        assertSameRequest("userLogout → mallLogout",
                () -> legacy.user.userLogout("jwt"),
                () -> modern.user.mallLogout("jwt"));
        assertSameRequest("userJoinCheck → mallJoinCheck",
                () -> legacy.user.userJoinCheck("id-exist", "abc"),
                () -> modern.user.mallJoinCheck("id-exist", "abc"));
        assertSameRequest("uidExist",
                () -> legacy.user.uidExist("uid_1"),
                () -> modern.user.uidExist("uid_1"));

        assertSameRequest("products → mallList",
                () -> legacy.product.products(),
                () -> modern.product.mallList());
        assertSameRequest("productDetail → mallDetail",
                () -> legacy.product.productDetail("product_1", "jwt"),
                () -> modern.product.mallDetail("product_1", "jwt"));
    }

    @Test
    @DisplayName("product / order / orderCancel / category / coupon / point / cart")
    void catalogModulesMatchLegacy() throws Exception {
        assertSameRequest("product.list",
                () -> legacy.product.list(new ProductListParams()),
                () -> modern.product.list(new ProductListParams()));
        assertSameRequest("product.detail",
                () -> legacy.product.detail("product_1"),
                () -> modern.product.detail("product_1"));
        assertSameRequest("product.delete",
                () -> legacy.product.delete("product_1"),
                () -> modern.product.delete("product_1"));

        assertSameRequest("order.list",
                () -> legacy.order.list(new OrderListParams()),
                () -> modern.order.list(new OrderListParams()));
        assertSameRequest("order.detail",
                () -> legacy.order.detail("order_1"),
                () -> modern.order.detail("order_1"));

        assertSameRequest("category.list", () -> legacy.category.list(), () -> modern.category.list());
        assertSameRequest("category.detail",
                () -> legacy.category.detail("category_1"),
                () -> modern.category.detail("category_1"));

        assertSameRequest("coupon.list", () -> legacy.coupon.list(), () -> modern.coupon.list());
        assertSameRequest("point.balance", () -> legacy.point.balance(), () -> modern.point.balance());
        assertSameRequest("cart.orderPreview",
                () -> legacy.cart.orderPreview(),
                () -> modern.cart.orderPreview());
    }

    @Test
    @DisplayName("orderSubscription 계열 / mallSetting / webhook")
    void subscriptionAndSettingModulesMatchLegacy() throws Exception {
        assertSameRequest("orderSubscription.detail",
                () -> legacy.orderSubscription.detail("subscription_1"),
                () -> modern.orderSubscription.detail("subscription_1"));
        assertSameRequest("orderSubscription.requestIng.calculateTerminationFee",
                () -> legacy.orderSubscription.requestIng.calculateTerminationFee("subscription_1"),
                () -> modern.orderSubscription.requestIng.calculateTerminationFee("subscription_1"));
        assertSameRequest("orderSubscriptionBill.detail",
                () -> legacy.orderSubscriptionBill.detail("bill_1"),
                () -> modern.orderSubscriptionBill.detail("bill_1"));
        assertSameRequest("orderSubscriptionRequest.list",
                () -> legacy.orderSubscriptionRequest.list(),
                () -> modern.orderSubscriptionRequest.list());

        assertSameRequest("mallSetting.getMallSetting → detail",
                () -> legacy.mallSetting.getMallSetting(),
                () -> modern.mallSetting.detail());
        assertSameRequest("mallSetting.updateMallSetting → update",
                () -> legacy.mallSetting.updateMallSetting(new SMallSetting()),
                () -> modern.mallSetting.update(new SMallSetting()));

        assertSameRequest("webhook.sendTest",
                () -> legacy.webhook.sendTest(),
                () -> modern.webhook.sendTest());
    }

    @Test
    @DisplayName("알림톡 계열 — 신규 모듈 표면이 기존 표면과 같은 요청을 만든다")
    void alimtalkModulesMatchLegacy() throws Exception {
        assertSameRequest("alimtalkSend.send",
                () -> legacy.alimtalkSend.send(sendParams()),
                () -> modern.alimtalkSend.send(sendParams()));
        assertSameRequest("alimtalkSend.cancel",
                () -> legacy.alimtalkSend.cancel("RCP_1"),
                () -> modern.alimtalkSend.cancel("RCP_1"));

        assertSameRequest("alimtalkSender.categories",
                () -> legacy.alimtalkSender.categories(),
                () -> modern.alimtalkSender.categories());
        assertSameRequest("alimtalkSender.detail",
                () -> legacy.alimtalkSender.detail("KSP_1", true),
                () -> modern.alimtalkSender.detail("KSP_1", true));

        assertSameRequest("alimtalkTemplate.list",
                () -> legacy.alimtalkTemplate.list(),
                () -> modern.alimtalkTemplate.list());
        assertSameRequest("alimtalkTemplate.inspect",
                () -> legacy.alimtalkTemplate.inspect("TPL_1"),
                () -> modern.alimtalkTemplate.inspect("TPL_1"));
        assertSameRequest("alimtalkTemplate.export",
                () -> legacy.alimtalkTemplate.export(),
                () -> modern.alimtalkTemplate.export());

        assertSameRequest("alimtalkOfficial.recommend",
                () -> legacy.alimtalkOfficial.recommend("주문이 완료되었습니다"),
                () -> modern.alimtalkOfficial.recommend("주문이 완료되었습니다"));

        assertSameRequest("alimtalkMessage.stats",
                () -> legacy.alimtalkMessage.stats("2026-08-01", "2026-08-27"),
                () -> modern.alimtalkMessage.stats("2026-08-01", "2026-08-27"));

        assertSameRequest("alimtalkOptout.check",
                () -> legacy.alimtalkOptout.check("01012345678"),
                () -> modern.alimtalkOptout.check("01012345678"));

        assertSameRequest("alimtalkWebhook.detail",
                () -> legacy.alimtalkWebhook.detail(),
                () -> modern.alimtalkWebhook.detail());
        assertSameRequest("alimtalkWebhook.deliveries",
                () -> legacy.alimtalkWebhook.deliveries(),
                () -> modern.alimtalkWebhook.deliveries());
    }

    @Test
    @DisplayName("subscriptionSetting — 기존 BootpayStore 에서 도달할 수 없던 모듈")
    void subscriptionSettingIsReachable() throws Exception {
        BootpayResponse res = modern.subscriptionSetting.list(new ListParams());

        assertTrue(res.isSuccess());
        assertEquals("GET", lastMethod);
        assertNotNull(lastPath);
    }

    @Test
    @DisplayName("role 은 BOOTPAY-ROLE 헤더로 전달되어야 한다")
    void roleIsSentAsHeader() throws Exception {
        BootpayCommerce manager = BootpayCommerce.builder()
                .clientKey("test_ck")
                .secretKey("test_sk")
                .role(BootpayRole.MANAGER)
                .build();
        manager.unwrap().baseUrl = modern.unwrap().baseUrl;
        manager.unwrap().setTokenFromAPI("test_token");

        assertEquals(BootpayRole.MANAGER, manager.role());
        manager.project.me();
        assertEquals("manager", lastRole);

        manager.role(BootpayRole.SUPERVISOR);
        manager.project.me();
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("모듈 응답은 기존 BootpayStoreResponse 와 같은 본문을 담아야 한다")
    void moduleResponseCarriesSameBody() throws Exception {
        BootpayStoreResponse before = legacy.project.me();
        BootpayResponse after = modern.project.me();

        assertEquals(before.isSuccess(), after.isSuccess());
        assertEquals(before.getData().get("ok"), after.get("ok"));
        assertTrue(after.asMap().containsKey("data"), "asMap() 은 기존 응답 구조를 그대로 노출해야 한다");
    }

    private static AlimtalkSendParams sendParams() {
        AlimtalkSendParams params = new AlimtalkSendParams();
        params.templateCode = "TPL_1";
        params.to = "01012345678";
        return params;
    }

    private static SUser user() {
        SUser user = new SUser();
        user.loginId = "user_1";
        return user;
    }
}
