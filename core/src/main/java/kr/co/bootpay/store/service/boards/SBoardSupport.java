package kr.co.bootpay.store.service.boards;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.context.RequestContext;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Map;

/**
 * 커머스 게시판 API(FAQ · 공지사항 · 1:1 문의 · 상품문의 · 상품평)가 공유하는 요청 조립 헬퍼.
 *
 * <p>★ 다섯 도메인 모두 같은 규칙을 쓴다 ★ {@code Bootpay-Role} 은 운영자 모드면 supervisor, 아니면 user 이고,
 * {@code Idempotency-Key} 는 미지정시 호출마다 생성한다. 회원 식별용 {@code Bootpay-User-JWT} 는 값이 있을 때만 붙는다.</p>
 *
 * <p>★ null 만 걷어낸다 ★ ruby SDK 의 {@code .compact} 와 같은 규칙이다 — {@code false} · 빈 문자열 · 빈 배열은
 * 그대로 전송된다 (예: {@code images: []} 는 "이미지 전부 삭제", {@code title: ""} 은 "제목 삭제" 를 뜻한다).</p>
 */
final class SBoardSupport {

    private SBoardSupport() {
    }

    /**
     * 게시판 API 요청 컨텍스트.
     *
     * @param supervisor     true 면 Bootpay-Role 을 supervisor 로, 아니면 user 로 보낸다
     * @param userJwt        회원 JWT (값이 있을 때만 Bootpay-User-JWT 헤더로 전송)
     * @param idempotencyKey 미지정시 자동 생성
     */
    static RequestContext context(Boolean supervisor, String userJwt, String idempotencyKey) {
        return RequestContext.builder()
                .role(isSupervisor(supervisor) ? "supervisor" : "user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .userJwt(userJwt)
                .build();
    }

    /** supervisor 전용 엔드포인트(등록·수정·삭제·답변)의 요청 컨텍스트. */
    static RequestContext supervisorContext(String idempotencyKey) {
        return context(Boolean.TRUE, null, idempotencyKey);
    }

    /** 회원 전용 엔드포인트(작성·수정)의 요청 컨텍스트. */
    static RequestContext userContext(String userJwt, String idempotencyKey) {
        return context(Boolean.FALSE, userJwt, idempotencyKey);
    }

    static boolean isSupervisor(Boolean supervisor) {
        return supervisor != null && supervisor;
    }

    static Gson gson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    /** 쿼리 파라미터를 값이 있을 때만 담는다. */
    static void put(List<NameValuePair> pairs, String key, Object value) {
        if (value == null) return;
        pairs.add(new BasicNameValuePair(key, String.valueOf(value)));
    }

    /** 바디 맵에 값이 있을 때만 담는다 ({@code false} · 빈 문자열 · 빈 배열은 그대로 전송된다). */
    static void put(Map<String, Object> body, String key, Object value) {
        if (value == null) return;
        body.put(key, value);
    }

    /** 페이지 번호 — 미지정시 ruby SDK 와 같은 기본값 1. */
    static int pageOrDefault(Integer page) {
        return page == null ? 1 : page;
    }

    /** 페이지 크기 — 미지정시 ruby SDK 와 같은 기본값 20. */
    static int limitOrDefault(Integer limit) {
        return limit == null ? 20 : limit;
    }

    /**
     * 쿼리 파라미터를 URL 뒤에 붙인다.
     *
     * <p>{@code httpDelete} 는 쿼리 목록을 받는 오버로드가 없어 DELETE 요청에서만 쓴다.</p>
     */
    static String withQuery(String url, List<NameValuePair> pairs) {
        if (pairs == null || pairs.isEmpty()) return url;
        return url + "?" + URLEncodedUtils.format(pairs, "UTF-8");
    }

    /** 필수 값 검증 — 비어 있으면 요청을 보내지 않고 예외를 던진다. */
    static void requireValue(Object value, String name) throws Exception {
        if (value == null || "".equals(value)) throw new Exception(name + " 값이 비어있습니다.");
    }
}
