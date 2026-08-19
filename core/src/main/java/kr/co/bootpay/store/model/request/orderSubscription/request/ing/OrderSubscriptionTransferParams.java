package kr.co.bootpay.store.model.request.orderSubscription.request.ing;

/**
 * 구독 이전/승계 요청 파라미터 (POST /v1/order_subscriptions/requests/ing/transfer)
 */
public class OrderSubscriptionTransferParams {
    public String orderSubscriptionId;
    public String newUserId;
    public String newUsername;
    public String newUserEmail;
    public String newUserPhone;
    public String newUserAddress;
    public String walletId;
    public String reason;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
