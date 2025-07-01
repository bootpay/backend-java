package kr.co.bootpay.store.model.request.orderSubscription;

import kr.co.bootpay.store.model.request.ListParams;

public class OrderSubscriptionUpdateParams {
    public String orderSubscriptionId;
    public String productId;
    public String productOptionId;
    public String orderName;
    public Double totalSubscriptionDuration;
    public Double quantity;
    public String addressId;
    public String username;
    public String phone;
    public String email;
    public Boolean useFreeTrial;
    public Integer freeTrialDay;
    public String serviceStartAt;
    public String serviceEndAt;
}
