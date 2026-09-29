package kr.co.bootpay.commerce;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.faq.FaqCreateParams;
import kr.co.bootpay.store.model.request.faq.FaqListParams;
import kr.co.bootpay.store.model.request.faq.FaqUpdateParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryCreateParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDeleteParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryDetailParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryListParams;
import kr.co.bootpay.store.model.request.inquiry.InquiryUpdateParams;
import kr.co.bootpay.store.model.request.notice.NoticeCreateParams;
import kr.co.bootpay.store.model.request.notice.NoticeListParams;
import kr.co.bootpay.store.model.request.notice.NoticeUpdateParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaCreateParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaDeleteParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaListParams;
import kr.co.bootpay.store.model.request.productQna.ProductQnaUpdateParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewCreateParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewDeleteParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewListParams;
import kr.co.bootpay.store.model.request.productReview.ProductReviewUpdateParams;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 커머스 게시판 API(FAQ · 공지사항 · 1:1 문의 · 상품문의 · 상품평) — wire-format 검증 (네트워크 불필요).
 *
 * <p>ruby SDK {@code lib/bootpay_store/concern/{faq,notice,inquiry,product_qna,product_review}.rb} 와
 * 같은 method / path / query / header / body 를 만드는지 로컬 루프백 HTTP 서버로 캡처해 검증한다.</p>
 */
@DisplayName("Commerce API - 게시판 Wire Format (ruby SDK parity)")
class CommerceBoardWireFormatTest {

    private static HttpServer server;
    private static BootpayStore store;

    private static volatile String lastMethod;
    private static volatile String lastPath;
    private static volatile String lastQuery;
    private static volatile String lastBody;
    private static volatile String lastRole;
    private static volatile String lastIdempotencyKey;
    private static volatile String lastUserJwt;

