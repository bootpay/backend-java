package kr.co.bootpay.store.service.order_subscription_bill;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.ListParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SOrderSubscriptionBillService {
    /**
     * 정기구독 빌(회차) 목록 조회
     * GET /v1/order_subscription_bills
     * page/limit 미지정시 각각 1 / 20 이 적용되고, user scope + Idempotency-Key 로 요청한다.
     */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderSubscriptionBillListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        String url = "order_subscription_bills";
        String idempotencyKey = params != null ? params.idempotencyKey : null;

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        if (params != null) {
            // order_subscription_id 또는 ex_uid 지원
            if (params.orderSubscriptionId != null) nameValuePairList.add(new BasicNameValuePair("order_subscription_id", params.orderSubscriptionId));
            if (params.exUid != null) nameValuePairList.add(new BasicNameValuePair("ex_uid", params.exUid));
            if (params.externalUid != null) nameValuePairList.add(new BasicNameValuePair("external_uid", params.externalUid));
            if (params.uid != null) nameValuePairList.add(new BasicNameValuePair("uid", params.uid));
        }
        // page/limit 미지정시 각각 1 / 20 적용
        nameValuePairList.add(new BasicNameValuePair("page", params != null && params.page != null ? params.page.toString() : "1"));
        nameValuePairList.add(new BasicNameValuePair("limit", params != null && params.limit != null ? params.limit.toString() : "20"));
        if (params != null) {
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.status != null && !params.status.isEmpty()) {
                String statusStr = params.status.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                nameValuePairList.add(new BasicNameValuePair("status", statusStr));
            }
        }

        HttpGet get = bootpay.httpGet(url, nameValuePairList, userContext(idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }


    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderSubscriptionBillId) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("order_subscription_bills/" + orderSubscriptionBillId);

        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SOrderSubscriptionBill orderSubscriptionBill) throws Exception {
        bootpay.requireCommerceCredentials();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
//        HttpPost post = bootpay.httpPostMultipart("products", fileList, params);
        HttpPut put = bootpay.httpPut("order_subscription_bills/" + orderSubscriptionBill.orderSubscriptionBillId, new StringEntity(gson.toJson(orderSubscriptionBill), "UTF-8"));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    /**
     * 빌 조회 요청 컨텍스트 — user scope. Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext userContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
