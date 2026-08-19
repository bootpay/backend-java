package kr.co.bootpay.store.model.request.orderSubscription;

import kr.co.bootpay.store.model.request.ListParams;

public class OrderSubscriptionUpdateParams {
    public String orderSubscriptionId;
    public String productId;
    public String productOptionId;
    public String orderName;
    public Double totalSubscriptionDuration;
    public Double quantity;
    public String addressId;
    public String username;
    public String phone;
    public String email;
    public Boolean useFreeTrial;
    public Integer freeTrialDay;
    public String serviceStartAt;
    public String nextBillingAt;
    public String billingKey;
    public Integer status;
    public String paymentNextAt;
    public String serviceEndAt;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
