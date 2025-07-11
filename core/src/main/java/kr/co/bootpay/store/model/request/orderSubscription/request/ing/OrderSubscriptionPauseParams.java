package kr.co.bootpay.store.model.request.orderSubscription.request.ing;

import kr.co.bootpay.store.model.request.ListParams;

public class OrderSubscriptionPauseParams {
    public String orderSubscriptionId;
    public String orderNumber; //주문번호
    public String reason;
    public String pausedAt; //예상 재개일
    public String expectedResumeAt; //예상 재개일
}
