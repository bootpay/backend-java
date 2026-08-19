package kr.co.bootpay.store.model.request.orderSubscription;

import java.util.Map;

/**
 * 수시결제(온디맨드) charge_key 즉시 결제 파라미터 (POST /v1/order_subscriptions/charge)
 * ⚠️ charge_key 는 body 로만 전송한다 (URL/query 금지 — 액세스 로그 노출 방지)
 */
public class SupervisorChargeParams {
    public String chargeKey;
    public Double price;
    public Double taxFreePrice;
    public Map<String, Object> user;
    public Map<String, Object> metadata;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
