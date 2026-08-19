package kr.co.bootpay.store.service.webhook;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.Map;

public class SWebhookService {

    /**
     * 테스트 웹훅 발송
     * POST /v1/webhook/test
     * 등록된 웹훅 URL 로 테스트 페이로드를 보내 연동을 확인할 때 쓴다.
     * @param headerContentType 웹훅 본문 Content-Type (미지정시 전송하지 않는다 — 서버 기본값 사용)
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse sendTest(BootpayStoreObject bootpay, Integer headerContentType, String idempotencyKey) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        if (headerContentType != null) {
            params.put("header_content_type", headerContentType);
        }

        RequestContext context = RequestContext.builder()
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();

        HttpPost post = bootpay.httpPost("webhook/test", new StringEntity(gson.toJson(params), "UTF-8"), context);

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }
}
