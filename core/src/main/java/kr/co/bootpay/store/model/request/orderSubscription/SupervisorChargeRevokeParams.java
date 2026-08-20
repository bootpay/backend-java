package kr.co.bootpay.store.model.request.orderSubscription;

import java.util.Map;

/**
 * 수시결제(온디맨드) charge_key 해지 파라미터 (DELETE /v1/order_subscriptions/charge)
 * 해지 이후 해당 키로의 재결제는 불가능하다.
 */
public class SupervisorChargeRevokeParams {
    public String chargeKey;
    public Map<String, Object> user;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
