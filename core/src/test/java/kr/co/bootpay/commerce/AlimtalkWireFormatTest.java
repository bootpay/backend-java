package kr.co.bootpay.commerce;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SAlimtalkRecipient;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkMessageListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialRecommendParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOptoutListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendBulkParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSenderCreateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateCreateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateExportParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkTemplateUpdateParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookDeliveriesParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 알림톡 v1 API 35종 — wire-format 검증 (네트워크 불필요).
 *
 * <p>ruby SDK {@code lib/bootpay_store/concern/alimtalk_*.rb} 와 같은 method / path / query / body 를
 * 만드는지 로컬 루프백 HTTP 서버로 캡처해 검증한다.</p>
 */
@DisplayName("Commerce API - 알림톡 Wire Format (ruby SDK parity)")
class AlimtalkWireFormatTest {

    private static HttpServer server;
    private static BootpayStore store;

    private static volatile String lastMethod;
    private static volatile String lastPath;
    private static volatile String lastQuery;
    private static volatile String lastBody;
    private static volatile String lastRole;
    private static volatile String lastIdempotencyKey;
    private static volatile String lastAccept;
    private static volatile String lastContentType;

    @BeforeAll
    static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", AlimtalkWireFormatTest::capture);
        server.start();

