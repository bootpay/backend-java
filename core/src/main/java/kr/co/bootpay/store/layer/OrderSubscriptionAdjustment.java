package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.orderSubscriptionAdjustment.OrderSubscriptionAdjustmentUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscription_adjustment.SOrderSubscriptionAdjustmentService;
import kr.co.bootpay.store.service.order_subscription_bill.SOrderSubscriptionBillService;

import java.util.List;

public class OrderSubscriptionAdjustment {
    private final BootpayStore bootpay;

    public OrderSubscriptionAdjustment(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse create(String orderSubscriptionId, SOrderSubscriptionAdjustment adjustment)  throws Exception {
        return SOrderSubscriptionAdjustmentService.create(
                bootpay,
                orderSubscriptionId,
                adjustment
        );
    }

    public BootpayStoreResponse update(OrderSubscriptionAdjustmentUpdateParams params) throws Exception {
        return SOrderSubscriptionAdjustmentService.update(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse delete(String orderSubscriptionId, String orderSubscriptionAdjustmentId) throws Exception {
        return SOrderSubscriptionAdjustmentService.delete(
                bootpay,
                orderSubscriptionId,
                orderSubscriptionAdjustmentId
        );
    }
}
