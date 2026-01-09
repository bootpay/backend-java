package kr.co.bootpay.store.service.order_subscriptions;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SOrderSubscriptionService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderSubscriptionListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        String url = "order_subscriptions";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if(params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
            if(params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));

            if(params.requestType != null) nameValuePairList.add(new BasicNameValuePair("request_type", params.requestType.toString()));

            // user_group_id 또는 ex_uid 지원
            if(params.userGroupId != null) nameValuePairList.add(new BasicNameValuePair("user_group_id", params.userGroupId));
            if(params.userGroupExUid != null) nameValuePairList.add(new BasicNameValuePair("user_group_ex_uid", params.userGroupExUid));
            if(params.userGroupExternalUid != null) nameValuePairList.add(new BasicNameValuePair("user_group_external_uid", params.userGroupExternalUid));
            if(params.userGroupUid != null) nameValuePairList.add(new BasicNameValuePair("user_group_uid", params.userGroupUid));

            // user_id 또는 ex_uid 지원
            if(params.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", params.userId));
            if(params.userExUid != null) nameValuePairList.add(new BasicNameValuePair("user_ex_uid", params.userExUid));
            if(params.userExternalUid != null) nameValuePairList.add(new BasicNameValuePair("user_external_uid", params.userExternalUid));
            if(params.userUid != null) nameValuePairList.add(new BasicNameValuePair("user_uid", params.userUid));

            if(params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if(params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if(params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }


    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderSubscriptionId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("order_subscriptions/" + orderSubscriptionId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, OrderSubscriptionUpdateParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if(params == null) {
            throw new Exception("params 값이 비어있습니다");
        }
        if(params.orderSubscriptionId == null || params.orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPut put = bootpay.httpPut("order_subscriptions/" + params.orderSubscriptionId, new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 구독 승인
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param reason 승인 사유 (선택)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse approve(BootpayStoreObject bootpay, String orderSubscriptionId, String reason) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        java.util.Map<String, String> params = new java.util.HashMap<>();
        if(reason != null && !reason.isEmpty()) {
            params.put("reason", reason);
        }

        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/approve", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 구독 거절
     * @param bootpay BootpayStoreObject
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param reason 거절 사유 (필수)
     * @return BootpayStoreResponse
     */
    static public BootpayStoreResponse reject(BootpayStoreObject bootpay, String orderSubscriptionId, String reason) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if(orderSubscriptionId == null || orderSubscriptionId.isEmpty()) {
            throw new Exception("order_subscription_id 값이 비어있습니다");
        }
        if(reason == null || reason.isEmpty()) {
            throw new Exception("reason 값이 비어있습니다 (거절 사유 필수)");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("reason", reason);

        HttpPut put = bootpay.httpPut("order_subscriptions/" + orderSubscriptionId + "/reject", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

}
