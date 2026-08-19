package kr.co.bootpay.store.model.request.orderSubscription.request.ing;

public class OrderSubscriptionResumeParams {
    public String orderSubscriptionId;
    public String orderNumber; //주문번호
    public String reason;
    public String resumeAt; //재개일
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
