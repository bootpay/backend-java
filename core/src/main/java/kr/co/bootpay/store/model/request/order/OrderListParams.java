package kr.co.bootpay.store.model.request.order;

import kr.co.bootpay.store.model.request.ListParams;

import java.util.List;

public class OrderListParams extends ListParams {
    public String csType;
    public List<String> orderSubscriptionIds;
    public String cssAt;
    public String cseAt;
    public List<Integer> status;
    public List<Integer> paymentStatus;
    public String userId;
    public String userGroupId;
    public Integer subscriptionBillingType;

    public static final int SUBSCRIPTION_BILLING_TYPE_NONE = 0; //구독결제 아님
    public static final int SUBSCRIPTION_BILLING_TYPE_EACH = 1; //각각 결제
    public static final int SUBSCRIPTION_BILLING_TYPE_GROUP = 2; //통합 결제

}
