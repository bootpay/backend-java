package kr.co.bootpay.store.service.order_subscriptions.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestUpdateParams;
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

// 구독 변경요청 리소스 (order-subscription-requests)
// ⚠️ 하이픈 경로다. order_subscriptions · order_subscription_bills 는 언더스코어이므로 복사시 주의할 것.
public class SOrderSubscriptionRequestService {

    // 구독 변경요청 목록 (GET order-subscription-requests)
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderSubscriptionRequestListParams params) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = listRequest(bootpay, params);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    // 구독 변경요청 상세 (GET order-subscription-requests/:id)
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String requestHistoryId) throws Exception {
        return detail(bootpay, requestHistoryId, null);
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String requestHistoryId, String projectId) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = detailRequest(bootpay, requestHistoryId, projectId);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    // 구독 변경요청 승인/반려 (PUT order-subscription-requests/:id)
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, OrderSubscriptionRequestUpdateParams params) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut put = updateRequest(bootpay, params);
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse approve(BootpayStoreObject bootpay, OrderSubscriptionRequestUpdateParams params) throws Exception {
        if (params == null) throw new Exception("params 값이 비어있습니다");
        params.approval = OrderSubscriptionRequestUpdateParams.APPROVAL_APPROVE;
        return update(bootpay, params);
    }

    static public BootpayStoreResponse reject(BootpayStoreObject bootpay, OrderSubscriptionRequestUpdateParams params) throws Exception {
        if (params == null) throw new Exception("params 값이 비어있습니다");
        params.approval = OrderSubscriptionRequestUpdateParams.APPROVAL_REJECT;
        return update(bootpay, params);
    }

    static HttpGet listRequest(BootpayStoreObject bootpay, OrderSubscriptionRequestListParams params) throws Exception {
        validateAuthorization(bootpay);

        String url = "order-subscription-requests";
        if (params == null) return bootpay.httpGet(url, new RequestContext("user"));

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        if (params.projectId != null) nameValuePairList.add(new BasicNameValuePair("project_id", params.projectId));
        if (params.orderSubscriptionId != null) nameValuePairList.add(new BasicNameValuePair("order_subscription_id", params.orderSubscriptionId));
        if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
        if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));
        if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
        if (params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
        if (params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));
        if (params.status != null) nameValuePairList.add(new BasicNameValuePair("status", params.status.toString()));
        if (params.requestType != null) nameValuePairList.add(new BasicNameValuePair("request_type", params.requestType.toString()));
        if (params.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", params.userId));
        if (params.userGroupId != null) nameValuePairList.add(new BasicNameValuePair("user_group_id", params.userGroupId));

        return bootpay.httpGet(url, nameValuePairList, new RequestContext(roleFor(params.projectId)));
    }

    static HttpGet detailRequest(BootpayStoreObject bootpay, String requestHistoryId, String projectId) throws Exception {
        validateAuthorization(bootpay);
        if (requestHistoryId == null || requestHistoryId.isEmpty()) throw new Exception("request_history_id 값이 비어있습니다");

        String url = "order-subscription-requests/" + requestHistoryId;
        RequestContext context = new RequestContext(roleFor(projectId));
        if (projectId == null || projectId.isEmpty()) return bootpay.httpGet(url, context);

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("project_id", projectId));
        return bootpay.httpGet(url, nameValuePairList, context);
    }

    static HttpPut updateRequest(BootpayStoreObject bootpay, OrderSubscriptionRequestUpdateParams params) throws Exception {
        validateAuthorization(bootpay);
        if (params == null) throw new Exception("params 값이 비어있습니다");
        if (params.requestHistoryId == null || params.requestHistoryId.isEmpty()) throw new Exception("request_history_id 값이 비어있습니다");
        if (params.approval == null || params.approval.isEmpty()) throw new Exception("approval 값이 비어있습니다");

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return bootpay.httpPut(
                "order-subscription-requests/" + params.requestHistoryId,
                new StringEntity(gson.toJson(params), "UTF-8"),
                new RequestContext("supervisor")
        );
    }

    // project_id를 주면 supervisor(프로젝트 전체 검색), 없으면 user(본인 요청)로 조회한다
    static private String roleFor(String projectId) {
        return (projectId == null || projectId.isEmpty()) ? "user" : "supervisor";
    }

    // 토큰이 없다면 client key / secret key 기반의 Basic 인증을 사용한다
    static private void validateAuthorization(BootpayStoreObject bootpay) throws Exception {
        if (bootpay.getAuthorizationHeader(null) == null) throw new Exception("token 값이 비어있습니다.");
    }
}
