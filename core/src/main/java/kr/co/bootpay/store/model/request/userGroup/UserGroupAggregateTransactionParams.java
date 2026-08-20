package kr.co.bootpay.store.model.request.userGroup;

public class UserGroupAggregateTransactionParams {
    public String userGroupId;
    public Boolean useSubscriptionAggregateTransaction;
    public Integer subscriptionMonthDay;
    public Integer subscriptionWeekDay;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
