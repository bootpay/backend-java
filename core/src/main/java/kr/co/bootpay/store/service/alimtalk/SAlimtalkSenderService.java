package kr.co.bootpay.store.service.alimtalk;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkSenderCreateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 알림톡 발신프로필(카카오채널) 생명주기 — GET /v1/alimtalk/categories · /senders 계열
 *
 * <p>카테고리 조회 → OTP 발송 → 발신프로필 등록 → 목록/상세 → 연동 해지 순으로 쓴다.
 * 등록이 끝나면 서버가 그룹키 등록까지 자동으로 하므로, 공식 템플릿은 별도 채택 없이 바로 발송된다.</p>
 *
 * <p>⚠️ 실제 부작용: {@code otp} 는 채널 관리자 휴대폰으로 <b>문자를 실제 발송</b>하고,
 * {@code create} 는 카카오에 발신프로필을 <b>실제 등록</b>한다. 샌드박스가 없다.</p>
 */
public class SAlimtalkSenderService {

    /**
     * 카카오 카테고리 목록 조회
     * GET /v1/alimtalk/categories
     *
     * <p>발신프로필 등록 시 필요한 {@code category_code} 후보다. 벤더 응답을 그대로 프록시한다.</p>
     */
    static public BootpayStoreResponse categories(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("alimtalk/categories", SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 채널 관리자폰으로 OTP 발송
     * POST /v1/alimtalk/senders/otp
     *
     * <p>⚠️ 실제로 문자가 나간다. 여기서 받은 인증번호를 {@link #create} 의 {@code otp} 로 넘긴다.</p>
     */
    static public BootpayStoreResponse otp(BootpayStoreObject bootpay, String yellowId, String phone) throws Exception {
        bootpay.requireCommerceCredentials();
        if (yellowId == null || yellowId.isEmpty()) throw new Exception("yellowId 값이 비어있습니다.");
        if (phone == null || phone.isEmpty()) throw new Exception("phone 값이 비어있습니다.");

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "yellow_id", yellowId);
        SAlimtalkSupport.put(body, "phone", phone);

        HttpPost post = bootpay.httpPost("alimtalk/senders/otp",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 발신프로필 등록
     * POST /v1/alimtalk/senders
     *
     * <p>⚠️ 카카오에 발신프로필이 실제 등록된다. 같은 {@code yellow_id} 를 다시 등록하면 기존 프로필을 재사용한다(dedup).</p>
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, AlimtalkSenderCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        if (params.otp == null || params.otp.isEmpty()) throw new Exception("otp 값이 비어있습니다.");
        if (params.yellowId == null || params.yellowId.isEmpty()) throw new Exception("yellowId 값이 비어있습니다.");
        if (params.phone == null || params.phone.isEmpty()) throw new Exception("phone 값이 비어있습니다.");
        if (params.categoryCode == null || params.categoryCode.isEmpty()) throw new Exception("categoryCode 값이 비어있습니다.");

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "otp", params.otp);
        SAlimtalkSupport.put(body, "yellow_id", params.yellowId);
        SAlimtalkSupport.put(body, "phone", params.phone);
        SAlimtalkSupport.put(body, "category_code", params.categoryCode);

        HttpPost post = bootpay.httpPost("alimtalk/senders",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 연동한 채널 목록 조회
     * GET /v1/alimtalk/senders
     *
     * <p>자체 DB 만 조회하며 벤더를 호출하지 않는다. 응답은 {@code { list: [...], count: N }}.</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("alimtalk/senders", SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 채널 상세 조회
     * GET /v1/alimtalk/senders/{ksp_id}
     *
     * <p>{@code sync} 가 true 면 벤더에서 채널 상태를 다시 읽어 반영한다(느리다). 미지정이면 자체 DB 만 본다.</p>
     * <p>⚠️ 미연동/미존재 채널은 404, 다른 프로젝트의 채널은 403 으로 오며 둘 다 {@code error_code} 는 3024 다.</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String kspId, Boolean sync) throws Exception {
        bootpay.requireCommerceCredentials();
        if (kspId == null || kspId.isEmpty()) throw new Exception("kspId 값이 비어있습니다.");

        List<NameValuePair> pairs = new ArrayList<>();
        SAlimtalkSupport.put(pairs, "sync", sync);

        HttpGet get = bootpay.httpGet("alimtalk/senders/" + kspId, pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 채널 연동 해지
     * DELETE /v1/alimtalk/senders/{ksp_id}
     *
     * <p>이 프로젝트와의 연동만 끊는다 — 채널 모델과 템플릿은 보존된다. 성공 시 본문은 null 이다.</p>
     */
    static public BootpayStoreResponse release(BootpayStoreObject bootpay, String kspId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (kspId == null || kspId.isEmpty()) throw new Exception("kspId 값이 비어있습니다.");

        HttpDelete delete = bootpay.httpDelete("alimtalk/senders/" + kspId, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 채널 변수 예문 사전 갱신
     * PUT /v1/alimtalk/senders/{ksp_id}/variable_examples
     *
     * <p>템플릿 미리보기에서 {@code #{user_name}} 대신 '홍길동' 처럼 읽히게 하는 <b>표시용</b> 값이다.</p>
     * <p>⚠️ 발송값이 아니다 — 벤더로 전송되지 않으므로 검수 상태와 무관하다. 보낸 키만 덮어쓴다(부분 갱신).
     * 키에 '.' 이나 선행 '$' 는 쓸 수 없다.</p>
     */
    static public BootpayStoreResponse variableExamples(BootpayStoreObject bootpay, String kspId, Map<String, Object> examples) throws Exception {
        bootpay.requireCommerceCredentials();
        if (kspId == null || kspId.isEmpty()) throw new Exception("kspId 값이 비어있습니다.");
        if (examples == null) throw new Exception("examples 값이 비어있습니다.");

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "examples", examples);

        HttpPut put = bootpay.httpPut("alimtalk/senders/" + kspId + "/variable_examples",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }
}
