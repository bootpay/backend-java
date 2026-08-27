package kr.co.bootpay.store.service.alimtalk;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialListParams;
import kr.co.bootpay.store.model.request.alimtalk.AlimtalkOfficialRecommendParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 부트페이 공식 알림톡 템플릿 카탈로그 — /v1/alimtalk/official 계열
 *
 * <p>부트페이가 미리 카카오 승인을 받아 둔 템플릿이라, 그룹키가 등록된 채널이면 <b>검수 없이 즉시 발송</b>된다.
 * {@code alimtalkSender.create} 로 채널을 등록하면 그룹 등록이 함께 끝나므로 따로 채택할 것이 없다.</p>
 *
 * <p>전부 조회 계열이라 부작용이 없다 (자체 DB 만 본다).</p>
 */
public class SAlimtalkOfficialService {

    /**
     * 공식 템플릿 검색
     * GET /v1/alimtalk/official
     *
     * <p>{@code keyword} 는 본문·이름·분류를 부분일치(대소문자 무시)로 훑으며, 서버가 먼저 보는 정본 키인
     * {@code q} 로 전송된다.</p>
     *
     * <p>응답: {@code { list: [...], count, page, per, categories: [...] }}</p>
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, AlimtalkOfficialListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        List<NameValuePair> pairs = new ArrayList<>();
        if (params != null) {
            // 서버는 q 를 먼저 보고 없으면 keyword 를 본다 — 정본 키인 q 로 보낸다
            SAlimtalkSupport.put(pairs, "q", params.keyword);
            SAlimtalkSupport.put(pairs, "category", params.category);
            SAlimtalkSupport.put(pairs, "msg_type", params.msgType);
            SAlimtalkSupport.put(pairs, "page", params.page);
            SAlimtalkSupport.put(pairs, "per", params.per);
            SAlimtalkSupport.put(pairs, "ksp_id", params.kspId);
        }

        HttpGet get = bootpay.httpGet("alimtalk/official", pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 보내려는 문구로 공식 템플릿 추천
     * POST /v1/alimtalk/official/recommend
     *
     * <p>유사도 {@code score}(0~1) 내림차순으로 돌려준다.</p>
     */
    static public BootpayStoreResponse recommend(BootpayStoreObject bootpay, AlimtalkOfficialRecommendParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null || params.text == null || params.text.isEmpty()) throw new Exception("text 값이 비어있습니다.");

        Gson gson = SAlimtalkSupport.gson();

        Map<String, Object> body = new LinkedHashMap<>();
        SAlimtalkSupport.put(body, "text", params.text);
        SAlimtalkSupport.put(body, "category", params.category);
        SAlimtalkSupport.put(body, "limit", params.limit); // 서버 기본 5
        SAlimtalkSupport.put(body, "ksp_id", params.kspId);

        HttpPost post = bootpay.httpPost("alimtalk/official/recommend",
                new StringEntity(gson.toJson(body), "UTF-8"), SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 공식 템플릿 상세 조회
     * GET /v1/alimtalk/official/{code}
     *
     * <p>{@code code} 는 서버 채번 코드(슬래시를 포함하지 않는다). 없거나 미노출이면 404(3015).</p>
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String code, String kspId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (code == null || code.isEmpty()) throw new Exception("code 값이 비어있습니다.");

        List<NameValuePair> pairs = new ArrayList<>();
        SAlimtalkSupport.put(pairs, "ksp_id", kspId);

        HttpGet get = bootpay.httpGet("alimtalk/official/" + code, pairs, SAlimtalkSupport.context());
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
