package kr.co.bootpay.store.service.alimtalk;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOptoutListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 알림톡 수신거부 — /v1/alimtalk/optouts 계열 (가맹점 CRM 수신거부 동기화용)
 *
 * <p>발송 판정과 <b>같은 기준</b>으로 다룬다 — 부트페이 전역(global) + 내 프로젝트.</p>
 * <p>⚠️ 전역 건은 <b>조회는 되지만 해제할 수 없다</b>({@code releasable: false}).
 * 이걸 노출하지 않으면 "화면엔 수신거부가 아닌데 발송은 3021 로 막히는" 상태가 된다.</p>
 */
public class SAlimtalkOptoutService {

    /**
     * 수신거부 목록 조회
     * GET /v1/alimtalk/optouts
     *
     * <p>{@code phone} 은 숫자만 남겨 <b>부분일치</b>로 찾는다 (정확 매칭이 아니다). 50건 단위로 페이징된다.</p>
     * <p>응답: {@code { list: [{ id, phone, scope, global, releasable, source, reason, opted_out_at, created_at }], count, page }}</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, AlimtalkOptoutListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            SAlimtalkSupport.put(pairs, "phone", params.phone);
            SAlimtalkSupport.put(pairs, "page", params.page);
        }

        HttpGet get = bootpay.httpGet("alimtalk/optouts", pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 수신거부 등록
     * POST /v1/alimtalk/optouts
     *
     * <p>내 프로젝트 스코프로 등록된다({@code source: api}). 같은 번호를 다시 등록해도 멱등이다.</p>
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, String phone, String reason) throws Exception {
        bootpay.requireCommerceCredentials();
        if (phone == null || phone.isEmpty()) throw new Exception("phone 값이 비어있습니다.");

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "phone", phone);
        SAlimtalkSupport.put(body, "reason", reason);

        HttpPost post = bootpay.httpPost("alimtalk/optouts",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 발송 전 수신거부 사전 확인
     * POST /v1/alimtalk/optouts/check
     *
     * <p>발송 판정과 <b>같은 축</b>으로 대조하므로, 벌크에서 {@code skipped} 로 낭비될 건을 미리 뺄 수 있다.
     * 단건({@code phone})·다건({@code phones}) 모두 받는다.</p>
     * <p>⚠️ 1회 최대 1,000건이고 넘으면 -48 이다 (중복은 서버가 제거).</p>
     * <p>응답: {@code { list: [{ phone, opted_out, global, releasable, opted_out_at }], count, opted_out_count }}</p>
     */
    static public BootpayStoreResponse check(BootpayStoreObject bootpay, List<String> phones, String phone) throws Exception {
        bootpay.requireCommerceCredentials();

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "phones", phones);
        SAlimtalkSupport.put(body, "phone", phone);

        HttpPost post = bootpay.httpPost("alimtalk/optouts/check",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 수신거부 해제
     * DELETE /v1/alimtalk/optouts/{phone}
     *
     * <p>내 프로젝트 스코프 건만 해제되며 멱등이다 (없어도 성공).</p>
     * <p>⚠️ 전역 차단은 해제되지 않고 {@code global_blocked: true} 로 알려 준다 —
     * "지웠는데 여전히 막히는" 상태를 응답으로 드러내기 위함이다.</p>
     * <p>응답: {@code { phone, released, global_blocked }}</p>
     */
    static public BootpayStoreResponse release(BootpayStoreObject bootpay, String phone) throws Exception {
        bootpay.requireCommerceCredentials();
        if (phone == null || phone.isEmpty()) throw new Exception("phone 값이 비어있습니다.");

        HttpDelete delete = bootpay.httpDelete("alimtalk/optouts/" + SAlimtalkSupport.pathSegment(phone),
                SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }
}
