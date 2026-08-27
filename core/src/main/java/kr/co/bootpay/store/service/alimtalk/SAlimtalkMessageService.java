package kr.co.bootpay.store.service.alimtalk;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkMessageListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

/**
 * 알림톡 발송내역·집계 — GET /v1/alimtalk/messages 계열
 *
 * <p><b>유료</b> 알림톡만 조회된다 (무료 커머스 알림톡은 포함되지 않는다).
 * 상태는 벤더 결과 동기화로 확정되므로 접수 직후에는 {@code requested} 로 보인다.</p>
 */
public class SAlimtalkMessageService {

    /**
     * 발송내역 목록 조회
     * GET /v1/alimtalk/messages
     *
     * <p>⚠️ 기간 기본값은 최근 30일이고 최대 조회 폭은 92일이다 — 초과분은 거부되지 않고 시작일이 당겨져 잘린다.
     * 실제 적용된 구간은 응답의 {@code period} 로 확인한다.</p>
     *
     * <p>응답: {@code { list: [...], count, page, per, period: { from, to } }}</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, AlimtalkMessageListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            SAlimtalkSupport.put(pairs, "template_code", params.templateCode);
            SAlimtalkSupport.put(pairs, "status", params.status);
            SAlimtalkSupport.put(pairs, "ref_id", params.refId);
            SAlimtalkSupport.put(pairs, "to", params.to);
            SAlimtalkSupport.put(pairs, "s_at", params.sAt);
            SAlimtalkSupport.put(pairs, "e_at", params.eAt);
            SAlimtalkSupport.put(pairs, "page", params.page);
            SAlimtalkSupport.put(pairs, "limit", params.limit);
        }

        HttpGet get = bootpay.httpGet("alimtalk/messages", pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 기간 집계 조회
     * GET /v1/alimtalk/messages/stats
     *
     * <p>일자별 집계 원장에서 읽으므로 응답이 빠르다.</p>
     * <p>⚠️ {@code billing.unit_price_source} 가 {@code default} 면 <b>잠정 단가</b>다 (확정 청구액이 아니다).</p>
     * <p>⚠️ {@code billable_count} 는 성공 − 폴백이다 — 폴백분은 LMS 단가로 따로 계산된다.</p>
     */
    static public BootpayStoreResponse stats(BootpayStoreObject bootpay, String sAt, String eAt) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        SAlimtalkSupport.put(pairs, "s_at", sAt);
        SAlimtalkSupport.put(pairs, "e_at", eAt);

        HttpGet get = bootpay.httpGet("alimtalk/messages/stats", pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 단건 발송 결과 조회
     * GET /v1/alimtalk/messages/{receipt_id}
     *
     * <p>실패 사유는 {@code error_code} · {@code error_message} 에 담긴다.
     * {@code fallback_type} 은 폴백이 꺼진 건이면 null, 켜진 건이면 LMS 다.
     * 다른 프로젝트의 건이거나 없으면 404(3025).</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String receiptId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");

        HttpGet get = bootpay.httpGet("alimtalk/messages/" + receiptId, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
