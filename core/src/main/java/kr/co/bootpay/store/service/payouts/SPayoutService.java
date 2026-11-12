package kr.co.bootpay.store.service.payouts;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.payout.PayoutCreateParams;
import kr.co.bootpay.store.model.request.payout.PayoutListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 입점사 지급 요청 — /v1/sdk/payouts 계열, 인스턴스 role 과 무관하게 supervisor 전용. */
public class SPayoutService {

    /** 지급 요청 등록 — 같은 merchantPayoutId 재호출은 처음 만든 항목을 반환합니다. */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, PayoutCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        requireValue(params.subSellerId, "subSellerId");
        requireValue(params.amount, "amount");
        requireValue(params.merchantPayoutId, "merchantPayoutId");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("sub_seller_id", params.subSellerId);
        body.put("amount", params.amount);
        body.put("merchant_payout_id", params.merchantPayoutId);
        body.put("order_number", params.orderNumber);
        body.put("payout_date", params.payoutDate);
        String json = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create().toJson(body);
        HttpPost post = bootpay.httpPost("sdk/payouts", new StringEntity(json, "UTF-8"), context(params.idempotencyKey));
        return bootpay.responseToJsonObject(bootpay.execute(post));
    }

    /** @param payoutId merchant_payout_id 또는 지급 항목 id */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String payoutId, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        HttpGet get = bootpay.httpGet(path(payoutId), context(idempotencyKey));
        return bootpay.responseToJsonObject(bootpay.execute(get));
    }

    /** 최근 생성 순 지급 목록 — 서버 기본 page/limit 을 보존하도록 미지정 값은 쿼리에 넣지 않습니다. */
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, PayoutListParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        List<NameValuePair> query = new ArrayList<>();
        if (params != null) {
            if (params.status != null) query.add(new BasicNameValuePair("status", params.status.toString()));
            if (params.subSellerId != null) query.add(new BasicNameValuePair("sub_seller_id", params.subSellerId));
            if (params.from != null) query.add(new BasicNameValuePair("from", params.from));
            if (params.to != null) query.add(new BasicNameValuePair("to", params.to));
            if (params.page != null) query.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) query.add(new BasicNameValuePair("limit", params.limit.toString()));
        }
        RequestContext context = context(params == null ? null : params.idempotencyKey);
        HttpGet get = query.isEmpty()
                ? bootpay.httpGet("sdk/payouts", context)
                : bootpay.httpGet("sdk/payouts", query, context);
        return bootpay.responseToJsonObject(bootpay.execute(get));
    }

    private static RequestContext context(String idempotencyKey) {
        return RequestContext.builder().role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey)).build();
    }

    private static String path(String payoutId) throws Exception {
        requireValue(payoutId, "payoutId");
        return "sdk/payouts/" + URLEncoder.encode(payoutId, "UTF-8").replace("+", "%20");
    }

    private static void requireValue(Object value, String name) throws Exception {
        if (value == null || "".equals(value)) throw new Exception(name + " 값이 비어있습니다.");
    }
}
