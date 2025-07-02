package kr.co.bootpay.store.service.order_subscription_adjustment;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.orderSubscriptionAdjustment.OrderSubscriptionAdjustmentUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SOrderSubscriptionAdjustmentService {


    static public BootpayStoreResponse create(BootpayStoreObject bootpay, String orderSubscriptionId, SOrderSubscriptionAdjustment adjustment) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("order_subscriptions/" + orderSubscriptionId + "/adjustments", new StringEntity(gson.toJson(adjustment), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, OrderSubscriptionAdjustmentUpdateParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
//        HttpPost post = bootpay.httpPostMultipart("products", fileList, params);
        HttpPut put = bootpay.httpPut("order_subscriptions/" + params.orderSubscriptionId + "/adjustments", new StringEntity(gson.toJson(params), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String orderSubscriptionId, String orderSubscriptionAdjustmentId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");


        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = bootpay.httpDelete("order_subscriptions/" + orderSubscriptionId + "/adjustments?order_subscription_adjustment_id=" + orderSubscriptionAdjustmentId);

//        HttpResponse response = client.execute(delete);
//        return bootpay.responseToJsonObject(response);

        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);
    }
}
