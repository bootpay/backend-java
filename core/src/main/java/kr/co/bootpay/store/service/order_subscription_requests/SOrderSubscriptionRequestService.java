package kr.co.bootpay.store.service.order_subscription_requests;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
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
        String projectId = params != null ? params.projectId : null;
        String idempotencyKey = params != null ? params.idempotencyKey : null;

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        if (projectId != null) nameValuePairList.add(new BasicNameValuePair("project_id", projectId));
        // page/limit 미지정시 각각 1 / 20 적용
        nameValuePairList.add(new BasicNameValuePair("page", params != null && params.page != null ? params.page.toString() : "1"));
        nameValuePairList.add(new BasicNameValuePair("limit", params != null && params.limit != null ? params.limit.toString() : "20"));
        if (params != null) {
            if (params.orderSubscriptionId != null) nameValuePairList.add(new BasicNameValuePair("order_subscription_id", params.orderSubscriptionId));
            if (params.requestType != null) nameValuePairList.add(new BasicNameValuePair("request_type", params.requestType.toString()));
            if (params.status != null) nameValuePairList.add(new BasicNameValuePair("status", params.status.toString()));
            if (params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
            if (params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", params.userId));
            if (params.userGroupId != null) nameValuePairList.add(new BasicNameValuePair("user_group_id", params.userGroupId));
        }

        HttpGet get = bootpay.httpGet(url, nameValuePairList, requestContext(projectId, idempotencyKey));
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
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
            HttpGet get = bootpay.httpGet(url, nameValuePairList, requestContext(projectId, null));
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url, requestContext(null, null));
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
        body.price = params.price;
        body.taxFreePrice = params.taxFreePrice;
        body.terminationFee = params.terminationFee;
        body.lastBillRefundPrice = params.lastBillRefundPrice;
        body.finalFee = params.finalFee;
        body.serviceEndAt = params.serviceEndAt;

        HttpPut put = bootpay.httpPut(
                "order-subscription-requests/" + params.orderSubscriptionRequestHistoryId,
                new StringEntity(gson.toJson(body), "UTF-8"),
                supervisorContext(params.idempotencyKey));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 조회 요청 컨텍스트 — project_id 가 있으면 supervisor, 없으면 user scope 다.
     * Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext requestContext(String projectId, String idempotencyKey) {
        return RequestContext.builder()
                .role(projectId != null && !projectId.isEmpty() ? "supervisor" : "user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }

    /**
     * 승인/반려 요청 컨텍스트 — 서버가 supervisor scope 를 요구한다.
     */
    private static RequestContext supervisorContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
