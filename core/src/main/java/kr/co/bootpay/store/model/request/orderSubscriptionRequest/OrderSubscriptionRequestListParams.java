package kr.co.bootpay.store.model.request.orderSubscriptionRequest;

public class OrderSubscriptionRequestListParams {
    public String projectId;
    public String orderSubscriptionId;
    public Integer page;
    public Integer limit;
    public Integer requestType;
    public Integer status;
    public String sAt;
    public String eAt;
    public String keyword;
    public String userId;
    public String userGroupId;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, query 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
