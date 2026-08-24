package kr.co.bootpay.store.service.order_subscription_adjustment;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.http.HttpDeleteWithBody;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.orderSubscriptionAdjustment.OrderSubscriptionAdjustmentUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SOrderSubscriptionAdjustmentService {


    static public BootpayStoreResponse create(BootpayStoreObject bootpay, String orderSubscriptionId, SOrderSubscriptionAdjustment adjustment) throws Exception {
        bootpay.requireCommerceCredentials();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("order_subscriptions/" + orderSubscriptionId + "/adjustments", new StringEntity(gson.toJson(adjustment), "UTF-8"),
                supervisorContext(null));

        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, OrderSubscriptionAdjustmentUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("order_subscriptions/" + params.orderSubscriptionId + "/adjustments", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(null));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 조정항목 삭제
     * DELETE /v1/order_subscriptions/{order_subscription_id}/adjustments
     * ⚠️ 대상 ID 는 query 가 아니라 body 로 보낸다.
     */
    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String orderSubscriptionId, String orderSubscriptionAdjustmentId) throws Exception {
        bootpay.requireCommerceCredentials();

        String body = "{\"order_subscription_adjustment_id\":\"" + orderSubscriptionAdjustmentId + "\"}";
        HttpDeleteWithBody delete = bootpay.httpDeleteWithBody("order_subscriptions/" + orderSubscriptionId + "/adjustments",
                new StringEntity(body, "UTF-8"), supervisorContext(null));

        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 조정항목 API 요청 컨텍스트 — 서버가 supervisor scope 를 요구한다.
     * Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext supervisorContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
