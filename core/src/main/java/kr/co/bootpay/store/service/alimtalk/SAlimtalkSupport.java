package kr.co.bootpay.store.service.alimtalk;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.context.RequestContext;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Map;

/**
 * 알림톡 서비스가 공유하는 요청 조립 헬퍼.
 *
 * <p>★Idempotency-Key 를 싣지 않는다★ 알림톡 API 는 이 헤더를 읽지 않는다 (멱등은 발송의 {@code ref_id} 로만 성립).
 * invoice/product 처럼 무조건 붙이면 서버가 주지 않는 보장을 주는 것처럼 보인다.</p>
 *
 * <p>★{@code BOOTPAY-ROLE} 은 항상 user★ 알림톡 스코프 키가 전부 {@code user:alimtalk_*} 다.
 * 인스턴스가 supervisor/manager 로 설정돼 있어도 알림톡 요청은 user 로 나간다.</p>
 */
final class SAlimtalkSupport {

    private SAlimtalkSupport() {
    }

    /** 알림톡 전용 요청 컨텍스트 — role 은 user 로 고정하고 Idempotency-Key 는 붙이지 않는다. */
    static RequestContext context() {
        return RequestContext.builder()
                .role("user")
                .build();
    }

    static Gson gson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    /**
     * 쿼리 파라미터를 값이 있을 때만 담는다 (ruby SDK 의 {@code .compact} 와 같은 규칙 — null 만 걷어낸다).
     */
    static void put(List<NameValuePair> pairs, String key, Object value) {
        if (value == null) return;
        pairs.add(new BasicNameValuePair(key, String.valueOf(value)));
    }

    /** 바디 맵에 값이 있을 때만 담는다 ({@code false} · 빈 문자열은 그대로 전송된다). */
    static void put(Map<String, Object> body, String key, Object value) {
        if (value == null) return;
        body.put(key, value);
    }

    /** URL path segment 로 쓸 값을 인코딩한다 (수신거부 해제의 전화번호 등). */
    static String pathSegment(String value) throws Exception {
        if (value == null) return "";
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20");
    }
}
