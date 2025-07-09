package kr.co.bootpay.store.service.order_subscriptions.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
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


    static public BootpayStoreResponse calculateTerminationFee(BootpayStoreObject bootpay, String orderSubscriptionId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("order_subscriptions/requests/ing/calculate_termination_fee?order_subscription_id=" + orderSubscriptionId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
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



//    static public BootpayStoreResponse purchase(BootpayStoreObject bootpay, OrderSubscriptionPauseParams params) throws Exception {
//
//    }


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
