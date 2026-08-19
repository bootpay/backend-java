package kr.co.bootpay.store.model.request.orderSubscriptionRequest;

public class OrderSubscriptionRequestUpdateParams {
    public String orderSubscriptionRequestHistoryId;
    /** "approve" 또는 "reject" */
    public String approval;
    public String reason;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
