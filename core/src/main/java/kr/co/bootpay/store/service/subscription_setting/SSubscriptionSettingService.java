package kr.co.bootpay.store.service.subscription_setting;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.pojo.SSubscriptionSetting;
import kr.co.bootpay.store.model.request.ListParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.*;

import static kr.co.bootpay.BootpayResponse.responseJson;

public class SSubscriptionSettingService {
    static public HashMap<String, Object> list(BootpayStoreObject bootpay, ListParams params) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> payload = new HashMap<>();
        if (params.keyword != null) payload.put("keyword", params.keyword);
        if (params.page != null) payload.put("page", params.page);
        if (params.limit != null) payload.put("limit", params.limit);

        // 파라미터를 URL 쿼리 문자열로 변환
        StringBuilder query = new StringBuilder("subscription_settings?");
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 마지막 '&' 제거
        if (query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        // GET 요청 객체 생성
        HttpGet get = bootpay.httpGet(query.toString());

        // HTTP 요청 전송 및 응답 수신
        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public HashMap<String, Object> create(BootpayStoreObject bootpay, SSubscriptionSetting subscriptionSetting) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("subscription_settings", new StringEntity(gson.toJson(subscriptionSetting), "UTF-8"));
        HttpResponse response = client.execute(post);

        // 응답 처리
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    static public HashMap<String, Object> update(BootpayStoreObject bootpay, SSubscriptionSetting subscriptionSetting) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("subscription_settings/" + subscriptionSetting.subscriptionSettingId, new StringEntity(gson.toJson(subscriptionSetting), "UTF-8"));
        HttpResponse response = client.execute(put);

        // 응답 처리
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }


    static public HashMap<String, Object> detail(BootpayStoreObject bootpay, String subscriptionSettingId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("subscription_settings/" + subscriptionSettingId);

        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public HashMap<String, Object> delete(BootpayStoreObject bootpay, String subscriptionSettingId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpDelete delete = bootpay.httpDelete("subscription_settings/" + subscriptionSettingId);

        HttpResponse response = client.execute(delete);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }
}
