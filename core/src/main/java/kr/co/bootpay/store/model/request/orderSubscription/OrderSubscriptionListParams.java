package kr.co.bootpay.store.model.request.orderSubscription;

import kr.co.bootpay.store.model.request.ListParams;

public class OrderSubscriptionListParams extends ListParams {
    public String sAt;
    public String eAt;
    public Integer requestType;
    public String userGroupId;

    public Integer status;
    public String userId;
}
