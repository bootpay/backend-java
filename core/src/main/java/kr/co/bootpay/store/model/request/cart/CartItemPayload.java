package kr.co.bootpay.store.model.request.cart;

public class CartItemPayload {
    public String productId;
    public String productOptionId;
    public Integer quantity;
    public Boolean isSubscription;
    public String subscriptionPeriodId;
}
