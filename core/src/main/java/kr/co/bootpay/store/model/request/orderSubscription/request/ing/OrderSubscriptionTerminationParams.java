package kr.co.bootpay.store.model.request.orderSubscription.request.ing;

public class OrderSubscriptionTerminationParams {
    public String orderSubscriptionId;
    public String orderNumber; //주문번호
    public Double terminationFee;
    public Double lastBillRefundPrice;
    public Double finalFee;
    public String serviceEndAt;
    public String reason;
}
