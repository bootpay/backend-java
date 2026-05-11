package kr.co.bootpay.store.service.order_subscription_requests;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SOrderSubscriptionRequestService {

    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderSubscriptionRequestListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        String url = "order-subscription-requests";
        if (params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.projectId != null) nameValuePairList.add(new BasicNameValuePair("project_id", params.projectId));
            if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));
            if (params.requestType != null) nameValuePairList.add(new BasicNameValuePair("request_type", params.requestType.toString()));
            if (params.status != null) nameValuePairList.add(new BasicNameValuePair("status", params.status.toString()));
            if (params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
            if (params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderSubscriptionRequestHistoryId, String projectId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (orderSubscriptionRequestHistoryId == null || orderSubscriptionRequestHistoryId.isEmpty()) {
            throw new Exception("orderSubscriptionRequestHistoryId 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        String url = "order-subscription-requests/" + orderSubscriptionRequestHistoryId;
        if (projectId != null && !projectId.isEmpty()) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("project_id", projectId));
            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, OrderSubscriptionRequestUpdateParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (params == null) {
            throw new Exception("params 값이 비어있습니다.");
        }
        if (params.orderSubscriptionRequestHistoryId == null || params.orderSubscriptionRequestHistoryId.isEmpty()) {
            throw new Exception("orderSubscriptionRequestHistoryId 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // ID 는 URL 에 포함하고 body 에서는 제외
        OrderSubscriptionRequestUpdateParams body = new OrderSubscriptionRequestUpdateParams();
        body.approval = params.approval;
        body.reason = params.reason;

        HttpPut put = bootpay.httpPut(
                "order-subscription-requests/" + params.orderSubscriptionRequestHistoryId,
                new StringEntity(gson.toJson(body), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }
}
