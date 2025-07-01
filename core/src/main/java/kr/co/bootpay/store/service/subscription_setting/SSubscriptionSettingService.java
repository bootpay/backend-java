package kr.co.bootpay.store.service.subscription_setting;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SSubscriptionSetting;
import kr.co.bootpay.store.model.request.ListParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SSubscriptionSettingService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, ListParams params) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        String url = "subscription_settings";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse create(BootpayStoreObject bootpay, SSubscriptionSetting subscriptionSetting) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("subscription_settings", new StringEntity(gson.toJson(subscriptionSetting), "UTF-8"));
        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SSubscriptionSetting subscriptionSetting) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("subscription_settings/" + subscriptionSetting.subscriptionSettingId, new StringEntity(gson.toJson(subscriptionSetting), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }


    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String subscriptionSettingId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("subscription_settings/" + subscriptionSettingId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String subscriptionSettingId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpDelete delete = bootpay.httpDelete("subscription_settings/" + subscriptionSettingId);

        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }
}
