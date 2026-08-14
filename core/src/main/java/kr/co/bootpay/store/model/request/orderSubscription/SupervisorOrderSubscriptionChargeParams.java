package kr.co.bootpay.store.model.request.orderSubscription;

import java.util.Map;

public class SupervisorOrderSubscriptionChargeParams {
    // Idempotency-Key 헤더로 전송되므로 body에는 포함하지 않는다
    public transient String idempotencyKey;
    public String chargeKey;
    public Double price;
    public Double taxFreePrice;
    public Map<String, Object> user;
    public Map<String, Object> metadata;
}
