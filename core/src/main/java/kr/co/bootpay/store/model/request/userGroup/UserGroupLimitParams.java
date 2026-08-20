package kr.co.bootpay.store.model.request.userGroup;

public class UserGroupLimitParams {
    public String userGroupId;
    public Boolean useLimit;
    public Double limitMonthPurchase;
    public Double limitWeekPurchase;
    public String limitMessage;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