        store = new BootpayStore(new TokenPayload("test_ck", "test_sk"), "PRODUCTION");
        store.baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/v1/";
        // 알림톡은 인스턴스 role 과 무관하게 항상 user 로 나가야 한다 — 일부러 다른 값으로 둔다
        store.setRole("supervisor");
    }

    @AfterAll
    static void tearDown() {
        if (server != null) server.stop(0);
    }

    @BeforeEach
    void resetCapture() {
        lastMethod = lastPath = lastQuery = lastBody = lastRole = lastIdempotencyKey = lastAccept = lastContentType = null;
    }

    private static void capture(HttpExchange exchange) throws java.io.IOException {
        lastMethod = exchange.getRequestMethod();
        lastPath = exchange.getRequestURI().getPath();
        lastQuery = exchange.getRequestURI().getQuery();
        lastRole = exchange.getRequestHeaders().getFirst("BOOTPAY-ROLE");
        lastIdempotencyKey = exchange.getRequestHeaders().getFirst("Idempotency-Key");
        lastAccept = exchange.getRequestHeaders().getFirst("Accept");
        lastContentType = exchange.getRequestHeaders().getFirst("Content-Type");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream in = exchange.getRequestBody()) {
            byte[] chunk = new byte[4096];
            int read;
            while ((read = in.read(chunk)) != -1) buffer.write(chunk, 0, read);
        }
        lastBody = new String(buffer.toByteArray(), StandardCharsets.UTF_8);

        // 템플릿 내보내기 format=csv 는 서버가 CSV 원문을 돌려준다 (JSON 이 아니다)
        boolean csv = "/v1/alimtalk/templates/export".equals(lastPath)
                && lastQuery != null && lastQuery.contains("format=csv");

        byte[] response = (csv ? "code,name\nTPL_1,주문완료\n" : "{\"ok\":true}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", csv ? "text/csv; charset=utf-8" : "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(response);
        }
    }

    // ══════════════════════════════════════════════════════════
    // 공통 계약 — role 은 항상 user, Idempotency-Key 는 붙이지 않는다
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("알림톡은 인스턴스 role 이 supervisor 여도 항상 BOOTPAY-ROLE: user 로 보낸다")
    void testRoleAlwaysUser() throws Exception {
        store.alimtalkSender.list();
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("알림톡은 Idempotency-Key 를 싣지 않는다 (서버가 읽지 않는다 — 멱등은 ref_id 로만 성립)")
    void testNoIdempotencyKey() throws Exception {
        store.alimtalkSend.send("TPL_1", "01000000000");
        assertNull(lastIdempotencyKey);

        store.alimtalkMessage.list();
        assertNull(lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // 발송 — /alimtalk/send
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("send - POST alimtalk/send, variables/ref_id/reserved_at/sender_key/user_id 전송")
    void testSend() throws Exception {
        Map<String, Object> variables = new LinkedHashMap<>();
        variables.put("company_name", "부트페이몰");
        variables.put("user_name", "홍길동");

        AlimtalkSendParams params = new AlimtalkSendParams();
        params.templateCode = "TPL_1";
        params.to = "01012345678";
        params.variables = variables;
        params.refId = "order-0001";
        params.reservedAt = "2026-08-28T10:00:00+09:00";
        params.senderKey = "SENDER_KEY";
        params.userId = "USER_1";
        store.alimtalkSend.send(params);

        assertAll(
                () -> assertEquals("POST", lastMethod),
                () -> assertEquals("/v1/alimtalk/send", lastPath),
                () -> assertNull(lastQuery),
                () -> assertTrue(lastBody.contains("\"template_code\":\"TPL_1\""), lastBody),
                () -> assertTrue(lastBody.contains("\"to\":\"01012345678\""), lastBody),
                () -> assertTrue(lastBody.contains("\"company_name\":\"부트페이몰\""), lastBody),
                () -> assertTrue(lastBody.contains("\"ref_id\":\"order-0001\""), lastBody),
                () -> assertTrue(lastBody.contains("\"reserved_at\":\"2026-08-28T10:00:00+09:00\""), lastBody),
                () -> assertTrue(lastBody.contains("\"sender_key\":\"SENDER_KEY\""), lastBody),
                () -> assertTrue(lastBody.contains("\"user_id\":\"USER_1\""), lastBody),
                () -> assertEquals("user", lastRole)
        );
    }

    @Test
    @DisplayName("send - fallback 은 false 도 그대로 전송한다 (미지정 null 과 다르다)")
    void testSendFallbackFalseIsSent() throws Exception {
        AlimtalkSendParams off = new AlimtalkSendParams();
        off.templateCode = "TPL_1";
        off.to = "01012345678";
        off.fallback = false;
        store.alimtalkSend.send(off);
        assertTrue(lastBody.contains("\"fallback\":false"), "false 는 명시적으로 끄는 뜻이라 전송해야 한다: " + lastBody);

        AlimtalkSendParams unspecified = new AlimtalkSendParams();
        unspecified.templateCode = "TPL_1";
        unspecified.to = "01012345678";
        store.alimtalkSend.send(unspecified);
        assertFalse(lastBody.contains("fallback"), "미지정이면 전송하지 않는다 (프로젝트 기본값을 따른다): " + lastBody);
    }

    @Test
    @DisplayName("sendBulk - POST alimtalk/send/bulk, recipients 는 to/ref_id/variables 로 직렬화")
    void testSendBulk() throws Exception {
        Map<String, Object> variables = new LinkedHashMap<>();
        variables.put("user_name", "홍길동");

        List<SAlimtalkRecipient> recipients = new ArrayList<>();
        recipients.add(new SAlimtalkRecipient("01012345678", "bulk-0001", variables));

        AlimtalkSendBulkParams params = new AlimtalkSendBulkParams();
        params.templateCode = "TPL_1";
        params.recipients = recipients;
        params.fallback = true;
        store.alimtalkSend.sendBulk(params);

        assertAll(
                () -> assertEquals("POST", lastMethod),
                () -> assertEquals("/v1/alimtalk/send/bulk", lastPath),
                () -> assertTrue(lastBody.contains("\"recipients\":["), lastBody),
                () -> assertTrue(lastBody.contains("\"to\":\"01012345678\""), lastBody),
                () -> assertTrue(lastBody.contains("\"ref_id\":\"bulk-0001\""), lastBody),
                () -> assertTrue(lastBody.contains("\"user_name\":\"홍길동\""), lastBody),
                () -> assertTrue(lastBody.contains("\"fallback\":true"), lastBody)
        );
    }

    @Test
    @DisplayName("send.cancel - DELETE alimtalk/send/{receipt_id}")
    void testSendCancel() throws Exception {
        store.alimtalkSend.cancel("RCP_1");

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/alimtalk/send/RCP_1", lastPath);
        assertEquals("user", lastRole);
    }

    // ══════════════════════════════════════════════════════════
    // 발신프로필 — /alimtalk/categories · /alimtalk/senders
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("sender.categories - GET alimtalk/categories")
    void testSenderCategories() throws Exception {
        store.alimtalkSender.categories();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/categories", lastPath);
        assertNull(lastQuery);
    }

    @Test
    @DisplayName("sender.otp - POST alimtalk/senders/otp, yellow_id/phone 전송")
    void testSenderOtp() throws Exception {
        store.alimtalkSender.otp("@bootpay", "01012345678");

        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/senders/otp", lastPath);
        assertTrue(lastBody.contains("\"yellow_id\":\"@bootpay\""), lastBody);
        assertTrue(lastBody.contains("\"phone\":\"01012345678\""), lastBody);
    }

    @Test
    @DisplayName("sender.create - POST alimtalk/senders, otp/yellow_id/phone/category_code 전송")
    void testSenderCreate() throws Exception {
        AlimtalkSenderCreateParams params = new AlimtalkSenderCreateParams();
        params.otp = "123456";
        params.yellowId = "@bootpay";
        params.phone = "01012345678";
        params.categoryCode = "001001";
        store.alimtalkSender.create(params);

        assertAll(
                () -> assertEquals("POST", lastMethod),
                () -> assertEquals("/v1/alimtalk/senders", lastPath),
                () -> assertTrue(lastBody.contains("\"otp\":\"123456\""), lastBody),
                () -> assertTrue(lastBody.contains("\"yellow_id\":\"@bootpay\""), lastBody),
                () -> assertTrue(lastBody.contains("\"category_code\":\"001001\""), lastBody)
        );
    }

    @Test
    @DisplayName("sender.detail - GET alimtalk/senders/{ksp_id}, sync 는 지정시에만 query 전송")
    void testSenderDetail() throws Exception {
        store.alimtalkSender.detail("KSP_1");
        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/senders/KSP_1", lastPath);
        assertNull(lastQuery, "sync 미지정이면 query 를 붙이지 않는다");

        store.alimtalkSender.detail("KSP_1", true);
        assertEquals("sync=true", lastQuery);
    }

    @Test
    @DisplayName("sender.release - DELETE alimtalk/senders/{ksp_id}")
    void testSenderRelease() throws Exception {
        store.alimtalkSender.release("KSP_1");

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/alimtalk/senders/KSP_1", lastPath);
    }

    @Test
    @DisplayName("sender.variableExamples - PUT alimtalk/senders/{ksp_id}/variable_examples, examples 로 감싸 전송")
    void testSenderVariableExamples() throws Exception {
        Map<String, Object> examples = new LinkedHashMap<>();
        examples.put("user_name", "홍길동");
        store.alimtalkSender.variableExamples("KSP_1", examples);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/alimtalk/senders/KSP_1/variable_examples", lastPath);
        assertEquals("{\"examples\":{\"user_name\":\"홍길동\"}}", lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // 자체 템플릿 — /alimtalk/templates
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("template.list - GET alimtalk/templates, ins/sort/keyword query 전송")
    void testTemplateList() throws Exception {
        AlimtalkTemplateListParams params = new AlimtalkTemplateListParams();
        params.ins = "3";
        params.sort = "latest";
        params.keyword = "주문";
        store.alimtalkTemplate.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/templates", lastPath);
        assertTrue(lastQuery.contains("ins=3"), lastQuery);
        assertTrue(lastQuery.contains("sort=latest"), lastQuery);
    }

    @Test
    @DisplayName("template.create - POST alimtalk/templates, ksp_id/register + 본문 필드 전송")
    void testTemplateCreate() throws Exception {
        Map<String, Object> button = new LinkedHashMap<>();
        button.put("name", "주문 확인");
        button.put("type", "WL");

        List<Map<String, Object>> buttons = new ArrayList<>();
        buttons.add(button);

        AlimtalkTemplateCreateParams params = new AlimtalkTemplateCreateParams();
        params.kspId = "KSP_1";
        params.register = false;
        params.name = "주문 완료 안내";
        params.content = "#{user_name}님, 주문이 완료되었습니다.";
        params.msgType = "BA";
        params.emphasizeType = "NONE";
        params.buttons = buttons;
        params.tags = Arrays.asList("order", "commerce");
        store.alimtalkTemplate.create(params);

        assertAll(
                () -> assertEquals("POST", lastMethod),
                () -> assertEquals("/v1/alimtalk/templates", lastPath),
                () -> assertTrue(lastBody.contains("\"ksp_id\":\"KSP_1\""), lastBody),
                () -> assertTrue(lastBody.contains("\"register\":false"), "register=false 는 그대로 전송: " + lastBody),
                () -> assertTrue(lastBody.contains("\"msg_type\":\"BA\""), lastBody),
                () -> assertTrue(lastBody.contains("\"emphasize_type\":\"NONE\""), lastBody),
                () -> assertTrue(lastBody.contains("\"buttons\":[{\"name\":\"주문 확인\",\"type\":\"WL\"}]"), lastBody),
                () -> assertTrue(lastBody.contains("\"tags\":[\"order\",\"commerce\"]"), lastBody)
        );
    }

    @Test
    @DisplayName("template.create - 미지정 필드는 전송하지 않는다 (ruby .compact 와 같은 규칙)")
    void testTemplateCreateOmitsNulls() throws Exception {
        AlimtalkTemplateCreateParams params = new AlimtalkTemplateCreateParams();
        params.kspId = "KSP_1";
        params.name = "주문 완료 안내";
        store.alimtalkTemplate.create(params);

        assertFalse(lastBody.contains("register"), lastBody);
        assertFalse(lastBody.contains("content"), lastBody);
        assertFalse(lastBody.contains("buttons"), lastBody);
        assertFalse(lastBody.contains("storage_image_url"), lastBody);
    }

    @Test
    @DisplayName("template.create - attrs 는 이름 변환 없이 그대로 실려 나간다 (ruby **attrs)")
    void testTemplateCreateExtraAttrs() throws Exception {
        Map<String, Object> attrs = new LinkedHashMap<>();
        attrs.put("new_server_field", "value");

        AlimtalkTemplateCreateParams params = new AlimtalkTemplateCreateParams();
        params.kspId = "KSP_1";
        params.attrs = attrs;
        store.alimtalkTemplate.create(params);

        assertTrue(lastBody.contains("\"new_server_field\":\"value\""), lastBody);
    }

    @Test
    @DisplayName("template.update - PUT alimtalk/templates/{id}, ksp_id/register 는 섞이지 않는다")
    void testTemplateUpdate() throws Exception {
        AlimtalkTemplateUpdateParams params = new AlimtalkTemplateUpdateParams();
        params.name = "주문 완료 안내";
        params.content = "#{user_name}님, 감사합니다.";
        params.storageImageUrl = ""; // 빈 값은 이미지 삭제 신호라 그대로 전송해야 한다
        store.alimtalkTemplate.update("TPL_DOC_1", params);

        assertAll(
                () -> assertEquals("PUT", lastMethod),
                () -> assertEquals("/v1/alimtalk/templates/TPL_DOC_1", lastPath),
                () -> assertTrue(lastBody.contains("\"storage_image_url\":\"\""), "빈 값은 이미지 삭제 신호다: " + lastBody),
                () -> assertFalse(lastBody.contains("ksp_id"), lastBody),
                () -> assertFalse(lastBody.contains("register"), lastBody)
        );
    }

    @Test
    @DisplayName("template.detail - GET alimtalk/templates/{id}, sync 는 지정시에만 query 전송")
    void testTemplateDetail() throws Exception {
        store.alimtalkTemplate.detail("TPL_DOC_1");
        assertEquals("/v1/alimtalk/templates/TPL_DOC_1", lastPath);
        assertNull(lastQuery);

        store.alimtalkTemplate.detail("TPL_DOC_1", false);
        assertEquals("sync=false", lastQuery);
    }

    @Test
    @DisplayName("template.delete / register / inspect - 경로와 method")
    void testTemplateLifecycle() throws Exception {
        store.alimtalkTemplate.delete("TPL_DOC_1");
        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/alimtalk/templates/TPL_DOC_1", lastPath);

        store.alimtalkTemplate.register("TPL_DOC_1");
        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/templates/TPL_DOC_1/register", lastPath);
        assertEquals("{}", lastBody);

        store.alimtalkTemplate.inspect("TPL_DOC_1");
        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/templates/TPL_DOC_1/inspect", lastPath);
        assertEquals("{}", lastBody);
    }

    @Test
    @DisplayName("template.export - format 미지정시 json 을 보낸다 (서버 기본 csv 를 SDK 가 덮는다)")
    void testTemplateExportDefaultsToJson() throws Exception {
        BootpayStoreResponse res = store.alimtalkTemplate.export();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/templates/export", lastPath);
        assertEquals("format=json", lastQuery);
        assertTrue(res.isSuccess());
        assertEquals(Boolean.TRUE, res.getData().get("ok"));
    }

    @Test
    @DisplayName("template.export - csv 는 파싱 없이 { body, content_type } 원문으로 돌려준다")
    void testTemplateExportCsvRaw() throws Exception {
        AlimtalkTemplateExportParams params = new AlimtalkTemplateExportParams();
        params.format = "csv";
        params.scope = "private";
        params.includeContent = true;
        BootpayStoreResponse res = store.alimtalkTemplate.export(params);

        assertAll(
                () -> assertEquals("GET", lastMethod),
                () -> assertEquals("/v1/alimtalk/templates/export", lastPath),
                () -> assertTrue(lastQuery.contains("format=csv"), lastQuery),
                () -> assertTrue(lastQuery.contains("scope=private"), lastQuery),
                () -> assertTrue(lastQuery.contains("include_content=true"), lastQuery),
                () -> assertEquals("*/*", lastAccept, "CSV 경로는 Accept 를 */* 로 보낸다"),
                () -> assertTrue(res.isSuccess(), "CSV 응답이 통신 실패로 보고되면 안 된다"),
                () -> assertFalse(res.hasError()),
                () -> assertEquals("code,name\nTPL_1,주문완료\n", res.getData().get("body")),
                () -> assertTrue(String.valueOf(res.getData().get("content_type")).startsWith("text/csv"))
        );
    }

    @Test
    @DisplayName("template.image - multipart 로 image 필드에 파일 1개, replace_url 동봉")
    void testTemplateImageUpload() throws Exception {
        File image = File.createTempFile("alimtalk-", ".png");
        image.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(image)) {
            out.write(new byte[]{(byte) 0x89, 'P', 'N', 'G'});
        }

        store.alimtalkTemplate.image(image, "https://cdn.example.com/old.png");

        assertAll(
                () -> assertEquals("POST", lastMethod),
                () -> assertEquals("/v1/alimtalk/templates/image", lastPath),
                () -> assertTrue(lastContentType.startsWith("multipart/form-data"), lastContentType),
                () -> assertTrue(lastContentType.contains("boundary="), "boundary 가 사라지면 본문이 깨진다: " + lastContentType),
                () -> assertTrue(lastBody.contains("name=\"image\""), lastBody),
                () -> assertFalse(lastBody.contains("name=\"images[0]\""), "알림톡은 images[i] 가 아니라 image 단일 필드다: " + lastBody),
                () -> assertTrue(lastBody.contains("name=\"replace_url\""), lastBody),
                () -> assertTrue(lastBody.contains("https://cdn.example.com/old.png"), lastBody)
        );
    }

    @Test
    @DisplayName("template.highlightImage - 본문 이미지와 다른 엔드포인트로 나간다")
    void testTemplateHighlightImageUpload() throws Exception {
        File image = File.createTempFile("alimtalk-highlight-", ".png");
        image.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(image)) {
            out.write(new byte[]{(byte) 0x89, 'P', 'N', 'G'});
        }

        store.alimtalkTemplate.highlightImage(image);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/templates/highlight_image", lastPath);
        assertTrue(lastBody.contains("name=\"image\""), lastBody);
        assertFalse(lastBody.contains("name=\"replace_url\""), "replace_url 미지정시 붙이지 않는다: " + lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // 공식 템플릿 — /alimtalk/official
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("official.list - keyword 는 서버 정본 키인 q 로 전송한다")
    void testOfficialList() throws Exception {
        AlimtalkOfficialListParams params = new AlimtalkOfficialListParams();
        params.keyword = "order";
        params.msgType = "BA";
        params.per = 50;
        params.kspId = "KSP_1";
        store.alimtalkOfficial.list(params);

        assertAll(
                () -> assertEquals("GET", lastMethod),
                () -> assertEquals("/v1/alimtalk/official", lastPath),
                () -> assertTrue(lastQuery.contains("q=order"), lastQuery),
                () -> assertFalse(lastQuery.contains("keyword="), "정본 키는 q 다: " + lastQuery),
                () -> assertTrue(lastQuery.contains("msg_type=BA"), lastQuery),
                () -> assertTrue(lastQuery.contains("per=50"), lastQuery),
                () -> assertTrue(lastQuery.contains("ksp_id=KSP_1"), lastQuery)
        );
    }

    @Test
    @DisplayName("official.recommend - POST alimtalk/official/recommend, text 전송")
    void testOfficialRecommend() throws Exception {
        AlimtalkOfficialRecommendParams params = new AlimtalkOfficialRecommendParams();
        params.text = "주문이 완료되었습니다";
        params.limit = 3;
        store.alimtalkOfficial.recommend(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/official/recommend", lastPath);
        assertTrue(lastBody.contains("\"text\":\"주문이 완료되었습니다\""), lastBody);
        assertTrue(lastBody.contains("\"limit\":3"), lastBody);
    }

    @Test
    @DisplayName("official.detail - GET alimtalk/official/{code}, ksp_id 는 지정시에만 전송")
    void testOfficialDetail() throws Exception {
        store.alimtalkOfficial.detail("OFFICIAL_1");
        assertEquals("/v1/alimtalk/official/OFFICIAL_1", lastPath);
        assertNull(lastQuery);

        store.alimtalkOfficial.detail("OFFICIAL_1", "KSP_1");
        assertEquals("ksp_id=KSP_1", lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // 발송내역 — /alimtalk/messages
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("message.list - GET alimtalk/messages, 필터 전부 query 전송")
    void testMessageList() throws Exception {
        AlimtalkMessageListParams params = new AlimtalkMessageListParams();
        params.templateCode = "TPL_1";
        params.status = "success";
        params.refId = "order-0001";
        params.to = "01012345678";
        params.sAt = "2026-08-01";
        params.eAt = "2026-08-27";
        params.page = 2;
        params.limit = 100;
        store.alimtalkMessage.list(params);

        assertAll(
                () -> assertEquals("GET", lastMethod),
                () -> assertEquals("/v1/alimtalk/messages", lastPath),
                () -> assertTrue(lastQuery.contains("template_code=TPL_1"), lastQuery),
                () -> assertTrue(lastQuery.contains("status=success"), lastQuery),
                () -> assertTrue(lastQuery.contains("ref_id=order-0001"), lastQuery),
                () -> assertTrue(lastQuery.contains("to=01012345678"), lastQuery),
                () -> assertTrue(lastQuery.contains("s_at=2026-08-01"), lastQuery),
                () -> assertTrue(lastQuery.contains("e_at=2026-08-27"), lastQuery),
                () -> assertTrue(lastQuery.contains("page=2"), lastQuery),
                () -> assertTrue(lastQuery.contains("limit=100"), lastQuery)
        );
    }

    @Test
    @DisplayName("message.stats - GET alimtalk/messages/stats, s_at/e_at 만 전송")
    void testMessageStats() throws Exception {
        store.alimtalkMessage.stats("2026-08-01", "2026-08-27");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/messages/stats", lastPath);
        assertTrue(lastQuery.contains("s_at=2026-08-01"), lastQuery);
        assertTrue(lastQuery.contains("e_at=2026-08-27"), lastQuery);
    }

    @Test
    @DisplayName("message.detail - GET alimtalk/messages/{receipt_id}")
    void testMessageDetail() throws Exception {
        store.alimtalkMessage.detail("RCP_1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/messages/RCP_1", lastPath);
        assertNull(lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // 수신거부 — /alimtalk/optouts
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("optout.list - GET alimtalk/optouts, phone/page query 전송")
    void testOptoutList() throws Exception {
        AlimtalkOptoutListParams params = new AlimtalkOptoutListParams();
        params.phone = "0101234";
        params.page = 2;
        store.alimtalkOptout.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/optouts", lastPath);
        assertTrue(lastQuery.contains("phone=0101234"), lastQuery);
        assertTrue(lastQuery.contains("page=2"), lastQuery);
    }

    @Test
    @DisplayName("optout.create - POST alimtalk/optouts, phone/reason 전송")
    void testOptoutCreate() throws Exception {
        store.alimtalkOptout.create("01012345678", "고객 요청");

        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/optouts", lastPath);
        assertTrue(lastBody.contains("\"phone\":\"01012345678\""), lastBody);
        assertTrue(lastBody.contains("\"reason\":\"고객 요청\""), lastBody);
    }

    @Test
    @DisplayName("optout.check - 단건은 phone, 다건은 phones 로 보낸다")
    void testOptoutCheck() throws Exception {
        store.alimtalkOptout.check("01012345678");
        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/optouts/check", lastPath);
        assertEquals("{\"phone\":\"01012345678\"}", lastBody);

        store.alimtalkOptout.check(Arrays.asList("01012345678", "01011112222"));
        assertEquals("{\"phones\":[\"01012345678\",\"01011112222\"]}", lastBody);
    }

    @Test
    @DisplayName("optout.release - DELETE alimtalk/optouts/{phone}")
    void testOptoutRelease() throws Exception {
        store.alimtalkOptout.release("01012345678");

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/alimtalk/optouts/01012345678", lastPath);
    }

    // ══════════════════════════════════════════════════════════
    // 알림톡 웹훅 — /alimtalk/webhook (주문·구독 웹훅과 별개)
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("alimtalkWebhook - 주문 웹훅(webhook/test)과 다른 경로를 쓴다")
    void testAlimtalkWebhookIsSeparateFromOrderWebhook() throws Exception {
        store.alimtalkWebhook.test();
        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/webhook/test", lastPath);
        assertEquals("{}", lastBody);

        store.webhook.sendTest();
        assertEquals("/v1/webhook/test", lastPath);
    }

    @Test
    @DisplayName("alimtalkWebhook.detail / rotateSecret - 경로와 method")
    void testAlimtalkWebhookDetailAndRotate() throws Exception {
        store.alimtalkWebhook.detail();
        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/webhook", lastPath);

        store.alimtalkWebhook.rotateSecret();
        assertEquals("POST", lastMethod);
        assertEquals("/v1/alimtalk/webhook/secret", lastPath);
        assertEquals("{}", lastBody);
    }

    @Test
    @DisplayName("alimtalkWebhook.update - PUT alimtalk/webhook, url/events/enabled 전송")
    void testAlimtalkWebhookUpdate() throws Exception {
        AlimtalkWebhookUpdateParams params = new AlimtalkWebhookUpdateParams();
        params.url = "https://example.com/hooks/alimtalk";
        params.events = Arrays.asList(301, 302, 310);
        params.enabled = true;
        store.alimtalkWebhook.update(params);

        assertAll(
                () -> assertEquals("PUT", lastMethod),
                () -> assertEquals("/v1/alimtalk/webhook", lastPath),
                () -> assertTrue(lastBody.contains("\"url\":\"https://example.com/hooks/alimtalk\""), lastBody),
                () -> assertTrue(lastBody.contains("\"events\":[301,302,310]"), lastBody),
                () -> assertTrue(lastBody.contains("\"enabled\":true"), lastBody)
        );
    }

    @Test
    @DisplayName("alimtalkWebhook.update - enabled=false 는 그대로 전송한다")
    void testAlimtalkWebhookUpdateDisabled() throws Exception {
        AlimtalkWebhookUpdateParams params = new AlimtalkWebhookUpdateParams();
        params.enabled = false;
        store.alimtalkWebhook.update(params);

        assertEquals("{\"enabled\":false}", lastBody);
    }

    @Test
    @DisplayName("alimtalkWebhook.deliveries - GET alimtalk/webhook/deliveries, page/limit query 전송")
    void testAlimtalkWebhookDeliveries() throws Exception {
        AlimtalkWebhookDeliveriesParams params = new AlimtalkWebhookDeliveriesParams();
        params.page = 3;
        params.limit = 50;
        store.alimtalkWebhook.deliveries(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/alimtalk/webhook/deliveries", lastPath);
        assertTrue(lastQuery.contains("page=3"), lastQuery);
        assertTrue(lastQuery.contains("limit=50"), lastQuery);
    }

    // ══════════════════════════════════════════════════════════
    // 필수값 검증 — 네트워크 요청 전에 거절한다
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("필수값이 비면 요청을 보내기 전에 거절한다")
    void testRequiredValidation() {
        assertThrows(Exception.class, () -> store.alimtalkSend.send(new AlimtalkSendParams()));
        assertThrows(Exception.class, () -> store.alimtalkSend.sendBulk(new AlimtalkSendBulkParams()));
        assertThrows(Exception.class, () -> store.alimtalkTemplate.create(new AlimtalkTemplateCreateParams()));
        assertThrows(Exception.class, () -> store.alimtalkOptout.create(null));
        assertThrows(Exception.class, () -> store.alimtalkSender.detail(""));
    }

    @Test
    @DisplayName("HashMap 변수도 그대로 직렬화된다 (send 오버로드)")
    void testSendOverloadWithHashMap() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user_name", "홍길동");
        store.alimtalkSend.send("TPL_1", "01012345678", variables);

        assertEquals("/v1/alimtalk/send", lastPath);
        assertTrue(lastBody.contains("\"user_name\":\"홍길동\""), lastBody);
    }
}
