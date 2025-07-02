package kr.co.bootpay.store.model.request.orderSubscriptionAdjustment;

import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;

import java.util.List;

public class OrderSubscriptionAdjustmentUpdateParams {
    public String orderSubscriptionId;
    public Integer duration;
    public List<SOrderSubscriptionAdjustment> adjustments;
}
