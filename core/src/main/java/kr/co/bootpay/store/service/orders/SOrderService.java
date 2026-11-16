package kr.co.bootpay.store.service.orders;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import kr.co.bootpay.store.model.request.order.OrderPurchasesUpdateParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SOrderService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderListParams params) throws Exception {

        bootpay.requireCommerceCredentials();

        String url = "orders";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.csType != null) nameValuePairList.add(new BasicNameValuePair("cs_type", params.csType));
            if (params.searchDateFrom != null) nameValuePairList.add(new BasicNameValuePair("search_date_from", params.searchDateFrom));
            if (params.searchDateTo != null) nameValuePairList.add(new BasicNameValuePair("search_date_to", params.searchDateTo));
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

            HttpGet get = bootpay.httpGet(url, nameValuePairList, RequestContext.ofRole("user"));
            HttpResponse response = bootpay.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url, RequestContext.ofRole("user"));
            HttpResponse response = bootpay.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderId) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("orders/" + orderId, RequestContext.ofRole("user"));

        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse month(BootpayStoreObject bootpay, String userGroupId, String searchDate) throws Exception {
        bootpay.requireCommerceCredentials();
        if (userGroupId == null || userGroupId.isEmpty()) {
            throw new Exception("user_group_id 값이 비어있습니다.");
        }
        if (searchDate == null || searchDate.isEmpty()) {
            throw new Exception("search_date 값이 비어있습니다.");
        }

        bootpay.requireCommerceCredentials();


        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("user_group_id", userGroupId));
        nameValuePairList.add(new BasicNameValuePair("search_date", searchDate));



//        HttpPost post = bootpay.httpPost("orders/month", new StringEntity(gson.toJson(params), "UTF-8"));


        HttpGet get = bootpay.httpGet("orders/month", nameValuePairList, RequestContext.ofRole("user"));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 발송 정보 조회 — Bootpay-Role 은 항상 user 로 보낸다.
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse purchases(BootpayStoreObject bootpay, String orderNumber, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        if (orderNumber == null || orderNumber.isEmpty()) {
            throw new Exception("orderNumber 값이 비어있습니다.");
        }

        HttpGet get = bootpay.httpGet("orders/" + URLEncoder.encode(orderNumber, "UTF-8") + "/purchases", purchasesContext(idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 발송처리 — 발주 단위 상태·운송장 갱신. Bootpay-Role 은 항상 user.
     */
    static public BootpayStoreResponse updatePurchases(BootpayStoreObject bootpay, OrderPurchasesUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null || params.orderNumber == null || params.orderNumber.isEmpty()) {
            throw new Exception("orderNumber 값이 비어있습니다.");
        }
        if (params.purchases == null || params.purchases.isEmpty()) {
            throw new Exception("purchases 값이 비어있습니다.");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("orders/" + URLEncoder.encode(params.orderNumber, "UTF-8") + "/purchases",
                new StringEntity(gson.toJson(params), "UTF-8"),
                purchasesContext(params.idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    private static RequestContext purchasesContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
