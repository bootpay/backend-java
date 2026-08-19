package kr.co.bootpay.store.service.orders;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelActionParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SOrderCancelService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderCancelListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        String url = "order/cancel";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.orderId != null) nameValuePairList.add(new BasicNameValuePair("order_id", params.orderId));
            if (params.orderNumber != null) nameValuePairList.add(new BasicNameValuePair("order_number", params.orderNumber));

            HttpGet get = bootpay.httpGet(url, nameValuePairList, userContext(null));
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url, userContext(null));
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }


    static public BootpayStoreResponse request(BootpayStoreObject bootpay, OrderCancelParams orderCancelParams) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

//        if(orderCancelParams.isSupervisor) role = "supervisor" + "/";
        HttpPost post = bootpay.httpPost("order/cancel", new StringEntity(gson.toJson(orderCancelParams), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse withdraw(BootpayStoreObject bootpay, String orderCancelRequestHistoryId) throws Exception {
        return withdraw(bootpay, orderCancelRequestHistoryId, null);
    }

    /**
     * (구매자) 주문 취소 요청 철회
     * PUT /v1/order/cancel/{order_cancellation_request_id}/withdraw
     * @param orderCancelRequestHistoryId 취소 요청 이력 ID (서버 정식 이름은 order_cancellation_request_id — 같은 값이다)
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse withdraw(BootpayStoreObject bootpay, String orderCancelRequestHistoryId, String idempotencyKey) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("order/cancel/" + orderCancelRequestHistoryId + "/withdraw", new StringEntity("{}", "UTF-8"),
                userContext(idempotencyKey));
        HttpResponse response = client.execute(put);

        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse approve(BootpayStoreObject bootpay, OrderCancelActionParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("order/cancel/" + cancellationId(params) + "/approve", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(params.idempotencyKey));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse reject(BootpayStoreObject bootpay, OrderCancelActionParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("order/cancel/" + cancellationId(params) + "/reject", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext(params.idempotencyKey));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 취소 요청 이력 ID 를 뽑는다.
     * 정식 이름은 order_cancellation_request_id 이며, 구 이름 order_cancel_request_history_id 도 계속 받는다.
     */
    private static String cancellationId(OrderCancelActionParams params) throws Exception {
        String id = params.orderCancellationRequestId != null && !params.orderCancellationRequestId.isEmpty()
                ? params.orderCancellationRequestId
                : params.orderCancelRequestHistoryId;
        if (id == null || id.isEmpty()) throw new Exception("order_cancellation_request_id 값이 비어있습니다.");
        return id;
    }

    /**
     * 구매자 scope 요청 컨텍스트 — Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext userContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }

    /**
     * 관리자(승인/반려) scope 요청 컨텍스트 — Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext supervisorContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
