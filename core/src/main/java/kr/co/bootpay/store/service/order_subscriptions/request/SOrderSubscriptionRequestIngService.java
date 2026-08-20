package kr.co.bootpay.store.service.order_subscriptions.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPurchaseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTransferParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SOrderSubscriptionRequestIngService {
    static public BootpayStoreResponse pause(BootpayStoreObject bootpay, OrderSubscriptionPauseParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("order_subscriptions/requests/ing/pause", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse resume(BootpayStoreObject bootpay, OrderSubscriptionResumeParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("order_subscriptions/requests/ing/resume", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }


    // 중도해지 수수료 사전계산 (GET order_subscriptions/requests/ing/calculate_termination_fee)
    // 해지 요청 전에 얼마가 나오는지 미리 보여줄 때 쓴다
    static public BootpayStoreResponse calculateTerminationFee(BootpayStoreObject bootpay, String orderSubscriptionId, String orderNumber) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = calculateTerminationFeeRequest(bootpay, orderSubscriptionId, orderNumber);
        HttpResponse response = client.execute(get);

        return bootpay.responseToJsonObject(response);
    }

    static HttpGet calculateTerminationFeeRequest(BootpayStoreObject bootpay, String orderSubscriptionId, String orderNumber) throws Exception {
        // 토큰 유효성 검증
        if (bootpay == null || bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new IllegalArgumentException("Bootpay 토큰이 비어있습니다.");
        }

        // orderSubscriptionId 또는 orderNumber 중 하나는 필수
        boolean hasOrderSubscriptionId = orderSubscriptionId != null && !orderSubscriptionId.trim().isEmpty();
        boolean hasOrderNumber = orderNumber != null && !orderNumber.trim().isEmpty();

        if (!hasOrderSubscriptionId && !hasOrderNumber) {
            throw new IllegalArgumentException("orderSubscriptionId 또는 orderNumber 중 하나는 필수입니다.");
        }

        // 값이 설정되지 않은 필드는 전송하지 않는다 (둘 다 있으면 둘 다 전송한다)
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        if (hasOrderSubscriptionId) nameValuePairList.add(new BasicNameValuePair("order_subscription_id", orderSubscriptionId));
        if (hasOrderNumber) nameValuePairList.add(new BasicNameValuePair("order_number", orderNumber));

        return bootpay.httpGet("order_subscriptions/requests/ing/calculate_termination_fee", nameValuePairList);
    }

    // 오버로드: orderNumber만 전달하는 경우
    static public BootpayStoreResponse calculateTerminationFeeByOrderNumber(BootpayStoreObject bootpay, String orderNumber) throws Exception {
        return calculateTerminationFee(bootpay, null, orderNumber);
    }

    // 오버로드: orderSubscriptionId만 전달하는 경우
    static public BootpayStoreResponse calculateTerminationFeeBySubscriptionId(BootpayStoreObject bootpay, String orderSubscriptionId) throws Exception {
        return calculateTerminationFee(bootpay, orderSubscriptionId, null);
    }


    static public BootpayStoreResponse termination(BootpayStoreObject bootpay, OrderSubscriptionTerminationParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("order_subscriptions/requests/ing/termination", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }


    // 중도인수 요청 (POST order_subscriptions/requests/ing/purchase)
    static public BootpayStoreResponse purchase(BootpayStoreObject bootpay, OrderSubscriptionPurchaseParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("order_subscriptions/requests/ing/purchase", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    // 구독 이전/승계 요청 (POST order_subscriptions/requests/ing/transfer)
    static public BootpayStoreResponse transfer(BootpayStoreObject bootpay, OrderSubscriptionTransferParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("order_subscriptions/requests/ing/transfer", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }


//    static public BootpayStoreResponse calculatePurchasePrice(BootpayStoreObject bootpay, String orderSubscriptionId) throws Exception {
//        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
//            throw new Exception("token 값이 비어있습니다.");
//        }
//
//        HttpClient client = HttpClientBuilder.create().build();
//
//        HttpGet get = bootpay.httpGet("seller/payment/method" );
//
//        get.setHeader("Authorization", bootpay.getTokenValue());
//        HttpResponse response = client.execute(get);
//        return bootpay.responseToJsonObject(response);
//    }


}
