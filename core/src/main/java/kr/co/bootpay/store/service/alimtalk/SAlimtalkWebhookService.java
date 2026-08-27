package kr.co.bootpay.store.service.alimtalk;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookDeliveriesParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkWebhookUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 알림톡 발송결과·검수결과 웹훅 설정 — /v1/alimtalk/webhook 계열
 *
 * <p>⚠️ <b>주문·구독 통합 웹훅과 완전히 별개다.</b> 알림톡 이벤트를 기존 주문 웹훅 URL 로 태우면 그 수신 서버가
 * 모르는 payload 를 받아 기존 연동이 깨진다. 그래서 수신 URL 을 따로 둔다
 * ({@code SWebhookService.sendTest} 는 주문 웹훅용이다 — 이 클래스의 {@link #test} 와 혼동하지 말 것).</p>
 *
 * <h3>서명 검증</h3>
 * <p>요청에 다음 헤더가 붙는다.<br>
 * {@code X-Bootpay-Signature: sha256=HMAC_SHA256(secret, "{X-Bootpay-Timestamp}.{raw_body}")}<br>
 * 타임스탬프가 5분 이상 지난 요청은 거부한다 (replay 방지).</p>
 */
public class SAlimtalkWebhookService {

    /**
     * 웹훅 설정 조회
     * GET /v1/alimtalk/webhook
     *
     * <p>시크릿은 앞 12자만 노출된다. 미설정이면 {@code { configured: false }} 로 온다.</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("alimtalk/webhook", SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 웹훅 설정 저장
     * PUT /v1/alimtalk/webhook
     *
     * <p>{@code url} 은 <b>https 만</b> 허용한다 (아니면 3028). 최초 저장 시 서명 시크릿이 자동 발급된다.</p>
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, AlimtalkWebhookUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        if (params != null) {
            SAlimtalkSupport.put(body, "url", params.url);
            SAlimtalkSupport.put(body, "events", params.events);
            SAlimtalkSupport.put(body, "enabled", params.enabled);
        }

        HttpPut put = bootpay.httpPut("alimtalk/webhook",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 테스트 이벤트 1건 발송
     * POST /v1/alimtalk/webhook/test
     *
     * <p>⚠️ <b>설정된 URL 로 실제 HTTP 요청이 나간다.</b> 구독 여부와 무관하게 보낸다.
     * 웹훅이 설정돼 있지 않으면 3029. 응답: {@code { delivery_id, url, queued }}</p>
     */
    static public BootpayStoreResponse test(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpPost post = bootpay.httpPost("alimtalk/webhook/test",
                new StringEntity("{}", "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 서명 시크릿 재발급
     * POST /v1/alimtalk/webhook/secret
     *
     * <p>⚠️ <b>이 응답에서만 secret 원문을 돌려준다</b> (이후 조회는 마스킹된다).</p>
     * <p>⚠️ 이미 큐에 있는 전송 건은 발송 당시 시크릿으로 서명된다.</p>
     */
    static public BootpayStoreResponse rotateSecret(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpPost post = bootpay.httpPost("alimtalk/webhook/secret",
                new StringEntity("{}", "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 웹훅 전송 이력 조회
     * GET /v1/alimtalk/webhook/deliveries
     *
     * <p>성공·실패를 모두 남긴다. 응답: {@code { list: [{ delivery_id, event, event_code, url, status,
     * retry_count, max_retry, tags, created_at }], count, page, per }}</p>
     */
    static public BootpayStoreResponse deliveries(BootpayStoreObject bootpay, AlimtalkWebhookDeliveriesParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            SAlimtalkSupport.put(pairs, "page", params.page);
            SAlimtalkSupport.put(pairs, "limit", params.limit);
        }

        HttpGet get = bootpay.httpGet("alimtalk/webhook/deliveries", pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
