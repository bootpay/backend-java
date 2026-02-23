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
