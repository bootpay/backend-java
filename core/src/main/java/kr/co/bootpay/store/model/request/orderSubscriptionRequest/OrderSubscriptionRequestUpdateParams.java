package kr.co.bootpay.store.model.request.orderSubscriptionRequest;

public class OrderSubscriptionRequestUpdateParams {
    public String orderSubscriptionRequestHistoryId;
    /** "approve" 또는 "reject" */
    public String approval;
    public String reason;
}