    @BeforeAll
    static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", CommerceBoardWireFormatTest::capture);
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
        lastMethod = lastPath = lastQuery = lastBody = lastRole = lastIdempotencyKey = lastUserJwt = null;
    }

    private static void capture(HttpExchange exchange) throws java.io.IOException {
        lastMethod = exchange.getRequestMethod();
        lastPath = exchange.getRequestURI().getPath();
        lastQuery = exchange.getRequestURI().getQuery();
        lastRole = exchange.getRequestHeaders().getFirst("BOOTPAY-ROLE");
        lastIdempotencyKey = exchange.getRequestHeaders().getFirst("Idempotency-Key");
        lastUserJwt = exchange.getRequestHeaders().getFirst("Bootpay-User-JWT");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream in = exchange.getRequestBody()) {
            byte[] chunk = new byte[4096];
            int read;
            while ((read = in.read(chunk)) != -1) buffer.write(chunk, 0, read);
        }
        lastBody = new String(buffer.toByteArray(), StandardCharsets.UTF_8);

        byte[] response = "{\"ok\":true}".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(response);
        }
    }

    // ══════════════════════════════════════════════════════════
    // FAQ — /v1/faqs
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("faq.list - GET faqs, page 1 / limit 20 기본값, 고객 모드")
    void testFaqListDefaults() throws Exception {
        store.faq.list();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/faqs", lastPath);
        assertEquals("page=1&limit=20", lastQuery);
        assertEquals("user", lastRole);
        assertNotNull(lastIdempotencyKey, "Idempotency-Key 자동 생성");
        assertFalse(lastIdempotencyKey.isEmpty());
    }

    @Test
    @DisplayName("faq.list - supervisor 면 BOOTPAY-ROLE: supervisor, keyword·view 는 지정된 것만 전송")
    void testFaqListSupervisor() throws Exception {
        FaqListParams params = new FaqListParams();
        params.page = 2;
        params.limit = 5;
        params.keyword = "배송";
        params.view = "all";
        params.supervisor = true;
        params.idempotencyKey = "faq-list-key";
        store.faq.list(params);

        assertEquals("page=2&limit=5&keyword=배송&view=all", lastQuery);
        assertEquals("supervisor", lastRole);
        assertEquals("faq-list-key", lastIdempotencyKey);
    }

    @Test
    @DisplayName("faq.detail - GET faqs/{id}, supervisor 여부로 role 이 갈린다")
    void testFaqDetail() throws Exception {
        store.faq.detail("FAQ_1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/faqs/FAQ_1", lastPath);
        assertNull(lastQuery);
        assertEquals("user", lastRole);

        store.faq.detail("FAQ_1", true);
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("faq.create - POST faqs, supervisor 전용, null 필드는 본문에서 빠진다")
    void testFaqCreate() throws Exception {
        FaqCreateParams params = new FaqCreateParams();
        params.title = "배송 문의";
        params.content = "2~3일 걸립니다";
        store.faq.create(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/faqs", lastPath);
        assertEquals("supervisor", lastRole);
        assertEquals("{\"title\":\"배송 문의\",\"content\":\"2~3일 걸립니다\"}", lastBody);
        assertNotNull(lastIdempotencyKey);
    }

    @Test
    @DisplayName("faq.create - title·content 가 비면 요청을 보내지 않는다")
    void testFaqCreateRequiresTitleAndContent() {
        FaqCreateParams params = new FaqCreateParams();
        params.content = "내용만 있다";

        assertThrows(Exception.class, () -> store.faq.create(params));
        assertNull(lastMethod, "요청이 나가면 안 됩니다");
    }

    @Test
    @DisplayName("faq.update - PUT faqs/{id}, 빈 images 는 '모두 삭제' 의미라 그대로 전송한다")
    void testFaqUpdateEmptyImages() throws Exception {
        FaqUpdateParams params = new FaqUpdateParams();
        params.faqId = "FAQ_1";
        params.isDisplay = false;
        params.images = new ArrayList<>();
        store.faq.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/faqs/FAQ_1", lastPath);
        assertEquals("supervisor", lastRole);
        assertEquals("{\"images\":[],\"is_display\":false}", lastBody);
        assertFalse(lastBody.contains("title"), "미지정 필드는 전송하지 않는다: " + lastBody);
    }

    @Test
    @DisplayName("faq.delete - DELETE faqs/{id}, supervisor 전용")
    void testFaqDelete() throws Exception {
        store.faq.delete("FAQ_1", "faq-del-key");

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/faqs/FAQ_1", lastPath);
        assertNull(lastQuery);
        assertEquals("supervisor", lastRole);
        assertEquals("faq-del-key", lastIdempotencyKey);
    }

    // ══════════════════════════════════════════════════════════
    // 공지사항 — /v1/notices
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("notice.list - GET notices, page 1 / limit 20 기본값")
    void testNoticeListDefaults() throws Exception {
        store.notice.list();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/notices", lastPath);
        assertEquals("page=1&limit=20", lastQuery);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("notice.detail - GET notices/{id}, supervisor 면 비공개 공지까지")
    void testNoticeDetail() throws Exception {
        store.notice.detail("NOTICE_1", true);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/notices/NOTICE_1", lastPath);
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("notice.create - POST notices, is_notice / is_display 는 snake_case 로 직렬화")
    void testNoticeCreate() throws Exception {
        NoticeCreateParams params = new NoticeCreateParams();
        params.title = "휴무 안내";
        params.content = "설 연휴 배송 안내";
        params.isNotice = true;
        params.isDisplay = true;
        params.images = Arrays.<Object>asList("https://img.example.com/1.png");
        store.notice.create(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/notices", lastPath);
        assertEquals("supervisor", lastRole);
        assertTrue(lastBody.contains("\"is_notice\":true"), lastBody);
        assertTrue(lastBody.contains("\"is_display\":true"), lastBody);
        assertTrue(lastBody.contains("\"images\":[\"https://img.example.com/1.png\"]"), lastBody);
    }

    @Test
    @DisplayName("notice.update - PUT notices/{id}, 보낸 필드만 전송")
    void testNoticeUpdate() throws Exception {
        NoticeUpdateParams params = new NoticeUpdateParams();
        params.noticeId = "NOTICE_1";
        params.title = "수정된 제목";
        store.notice.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/notices/NOTICE_1", lastPath);
        assertEquals("supervisor", lastRole);
        assertEquals("{\"title\":\"수정된 제목\"}", lastBody);
    }

    @Test
    @DisplayName("notice.delete - DELETE notices/{id}, supervisor 전용")
    void testNoticeDelete() throws Exception {
        store.notice.delete("NOTICE_1");

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/notices/NOTICE_1", lastPath);
        assertEquals("supervisor", lastRole);
    }

    // ══════════════════════════════════════════════════════════
    // 1:1 문의 — /v1/inquiries
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("inquiry.list - GET inquiries, answered 는 문자열로, userJwt 는 헤더로 전송")
    void testInquiryList() throws Exception {
        InquiryListParams params = new InquiryListParams();
        params.userId = "USER_1";
        params.answered = false;
        params.userJwt = "jwt-token";
        store.inquiry.list(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/inquiries", lastPath);
        assertEquals("user_id=USER_1&answered=false&page=1&limit=20", lastQuery);
        assertEquals("user", lastRole);
        assertEquals("jwt-token", lastUserJwt, "회원 JWT 는 Bootpay-User-JWT 헤더로 전송");
    }

    @Test
    @DisplayName("inquiry.list - supervisor 면 몰 전체 문의, user_id 는 작성 회원 필터")
    void testInquiryListSupervisor() throws Exception {
        InquiryListParams params = new InquiryListParams();
        params.supervisor = true;
        params.answered = true;
        store.inquiry.list(params);

        assertEquals("supervisor", lastRole);
        assertEquals("answered=true&page=1&limit=20", lastQuery);
        assertNull(lastUserJwt);
    }

    @Test
    @DisplayName("inquiry.detail - GET inquiries/{id}, user_id·login_id 는 query 로")
    void testInquiryDetail() throws Exception {
        InquiryDetailParams params = new InquiryDetailParams();
        params.inquiryId = "INQ_1";
        params.loginId = "buyer01";
        store.inquiry.detail(params);

        assertEquals("GET", lastMethod);
        assertEquals("/v1/inquiries/INQ_1", lastPath);
        assertEquals("login_id=buyer01", lastQuery);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("inquiry.create - POST inquiries, 항상 user role")
    void testInquiryCreate() throws Exception {
        InquiryCreateParams params = new InquiryCreateParams();
        params.content = "언제 배송되나요?";
        params.userId = "USER_1";
        params.productId = "PRODUCT_1";
        params.option = "블랙 / L";
        params.orderId = "ORDER_1";
        params.userJwt = "jwt-token";
        store.inquiry.create(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/inquiries", lastPath);
        assertEquals("user", lastRole);
        assertEquals("jwt-token", lastUserJwt);
        assertEquals("{\"user_id\":\"USER_1\",\"content\":\"언제 배송되나요?\",\"product_id\":\"PRODUCT_1\","
                + "\"option\":\"블랙 / L\",\"order_id\":\"ORDER_1\"}", lastBody);
    }

    @Test
    @DisplayName("inquiry.update - PUT inquiries/{id}, 빈 제목은 '제목 삭제' 라 그대로 전송한다")
    void testInquiryUpdateBlankTitle() throws Exception {
        InquiryUpdateParams params = new InquiryUpdateParams();
        params.inquiryId = "INQ_1";
        params.title = "";
        params.content = "내용 수정";
        store.inquiry.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/inquiries/INQ_1", lastPath);
        assertEquals("user", lastRole);
        assertEquals("{\"title\":\"\",\"content\":\"내용 수정\"}", lastBody);
    }

    @Test
    @DisplayName("inquiry.delete - DELETE inquiries/{id}, user_id 는 query 로, supervisor 가능")
    void testInquiryDelete() throws Exception {
        InquiryDeleteParams params = new InquiryDeleteParams();
        params.inquiryId = "INQ_1";
        params.userId = "USER_1";
        params.supervisor = true;
        store.inquiry.delete(params);

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/inquiries/INQ_1", lastPath);
        assertEquals("user_id=USER_1", lastQuery);
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("inquiry.answer - PUT inquiries/{id}/answer, supervisor 전용, body 는 content 만")
    void testInquiryAnswer() throws Exception {
        store.inquiry.answer("INQ_1", "2~3일 내 발송됩니다");

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/inquiries/INQ_1/answer", lastPath);
        assertEquals("supervisor", lastRole);
        assertEquals("{\"content\":\"2~3일 내 발송됩니다\"}", lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // 상품문의 — /v1/product-qnas
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("productQna.list - GET product-qnas, 경로는 하이픈(product-qnas)")
    void testProductQnaList() throws Exception {
        store.productQna.list("PRODUCT_1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/product-qnas", lastPath);
        assertEquals("product_id=PRODUCT_1&page=1&limit=20", lastQuery);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("productQna.list - supervisor + view=all 이면 몰 전체(숨김 포함)")
    void testProductQnaListSupervisorAll() throws Exception {
        ProductQnaListParams params = new ProductQnaListParams();
        params.view = "all";
        params.supervisor = true;
        params.limit = 50;
        store.productQna.list(params);

        assertEquals("view=all&page=1&limit=50", lastQuery);
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("productQna.detail - GET product-qnas/{id}")
    void testProductQnaDetail() throws Exception {
        store.productQna.detail("QNA_1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/product-qnas/QNA_1", lastPath);
        assertNull(lastQuery);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("productQna.create - 비회원은 guest_name + guest_password 로 작성한다")
    void testProductQnaCreateGuest() throws Exception {
        ProductQnaCreateParams params = new ProductQnaCreateParams();
        params.productId = "PRODUCT_1";
        params.title = "재입고 문의";
        params.content = "품절된 색상 재입고 예정이 있나요?";
        params.isSecret = true;
        params.guestName = "비회원";
        params.guestPassword = "1234";
        store.productQna.create(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/product-qnas", lastPath);
        assertEquals("user", lastRole);
        assertEquals("{\"product_id\":\"PRODUCT_1\",\"title\":\"재입고 문의\","
                + "\"content\":\"품절된 색상 재입고 예정이 있나요?\",\"is_secret\":true,"
                + "\"guest_name\":\"비회원\",\"guest_password\":\"1234\"}", lastBody);
    }

    @Test
    @DisplayName("productQna.update - PUT product-qnas/{id}")
    void testProductQnaUpdate() throws Exception {
        ProductQnaUpdateParams params = new ProductQnaUpdateParams();
        params.productQnaId = "QNA_1";
        params.content = "수정된 문의";
        params.isSecret = false;
        params.guestPassword = "1234";
        store.productQna.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/product-qnas/QNA_1", lastPath);
        assertEquals("user", lastRole);
        assertEquals("{\"content\":\"수정된 문의\",\"is_secret\":false,\"guest_password\":\"1234\"}", lastBody);
    }

    @Test
    @DisplayName("productQna.delete - guest_password 는 query 가 아니라 body 로 전송한다")
    void testProductQnaDeleteGuestPasswordInBody() throws Exception {
        ProductQnaDeleteParams params = new ProductQnaDeleteParams();
        params.productQnaId = "QNA_1";
        params.guestPassword = "1234";
        store.productQna.delete(params);

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/product-qnas/QNA_1", lastPath);
        assertNull(lastQuery, "guest_password 는 query 로 전송되면 안 됩니다");
        assertEquals("{\"guest_password\":\"1234\"}", lastBody);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("productQna.delete - 값이 없으면 빈 본문 {} 를 보낸다")
    void testProductQnaDeleteEmptyBody() throws Exception {
        store.productQna.delete("QNA_1");

        assertEquals("DELETE", lastMethod);
        assertEquals("{}", lastBody);
    }

    @Test
    @DisplayName("productQna.answer - PUT product-qnas/{id}/answer, supervisor 전용")
    void testProductQnaAnswer() throws Exception {
        store.productQna.answer("QNA_1", "다음 주 입고 예정입니다");

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/product-qnas/QNA_1/answer", lastPath);
        assertEquals("supervisor", lastRole);
        assertEquals("{\"content\":\"다음 주 입고 예정입니다\"}", lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // 상품평 — /v1/reviews
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("productReview.list - GET reviews (products/{id}/reviews 아님), page·limit 이 앞에 온다")
    void testProductReviewListDefaults() throws Exception {
        store.productReview.list();

        assertEquals("GET", lastMethod);
        assertEquals("/v1/reviews", lastPath);
        assertEquals("page=1&limit=20", lastQuery);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("productReview.list - supervisor 면 몰 전체 상품평")
    void testProductReviewListSupervisor() throws Exception {
        ProductReviewListParams params = new ProductReviewListParams();
        params.productId = "PRODUCT_1";
        params.view = "all";
        params.supervisor = true;
        store.productReview.list(params);

        assertEquals("page=1&limit=20&product_id=PRODUCT_1&view=all", lastQuery);
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("productReview.detail - GET reviews/{id}")
    void testProductReviewDetail() throws Exception {
        store.productReview.detail("REVIEW_1");

        assertEquals("GET", lastMethod);
        assertEquals("/v1/reviews/REVIEW_1", lastPath);
        assertEquals("user", lastRole);
    }

    @Test
    @DisplayName("productReview.create - POST reviews, 회원 전용")
    void testProductReviewCreate() throws Exception {
        ProductReviewCreateParams params = new ProductReviewCreateParams();
        params.orderId = "ORDER_1";
        params.productId = "PRODUCT_1";
        params.rating = 5;
        params.content = "아주 만족합니다";
        params.images = Arrays.<Object>asList("https://img.example.com/review.png");
        params.loginId = "buyer01";
        store.productReview.create(params);

        assertEquals("POST", lastMethod);
        assertEquals("/v1/reviews", lastPath);
        assertEquals("user", lastRole);
        assertEquals("{\"login_id\":\"buyer01\",\"order_id\":\"ORDER_1\",\"product_id\":\"PRODUCT_1\","
                + "\"rating\":5,\"content\":\"아주 만족합니다\","
                + "\"images\":[\"https://img.example.com/review.png\"]}", lastBody);
    }

    @Test
    @DisplayName("productReview.create - orderId·productId·rating·content 가 비면 요청을 보내지 않는다")
    void testProductReviewCreateRequiresFields() {
        ProductReviewCreateParams params = new ProductReviewCreateParams();
        params.orderId = "ORDER_1";
        params.productId = "PRODUCT_1";
        params.content = "별점이 없다";

        assertThrows(Exception.class, () -> store.productReview.create(params));
        assertNull(lastMethod, "요청이 나가면 안 됩니다");
    }

    @Test
    @DisplayName("productReview.update - PUT reviews/{id}, 빈 images 는 '모두 삭제' 라 그대로 전송한다")
    void testProductReviewUpdateEmptyImages() throws Exception {
        ProductReviewUpdateParams params = new ProductReviewUpdateParams();
        params.productReviewId = "REVIEW_1";
        params.rating = 3;
        params.images = new ArrayList<Object>();
        store.productReview.update(params);

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/reviews/REVIEW_1", lastPath);
        assertEquals("user", lastRole);
        assertEquals("{\"rating\":3,\"images\":[]}", lastBody);
    }

    @Test
    @DisplayName("productReview.delete - DELETE reviews/{id}, reason 은 query 로 전송")
    void testProductReviewDelete() throws Exception {
        ProductReviewDeleteParams params = new ProductReviewDeleteParams();
        params.productReviewId = "REVIEW_1";
        params.reason = "광고성 리뷰";
        params.supervisor = true;
        store.productReview.delete(params);

        assertEquals("DELETE", lastMethod);
        assertEquals("/v1/reviews/REVIEW_1", lastPath);
        assertEquals("reason=광고성+리뷰", lastQuery);
        assertEquals("supervisor", lastRole);
    }

    @Test
    @DisplayName("productReview.reply - PUT reviews/{id}/reply, supervisor 전용")
    void testProductReviewReply() throws Exception {
        store.productReview.reply("REVIEW_1", "소중한 후기 감사합니다");

        assertEquals("PUT", lastMethod);
        assertEquals("/v1/reviews/REVIEW_1/reply", lastPath);
        assertEquals("supervisor", lastRole);
        assertEquals("{\"content\":\"소중한 후기 감사합니다\"}", lastBody);
    }

    // ══════════════════════════════════════════════════════════
    // 공통 계약
    // ══════════════════════════════════════════════════════════

    @Test
    @DisplayName("게시판 API 는 모든 요청에 Idempotency-Key 를 붙이고, 미지정이면 호출마다 새로 만든다")
    void testIdempotencyKeyGeneratedPerCall() throws Exception {
        List<String> keys = new ArrayList<>();
        store.faq.list();
        keys.add(lastIdempotencyKey);
        store.faq.list();
        keys.add(lastIdempotencyKey);

        assertNotNull(keys.get(0));
        assertNotNull(keys.get(1));
        assertNotEquals(keys.get(0), keys.get(1), "호출마다 새 키를 만든다");
    }

    @Test
    @DisplayName("게시판 API 의 role 은 인스턴스 role 이 아니라 supervisor 플래그로 정해진다")
    void testRoleIgnoresInstanceRole() throws Exception {
        BootpayStore managerStore = new BootpayStore(new TokenPayload("test_ck", "test_sk"), "PRODUCTION");
        managerStore.baseUrl = store.baseUrl;
        managerStore.setRole("manager");

        managerStore.faq.list();
        assertEquals("user", lastRole, "supervisor 플래그가 없으면 user 로 나간다");

        FaqListParams params = new FaqListParams();
        params.supervisor = true;
        managerStore.faq.list(params);
        assertEquals("supervisor", lastRole);
    }
}
