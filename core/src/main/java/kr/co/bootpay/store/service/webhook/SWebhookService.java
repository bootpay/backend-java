package kr.co.bootpay.store.service.webhook;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.webhook.TestWebhookParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class SWebhookService {
    // 테스트 웹훅을 발송한다 (POST webhook/test)
    // header_content_type 을 지정하지 않으면 서버 기본값으로 발송된다
    static public BootpayStoreResponse sendTestWebhook(BootpayStoreObject bootpay, TestWebhookParams params) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = sendTestWebhookRequest(bootpay, params);
        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse sendTestWebhook(BootpayStoreObject bootpay) throws Exception {
        return sendTestWebhook(bootpay, new TestWebhookParams());
    }

    static HttpPost sendTestWebhookRequest(BootpayStoreObject bootpay, TestWebhookParams params) throws Exception {
        validateAuthorization(bootpay);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 값이 설정되지 않은 필드는 전송하지 않는다 (ruby의 compact 동작)
        String body = gson.toJson(params == null ? new TestWebhookParams() : params);

        return bootpay.httpPost("webhook/test", new StringEntity(body, "UTF-8"));
    }

    // 토큰이 없다면 client key / secret key 기반의 Basic 인증을 사용한다
    static private void validateAuthorization(BootpayStoreObject bootpay) throws Exception {
        if (bootpay.getAuthorizationHeader(null) == null) throw new Exception("token 값이 비어있습니다.");
    }
}
