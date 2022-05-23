package kr.co.bootpay.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Subscribe;
import kr.co.bootpay.model.request.SubscribePayload;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

public class BillingService {
    static public HashMap<String, Object> getBillingKey(BootpayObject bootpay, Subscribe subscribe) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(subscribe.orderName == null || subscribe.orderName.isEmpty()) throw new Exception("item_name 값을 입력해주세요.");
        if(subscribe.subscriptionId == null || subscribe.subscriptionId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
        if(subscribe.pg == null || subscribe.pg.isEmpty()) throw new Exception("결제하고자 하는 pg alias를 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("request/subscribe", new StringEntity(gson.toJson(subscribe), "UTF-8"));

        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }

    static public HashMap<String, Object>  destroyBillingKey(BootpayObject bootpay, String billingKey) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(billingKey == null || billingKey.isEmpty()) throw new Exception("billingKey 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = bootpay.httpDelete("subscribe/billing_key/" + billingKey);
        delete.setHeader("Authorization", bootpay.getTokenValue());

        HttpResponse response = client.execute(delete);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }

    static public HashMap<String, Object> requestSubscribe(BootpayObject bootpay, SubscribePayload payload) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(payload.billingKey == null || payload.billingKey.isEmpty()) throw new Exception("billing_key 값을 입력해주세요.");
        if(payload.orderName == null || payload.orderName.isEmpty()) throw new Exception("item_name 값을 입력해주세요.");
        if(payload.price <= 0) throw new Exception("price 금액을 설정을 해주세요.");
        if(payload.orderId == null || payload.orderId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("subscribe/payment", new StringEntity(gson.toJson(payload), "UTF-8"));
        post.setHeader("Authorization", bootpay.getTokenValue());

        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }

    static public HashMap<String, Object> reserveSubscribe(BootpayObject bootpay, SubscribePayload reserve) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(reserve.billingKey == null || reserve.billingKey.isEmpty()) throw new Exception("billing_key 값을 입력해주세요.");
        if(reserve.orderName == null || reserve.orderName.isEmpty()) throw new Exception("item_name 값을 입력해주세요.");
        if(reserve.price <= 0) throw new Exception("price 금액을 설정을 해주세요.");
        if(reserve.orderId == null || reserve.orderId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
        if(reserve.reserveExecuteAt == 0) throw new Exception("execute_at 실행 시간을 설정해주세요.");
//        if(reserve.schedulerType == null || reserve.schedulerType.isEmpty()) reserve.schedulerType = "oneshot";

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("subscribe/payment/reserve", new StringEntity(gson.toJson(reserve), "UTF-8"));
        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);

        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }


    static public HashMap<String, Object>  reserveCancelSubscribe(BootpayObject bootpay, String reserve_id) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(reserve_id == null || reserve_id.isEmpty()) throw new Exception("reserve_id 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = bootpay.httpDelete("subscribe/payment/reserve/" + reserve_id);
        delete.setHeader("Authorization", bootpay.getTokenValue());

        HttpResponse response = client.execute(delete);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }
}
