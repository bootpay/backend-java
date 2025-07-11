package kr.co.bootpay.store.model.request.orderSubscription.request.ing;

public class OrderSubscriptionPurchaseParams {
    public String orderSubscriptionId;
    public String orderNumber; //주문번호
    public Double price;
    public Double taxFreePrice;
    public String reason;
}
