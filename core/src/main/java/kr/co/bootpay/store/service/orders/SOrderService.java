package kr.co.bootpay.store.service.orders;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SOrderService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderListParams params) throws Exception {

        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        String url = "orders";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.csType != null) nameValuePairList.add(new BasicNameValuePair("cs_type", params.csType));
            if (params.cssAt != null) nameValuePairList.add(new BasicNameValuePair("css_at", params.cssAt));
            if (params.cseAt != null) nameValuePairList.add(new BasicNameValuePair("cse_at", params.cseAt));
            if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));
            if (params.userId != null) nameValuePairList.add(new BasicNameValuePair("user_id", params.userId));
            if (params.userGroupId != null) nameValuePairList.add(new BasicNameValuePair("user_group_id", params.userGroupId));
            if (params.subscriptionBillingType != null) nameValuePairList.add(new BasicNameValuePair("subscription_billing_type", params.subscriptionBillingType.toString()));

            if (params.status != null && !params.status.isEmpty()) {
                String joined = params.status.stream().map(String::valueOf).collect(Collectors.joining(","));
                nameValuePairList.add(new BasicNameValuePair("status", joined));
            }

            if (params.paymentStatus != null && !params.paymentStatus.isEmpty()) {
                String joined = params.paymentStatus.stream().map(String::valueOf).collect(Collectors.joining(","));
                nameValuePairList.add(new BasicNameValuePair("payment_status", joined));
            }

            if (params.orderSubscriptionIds != null && !params.orderSubscriptionIds.isEmpty()) {
                String joined = params.orderSubscriptionIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                nameValuePairList.add(new BasicNameValuePair("order_subscription_ids", joined));
            }

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("orders/" + orderId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse month(BootpayStoreObject bootpay, String userGroupId, String searchDate) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (userGroupId == null || userGroupId.isEmpty()) {
            throw new Exception("user_group_id 값이 비어있습니다.");
        }
        if (searchDate == null || searchDate.isEmpty()) {
            throw new Exception("search_date 값이 비어있습니다.");
        }

        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();


        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("user_group_id", userGroupId));
        nameValuePairList.add(new BasicNameValuePair("search_date", searchDate));



//        HttpPost post = bootpay.httpPost("orders/month", new StringEntity(gson.toJson(params), "UTF-8"));


        HttpGet get = bootpay.httpGet("orders/month", nameValuePairList);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
