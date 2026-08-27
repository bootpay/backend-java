package kr.co.bootpay.store.service.alimtalk;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendBulkParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSendParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 알림톡 발송 — POST /v1/alimtalk/send · /send/bulk · DELETE /send/{receipt_id}
 *
 * <p>⚠️ <b>실제로 카카오톡이 발송되고 과금된다. 샌드박스가 없다.</b></p>
 *
 * <p>처리 순서: 멱등 확인 → 템플릿·채널 해석 → 발송권한 → 지갑 자격 → 발송제어 → 폴백 확정(발신번호 확보)
 * → 수신거부 대조 → 변수 치환·규격검증 → 접수(READY) → 워커 전송</p>
 *
 * <ul>
 *   <li><b>멱등</b>: 같은 (프로젝트, {@code ref_id}) 로 재요청하면 기존 receipt 를 그대로 돌려준다. 실패한 건만 재발송된다.</li>
 *   <li><b>필수 변수</b>: 템플릿 응답의 {@code required_variables} 를 모두 채워야 한다. 하나라도 비면 3017 로 거부된다.
 *       ⚠️ 다만 실제로 치환되어 나가는 건 본문·강조 타이틀·버튼 링크뿐이다 — 보조문구와 아이템리스트형 요소는
 *       발송 페이로드에 자리가 없어 카카오가 등록된 템플릿 문구 그대로 렌더한다.</li>
 *   <li><b>채널</b>: {@code sender_key}(공개키)로 지정한다. 생략하면 프로젝트 연동 채널로 해석하며,
 *       연동 채널이 둘 이상일 때만 필수다 ({@code ksp_id} 는 내부 문서 id 라 발송 API 에 쓰지 않는다).</li>
 * </ul>
 */
public class SAlimtalkSendService {

    /**
     * 단건 발송
     * POST /v1/alimtalk/send
     *
     * <p>응답: {@code { receipt_id, ref_id, to, status }} — 접수 직후 {@code status} 는 {@code requested}</p>
     */
    static public BootpayStoreResponse send(BootpayStoreObject bootpay, AlimtalkSendParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        if (params.templateCode == null || params.templateCode.isEmpty()) throw new Exception("templateCode 값이 비어있습니다.");
        if (params.to == null || params.to.isEmpty()) throw new Exception("to 값이 비어있습니다.");

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "template_code", params.templateCode);
        SAlimtalkSupport.put(body, "to", params.to);
        SAlimtalkSupport.put(body, "variables", params.variables);
        SAlimtalkSupport.put(body, "ref_id", params.refId);
        // 미지정(null)만 걷어낸다 — false 는 "폴백을 명시적으로 끈다"는 뜻이라 그대로 전달한다
        SAlimtalkSupport.put(body, "fallback", params.fallback);
        SAlimtalkSupport.put(body, "reserved_at", params.reservedAt);
        SAlimtalkSupport.put(body, "sender_key", params.senderKey);
        SAlimtalkSupport.put(body, "user_id", params.userId);

        HttpPost post = bootpay.httpPost("alimtalk/send",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 벌크 발송 — 1요청 = N수신자
     * POST /v1/alimtalk/send/bulk
     *
     * <p>응답: {@code { count, requested, skipped, rejected, receipts: [...] }}</p>
     */
    static public BootpayStoreResponse sendBulk(BootpayStoreObject bootpay, AlimtalkSendBulkParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        if (params.templateCode == null || params.templateCode.isEmpty()) throw new Exception("templateCode 값이 비어있습니다.");
        if (params.recipients == null || params.recipients.isEmpty()) throw new Exception("recipients 값이 비어있습니다.");

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "template_code", params.templateCode);
        SAlimtalkSupport.put(body, "recipients", params.recipients);
        SAlimtalkSupport.put(body, "fallback", params.fallback);
        SAlimtalkSupport.put(body, "reserved_at", params.reservedAt);
        SAlimtalkSupport.put(body, "sender_key", params.senderKey);
        SAlimtalkSupport.put(body, "user_id", params.userId);

        HttpPost post = bootpay.httpPost("alimtalk/send/bulk",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 예약 발송 취소
     * DELETE /v1/alimtalk/send/{receipt_id}
     *
     * <p>접수(READY) 상태의 예약 건만 취소할 수 있다 — 이미 전송에 들어갔으면 3023 이다.</p>
     */
    static public BootpayStoreResponse cancel(BootpayStoreObject bootpay, String receiptId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");

        HttpDelete delete = bootpay.httpDelete("alimtalk/send/" + receiptId, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }
}
