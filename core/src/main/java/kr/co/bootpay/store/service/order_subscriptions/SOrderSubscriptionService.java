package kr.co.bootpay.store.service.order_subscriptions;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionApproveParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionRejectParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionTerminateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionChargeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionChargeRevokeParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.http.HttpDeleteWithBody;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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

            if(params.requestType != null) nameValuePairList.add(new BasicNameValuePair("request_type", params.eAt));
            if(params.userGroupId != null) nameValuePairList.add(new BasicNameValuePair("user_group_id", params.userGroupId));
            if(params.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", params.userId));

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

    static public BootpayStoreResponse supervisorApprove(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorOrderSubscriptionApproveParams params) throws Exception {
        return supervisorAction(bootpay, "order_subscriptions/" + orderSubscriptionId + "/approve", params == null ? new SupervisorOrderSubscriptionApproveParams() : params);
    }

    static public BootpayStoreResponse supervisorReject(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorOrderSubscriptionRejectParams params) throws Exception {
        return supervisorAction(bootpay, "order_subscriptions/" + orderSubscriptionId + "/reject", params == null ? new SupervisorOrderSubscriptionRejectParams() : params);
    }

    static public BootpayStoreResponse supervisorTerminate(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorOrderSubscriptionTerminateParams params) throws Exception {
        return supervisorAction(bootpay, "order_subscriptions/" + orderSubscriptionId + "/terminate", params == null ? new SupervisorOrderSubscriptionTerminateParams() : params);
    }

    static public BootpayStoreResponse supervisorPause(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorOrderSubscriptionPauseParams params) throws Exception {
        return supervisorAction(bootpay, "order_subscriptions/" + orderSubscriptionId + "/pause", params);
    }

    static public BootpayStoreResponse supervisorResume(BootpayStoreObject bootpay, String orderSubscriptionId, SupervisorOrderSubscriptionResumeParams params) throws Exception {
        return supervisorAction(bootpay, "order_subscriptions/" + orderSubscriptionId + "/resume", params == null ? new SupervisorOrderSubscriptionResumeParams() : params);
    }

    // 수시결제(온디맨드) charge_key 즉시 결제
    // charge_key는 body로만 전송한다 (URL/query 금지 - 액세스 로그 노출 방지)
    static public BootpayStoreResponse supervisorCharge(BootpayStoreObject bootpay, SupervisorOrderSubscriptionChargeParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (params == null) {
            throw new Exception("params 값이 비어있습니다");
        }
        if (params.chargeKey == null || params.chargeKey.isEmpty()) {
            throw new Exception("charge_key 값이 비어있습니다");
        }
        if (params.price == null) {
            throw new Exception("price 금액을 설정을 해주세요.");
        }
        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        HttpPost post = bootpay.httpPost(
                "order_subscriptions/charge",
                new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorHeaders(params.idempotencyKey)
        );
        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    // 수시결제(온디맨드) charge_key 해지
    // 해지 이후 해당 키로의 재결제는 불가능하다
    static public BootpayStoreResponse supervisorChargeRevoke(BootpayStoreObject bootpay, SupervisorOrderSubscriptionChargeRevokeParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (params == null) {
            throw new Exception("params 값이 비어있습니다");
        }
        if (params.chargeKey == null || params.chargeKey.isEmpty()) {
            throw new Exception("charge_key 값이 비어있습니다");
        }
        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        HttpDeleteWithBody delete = bootpay.httpDeleteWithBody("order_subscriptions/charge", new StringEntity(gson.toJson(params), "UTF-8"));
        for (Map.Entry<String, String> entry : supervisorHeaders(params.idempotencyKey).entrySet()) {
            delete.setHeader(entry.getKey(), entry.getValue());
        }
        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    static private Map<String, String> supervisorHeaders(String idempotencyKey) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Idempotency-Key", (idempotencyKey == null || idempotencyKey.isEmpty()) ? UUID.randomUUID().toString() : idempotencyKey);
        headers.put("BOOTPAY-ROLE", "supervisor");
        return headers;
    }

    static private BootpayStoreResponse supervisorAction(BootpayStoreObject bootpay, String uri, Object params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        HttpPut put = bootpay.httpPut(uri, new StringEntity(gson.toJson(params), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

}
