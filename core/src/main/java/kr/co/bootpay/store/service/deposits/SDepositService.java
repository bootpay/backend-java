package kr.co.bootpay.store.service.deposits;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.deposit.DepositCreateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/** 뱅크알람 입금대기 — /v1/sdk/deposits 계열, 인스턴스 role 과 무관하게 supervisor 전용. */
public class SDepositService {

    /** 판매자 입금 계좌 목록 — 첫 계좌가 대표 계좌입니다. */
    static public BootpayStoreResponse accounts(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();
        HttpGet get = bootpay.httpGet("sdk/deposits/accounts", context(null));
        return bootpay.responseToJsonObject(bootpay.execute(get));
    }

    /** 웹훅 서명 비밀값 발급 — 원문은 이 응답에서만 받고, 다시 부르면 바뀝니다. */
    static public BootpayStoreResponse webhookSecret(BootpayStoreObject bootpay, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        HttpPost post = bootpay.httpPost("sdk/deposits/webhook_secret", new StringEntity("{}", "UTF-8"), context(idempotencyKey));
        return bootpay.responseToJsonObject(bootpay.execute(post));
    }

    /** 입금대기 등록 — 같은 merchantOrderId 로 금액·입금자명·계좌가 같은 재호출은 기존 주문을 돌려줍니다 (다르면 18110). */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, DepositCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) throw new Exception("params 값이 비어있습니다.");
        requireValue(params.merchantOrderId, "merchantOrderId");
        requireValue(params.price, "price");
        requireValue(params.depositor, "depositor");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("merchant_order_id", params.merchantOrderId);
        body.put("price", params.price);
        body.put("depositor", params.depositor);
        body.put("order_name", params.orderName);
        body.put("memo", params.memo);
        body.put("buyer_name", params.buyerName);
        body.put("buyer_phone", params.buyerPhone);
        body.put("buyer_email", params.buyerEmail);
        body.put("account_id", params.accountId);
        body.put("webhook_url", params.webhookUrl);
        body.put("notify", params.notify);
        // Gson 기본 직렬화가 null 만 생략하므로 중첩 notify 의 false 도 보존됩니다.
        String json = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create().toJson(body);
        HttpPost post = bootpay.httpPost("sdk/deposits", new StringEntity(json, "UTF-8"), context(params.idempotencyKey));
        return bootpay.responseToJsonObject(bootpay.execute(post));
    }

    /** 입금대기 조회 — 기한이 지났으면 조회 시점에 expired 로 닫힙니다. */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String depositId) throws Exception {
        bootpay.requireCommerceCredentials();
        HttpGet get = bootpay.httpGet(path(depositId), context(null));
        return bootpay.responseToJsonObject(bootpay.execute(get));
    }

    /** 입금대기 취소 — 이미 닫힌 주문은 그대로 반환하고, 입금 확인된 주문은 18108입니다. */
    static public BootpayStoreResponse cancel(BootpayStoreObject bootpay, String depositId, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        HttpDelete delete = bootpay.httpDelete(path(depositId), context(idempotencyKey));
        return bootpay.responseToJsonObject(bootpay.execute(delete));
    }

    private static RequestContext context(String idempotencyKey) {
        return RequestContext.builder().role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey)).build();
    }

    private static String path(String depositId) throws Exception {
        requireValue(depositId, "depositId");
        return "sdk/deposits/" + URLEncoder.encode(depositId, "UTF-8").replace("+", "%20");
    }

    private static void requireValue(Object value, String name) throws Exception {
        if (value == null || "".equals(value)) throw new Exception(name + " 값이 비어있습니다.");
    }
}
