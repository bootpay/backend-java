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
    /**
     * 회차별 결제 금액의 <b>기준금액</b>.
     *
     * <p>바꾸면 결제예정(READY) 회차의 청구액이 즉시 다시 계산되고, 이후 회차도 이 금액으로 만들어진다.
     * 이미 결제된 회차는 그대로다. 0 이하는 받지 않는다.
     * 특정 회차만 가감하려면 {@code orderSubscriptionAdjustment.create} 를 쓴다.</p>
     */
    public Double price;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
